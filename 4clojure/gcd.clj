#(loop [a (if (< %1 %2) %2 %1)
        b (if (< %1 %2) %1 %2)]
   (if (= b 0)
     a
     (recur b (mod a b))))
