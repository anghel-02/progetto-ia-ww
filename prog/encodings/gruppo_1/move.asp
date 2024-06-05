#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
%cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
%moveCell(X,Y). --> cell where I can move unit 

%%AUXILIARY

%%GUESS
moveIn(X,Y) | moveOut(X,Y) :- moveCell(X,Y).

%%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.


%%WEAK
% prefer to move to a height 3 cell


