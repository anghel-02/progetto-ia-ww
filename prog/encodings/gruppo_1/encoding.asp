%%FACTS
%floor(X,Y,H) -->   |externally added 
%unit(X,Y,P) --> externally added 
%player(P) --> externally added


%%RULES

#show moveIn/2.
#show buildIn/2.

%%AUXILIARY
%cell(X,Y). -> playable cell , externally added 

%%GUESS
moveIn(X,Y) | moveOut(X,Y) :- cell(X,Y).
buildIn(X,Y) | buildOut(X,Y) :- cell(X,Y).


%%CHECK

%MOVE------------------------------------------------------------------------------

% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.

% can't choose an occupied cell
:- moveIn(X,Y), unit(X,Y).

% can't move to a floor higher than 1 relative to the current floor  




%-BUILD-----------------------------------------------------------------------------
% can't choose an occupied cell 
:- buildIn(X,Y), unit(X,Y).

% can choose only one cell
:- #count{X,Y : buildIn(X,Y)} <> 1.