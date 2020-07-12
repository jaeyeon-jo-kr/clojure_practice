(fn f [n]
  (cond
    (and (coll? n) (= (count n) 3)) (and (f (second n)) (f (nth n 2)))
   	(coll? n) false
   	(nil? n) true
    :else (true? n)))
