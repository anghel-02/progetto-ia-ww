% griglia 5x5
row(0..4).
col(0..4).

cell(X,Y) :- row(X), col(Y).