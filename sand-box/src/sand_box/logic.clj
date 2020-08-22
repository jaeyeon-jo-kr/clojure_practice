(ns sand-box.logic
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:require
    [clojure.core.logic.pldb :as pldb]
    [clojure.core.logic.fd :as fd]))


(pldb/db-rel man p)
(pldb/db-rel woman p)
(pldb/db-rel likes p1 p2)
(pldb/db-rel fun p)

(def facts0
  (pldb/db
    [man 'Bob]
    [man 'John]
    [man 'Ricky]

    [woman 'Mary]
    [woman 'Martha]
    [woman 'Lucy]

    [likes 'Bob 'Mary]
    [likes 'John 'Martha]
    [likes 'Ricky 'Lucy]))


(def facts1 (-> facts0 (pldb/db-fact fun 'Lucy)))


(comment
  (pldb/with-db facts1
                (run* [q] (fresh [x y]
                                 (fun y)
                                 (likes x y)
                                 (== q [x y])))))

(defne arco [x y]
       ([:a :b])
       ([:b :a])
       ([:b :d]))

(def patho
  (tabled [x y]
          (conde
            [(arco x y)]
            [(fresh [z]
                    (arco x z)
                    (patho z y))])))

(comment (run* [q] (patho :a q)))


