(ns 59_juxt_a_position)
;Take a set of functions and return a new function that
; takes a variable number of arguments and returns a sequence containing
; the result of applying each function left-to-right to the argument list.

; (= [21 6 1] ((__ + max min) 2 3 5 1 6 4))

(fn [& xs]
    (fn [& ys]
        (map #(apply % ys) xs)))