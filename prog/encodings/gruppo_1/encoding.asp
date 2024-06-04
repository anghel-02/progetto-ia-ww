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
% :- #count{X,Y : moveIn(X,Y)} <> 1.
:~ #count{X,Y : buildIn(X,Y)} = 0. [2@1]
:~ moveIn(X,Y). [1@1, X,Y]

% can't choose an occupied cell
unit(X,Y,P) :- cell(X,Y,_,P), P<>-1.
:- moveIn(X,Y), unit(X,Y,_).

% can move to a floor :
%1) higher than 1 relative to the current floor OR 
%2) lower than the current floor, 
:- moveIn(X,Y), cell(X,Y,H,_), myUnit(_,_,Hunit,_),  H > Hunit+1.
%--BUILD-----------------------------------------------------------------------------
%%AUXILIARY

%Buildable cells
enemyUnit(X,Y) :- unit(X,Y,Enemy), player(P), Enemy<>P . %funziona solo con una pedina
floorRemoved(X,Y):- cell(X,Y,4,_).
temp(X,Y):- cell(X,Y,_,_), not moveIn(X,Y),not floorRemoved(X,Y), not enemyUnit(X,Y).
% Math.abs(toBuildX-currentX)  > 1 || Math.abs(toBuildY-currentY)  > 1 
buildCell(X1,Y1):-temp(X1,Y1), moveIn(X2,Y2), X1<>X2, Y1<>Y2, X=X1-X2,&abs(X;ZX), Y=Y1-Y2,&abs(Y;ZY), ZX<=1, ZY<=1.



%GUESS
buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y).
% buildIn(X,Y) :- myUnit(X,Y,_,_).

%CHECK
% can build only one cell, use weak to allow to get an answerset when the unit cannot build . 
% :- #count{X,Y : buildIn(X,Y)} <> 1.
:~ #count{X,Y : buildIn(X,Y)} = 0. [2@1]
:~ buildIn(X,Y). [1@1, X,Y]


% cell(0,0,0,-1). cell(0,1,0,-1). cell(0,2,0,0). cell(0,3,0,-1). cell(0,4,0,-1). cell(1,0,0,-1). cell(1,1,0,-1). cell(1,2,0,-1). cell(1,3,0,-1). cell(1,4,0,-1). cell(2,0,0,-1). cell(2,1,0,-1). cell(2,2,0,-1). cell(2,3,0,-1). cell(2,4,0,-1). cell(3,0,0,-1). cell(3,1,0,1). cell(3,2,0,-1). cell(3,3,0,-1). cell(3,4,0,-1). cell(4,0,0,-1). cell(4,1,0,-1). cell(4,2,0,-1). cell(4,3,0,-1). cell(4,4,0,-1). player(0). myUnit(0,2,0,0). playCell(0,1). playCell(1,1). playCell(1,2). playCell(1,3). playCell(0,3).



