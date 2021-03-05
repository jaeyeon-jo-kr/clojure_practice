(fn my-map [f x]
    (if (empty? x) (empty x)
      (lazy-seq (cons (f (first x)) (my-map f (next x))))))
