;;interleave two sequence

#(loop
  [a %1 b %2
   out []]
  (if (or (= a nil) (= b nil))
    out
    (recur
      (next a) (next b)
      (conj (conj out (first a)) (first b)))))
