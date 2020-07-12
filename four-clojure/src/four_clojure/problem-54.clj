(ns problem-54)
;;Write a function which returns a sequence of lists of x items each.
;; Lists of less than x items should not be returned.
;;(= (__ 3 (range 9)) '((0 1 2) (3 4 5) (6 7 8)))
(fn f [n c]
    (if (not= (count (take n c)) n)
      []
      (cons (take n c) (f n (drop n c)))))