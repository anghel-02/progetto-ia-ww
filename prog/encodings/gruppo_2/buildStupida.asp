#show buildIn/2.
%--BUILD-----------------------------------------------------------------------------

%FACTS
%cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
%moveCell(X,Y). --> cell where I can build 

%%AUXILIARY


%GUESS
buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y).

%CHECK
% can build only one cell.
:- #count{X,Y : buildIn(X,Y)} <> 1. 
