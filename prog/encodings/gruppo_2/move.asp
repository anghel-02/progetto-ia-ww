#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

% FACTS - added from Group.java
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% moveCell(X,Y,H). --> cell where I can move unit
% myUnit(X,Y,H,U,P) --> represent the choosed unit. X,Y coordinates |H height |U unitCode | P playerCode

% %%AUXILIARY
offset(-1..1). % used to calculate near cells

% %%GUESS
moveIn(X,Y) | moveOut(X,Y) :- moveCell(X,Y,_).

%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.

% WEAK -10 (massima priorità)
% necessario per ottenere sempre un insieme di risposte ottimale
:~ moveIn(X,Y). [0@10]

% Preferire mosse che si avvicinano a celle di altezza 3
:~ moveIn(X, Y), cell(X1, Y1, 3, _), X != X1, Y != Y1. [1@10]

% preferire muoversi su una cella di altezza 3 -> penalità per muoversi su un'altezza diversa da 3
:~ moveIn(X,Y), moveCell(X,Y,H), H<>3. [1@10]

% preferire muoversi su una cella più alta
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H<=Hmy. [1@9]

% Preferire mosse che evitano celle vicine ai nemici
:~ moveIn(X, Y), enemyUnit(X1, Y1), abs(X - X1) <= 1, abs(Y - Y1) <= 1. [2@8]


% Evitare mosse che portano su celle di altezza inferiore
:~ moveIn(X, Y), moveCell(X, Y, H), myUnit(_, _, Hmy, _, _), H < Hmy. [1@6]
