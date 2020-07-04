(ns four-clojure.105-Identify-keys-and-values)

(fn [coll]
  (loop [m {}
         e coll]
    (if (not-empty e)
      (recur (assoc m (first e)
                      (vec (take-while (complement keyword?) (rest e))))
             (vec (drop-while (complement keyword?) (rest e))))
      m)))

(fn [coll]
  (second
    (reduce
      (fn [[k m] c]
        (if (keyword? c)
          [c (assoc m c [])]
          [k (assoc m k (conj (m k) c))]))
      [nil {}] coll)))









