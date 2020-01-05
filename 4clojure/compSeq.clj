#(
  loop [in % out []]
  (cond
    (= in nil) out
    (= (first in) (last out)) (recur (next in) out)
    :else (recur (next in) (conj out (first in)))
  )
)
