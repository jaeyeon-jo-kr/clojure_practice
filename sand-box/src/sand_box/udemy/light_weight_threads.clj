(ns sand-box.udemy.light-weight-threads
  (:require [clojure.core.async :refer [chan >! <! go >!! <!!]]))

(let [c (chan)]
  (go (doseq [x (range 3)]
        (>! c x)))
  (go (dotimes [x 3]
        (println "Take : " (<! c)))))


