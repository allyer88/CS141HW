my_length([],0).
my_length([_H|T],R):- my_length(T,Tlength), R is Tlength+1.

my_member(A,[A|_T]).
my_member(A,[H|T]):- A\==H, my_member(A,T).

my_append([],L,L).
my_append([H|T],L, [H|NewT]):- my_append(T,L,NewT).


my_reverse([], []).
my_reverse(L, R):- my_reverse_helper(L,[],R).
my_reverse_helper([], X ,X).
my_reverse_helper([H|T], X, R):- my_reverse_helper(T, [H|X], R).
my_reverse_helper(H ,T,[RT,H]):- my_reverse(T,[RT,H]). 

my_nth([], _N, []).
my_nth(T, 1, T):-!.
my_nth([_H|T], N, NewR):- NewN is N-1,  my_nth(T, NewN, NewR),!.

my_remove(_A,[],[]).
my_remove(A, [H|T], [H|R]):- A\==H, my_remove(A,T,R),!.
my_remove(A, [_H|T], R):- my_remove(A, T, R).

my_subst(_X,_Y,[],[]).
my_subst(X, Y, [X|T], [Y|R]):- my_subst(X, Y, T, R),!.
my_subst(X, Y, [H|T], [H|R]):- my_subst(X, Y, T, R).

my_subset(_P, [], []).
my_subset(P, [H|T], [H|R]):- Terms =.. [P,H],Terms,  my_subset(P, T, R), !.
my_subset(P, [_H|T], R):- my_subset(P, T, R).

my_merge([], L2, L2):-!.
my_merge(L1, [], L1):-!.
my_merge([H1|T1], [H2|T2], [H1|R]):- H1=<H2, my_merge(T1, [H2|T2], R),!.
my_merge([H1|T1], [H2|T2], [H2|R]):- my_merge([H1|T1], T2, R).


my_sublist(_L1, []):- false.
my_sublist([], _L2).
my_sublist([H1|T1], [H1|T2]):- my_sublist_helper(T1, T2),!.
my_sublist([H1|T1], [_H2|T2]):- my_sublist([H1|T1], T2),!.
my_sublist_helper([], _L2).
my_sublist_helper([H1|T1],[H1|T2]):- my_sublist([H1|T1],[H1|T2]).
my_sublist_helper([_H1|_T1],[_H2|_T2]):- false.

my_assoc(_A, [], _R):- false. 
my_assoc(A, [A|T], R):- get_value(T, R), !.
my_assoc(A, [_H|T], R):- my_assoc_next(A,T,R).
get_value([H|_T], H).
my_assoc_next(A, [_H|T], R):- my_assoc(A,T,R).

my_replace(_A,[],[]).
my_replace(A, [H|T], [NewR|R]):- my_assoc(H, A, NewR), my_replace(A,T,R),!.
my_replace(A, [H|T], [H|R]):- my_replace(A,T,R).

my_add([H1|T1], [H2|T2], [S|R]):-
	Sum is H1+H2, S is (Sum mod 10),  Carry is (Sum div 10), my_add_helper(T1, T2,Carry,R).
my_add_helper([H1|T1],T2,Carry,R):- Carry==1,H1\==[], NewH1 is H1+1, my_add([NewH1|T1], T2, R).
my_add_helper([], T2, Carry,R):- Carry=1,T2\==[], my_add([1], T2,R).
my_add_helper([],[], Carry,R):- Carry==1, my_add([1],[0],R).
my_add_helper([], T2,_,T2).
my_add_helper(T1,T2,_,R):- my_add(T1,T2,R).
