power(_X, 0, 1).
power(X, Y, R):-
	NewY is Y-1, power(X, NewY, NewR), R is NewR*X.
eval(X, X):- number(X).
eval(X^Y, R):- eval(X, XR), eval(Y, YR), power(XR, YR, R).
eval(X*Y, R):- eval(X, XR), eval(Y, YR), R is XR*YR.
eval(X/Y, R):- eval(Y, YR), Y=\=0, eval(X,XR), R is XR/YR.
eval(X-Y, R):- eval(X, XR), eval(Y, YR), R is XR-YR.
eval(X+Y, R):- eval(X, XR), eval(Y, YR), R is XR+YR.


simplify(X+0,X).
simplify(0+X,X).
simplify(_X*0,0).
simplify(0*_X,0).
simplify(X*1,X).
simplify(1*X,X).
simplify(_X/0,_R):- false.
simplify(X/1,X).
simplify(0/_X,0).
simplify(X/X,1).
simplify(X-0,X).
simplify(0-X,-X).
simplify(X-X,0).
simplify(_X^0,1).
simplify(X^1,X).
simplify(1^_X,1).
simplify((X*Y)/Y, X).
simplify(X,X):- atomic(X).
simplify(-X,-X):- atomic(X).
simplify(X^Y,R):-
	simplify(X,XR), simplify(Y,YR),
  	exp_helper(XR, YR, R).
simplify(X*Y,R):-
	simplify(X,XR), simplify(Y,YR),
	mul_helper(XR, YR, R).
simplify(X/Y,R):-
	simplify(X,XR), simplify(Y,YR),
     	div_helper(XR,YR,R).
simplify(X+Y,R):-
	simplify(X,XR), simplify(Y,YR),
 	add_helper(XR, YR,R).
simplify(X-Y,R):-
	simplify(X,XR), simplify(Y,YR),
	sub_helper(XR,YR,R).
%if those conditions meet, that means we do not need to simplify anymore.
exp_helper(X,Y,R):- number(X), number(Y), eval(X^Y, R).
exp_helper(X,Y,X^Y):- X\==1, Y\==1, Y\==0.
exp_helper(X,Y,R):- simplify(X^Y,R).
mul_helper(X,Y,R):- number(X), number(Y), eval(X*Y,R).
mul_helper(X,Y*Z, NewR*Z):- number(X), number(Y), eval(X*Y,NewR).
mul_helper(X,Y,X*Y):- X\==0, X\==1, Y\==0, Y\==1.
mul_helper(X,Y,R):- simplify(X*Y,R).
div_helper(X,Y,R):- number(X), number(Y), eval(X/Y,R).
div_helper(X,Y,X/Y):- X\==0, Y\==0, Y\==1, X\==Y.
div_helper(X*Y,Y,X). div_helper(Y*X,Y,X).
div_helper(X,Y,R):- simplify(X/Y,R).
add_helper(X,Y,R):- number(X), number(Y), eval(X+Y,R).
add_helper(X,Y,X+Y):- X\==0, Y\==0.
add_helper(X,Y,R):- simplify(X+Y,R).
sub_helper(X,Y,R):- number(X), number(Y), eval(X-Y,R).
sub_helper(X,Y,X-Y):- X\==0, Y\==0,X\==Y.
sub_helper(X,Y,R):- simplify(X-Y,R).
	
deriv(X,R):- simplify(X, NewR), deriv_helper(NewR, R).
deriv_helper(X^Y, R):- Exp is Y-1, simplify(Y*X^Exp, R).
deriv_helper(X, 0):- number(X).
deriv_helper(X, 1):- atomic(X).
deriv_helper(X*Y, R):- number(X), number(Y), eval(X*Y,R).
deriv_helper(X*Y, X):- number(X), atomic(Y).
deriv_helper(X*Y, Y):- number(Y), atomic(X).
deriv_helper(X*Y^Z, R):- deriv(Y^Z ,YR),!, simplify(X*YR, R).
deriv_helper(X/Y, NewX/Y^2):- number(X), atomic(Y), NewX is -X.
deriv_helper(X/Y^Z,R):- NewZ is Z+1, NewX is -X*Z, simplify(NewX/Y^NewZ, R).
deriv_helper(X+Y,R):- deriv_helper(X, XR), deriv_helper(Y, YR),!, check_neg(XR+YR, Result),!, simplify(Result,R).
deriv_helper(X-Y,R):- deriv_helper(X, XR), deriv_helper(Y, YR),!, check_neg(XR-YR, Result),!, simplify(Result,R).

check_neg(X+Y, X-R):- is_negative(Y, R).
check_neg(X+Y, X+Y).
check_neg(X-Y, X+R):- is_negative(Y, R).
check_neg(X-Y,X-Y).
is_negative(-_,_).
is_negative(A, NewArg) :-
    compound(A),                     
    A =.. [Functor|Args],              
    member(Arg, Args),                 
    is_negative(Arg, NegArg),          
    NegArg is -Arg,                   
    my_replace([Arg, NegArg], Args, NewArgs),
    NewArg =.. [Functor|NewArgs].      
is_negative(A,_) :-
    number(A),
    A < 0.
my_assoc(_A, [], _R):- false. 
my_assoc(A, [A|T], R):- get_value(T, R), !.
my_assoc(A, [_H|T], R):- my_assoc_next(A,T,R).
get_value([H|_T], H).
my_assoc_next(A, [_H|T], R):- my_assoc(A,T,R).
my_replace(_A,[],[]).
my_replace(A, [H|T], [NewR|R]):- my_assoc(H, A, NewR), my_replace(A,T,R),!.
my_replace(A, [H|T], [H|R]):- my_replace(A,T,R).


gender_checker(X, Y) :-
    (male(X), female(Y); female(X), male(Y); male(X), male(Y)).
language_checker(X,Y):-
    speaks(X, L), speaks(Y, L).
head_and_tail_checker(L,I,J):-
    my_nth(L, I, X), my_nth(L, J , Y),
    gender_checker(X,Y),
    language_checker(X,Y).

party_seating(L) :-
    L = [_,_,_,_,_,_,_,_,_,_],
    findall(Person, (male(Person); female(Person)), P),
    permutation(P, L),
    \+ (nth0(I,L, X), nth0(J, L, Y), abs(I - J) =:= 1, \+ (gender_checker(X, Y), language_checker(X, Y))),
    head_and_tail_checker(L,0,9).

my_nth([H|_], 0, H).
my_nth([_H|T], N, NewR):- NewN is N-1,  my_nth(T, NewN, NewR).
	
