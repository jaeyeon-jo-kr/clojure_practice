(ns 70-word-sorting)

;; Write a function that splits a sentence up into a sorted list of words.
;; Capitalization should not affect sort order and punctuation should be ignored.

;;;;(= (__  "Have a nice day.")
;   ["a" "day" "Have" "nice"])

(fn [s]
    (sort-by
      clojure.string/upper-case
      (re-seq #"\w+" s)))

