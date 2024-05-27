%%FACTS
%floor(X,Y,H) --> |externally added
%unit(X,Y,P) --> cell occupied by a unit of player P |externally added
%player(P). --> my Player |externally added
%cell(X,Y). -> playable cell , externally added

%%RULES

#show moveIn/2.
#show buildIn/2.

%%AUXILIARY




%%GUESS
moveIn(X,Y) | moveOut(X,Y) :- cell(X,Y).

%%CHECK

%MOVE------------------------------------------------------------------------------

% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.

% can't choose an occupied cell

:- moveIn(X,Y), unit(X,Y,_).

% can't moveIn to a floor higher than 1 relative to the current floor




%-BUILD-----------------------------------------------------------------------------
%PER ORA COSTRUISCE DOVE SI TROVAVA PRIMA DI MOVE
buildIn(X,Y):- unit(X,Y,P), player(P).



