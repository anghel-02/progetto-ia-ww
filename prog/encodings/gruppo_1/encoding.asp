#show moveIn/2.
#show buildIn/2.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
%cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
%player(P). --> my player code
%myUnit(X,Y,H,P). --> cell occupied by my unit BEFORE moving
%playCell(X,Y). --> cell where I can move unit 

%%AUXILIARY

%%GUESS
moveIn(X,Y) | moveOut(X,Y) :- playCell(X,Y).

%%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.

% can't choose an occupied cell
unit(X,Y) :- cell(X,Y,_,P), P<>-1.
:- moveIn(X,Y), unit(X,Y).

% can move to a floor :
%1) higher than 1 relative to the current floor OR 
%2) lower than the current floor, 
:- moveIn(X,Y), cell(X,Y,H,_), myUnit(_,_,Hunit,_),  H > Hunit+1.

%--BUILD-----------------------------------------------------------------------------

%AUXILIARY

%cell occupied by an enemy unit
% enemyUnit(X,Y) :- unit(X,Y,Enemy), player(P), P<>Enemy.

% buildCell(X,Y):- floor(X,Y,_), not moveIn(X,Y), not floor(X,Y,4), not enemyUnit(X,Y).
% :- buildCell(X,Y), moveIn(X1,Y1), X<>X1, Y<>Y1,  X > X1+1 .
% :- buildCell(X,Y), moveIn(X1,Y1), X<>X1, Y<>Y1,  X< X1-1 .

% :- buildCell(X,Y), moveIn(X1,Y1), X<>X1, Y<>Y1,  Y> Y1+1 .
% :- buildCell(X,Y), moveIn(X1,Y1), X<>X1, Y<>Y1,  Y< Y1-1 .

%GUESS
% buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y).
buildIn(X,Y) :- myUnit(X,Y,_,_).

%CHECK
% can build only one cell
% :- #count{X,Y : buildIn(X,Y)} <> 1.






