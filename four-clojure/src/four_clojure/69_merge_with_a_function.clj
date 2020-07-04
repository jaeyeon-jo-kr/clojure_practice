(ns four-clojure.69-merge-with-a-function)

(fn [f & coll]
  (reduce
    (fn [a b]
      (reduce-kv
        (fn [m k v]
          (assoc m k
                   (if-let [v2 (m k)] (f v2 v) v)))
        a b)) coll))


