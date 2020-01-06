;;Write a function which separates the items of a sequence by an arbitrary value.

(fn [a b] (into (list (last b)) (reverse (mapcat #(list % a) (pop b)))))
