;;Write a function which takes a variable number of booleans. Your function should return true if some of the parameters are true, but not all of the parameters are true. Otherwise your function should return false.

(fn [x & n]
  (let [arr (conj n x)]
   (and
     (not= nil (some (fn [x] (= x true)) arr))
     (not-every? (fn [x] (= x true)) arr))))
