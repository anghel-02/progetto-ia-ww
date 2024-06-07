#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

% %%FACTS - added from Group.java
% %cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% %moveCell(X,Y,H). --> cell where I can move unit
% %myUnit(X,Y,H,U,P) --> represent the choosed unit. X,Y coordinates |H height |U unitCode | P playerCode


%AUXILIARY
offset(-1..1). % used to calculate near cells

%GUESS
moveIn(X,Y) | moveOut(X,Y) :- moveCell(X,Y,_).

%check
%can choose only one cell
:-#count{X,Y : moveIn(X,Y)} <>1.

% prefer moving to an height 3 cell (you win) -> penalty for moving to a height != 3
:~ moveIn(X,Y), moveCell(X,Y,H), H<>3. [1@10]

% prefer moving on higher cell da rivedere il livello di priorità (fatta cosi penalizza anche scendere di troppe celle)
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H=Hmy. [1@9]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H=Hmy-1. [2@9]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H=Hmy-2. [3@9]

% controllare il centro 
:~ moveIn(X,Y), moveCell(2,2,_), myUnit(X,Y,_,_,_),X!=2, Y!=2. [1 @ 8]

% spostarsi dove ho celle 3 vicine  
myUnitCord(X, Y):- myUnit(X, Y,_,_,_).
near(X1,Y1,H) :- offset(DX), offset(DY),  myUnit(X1, Y1, H, _, _), notMyUnitCord(X, Y), X1 = X + DX, Y1 = Y + DY.
:~ moveIn(X, Y), near(X, Y, H), H != 3. [1 @ 7]

% don't move on cell level 4,a
:~ moveIn(X,Y), moveCell(X,Y,4). [1@6, X,Y]