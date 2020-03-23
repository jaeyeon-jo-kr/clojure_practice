(ns 65-black-box-testing)

;;Clojure has many sequence types, which act in subtly different ways.
;; The core functions typically convert them into a uniform "sequence" type and work with them that way, but it can be important to understand the behavioral and performance differences so that you know which kind is appropriate for your application.
;
;Write a function which takes a collection and returns one of :map, :set, :list, or :vector - describing the type of collection it was given.
;You won't be allowed to inspect their class or use the built-in predicates like list? -
; the point is to poke at them and understand their behavior.


;;class
;type
;Class
;vector?
;sequential?
;list?
;seq?
;map?
;set?
;instance?
;getClass


;;              list vector set map
;; reversible?   f     t     f   f
;; associative?  f     t     f   t

(fn [c]
    (cond
      (and (reversible? c) (associative? c)) :vector
      (and (not (reversible? c)) (associative? c)) :map
      (= 2 (count (conj (empty c) 1 1))) :list
      :else :set))
