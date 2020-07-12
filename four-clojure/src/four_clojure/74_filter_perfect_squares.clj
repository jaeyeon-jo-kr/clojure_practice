(ns 74-filter-perfect-squares)

(fn [s]
    (apply str
           (interpose
             ","
             (map str
                  (filter
                    (fn [x] (= x (last (take-while #(<= % x) (map #(* % %) (iterate inc 1))))))
                    (map read-string (clojure.string/split s #",")))))))



