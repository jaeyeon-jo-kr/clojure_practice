(ns four-clojure.anagram-finder)

(fn [coll]
  (let [char-cnt (fn [str]
                   (reduce-kv
                     (fn [m k v]
                       (assoc m k (count v))) {}
                     (group-by identity str)))]
    (set (filter #(not= (count %) 1) (set (vals (group-by char-cnt coll)))))))

