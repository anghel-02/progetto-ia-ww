%%FACTS

%cell(X,Y). -> playable cell , added by PlayerAi

%%RULES

#show in/2.

in(X,Y) | out(X,Y) :- cell(X,Y).

:- in(X,Y), unit(X,Y).
:- in(X,Y), floor(X,Y,4).

% can choose only one cell
:- #count{X,Y : in(X,Y)} <> 1.