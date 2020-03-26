(ns 60_sequence_reduction)


;; Write a function which behaves like reduce,
;; but returns each intermediate value of the reduction.
;; Your function must accept either two or three arguments,
;; and the return sequence must be lazy.


;;(= (take 5 (__ + (range))) [0 1 3 6 10])

;;(= (__ conj [1] [2 3 4]) [[1] [1 2] [1 2 3] [1 2 3 4]])

;; (= (last (__ * 2 [3 4 5])) (reduce * 2 [3 4 5]) 120)

(fn f ([g y]
       (f g [(first y)] (rest y)))
    ([g x y]
     (if
       (empty? y)
       x
       ((f (lazy-seq (conj x (g (last x) y))) (rest y))))))