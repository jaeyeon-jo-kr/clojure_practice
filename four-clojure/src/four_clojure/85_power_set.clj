(ns four-clojure.85-power-set)



;; Write a function which generates the power set of a given set. The power set of a set x is the set of all subsets of x, including the empty set and x itself.
;; (= (__ #{1 :a}) #{#{1 :a} #{:a} #{} #{1}})
;;(= (__ #{}) #{#{}})
;;(= (__ #{1 2 3})
;;   #{#{} #{1} #{2} #{3} #{1 2} #{1 3} #{2 3} #{1 2 3}})
;;(= (count (__ (into #{} (range 10)))) 1024)

(fn my-set [src]
  (cond
    (empty? src) #{#{}}
    (= 1 (count src)) #{src #{}}
    :else (reduce
            (fn [col el]
              (-> (into col (my-set (disj src el)))
                  (conj src)))
            #{} src)))
(comment
  (fn [src]
    (loop [res #{}
           coll src]
      (cond
        (empty? coll) (conj res #{})
        (= (count coll) 1) (conj res coll #{})
        :else (lazy-cat (cons res (map (partial into res))))))))




