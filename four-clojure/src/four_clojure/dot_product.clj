(fn [nums n]
    (sort-by first (reduce
                     (fn [coll e] (assoc coll (mod e n) (conj (get coll (mod e n)) e)))
                     (vec (repeat n []))
                     nums)))