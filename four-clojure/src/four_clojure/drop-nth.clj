;;Write a function which drops every Nth item from a sequence.

#(map (fn [x] (nth x 1))
    (filter
      (fn [x] (not= 0 (mod (inc (nth x 0)) %2)))
      (map vector (iterate inc 0) %1)))
