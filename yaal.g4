/**
 * YAAL Grammar
 */
grammar yaal;

rule_set
:
	'rules' ID 'is' 'evaluated as' combining_algorithm 'is composed by' ruledesc*
	'rule_set end'
;

combining_algorithm
:
	'Deny-overrides'
	| 'Permit-overrides'
	| 'First-applicable'
	| 'Only-one-applicable'
	| ID
;

ruledesc
:
	'rule' ID 'is' 'precondition' condition 'condition' condition 'action'
	action_id 'rule end'
;

condition
:
	'not' condition
	| condition '&' condition
	| condition '|' condition
	| arit_val arit_comp arit_val
	| str_val str_op str_val
	| bool_val
;

action_id
:
	'PERMIT'
	| 'DENY'
;

arit_val
:
	NUM
	| categ_attr
;

arit_comp
:
	'='
	| '<>'
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
	| '[a-z]'+
;

str_op
:
	'='
	| '<>'
;

ID
:
	[a-z]+
;

NUM
:
	[0-9]+
;

WS
:
	[ \t\r\n]+ -> skip
;
