(ns four-clojure.104-write-roman-numerals)

;; This is the inverse of Problem 92, but much easier.
;; Given an integer smaller than 4000,
;; return the corresponding roman numeral in uppercase,
;; adhering to the subtractive principle.


(comment (= "I" (__ 1))
         (= "XXX" (__ 30))
         (= "IV" (__ 4))
         (= "CXL" (__ 140))
         (= "DCCCXXVII" (__ 827))
         (= "MMMCMXCIX" (__ 3999))
         (= "XLVIII" (__ 48)))
(fn [x]
  (letfn [(digit->units [x]
            (case (mod x 10) 0 "" 1 "I" 2 "II" 3 "III" 4 "IV"
                  5 "V" 6 "VI" 7 "VII" 8 "VIII" 9 "IX"))
          (digit->tens [x]
            (case (-> (quot x 10) (mod 10))
                  0 "" 1 "X" 2 "XX" 3 "XXX" 4 "XL"
                  5 "L" 6 "LX" 7 "LXX" 8 "LXXX" 9 "XC"))
          (digit-hundreds [x]
            (case (-> (quot x 100) (mod 10))
                  0 "" 1 "C" 2 "CC" 3 "CCC" 4 "CD"
                  5 "D" 6 "DC" 7 "DCC" 8 "DCCC" 9 "CM"))
          (digit->thousands [x]
            (apply str "" (repeat (-> (quot x 1000) (mod 10)) "M")))]
    (apply str ((juxt digit->thousands digit-hundreds digit->tens digit->units) x))))

