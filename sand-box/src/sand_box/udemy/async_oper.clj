(ns sand-box.udemy.async-oper
  (:require [clojure.core.async :refer [chan put! take!]]))



(let [c (chan)]
  (put! c 42 (fn [v] (println "Sent: " v)))
  (comment (take! c (fn [v] (println "Got : " v)))))
