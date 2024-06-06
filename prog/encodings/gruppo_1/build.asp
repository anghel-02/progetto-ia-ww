#show buildIn/2.

% --BUILD----------------------------------------------------------------------------

%%FACTS
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cell (-1 if no player)
% unit(X,Y,H,U,P). --> represent a unit. X,Y coordinates |H height |U unitCode | P playerCode
% choosedUnit(U). --> in case of 2 units per player.
% buildCell(X,Y,H). --> cell where I can build
% enemyMoveCell(X,Y,H,U).

% AUXILIARY
myUnit(X,Y,H,U):- unit(X,Y,H,U,_), choosedUnit(U). 
friendUnit(X,Y,H,U):- unit(X,Y,H,U,P), player(P).
enemyUnit(X,Y,H,U):- unit(X,Y,H,U,Penemy), player(P), Penemy<>P.


% GUESS
buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y,_).

% CHECK
% can build on only one cell
:- #count{X,Y : buildIn(X,Y)} <> 1.

% WEAK -10 
maxPriority(10).

%
%0
%
% need this to always get an optimal answerset
:~ buildIn(X,Y),  maxPriority(MAX). [0@MAX]

% prefer to build (height 4) on a buildable height 3 cell near the enemy
:~ buildIn(X,Y), buildCell(X2,Y2,3), enemyMoveCell(X2,Y2,3,_), X <> X2,  maxPriority(MAX). [1@MAX] % penalize if exist a buildAble enemyMoveCell(X,Y,3,_) and don't build on it 
:~ buildIn(X,Y), buildCell(X2,Y2,3), enemyMoveCell(X2,Y2,3,_), Y <> Y2,  maxPriority(MAX). [1@MAX] % penalize if exist a buildAble enemyMoveCell(X,Y,3,_) and don't build on it 

%
%1
%
% prefer to build on cell height equal to my unit cell height
:~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_), H <> Hmy,  maxPriority(MAX). [1@MAX-1]


