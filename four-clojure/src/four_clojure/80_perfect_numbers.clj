(ns four-clojure.80-perfect-numbers)


(defn perfects? [n]
  (->> (range 1 n)
       (filter #(zero? (mod n %)))
       (apply +)
       (= n)))
