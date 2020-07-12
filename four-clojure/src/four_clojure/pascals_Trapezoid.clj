(second (
(fn [f1 f2 c] (iterate #(f1 f2 %) c))
(fn [f2 c]
  (reverse (cons (last c) (reverse (conj
    (map #(f2 c %) (range 1 (count c))) (first c))))))
(fn [c n] (+' (nth c n) (nth c (dec n))))
[2 3 2]))
