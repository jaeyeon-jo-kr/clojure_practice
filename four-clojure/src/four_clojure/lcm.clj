(fn lcm
  ([a] a)
  ([a b]
    ((fn [cols] (if (empty? cols) (* a b) (first cols)))
    (for [x (range a 400 a)
           y (range b 400 b)
           :when (= x  y)] x)))
  ([a b c] (lcm a (lcm b c)))
  ([a b c d] (lcm a (lcm b (lcm c d)))))

(fn [G & n]
  (reduce #(/ (* % %2 ) (G % %2)) n))
(fn g [a b] (if (= b 0) a (g b (rem a b))))
