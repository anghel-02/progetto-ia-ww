%%FACTS
%floor(X,Y,H) --> |externally added
%unit(X,Y,P) --> cell occupied by a unit of player P |externally added
%player(P). --> my Player |externally added
%cell(X,Y). -> playable cell , externally added

%%RULES

#show moveIn/2.
#show buildIn/2.

%--MOVE------------------------------------------------------------------------------

%AUXILIARY 
myUnit(X,Y,H) :- unit(X,Y,P), player(P), floor(X,Y,H).


%GUESS
moveIn(X,Y) | moveOut(X,Y) :- cell(X,Y).

%CHECK

% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.

% can't choose an occupied cell
:- moveIn(X,Y), unit(X,Y,_).

% can move to a floor :
%1) higher than 1 relative to the current floor OR 
%2) lower than the current floor, 
:- moveIn(X,Y), floor(X,Y,H), myUnit(_,_,Hunit),  H > Hunit+2. 



%--BUILD-----------------------------------------------------------------------------

%GUESS
buildIn(X,Y):- myUnit(X,Y,_).



