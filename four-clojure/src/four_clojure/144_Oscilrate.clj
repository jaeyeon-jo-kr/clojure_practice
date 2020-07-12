(ns four-clojure.144-Oscilrate)

;;Write an oscillating iterate: a function that takes an initial value and a variable number of functions.
;; It should return a lazy sequence of the functions applied to the value in order, restarting from the first function after it hits the end.
(defn n ([] [])
  ([e] [e])
  ([e & fs]
   (lazy-seq
     (cons e
           (apply n (-> (into [((first fs) e)] (vec (rest fs)))
                        (into [(first fs)])))))))







