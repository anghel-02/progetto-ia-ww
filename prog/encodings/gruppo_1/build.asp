#show buildIn/2.

% --BUILD----------------------------------------------------------------------------

% FACTS
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cell (-1 if no player)
% buildCell(X,Y,H). --> cell where I can build
% myUnit(X,Y,H,U,P). --> represent the chosen unit. X,Y coordinates |H height |U unitCode | P playerCode

% AUXILIARY
myPlayer(P) :- myUnit(_,_,_,_,P).
offset(-1..1). % used to calculate nearby cells
emptyCell(X,Y) :- cell(X,Y,H,-1), H <> 4. % playable cell with no player

% GUESS
buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y,_).

% CHECK
% can build on only one cell
:- #count{X,Y : buildIn(X,Y)} <> 1.

% WEAK -10 (max priority)
% need this to always get an optimal answerset
:~ buildIn(X,Y).[0@10]

% prefer to build height 4 on a buildable height 3 cell near the enemy
enemyUnit(X,Y,H) :- cell(X,Y,H,P), myPlayer(MyP), P <> -1, P <> MyP.
nearEnemy(X,Y,H) :- cell(X,Y,H,_), emptyCell(X,Y), enemyUnit(XEnemy,YEnemy,H), offset(OffX), offset(OffY), X == XEnemy + OffX, Y == YEnemy + OffY.

:~ buildIn(X,Y), nearEnemy(X2,Y2,3), X <> X2, Y <> Y2. [1@10]

% prefer to build on cell height equal to my unit cell height
:~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_,_), H <> Hmy. [1@9]