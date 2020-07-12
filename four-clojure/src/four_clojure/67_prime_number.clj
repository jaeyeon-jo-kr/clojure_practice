#(last (take %
             (iterate
               (fn [coll]
                   (conj coll
                         (first
                           (filter
                             (fn [n] (not-any? (fn [a] (= 0 (mod n a))) coll))
                             (iterate inc (inc (last coll)))))))
               [2])))