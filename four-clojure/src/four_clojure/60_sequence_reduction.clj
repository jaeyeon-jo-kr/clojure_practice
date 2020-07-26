(ns four-clojure.60-sequence-reduction)


;; Write a function which behaves like reduce,
;; but returns each intermediate value of the reduction.
;; Your function must accept either two or three arguments,
;; and the return sequence must be lazy.


;;(= (take 5 (__ + (range))) [0 1 3 6 10])

;;(= (__ conj [1] [2 3 4]) [[1] [1 2] [1 2 3] [1 2 3 4]])

;; (= (last (__ * 2 [3 4 5])) (reduce * 2 [3 4 5]) 120)

(fn my-reduct
  ([f coll]
   (lazy-seq
     (if (not-empty coll)
       (my-reduct f (first coll) (rest coll))
       ((f) coll)))
   ([f init coll]
    (if (empty? coll)
      init
    (cons init
          (lazy-seq
                 (when-let [s (seq coll)]
                   (my-reduct f (f init (first s)) (rest s)))))))))
