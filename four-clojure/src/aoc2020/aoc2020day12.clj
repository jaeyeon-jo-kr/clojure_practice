(ns aoc2020.aoc2020day12
  (:require [clojure.string :as str]
            [clojure.set :as set]))

;;   --- Day 12: Rain Risk ---
;; Your ferry made decent progress toward the island, but the storm came in faster than anyone expected. The ferry needs to take evasive actions!

;; Unfortunately, the ship's navigation computer seems to be malfunctioning; rather than giving a route directly to safety, it produced extremely circuitous instructions. When the captain uses the PA system to ask if anyone can help, you quickly volunteer.

;; The navigation instructions (your puzzle input) consists of a sequence of single-character actions paired with integer input values. After staring at them for a few minutes, you work out what they probably mean:

;; Action N means to move north by the given value.
;; Action S means to move south by the given value.
;; Action E means to move east by the given value.
;; Action W means to move west by the given value.
;; Action L means to turn left the given number of degrees.
;; Action R means to turn right the given number of degrees.
;; Action F means to move forward by the given value in the direction the ship is currently facing.
;; The ship starts by facing east. Only the L and R actions change the direction the ship is facing. (That is, if the ship is facing east and the next instruction is N10, the ship would move north 10 units, but would still move east if the following action were F.)

;; For example:

;; F10
;; N3
;; F7
;; R90
;; F11
;; These instructions would be handled as follows:

;; F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.
;; N3 would move the ship 3 units north to east 10, north 3.
;; F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.
;; R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.
;; F11 would move the ship 11 units south to east 17, south 8.
;; At the end of these instructions, the ship's Manhattan distance (sum of the absolute values of its east/west position and its north/south position) from its starting position is 17 + 8 = 25.

;; Figure out where--- Day 12: Rain Risk ---
;; Your ferry made decent progress toward the island, but the storm came in faster than anyone expected. The ferry needs to take evasive actions!

;; Unfortunately, the ship's navigation computer seems to be malfunctioning; rather than giving a route directly to safety, it produced extremely circuitous instructions. When the captain uses the PA system to ask if anyone can help, you quickly volunteer.

;; The navigation instructions (your puzzle input) consists of a sequence of single-character actions paired with integer input values. After staring at them for a few minutes, you work out what they probably mean:

;; Action N means to move north by the given value.
;; Action S means to move south by the given value.
;; Action E means to move east by the given value.
;; Action W means to move west by the given value.
;; Action L means to turn left the given number of degrees.
;; Action R means to turn right the given number of degrees.
;; Action F means to move forward by the given value in the direction the ship is currently facing.
;; The ship starts by facing east. Only the L and R actions change the direction the ship is facing. (That is, if the ship is facing east and the next instruction is N10, the ship would move north 10 units, but would still move east if the following action were F.)

;; For example:

;; F10
;; N3
;; F7
;; R90
;; F11
;; These instructions would be handled as follows:

;; F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.
;; N3 would move the ship 3 units north to east 10, north 3.
;; F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.
;; R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.
;; F11 would move the ship 11 units south to east 17, south 8.
;; At the end of these instructions, the ship's Manhattan distance (sum of the absolute values of its east/west position and its north/south position) from its starting position is 17 + 8 = 25.

;; Figure out where the navigation instructions lead. What is the Manhattan distance between that location and the ship's starting position?

;;  the navigation instructions lead. What is the Manhattan distance between that location and the ship's starting position?


;; (partial map (fn [f e] (f e)) [keyword read-string])


(def space
  (->> (slurp "res/day12.txt")
       (clojure.string/split-lines)
       (map (comp
             (partial map (fn [f e] (f e)) [keyword read-string])
             #(nthnext % 1)
             (partial re-matches #"([NSEWLRF])(\d+)")))))

(defn turn
  [[e n deg] [dir diff]]
  {:pre [(number? deg)]}
  (let [op (case dir :L - :R +)]
    (if op [e n (-> (op deg diff)
                    (mod 360))]
        [e n deg])))

(defn forward
  [[e n dir] [_ val]]
  (case dir
    0 [e (+ n val) dir]
    90 [(+ e val) n dir]
    180 [e (- n val) dir]
    270 [(- e val) n dir]))

(defn move
  [[e n deg] [dir val]]
  (case dir
    :N [e (+ n val) deg]
    :E [(+ e val) n deg]
    :S [e (- n val) deg]
    :W [(- e val) n deg]))

(defn action
  [cur [dir val]]
  (let [f (case dir
            (:N :E :S :W) move
            :F forward
            (:L :R) turn)]
    (f cur [dir val])))

(defn navigate
  [space]
  (reduce action [0 0 90] space))

(action [0 0 90] '(:R 90))

(defn abs [n]
  (if (neg-int? n)
    (- n) n))

(defn dist [a b]
  (+ (abs a) (abs b)))

(comment
  (let [[a b] (navigate space)]
    (dist a b))
  (first space))

(count [1 2 3 4])

(defn rotate-waypoint
  [[e n ew nw] [dir val]]
  {:pre [e n ew nw (keyword? dir)(number? val)]
   :post [#(= 4 (count %))]}
  (case [dir val]
    ([:R 90] [:L 270]) [e n nw (- ew)]
    ([:R 180] [:L 180]) [e n (- ew) (- nw)]
    ([:R 270] [:L 90]) [e n (- nw) ew]
    ([:R 0] [:L 0]) [e n ew nw]))

(defn forward-by-waypoint
  [[e n ew nw] [_ val]]
  {:pre [e n ew nw (number? val)]
   :post [#(= 4 (count %)) ]}
  [(+ e (* ew val)) (+ n (* nw val)) ew nw])

(defn move-waypoint
  [[e n ew nw] [dir val]]
  {:pre [e n ew nw dir val]
   :post [#(= 4 (count %))]}
  (case dir
    :N [e n ew (+ nw val)]
    :E [e n (+ ew val) nw]
    :S [e n ew (- nw val)]
    :W [e n (- ew val) nw]))

(defn action-p2
  [cur [dir val]]
  {:pre [cur dir val]
   :post [#(= 4 (count %))]}
  (let [f (case dir
            (:N :E :S :W) move-waypoint
            :F forward-by-waypoint
            (:L :R) rotate-waypoint)]
    (f cur [dir val])))

(defn navigate-p2
  [space]
  (reduce action-p2 [0 0 10 1] space))

(comment
  (rotate [0 0 10 1] '(:R 270))
  (let [[a b] (navigate-p2 space)]
    (dist a b)))
