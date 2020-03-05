;;Write a function which returns a map containing
;; the number of occurences of each distinct item in a sequence.
#(reduce
   (fn [counts x]
       (assoc counts x
               (inc (get counts x 0))))
   {} %)