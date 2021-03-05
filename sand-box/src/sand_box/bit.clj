(ns sand-box.bit
  (:require [taoensso.tufte :as tufte :refer (defnp p profiled profile)]))

(tufte/add-basic-println-handler! {:ns-pattern "*"})

;; https://dev.to/stopachka/simulating-ram-in-clojure-bj5

(defn rand-wire
  ([prefix]
   (-> (name prefix)
       (gensym)
       (keyword)))
  ([]
   (keyword (gensym "w"))))

(defn rand-wires
  ([prefix n]
   (repeatedly n #(rand-wire prefix)))
  ([n]
   (repeatedly n rand-wire)))




(defn wires
  [prefix n]
  (->> (range n)
       (mapv (comp keyword #(str (name prefix) %)))))

(defn nand-output [a b]
  (if (= a b 1) 0 1))

(def empty-state {:charge-map {} :nand-gates []})

(defn charge [state wire]
  (get-in state [:charge-map wire]))


(defn charges [state wires]
  (map (partial charge state) wires))

(defn set-charge [state wire charge]
  (assoc-in state [:charge-map wire] charge))

(set-charge empty-state :a 0)

(defn dependent-nand-gates
  [state wire]
  (filter
   (fn [{:keys [ins]}] (some #{wire} ins))
   (:nand-gates state)))

(declare trigger)
(defn trigger-nand-gate
  [state {:keys [ins out]}]
  (let [new-charge (apply nand-output (charges state ins))]
    (trigger state out new-charge)))

(defn trigger
  ([state wire new-v]
   (let [old-charge (charge state wire)
         state' (set-charge state wire new-v)
         new-charge (charge state' wire)]
     (if (= old-charge new-charge)
       state'
       (reduce (fn [acc-state out] (trigger-nand-gate acc-state out))
               state'
               (dependent-nand-gates state' wire))))))

(defn trigger-many [state wires charges]
  (reduce
   (fn [acc-state [wire charge]]
     (trigger acc-state wire charge))
   state
   (map vector wires charges)))

(defn wire-nand-gate [state a b o]
  (update state :nand-gates conj {:ins [a b] :out o}))

(defn wire-not-gate
  [state a o]
  (wire-nand-gate state a a o))

(defn wire-and-gate
  ([state a b o]
   (let [no (rand-wire)]
     (-> state
         (wire-nand-gate a b no)
         (wire-not-gate no o))))
  ([state a b c & more]
   (let [ins (into [a b c] (butlast more))
         comb (-> (repeatedly (dec (count ins)) rand-wire)
                  vec
                  (conj (last ins)))
         outs (->> (butlast comb)
                   (cons (last more))
                   vec)
         in-out-map (map (fn [in1 in2 out] [in1 in2 out]) ins comb outs)]
     (reduce (fn [s [in1 in2 out]]
               (wire-and-gate s in1 in2 out)) state in-out-map))))



(defn wire-multi-bit-and-gate
  [state ins1 ins2 os]
  (->> (map (fn [in1 in2 o] [in1 in2 o]) ins1 ins2 os)
       (reduce (fn [state [in1 in2 o]]
                 (wire-and-gate state in1 in2 o)) state)))

(defn wire-or-gate
  ([state a b o]
   (let [na (rand-wire :in-out-)
         nb (rand-wire :in-out-)]
     (-> state
         (wire-not-gate a na)
         (wire-not-gate b nb)
         (wire-nand-gate na nb o))))
  ([state a b c & more]
   (let [ins (into [a b c] (butlast more))
         comb (-> (repeatedly (dec (count ins)) rand-wire)
                  vec
                  (conj (last ins)))
         outs (->> (butlast comb)
                   (cons (last more))
                   vec)
         in-out-map (map (fn [in1 in2 out] [in1 in2 out]) ins comb outs)]
     (reduce (fn [s [in1 in2 out]]
               (wire-or-gate s in1 in2 out)) state in-out-map))))

(trigger-many
 (wire-or-gate empty-state :a :b :c :d :o)
 [:a :b :c :d] [1 1 0 1])

(defn wire-multi-bit-or-gate
  [state ins1 ins2 os]
  (->> (map (fn [in1 in2 o] [in1 in2 o]) ins1 ins2 os)
       (reduce (fn [state [in1 in2 o]]
                 (wire-or-gate state in1 in2 o)) state)))

(defn wire-xor-gate
  ([state a b o]
   (let [a' (rand-wire) b' (rand-wire)
         ab' (rand-wire) a'b (rand-wire)]
     (-> state
         (wire-not-gate b b')
         (wire-not-gate a a')
         (wire-and-gate a b' ab')
         (wire-and-gate a' b a'b)
         (wire-or-gate ab' a'b o))))
  ([state a b c & more]
   (let [ws (->> (repeatedly (count more) rand-wire)
                 (into []))
         ins1 (cons a ws)
         ins2 (into [b c] (butlast more))
         outs (conj ws (last more))]
     (->> (mapv (fn [i1 i2 o] [i1 i2 o]) ins1 ins2 outs)
          (reduce (fn [st [i1 i2 o]]
                    (wire-xor-gate st i1 i2 o)) state)))))

(take-nth 2 [1 2 2 3])
(->> (trigger-many
      (wire-xor-gate empty-state :a :b :o)
      [:a :b]
      [0 1])
     :charge-map
     :o)

(->> (trigger-many
      (wire-xor-gate empty-state :a :b :c :o)
      [:a :b :c]
      [0 0 1])
     :charge-map
     :o)

(defn wire-mux-gate
  ([state a b s o]
   (let [nots (rand-wire :in-out-)
         o1 (rand-wire :in-out-)
         o2 (rand-wire :in-out-)]
     (-> state
         (wire-not-gate s nots)
         (wire-and-gate a nots o1)
         (wire-and-gate b s o2)
         (wire-or-gate o1 o2 o))))
  ([state a b c d s0 s1 o]
   (let [s1' (rand-wire)
         s0' (rand-wire)
         ao (rand-wire)
         bo (rand-wire)
         co (rand-wire)
         do (rand-wire)]
     (-> state
         (wire-not-gate s0 s0')
         (wire-not-gate s1 s1')
         (wire-and-gate a s0' s1' ao)
         (wire-and-gate b s0 s1' bo)
         (wire-and-gate c s0' s1 co)
         (wire-and-gate d s0 s1 do)
         (wire-or-gate ao bo co do o))))
  ([state a b c d e f g h s0 s1 s2 o]
   (let [s0' (rand-wire) s1' (rand-wire) s2' (rand-wire)
         a' (rand-wire) b' (rand-wire) c' (rand-wire)
         d' (rand-wire) e' (rand-wire) f' (rand-wire)
         g' (rand-wire) h' (rand-wire)]
     (-> state
         (wire-not-gate s0 s0')
         (wire-not-gate s1 s1')
         (wire-not-gate s2 s2')
         (wire-and-gate a s0' s1' s2' a')
         (wire-and-gate b s0  s1' s2' b')
         (wire-and-gate c s0' s1  s2' c')
         (wire-and-gate d s0  s1  s2' d')
         (wire-and-gate e s0' s1' s2  e')
         (wire-and-gate f s0  s1' s2  f')
         (wire-and-gate g s0' s1  s2  g')
         (wire-and-gate h s0  s1  s2  h')
         (wire-or-gate a' b' c' d' e' f' g' h' o)))))

(defn wire-multibit-mux-gate
  ([state a b s o]
   (->> (map (fn [a' b' o1] [a' b' o1]) a b o)
        (reduce (fn [state' [a' b' o1]]
                  (wire-mux-gate state' a' b' s o1))
                state)))
  ([state a b c d s0 s1 o]
   (->> (map (fn [a' b' c' d' o'] [a' b' c' d' o'])
             a b c d o)
        (reduce (fn [state' [a' b' c' d' o1]]
                  (wire-mux-gate state' a' b' c' d' s0 s1 o1))
                state)))
  ([state a b c d e f g h s0 s1 s2 o]
   (->> (map (fn [a' b' c' d' e' f' g' h' o']
               [a' b' c' d' e' f' g' h' o'])
             a b c d e f g h o)
        (reduce (fn [state' [a' b' c' d' e' f' g' h' o']]
                  (wire-mux-gate state'
                                 a' b' c' d' e' f' g' h'
                                 s0 s1 s2 o'))
                state))))
(defn wire-demux-gate
  ([state a s o0 o1]
   (let [s' (rand-wire)]
     (-> state
         (wire-not-gate s s')
         (wire-and-gate a s' o0)
         (wire-and-gate a s o1))))
  ([state a s0 s1 o0 o1 o2 o3]
   (let [s0' (rand-wire) s1' (rand-wire)]
     (-> state
         (wire-not-gate s0 s0')
         (wire-not-gate s1 s1')
         (wire-and-gate a s0' s1' o0)
         (wire-and-gate a s0  s1' o1)
         (wire-and-gate a s0' s1  o2)
         (wire-and-gate a s0  s1  o3))))
  ([state a s0 s1 s2 o0 o1 o2 o3 o4 o5 o6 o7]
   (let [s0' (rand-wire) s1' (rand-wire) s2' (rand-wire)]
     (-> state
         (wire-not-gate s0 s0')
         (wire-not-gate s1 s1')
         (wire-not-gate s2 s2')
         (wire-and-gate a s0' s1' s2' o0)
         (wire-and-gate a s0  s1' s2' o1)
         (wire-and-gate a s0' s1  s2' o2)
         (wire-and-gate a s0  s1  s2' o3)
         (wire-and-gate a s0' s1' s2  o4)
         (wire-and-gate a s0  s1' s2  o5)
         (wire-and-gate a s0' s1  s2  o6)
         (wire-and-gate a s0  s1  s2  o7)))))

(defn wire-multi-bit-demux-gate
  ([state a s o0 o1]
   (->> (mapv (fn [a o0' o1'] [a o0' o1']) a o0 o1)
        (reduce (fn [state' [a' o0' o1']]
                  (wire-demux-gate state' a' s o0' o1')) state)))
  ([state a s0 s1 o0 o1 o2 o3]
   (->> (mapv (fn [a o0' o1' o2' o3']
                [a o0' o1' o2' o3'])
              a o0 o1 o2 o3)
        (reduce
         (fn [state' [a' o0' o1' o2' o3']]
           (wire-demux-gate state'
                            a' s0 s1
                            o0' o1' o2' o3')) state)))
  ([state a s0 s1 s2
    o0 o1 o2 o3
    o4 o5 o6 o7]
   (->> (mapv
         (fn [a o0' o1' o2' o3'
              o4' o5' o6' o7']
           [a o0' o1' o2' o3'
            o4' o5' o6' o7'])
         a o0 o1 o2 o3
         o4 o5 o6 o7)
        (reduce
         (fn [state' [a' o0' o1' o2' o3'
                      o4' o5' o6' o7']]
           (wire-demux-gate state'
                            a' s0 s1 s2
                            o0' o1' o2' o3'
                            o4' o5' o6' o7')) state))))

(defn wire-half-adder-gate
  "implements half-adder in : i0, i1
   out :c(carry) s(sum)"
  [state i0 i1 c s]
  (-> state
      (wire-and-gate i0 i1 c)
      (wire-xor-gate i0 i1 s)))

(-> (trigger-many
     (wire-half-adder-gate empty-state :a :b :c :s)
     [:a :b]
     [0 1])
    :charge-map
    (select-keys [:c :s]))


(defn wire-full-adder-gate
  "implements half-adder in : i0, i1
   out :c(carry) s(sum)"
  [state a b c carry sum]
  (let [bc (rand-wire)
        bxc (rand-wire)
        abxc (rand-wire)]
    (-> state
        (wire-xor-gate a b c sum)
        (wire-and-gate b c bc)
        (wire-xor-gate b c bxc)
        (wire-and-gate a bxc abxc)
        (wire-or-gate bc abxc carry))))

(-> (trigger-many
     (wire-full-adder-gate empty-state
                           :a :b :c :ca :s)
     [:a :b :c]
     [0 0 0])
    :charge-map
    (select-keys [:ca :s]))

(defn wire-multi-adder-gate
  [state a b out]
  (let [cs (repeatedly  (count out) rand-wire)
        state'
        (wire-half-adder-gate state (first a) (first b)
                              (first cs) (first out))]
    (->> (map (fn [i1 i2 c1 c2 o]
                [i1 i2 c1 c2 o])
              (rest a) (rest b) cs (rest cs) (rest out))
         (reduce (fn [s [a b c1 c2 o]]
                   (wire-full-adder-gate s a b c1 c2 o)) state'))))

(-> (trigger-many
     (wire-multi-adder-gate empty-state
                            (wires :a 4) (wires :b 4) (wires :o 4))
     (into (wires :a 4) (wires :b 4))
     (reverse [0 1 1 1 0 0 1 0])) ; 0 0 1 0
    :charge-map
    (select-keys (wires :o 4)))


(defn wire-inc-gate
  [state a o]
  (let [c (wires :c (dec (count a)))
        state' (-> (wire-not-gate state (first a) (first o))
                   (wire-half-adder-gate
                    (first a) (second a) (first c) (second o)))]
    (->> (map (fn [a b c d] [a b c d])
              (nthrest a 2) c (rest c) (nthrest o 2))
         (reduce (fn [s [i1 i2 c o]]
                   (wire-half-adder-gate s i1 i2 c o)) state'))))
(rest [1 2 3 4])
(-> (trigger-many
     (wire-inc-gate
      empty-state
      (wires :a 4) (wires :o 4))
     (into (wires :a 4))
     (reverse [0 1 0 1])) ; 0 0 1 0
    :charge-map
    (select-keys (wires :o 4)))

(defn- wire-single-bit-mod
  [state x zx nx ox]
  (let [x' (rand-wire) zx' (rand-wire) nx' (rand-wire)
        xzx'nx' (rand-wire) x'zx'nx (rand-wire) zxnx (rand-wire)]
    (-> state
        (wire-not-gate x x')
        (wire-not-gate zx zx')
        (wire-not-gate nx nx')
        (wire-and-gate x zx' nx' xzx'nx')
        (wire-and-gate x' zx' nx x'zx'nx)
        (wire-and-gate zx nx zxnx)
        (wire-or-gate xzx'nx' x'zx'nx zxnx ox))))
(rand-wires :ox 5)
(defn wire-alu
  [state x y
   zx nx zy ny
   f no zr ng
   out]
  (let [ox (rand-wires :ox (count x))
        x-pair (mapv (fn [i o] [i o]) x ox)
        state#0 (reduce
                 (fn [s [i o]]
                   (wire-single-bit-mod s i zx nx o))
                 state x-pair)
        oy (rand-wires :oy (count y))
        y-pair (mapv (fn [i o] [i o]) x oy)
        state#1 (reduce (fn [s [iy oy]]
                          (wire-single-bit-mod s iy zy ny oy))
                        state#0 y-pair)
        add-wires (wires :add (count x))
        and-wires (wires :and (count x))
        o-op-sel (wires :sel-op (count x))
        o-neg (wires :neg (count x))
        state#2 (-> state#1
                    (wire-multi-adder-gate ox oy add-wires)
                    (wire-multi-bit-and-gate ox oy and-wires)
                    (wire-multibit-mux-gate and-wires add-wires f o-op-sel)
                    (wire-not-gate o-op-sel o-neg)
                    (wire-multibit-mux-gate o-op-sel o-neg no out))
        nzr (rand-wire :nzr)
        state#3 (apply wire-and-gate state#2 (conj out nzr))]
    (-> state#3
        (wire-not-gate nzr zr)
        (wire-and-gate (last out) (last out) ng))))

(wire-and-gate empty-state :a :b :c)

(apply wire-and-gate empty-state [:a :b :c :d])

(conj '(1 2 3 4) 6)


(-> (trigger-many
     (wire-alu
      empty-state [:x] [:y]
      :zx :nx :zy :ny
      :f :no :zr :ng
      (wires :out 4))
     [:x :y
      :zx :nx :zy :ny
      :f :no]
     [1 0
      1 0 1 0
      1 0])
    :charge-map
    (select-keys (into (wires :out 4) [:zr :ng])))

(->> (-> (wire-alu
          empty-state [:x] [:y]
          :zx :nx :zy :ny
          :f :no :zr :ng
          (wires :out 4))
         :nand-gates)
     (filter (comp #{[:out3 :out3]} :ins)))

(defn alu
  "return [zr ng out]"
  [state x y zx nx zy ny f no]
  (let [x (multi-mux x (repeat 0) zx)
        x (multi-mux x (multi-not x) nx)
        y (multi-mux y (repeat 0) zy)
        y (multi-mux y (multi-not y) ny)
        out (multi-mux (multi-and x y) (multi-add x y) f)
        out (multi-mux out (multi-not out) no)
        zr (not-gw (apply or-gw out))
        ng (first out)]
    [zr ng out]))

(defn num->bit-vec
  [n]
  (->> (Integer/toBinaryString n)
       (vec)
       (map (comp #(- % 48) int))))

(defn DFF
  [[_ t] in]
  [in (inc t)])

(defn Bit
  [[out t] in load]
  (DFF [out t] (mux out in load)))

(defn Register
  [[out t] in load]
  (DFF [out t] (multi-mux out in load)))

(defn RAM8
  [[out t] in address load]
  (Register))



