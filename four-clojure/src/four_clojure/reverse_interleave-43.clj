(ns reverse-interleave-43)
;; Write a function which reverses the interleave process into x number of subsequences.

(= (__ [1 2 3 4 5 6] 2) '((1 3 5) (2 4 6)))
(fn [coll n]
    (juxt (map #(partial filter (partial = (mod % n))) (range 1 (inc n)))) coll)