(ns sand-box.bit-test
  (:require 
   [sand-box.bit? :refer [trigger-many
                          wire-multi-bit-and-gate
                          empty-state
                          wire-mux-gate
                          wire-multi-bit-demux-gate
                          wire-or-gate
                          wire-and-gate
                          wire-xor-gate]]))

(trigger-many
 (wire-multi-bit-and-gate
  empty-state
  [:a1 :a2 :a3 :a4] [:b1 :b2 :b3 :b4] [:o1 :o2 :o3 :o4])
 [:a1 :a2 :a3 :a4 :b1 :b2 :b3 :b4]
 [0 0 1 1 1 0 1 1])

(trigger-many
 (wire-mux-gate empty-state
                :a :b :c :d :e :f :g :h
                :s0 :s1 :s2
                :o)
 [:a :b :c :d :e :f :g :h :s0 :s1 :s2]
 [1 0 0 0 0 0 0 0
  0 0 0])

(-> (trigger-many
     (wire-multi-bit-demux-gate
      empty-state (wires :a 8) :s (wires :b 8) (wires :c 8))
     (conj (wires :a 8) :s)
     [0 1 0 1
      1 1 1 1
      1])
    :charge-map
    (select-keys (into (wires :b 8)
                       (wires :c 8))))

(-> (trigger-many
     (apply wire-demux-gate
            empty-state :a (into (wires :s 2) (wires :o 4)))
     (into [:a] (wires :s 2))
     [1 1 1])
    :charge-map
    (select-keys [:o0 :o1 :o2 :o3]))

(trigger-many
 (wire-demux-gate empty-state :a :s :o#0 :o#1)
 [:a :s] [1 0])

(->> (trigger-many
      (wire-xor-gate empty-state :a :b :c :o)
      [:a :b :c]
      [0 0 0])
     :charge-map
     :o)

(-> (trigger-many
     (wire-and-gate empty-state :a :b :c :d :o)
     [:a :b :c :d] [0 1 1 1])
    :charge-map
    :o)