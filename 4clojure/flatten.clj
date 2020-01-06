#(loop [in %1 out []]
  (cond
    (= in nil) out
    (not (sequable? in)) (recur (next in) (conj out (first in)))
    (sequable? in) (recur (first in) out)))
