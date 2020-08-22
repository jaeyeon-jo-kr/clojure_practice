(ns sand-box.async
  (:require [clojure.core.async :as async]))


(def ^:dynamic *waiting-chairs* (atom []))

(def ^:dynamic *barber-shop* (ref {:barber        :idle
                                   :barbing-chair :empty}))

(defmacro with-timeout [millis & body]
  `(let [future# (future ~@body)]
     (try
       (.get future# ~millis java.util.concurrent.TimeUnit/MILLISECONDS
             (catch java.util.concurrent.TimeoutException x#
               (do
                 (future-cancel future#)
                 nil))))))

(defn has-empty-chair? []
  (< (count @*waiting-chairs*) 3))

(def barbing
  (dosync
    (when (= (:barbing-chair @*barber-shop*) :seated)
      (do
        (ref-set *barber-shop* {:barber        :working
                                :barbing-chair :seated})
        (Thread/sleep 20)
        (ref-set *barber-shop* {:barber        :idle
                                :barbing-chair :empty})))))

(defn barbing-2 []
  (when-not (has-empty-chair?)
    (do
      (swap! *waiting-chairs* #(pop %))
      (future (Thread/sleep 20)
              (ref-set *barber-shop* {:barber        :idle
                                      :barbing-chair :empty})))))

(defn visit []
  (let [interval (+ 10 (rand-int 21))]
    (Thread/sleep interval)
    (when (has-empty-chair?)
      (swap! *waiting-chairs* #(conj % :seated)))))

(defn start []
  (async/timeout)
  (do (future (while true (do (repeat barbing-2)
                              (repeat visit))))
      (Thread/sleep 10000)))














