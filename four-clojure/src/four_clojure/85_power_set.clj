(ns four-clojure.85-power-set)



;; Write a function which generates the power set of a given set. The power set of a set x is the set of all subsets of x, including the empty set and x itself.
;; (= (__ #{1 :a}) #{#{1 :a} #{:a} #{} #{1}})
;;(= (__ #{}) #{#{}})
;;(= (__ #{1 2 3})
;;   #{#{} #{1} #{2} #{3} #{1 2} #{1 3} #{2 3} #{1 2 3}})
;;(= (count (__ (into #{} (range 10)))) 1024)

(comment (apply conj #{s} (map hash-set s)))

(defn a [s]
  {:pre [(set? s)]}
  (if (empty? s)
    #{s}
    (do (println s)
        (print "apply into : ")
        (print #{s} " ")
        (println (mapv #(a (disj s %)) s))
        (try
          (union into #{s} (mapv #(a (disj s %)) s))
          (catch Exception e (.printStackTrace e))))))


(defn powerset [ls]
  (if (empty? ls) '(())
                  (union (powerset (next ls))
                         (map #(conj % (first ls)) (powerset (next ls))))))



(comment
  (fn [src]
    (loop [res #{}
           coll src]
      (cond
        (empty? coll) (conj res #{})
        (= (count coll) 1) (conj res coll #{})
        :else (lazy-cat (cons res (map (partial into res))))))))




