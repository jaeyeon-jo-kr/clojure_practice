(fn [string]
  (assoc {}
    :suit
    (condp = (first string)
      \D :diamond
      \H :heart
      \C :club
      \S :spade)
    :rank
    (condp = (second string)
      \T 8
      \J 9
      \Q 10
      \K 11
      \A 12
      (- (int (second string)) (int \2)))))
