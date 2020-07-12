(fn [i & vars]
  (let [opers (partition 2 vars)]
        (reduce #((nth %2 0) %1 (nth %2 1)) i opers)))
