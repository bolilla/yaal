/**
 * YAAL Grammar
 */
 grammar yaal;

 /**There is only one policy, which is composed by one or more rules*/
 policy
 :
 	'policy' ID
 	(
 		'(' combining_algo ')'
 	)? 'begin' pol_rule+ 'end'
 ;

 /**How to decide which rule to apply*/
 combining_algo
 :
 	'Deny-overrides'
 	| 'Permit-overrides'
 	| 'First-applicable' //Default Value

 	| 'Only-one-applicable'
 	| 'Custom-combining-algorithm' ':' ID //In case of using a non-standard algorithm

 ;

 /** One rule of the policy. action is PERMIT by default*/
 pol_rule
 :
 	'rule' ID 'begin'
 	(
 		'action' action_id
 	)? 'target' condition 'condition' condition 'end'
 ;

 condition
 :
 	'not' condition
 	| condition bool_op condition
 	| condition bool_comp condition
 	| arit_val arit_comp arit_val
 	| str_val str_comp str_val
 	| bool_val
 ;

 action_id
 :
 	'PERMIT'
 	| 'DENY'
 ;

 bool_op
 :
 	'&'
 	| '|'
 ;

 bool_comp
 :
 	'='
 	| '!='
 ;

 arit_val
 :
 	NUM
 	| categ_attr
 ;

 arit_comp
 :
 	'='
 	| '!='
 	| '<'
 	| '>'
 	| '>='
 	| '<='
 ;

 bool_val
 :
 	'TRUE'
 	| 'FALSE'
 ;

 categ_attr
 :
 	ID '.' ID
 ;

 str_val
 :
 	categ_attr
 	| STRING
 ;

 str_comp
 :
 	'='
 	| '<>'
 ;

 ID
 :
 	LETTER
 	(
 		LETTER
 		| NUM
 		| '_'
 	)*
 ;

 LETTER
 :
 	(
 		[a-z]
 		| [A-Z]
 	)
 ;

 NUM
 :
 	[0-9]+
 ;

 WS
 :
 	[ \t\r\n]+ -> skip
 ;

 STRING
 :
 	'"'
 	(
 		ESC
 		| ~[\\"]
 	)* '"'
 ;

 ESC
 :
 	'\\"'
 	| '\\\\'
 ;
