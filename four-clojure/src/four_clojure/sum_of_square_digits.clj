
(fn [numbers]
  (count (filter (fn [number]
    (< number
      (reduce
        (fn [x y] (+ x (* y y))) 0
        (read-string (apply str "[" (apply str (interpose " " (str number))) "]")))))
        numbers)))
