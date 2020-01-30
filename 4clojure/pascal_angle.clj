;;pascal angle
;; like 1
;;     1 1
;;    1 2 1
;;   1 3 3 1
;;  1 4 6 4 1
(fn [x]
  (loop [out [1]
    n 1]
    (if (= x n)
      out
      (let
        [next-col (fn [prev n]
          (let [arr (range n)]
            (map
              #(if (or (= % 0) (= 1 (- n %))) 1
                  (+ (nth prev (dec %)) (nth prev %)))
                  arr)))]
      (recur (next-col out (inc n)) (inc n))))))

(fn [x]
  (reduce
    (fn [col elem]
      (map
        #(if (or (= % 1) (= 0 (- (count elem) %))) 1
          (+ (nth col (- % 2)) (nth col (dec %))))
          elem)) []
        (map #(range 1 (inc %)) (range 1 (inc x)))))
