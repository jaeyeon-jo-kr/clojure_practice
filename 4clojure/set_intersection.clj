;;Write a function which returns the intersection of two sets. The intersection is the sub-set of items that each set has in common.

;;(= (__ #{0 1 2 3} #{2 3 4 5}) #{2 3})

(fn [a b] (filter (fn [x] (=(nth x 1) (nth x 2))) (for [x a y b] [x y])))
