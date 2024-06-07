#show buildIn/2.

% --BUILD----------------------------------------------------------------------------

% FACTS
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cell (-1 if no player)
% buildCell(X,Y,H). --> cell where I can build
% myUnit(X,Y,H,U,P). --> represent the chosen unit. X,Y coordinates |H height |U unitCode | P playerCode

% AUXILIARY
myPlayer(P) :- myUnit(_,_,_,_,P).

% used to calculate nearby cells
offset(-1..1). 

% playable cell with no player
emptyCell(X,Y) :- cell(X,Y,H,-1), H <> 4. 

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
nearEnemy(X,Y,H) :- cell(X,Y,H,_), emptyCell(X,Y), enemyUnit(XEnemy,YEnemy,H), offset(OffX), offset(OffY), X = XEnemy + OffX, Y = YEnemy + OffY.

%splittare in due e aggiungere buildcell (per capire se posso cstruire su una cella di livello 3 vicino a lui)
:~ buildIn(X,Y), nearEnemy(X2,Y2,3), buildCell(X,Y,3), X <> X2, Y <> Y2. [1@10]

%don't build cell near enemy,a
%:~ buildIn(X, Y), buildCell(X,Y,3), nearEnemy(X1,Y1,H), X != X1, Y != Y1. [2@10, X,Y,X1,Y1,H]

%prefer not to build height 4 on a near cell
myUnitCord(X, Y):- myUnit(X,Y,_,_,_).
nearThree(X1, Y1):-offset(DX), offset(DY), myUnit(X, Y, 3, _, _), not myUnitCord(X,Y), X1 = X + DX, Y1 = Y + DY.
:~ buildIn(X, Y), nearThree(X, Y), buildCell(X, Y, 4). [1@7]

% prefer to build on cell height equal to my unit cell height
:~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_,_), H <> Hmy. [1@9]

% prefer to build height 3 on a near cell
myUnitCord(X, Y):- myUnit(X,Y,_,_,_).
nearTwoHeight(X1, Y1) :-offset(DX), offset(DY), myUnit(X, Y, 2, _, _), not myUnitCord(X,Y), X1 = X + DX, Y1 = Y + DY.
:~ buildIn(X, Y), nearTwoHeight(X1, Y1), buildCell(X1, Y1, H), H <> 3. [1@8]

