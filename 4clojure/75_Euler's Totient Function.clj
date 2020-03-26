(ns 75_Euler 's Totient Function)


;;Two numbers are coprime if their greatest common divisor equals 1. Euler's totient function f(x) is defined as the number of positive integers less than x which are coprime to x. The special case f(1) equals 1. Write a function which calculates Euler's totient function.
(fn [x]
    (if
      (= x 1)
      1
      (count
        (filter
          (fn [y]
              (empty? (filter
                        #(and (zero? (mod x %)) (zero? (mod y %)))
                        (range 2 (inc y))))) (range 1 x)))))

(fn [x y]
    )