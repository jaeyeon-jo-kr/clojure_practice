(ns four-clojure.102-into-camel-case)

;;When working with java, you often need to create an object with fieldsLikeThis,
;; but you'd rather work with a hashmap that has :keys-like-this until it's time to convert.
;; Write a function which takes lower-case hyphen-separated strings and converts them to camel-case strings.
(comment (= (__ "something") "something")
         (= (__ "multi-word-key") "multiWordKey")
         (= (__ "leaveMeAlone") "leaveMeAlone"))

(fn [s]
  (clojure.string/replace s #"-[a-z]" #(.toUpperCase (str (second %1)))))


(fn [s]
  (clojure.string/replace s #"-[a-z]" "BBB"))
