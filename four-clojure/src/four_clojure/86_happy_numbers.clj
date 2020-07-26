(ns four-clojure.86-happy-numbers)


;;Happy numbers are positive integers that follow a particular formula:
;;take each individual digit, square it, and then sum the squares to get a new number.
;;Repeat with the new number and eventually, you might get to a number whose squared sum is 1.
;;This is a happy number.
;; An unhappy number (or sad number) is one that loops endlessly.
;; Write a function that determines if a number is happy or not.
(comment (= (__ 7) true))

(defn happy-number [x]
  (loop [r []
         n x]
    (cond
      (= n 1) true
      (contains? r n) false
      :else (recur (conj r n)
                   (->> (str n)
                        (map #((fn [x] (* x x))
                               (read-string (str %))))
                        (apply +))))))



