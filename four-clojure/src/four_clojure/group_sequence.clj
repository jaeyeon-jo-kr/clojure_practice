(fn [f seqs]
  (reduce
   (fn [m e]
     (let [k (f e)
           prev (m k)
           v  (reverse (conj (reverse prev) e))]
     (assoc m k v)))
   {} seqs))
  
  



  
