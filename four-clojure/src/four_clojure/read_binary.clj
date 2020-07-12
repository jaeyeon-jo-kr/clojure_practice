(fn [s]
  (let [exp-2 #(reduce * (take % (repeat 2)))]
    (reduce +
            (keep-indexed
             (fn [i v]
               (case v
                 \1 (exp-2 i)
                 nil))
             (reverse s)))))
