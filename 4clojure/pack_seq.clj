;;Write a function which packs consecutive duplicates into sub-lists.

;;(= (__ [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3)))

(fn [x]
  (loop [in x out []]
    (if (= in [])
      out
      (recur
        (drop-while #(= (first in) %) in)
        (conj out (take-while #(= (first in) %) in))))))
