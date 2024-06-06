#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% unit(X,Y,H,U,P). --> represent a unit. X,Y coordinates |H height |U unitCode | P playerCode
% choosedUnit(U). --> in case of 2 units per player.
% moveCell(X,Y,H). --> cell where I can move unit 
% 

%%AUXILIARY
myUnit(X,Y,H,U):- unit(X,Y,H,U,_), choosedUnit(U). 
friendUnit(X,Y,H,U):- unit(X,Y,H,U,P), player(P).
enemyUnit(X,Y,H,U):- unit(X,Y,H,U,Penemy), player(P), Penemy<>P.

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
% prefer not moving away from a (not moveable) height 3 cell if the enemy can move to it
% enemyUnit(X,Y,H) :- cell(X,Y,H,P), myPlayer(MyP), P <> -1, P <> MyP.
% nearEnemy(X,Y,H) :- cell(X,Y,H,_), enemyUnit(XEnemy,YEnemy,_), offset(OffX), offset(OffY), X = XEnemy + OffX, Y = YEnemy + OffY.
% :- nearEnemy(X,Y,_), enemyUnit(X,Y,_).


% :~ moveIn(X,Y), nearEnemy(X2,Y2,3), offset(OffX), offset(OffY),  X > X2 + OffX, Y > Y2+ OffY,  maxPriority(MAX). [1@MAX-1] % penalty if move to a cell where can't build to nearEnemy(X,Y,3)  

%
% 2
%

% se il nemico è su una cella di livello 2 o 3, e nel suo vicinato c'è una cella di livello 3,
% è preferibile spostarti su una cella che faccia sì che tu abbia la cella di liv 3 nel tuo vicinato

% prefer moving to a height 2 cell if the cell is near an height 3 ->
% nearMoveCell(X,Y,H,X2,Y2):- emptyCell(X,Y,H), moveCell(X2,Y2,_), offset(OffX), offset(OffY), X = X2 + OffX, Y = Y2 + OffY.
% :- nearMoveCell(X,Y,_,_,_), moveCell(X,Y,_).

% :~ moveIn(X,Y), nearMoveCell(X,Y,3,X2,Y2), X<>X2,  maxPriority(MAX). [1@MAX-2] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  
% :~ moveIn(X,Y), nearMoveCell(X,Y,3,X2,Y2), Y<>Y2,  maxPriority(MAX). [1@MAX-2] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  

%
%3
%
% prefer moving on higher cell
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy  ,  maxPriority(MAX). [1@MAX-3]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-1,  maxPriority(MAX). [2@MAX-3]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-2,  maxPriority(MAX). [3@MAX-3]




% avvicinarsi al nemico

