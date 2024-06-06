#show buildIn/2.
%--BUILD-----------------------------------------------------------------------------

%FACTS
%cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
%buildCell(X,Y,H). --> cell where I can build 
%myUnit(X,Y,H,U,P) --> represent the choosed unit. X,Y coordinates |H height |U unitCode | P playerCode  

%%AUXILIARY
myPlayer(P):- myUnit(_,_,_,_,P).
offset(-1..1). % used to calculate near cells

%GUESS
buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y,_).

%CHECK
% can build only one cell.
:- #count{X,Y : buildIn(X,Y)} <> 1. 

%WEAK -10(max priority)

% prefer build H4 on a buildable H3 cell near the enemy
enemyUnit(X,Y,H):- cell(X,Y,H,P), myPlayer(MyP), P<>-1, P<>MyP.
nearEnemy(X,Y,H):- cell(X,Y,H,_), enemyUnit(XEnemy,YEnemy,_), offset(Off), X = XEnemy+Off, Y = YEnemy+Off.  

:~ buildIn(X,Y), nearEnemy(X2,Y2,3), X<>X2, Y<>Y2. [1@10]

% prefer build on cell high as my unit cell.
:~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_,_), H<>Hmy . [1@9]

