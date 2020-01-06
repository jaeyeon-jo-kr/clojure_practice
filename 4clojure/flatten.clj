;;Write a function which flattens a sequence.
;;test not run

;;(= (__ '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6))
;;test not run

;;(= (__ ["a" ["b"] "c"]) '("a" "b" "c"))
;;test not run

;;(= (__ '((((:a))))) '(:a))

#(let [s %]
   (filter (complement sequential?)
          (rest (tree-seq sequential? seq s))))
