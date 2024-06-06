#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

% %%FACTS - added from Group.java
% %cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% %moveCell(X,Y,H). --> cell where I can move unit 
% %myUnit(X,Y,H,U,P) --> represent the choosed unit. X,Y coordinates |H height |U unitCode | P playerCode  


% %%AUXILIARY
offset(-1..1). % used to calculate near cells

% %%GUESS
moveIn(X,Y) | moveOut(X,Y) :- moveCell(X,Y,_).

% %%CHECK
% % can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.


% %%WEAK - 10 (max priority)
% % need this to always get an optimal answerset
:~ moveIn(X,Y).[0@10]

% % prefer moving to an height 3 cell -> penalty for moving to a height != 3
:~ moveIn(X,Y), moveCell(X,Y,H), H<>3. [1@10]


% prefer moving on higher cell
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H=Hmy. [1@8]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H=Hmy-1. [1@8]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_,_), H=Hmy-2. [1@8]

% nearMe(X,Y,H):- cell(X,Y,H,_), myUnit(XMy,YMy,_), offset(Off), X = XMy+Off, Y = YMy+Off.

