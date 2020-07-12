(ns four-clojure.158-decurry)

;;Write a function that accepts a curried function of unknown arity n. Return an equivalent function of n arguments.
;;You may wish to read this.

(comment
  (= 10 ((__ (fn [a]
               (fn [b]
                 (fn [c]
                   (fn [d]
                     (+ a b c d))))))
         1 2 3 4))
  (= 24 ((__ (fn [a]
               (fn [b]
                 (fn [c]
                   (fn [d]
                     (* a b c d))))))
         1 2 3 4))
  (= 25 ((__ (fn [a]
               (fn [b]
                 (* a b))))
         5 5)))
(defn ex1
  [f]
  ((f (fn [a]
        (fn [b]
          (fn [c]
            (fn [d]
              (+ a b c d))))))
   1 2 3 4))
(defn ex2 [f]
  (f (fn [a]
       (fn [b]
         (fn [c]
           (fn [d]
             (* a b c d)))))
   1 2 3 4))

(def solution
  (fn f [fs]
    (fn g [& ns]
      (print ns)
      (if (empty? ns)
        fs
        (apply (f (fs (first ns))) (rest ns))))))






