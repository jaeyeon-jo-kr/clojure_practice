(ns 78_reimplement_trampoline)
;;Reimplement the function described in "Intro to Trampoline".
;;test not run

(= (letfn [(triple [x] #(sub-two (* 3 x)))
           (sub-two [x] #(stop? (- x 2)))
           (stop? [x] (if (> x 50) x #(triple x)))]
          (__ triple 2))
   82)
test not run

(= (letfn [(my-even? [x] (if (zero? x) true #(my-odd? (dec x))))
           (my-odd? [x] (if (zero? x) false #(my-even? (dec x))))]
          (map (partial __ my-even?) (range 6)))
   [true false true false true false])

(fn m
    ([f] (if-let [x (fn? f)] (m (f)) f))
    ([f & x] (if-let [y (fn? f)] (m (apply f x)) f)))