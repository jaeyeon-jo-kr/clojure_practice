(ns four-clojure.153-pairwise-disjoint-set)


;;
;; Given a set of sets, create a function which returns true if no two of those sets have any elements in common1 and false otherwise. Some of the test cases are a bit tricky, so pay a little more attention to them.
;
;Such sets are usually called pairwise disjoint or mutually disjoint.

(defn solve [f]
  (and
    (= (f #{#{\U} #{\s} #{\e \R \E} #{\P \L} #{\.}})
       true)
    (= (f #{#{:a :b :c :d :e}
            #{:a :b :c :d}
            #{:a :b :c}
            #{:a :b}
            #{:a}})
       false)
    (= (f #{#{[1 2 3] [4 5]}
            #{[1 2] [3 4 5]}
            #{[1] [2] 3 4 5}
            #{1 2 [3 4] [5]}})
       true)
    (= (f #{#{'(:x :y :z) '(:x :y) '(:z) '()}
            #{#{:x :y :z} #{:x :y} #{:z} #{}}
            #{'[:x :y :z] [:x :y] [:z] [] {}}})
       false)))


(def solution
  (fn [coll]
    (every? (fn [e]
              (empty?
                (clojure.set/intersection
                  e
                  (apply clojure.set/union (disj coll e))))) coll)))

