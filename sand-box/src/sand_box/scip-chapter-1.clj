(ns sand-box.scip-chapter-1
  (:require [taoensso.tufte :as tufte :refer (defnp p profiled profile)]))

(tufte/add-basic-println-handler! {})

(/ (+ 5 4 (- 2 (- 3 (+ 6 (/ 4 5)))))
   (* 3 (- 6 2) (- 2 7)))

(defn c13 [a b c]
  (cond
    (and (>= a c) (>= b c))
    (+ (* a a) (* b b))
    (and (>= a b) (>= c b))
    (+ (* b b) (* c c))
    (and (>= b a) (>= c a))
    (+ (* c c) (* a a))))

(defn c1-11
  [n]
  (if (< n 3)
    n
    (+ (c1-11 (dec n))
       (* 2 (c1-11 (- n 2)))
       (* 3 (c1-11 (- n 3))))))

((juxt butlast rest) [1 1])
(map (comp (partial apply +)))


(defn pascal-triangle
  []
  (let [next-row (fn [prev-row]
                   (->> (map + (butlast prev-row) (rest prev-row))
                        (cons 1)
                        reverse
                        (cons 1)
                        reverse))]
    (iterate next-row '(1))))

(defn next-row-2
  ([coll]
   (->> ((juxt butlast next) coll)
        (keep identity)
        ((fn [[x y]] (map + x y)))
        (cons 1)
        reverse
        (cons 1)
        reverse)))

(defn pascal-triangle-2
  []
  (iterate next-row-2 '(1)))

(defn fib-iter 
  [a b count]
  (if (= count 0)
    b 
    (fib-iter (+ a b) a (dec 1))))

(defn fib [n]
  (fib-iter 1 0 n))

(comment
  (profile ; Profile any `p` forms called during body execution
   {} ; Profiling options; we'll use the defaults for now
   (dotimes [_ 5]
     (p :get-pascal-triangle (take 1000 (pascal-triangle)))
     (p :get-pascal-triangle-2 (take 1000 (pascal-triangle-2))))))
