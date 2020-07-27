(ns four-clojure.132-Insert-between-two-items)

;;Write a function that takes a two-argument predicate, a value, and a collection;
;; and returns a new collection where the value is inserted between every two items that satisfy the predicate.

(comment (= '(1 :less 6 :less 7 4 3) (__ < :less [1 6 7 4 3]))
         (= '(2) (__ > :more [2])))

(defn b [p v coll]
  (loop [r []
         c coll]
    (let [e (first c)
          l (last r)]
      (lazy-seq
        (if (empty? c)
          r
          (recur
            (if (and l (p l e))
              (conj r v e)
              (conj r e))
            (next c)))))))





