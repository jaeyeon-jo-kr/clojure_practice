
;;Write a function which will split a sequence into two parts.

#(list (take %1 %2) (take-last (- (count %2) %1) %2))
