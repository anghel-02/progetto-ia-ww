#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% unit(X,Y,H,U,P). --> represent a unit. X,Y coordinates |H height |U unitCode | P playerCode
% choosedUnit(U). --> in case of 2 units per player.
% moveCell(X,Y,H). --> cell where I can move unit 
% enemyMoveCell(X,Y,H,U).

offset(-1..1).


%%AUXILIARY
myUnit(X,Y,H,U):- unit(X,Y,H,U,_), choosedUnit(U). 
friendUnit(X,Y,H,U):- unit(X,Y,H,U,P), player(P).
enemyUnit(X,Y,H,U):- unit(X,Y,H,U,Penemy), player(P), Penemy<>P.
validCell(X,Y,H,P) :-cell(X,Y,H,P), H<>4.

%%GUESS
moveIn(X,Y) | moveOut(X,Y) :- moveCell(X,Y,_).

%%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.


%%WEAK 
maxPriority(10).

%
% 0
%
% need this to always get an optimal answerset
:~ moveIn(X,Y),  maxPriority(MAX). [0@MAX ]

% prefer moving to an height 3 cell 
:~ moveIn(X,Y), moveCell(X,Y,H), H<>3,  maxPriority(MAX). [1@MAX] %penalty for moving to an height != 3

%
% 1
%
% prefer not moving away from a (not moveable for myUnit) height 3 cell if the enemy can move to it
nearEnemyMoveCell3(SX,SY,X,Y) :- enemyMoveCell(X,Y,3,U), offset(OffX), &sum(X,OffX;SX), offset(OffY), &sum(Y,OffY;SY), not enemyMoveCell(SX,SY,3,U).

:~ moveIn(X,Y), nearEnemyMoveCell3(X2,Y2,_,_), X<>X2,  maxPriority(MAX).  [1@MAX-1]   % penalty if move to a cell where myUnit can't build on enemyMoveCell(X,Y,3,_)  
:~ moveIn(X,Y), nearEnemyMoveCell3(X2,Y2,_,_), Y<>Y2,  maxPriority(MAX).  [1@MAX-1]   % penalty if move to a cell where myUnit can't build on enemyMoveCell(X,Y,3,_)  

% 2
%


% prefer moving to a height 2 cell if the cell is near an height 3 ->
h2_nearMoveCell3(X,Y,X2,Y2):- validCell(X,Y,3,_), moveCell(X2,Y2,H), H=2, offset(OffX), offset(OffY), &sum(X2,OffX;X), &sum(Y2,OffY;Y), not moveCell(X,Y,3).

:~ moveIn(X,Y), h2_nearMoveCell3(_,_,X2,Y2), X<>X2,  maxPriority(MAX). [1@MAX-2] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  
:~ moveIn(X,Y), h2_nearMoveCell3(_,_,X2,Y2), Y<>Y2,  maxPriority(MAX). [1@MAX-2] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  

%
%3
%
% prefer moving on higher cell
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy  ,  maxPriority(MAX). [1@MAX-3]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-1,  maxPriority(MAX). [2@MAX-3]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-2,  maxPriority(MAX). [3@MAX-3]


% avvicinarsi al nemico

