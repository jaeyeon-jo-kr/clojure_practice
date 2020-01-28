(fn [x y]
  (reduce +
          (map #(reduce * %) (partition 2 (interleave x y)))))
