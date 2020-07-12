(ns four-clojure.problem-148-the-big-devide)
;;the big device
;;Write a function which calculates the sum of all natural numbers under n (first argument)
;;which are evenly divisible by at least one of a and b (second and third argument).
;;Numbers a and b are guaranteed to be coprimes.

;;Note: Some test cases have a very large n, so the most obvious solution will exceed the time limit.

;; (= 0 (__ 3 17 11)) -> 0

;; (= 23 (__ 10 3 5)) ;; 3 + 5 + 6 + 9 = 23

;; (= 233168 (__ 1000 3 5))

;; (= "2333333316666668" (str (__ 100000000 3 5)))

(fn [n a b]
  (reduce + (filter #(or (zero? (mod % a))
                         (zero? (mod % b))) (range n))))

