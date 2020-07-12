;;;44-rotate-

(fn [f n s]
    (flatten (cons (drop (f n s) s) (take (f n s) s))))

(fn [n s] (mod n (count s)))
