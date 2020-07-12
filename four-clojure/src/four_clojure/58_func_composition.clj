(fn f
    ([f] (fn [x] (f x)))
    ([f g] (fn ([x] (f (g x)))
               ([x y] (f (g x y)))))
    ([f g h] (fn ([x] (f (g (h x))))
                 ([x y] (f (g (h x y))))
                 ([x y z] (f (g (h x y z))))
                 ([x y z p] (f (g (h x y z p)))))))
