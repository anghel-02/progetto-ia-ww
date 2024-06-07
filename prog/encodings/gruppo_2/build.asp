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


% WEAK -10 (massima priorità)
% necessario per ottenere sempre un insieme di risposte ottimale
:~ buildIn(X,Y).[0@10]

% preferire costruire l'altezza 4 su una cella giocabile di altezza 3 vicino al nemico
enemyUnit(X,Y,H) :- cella(X,Y,H,P), myPlayer(MyP), P <> -1, P <> MyP.
nearEnemy(X,Y,H) :- cella(X,Y,H,_), emptyCell(X,Y), enemyUnit(XEnemy,YEnemy,H), offset(OffX), offset(OffY), X == XEnemy + OffX, Y == YEnemy + OffY.

% Evita che l'avversario completi una linea
:- buildIn(X, Y), myUnit(_, _, _, _, P), enemyUnit(X, Y, H), H == 3, P <> -1.

:~ buildIn(X,Y), nearEnemy(X2,Y2,3), X <> X2, Y <> Y2. [1@10]

% preferire costruire su celle che portano l'altezza a 3
:~ buildIn(X,Y), cella(X,Y,2,_). [1@8]

% Evitare di costruire su celle vicine ai nemici
:~ buildIn(X, Y), enemyUnit(X1, Y1, _), abs(X - X1) <= 1, abs(Y - Y1) <= 1. [2@6]

% Evitare di costruire su celle occupate dai nemici
:~ buildIn(X, Y), enemyUnit(X, Y, _). [1@4]

% Preferire costruire su celle adiacenti alle unità nemiche
:~ buildIn(X, Y), enemyUnit(X1, Y1, _), abs(X - X1) <= 1, abs(Y - Y1) <= 1. [2@2]

% Preferire costruire su celle che non hanno unità vicine
:~ buildIn(X, Y), cella(X, Y, _, -1), #count{X1,Y1 : enemyUnit(X1,Y1,_), abs(X - X1) <= 1, abs(Y - Y1) <= 1} = 0. [1@1]