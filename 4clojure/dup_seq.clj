#(loop [in % out []]
        (if (= in nil)
          (seq out)
          (recur (next in) 
                 (concat out (take 2 (repeat (first in))))
          )
        )
)
