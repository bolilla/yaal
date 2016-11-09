//YAAL Grammar

rule_set:
	"rule_set" identifier "is" "evaluated as" evaluation_algorithm "is composed by" rule* "rule_set end"
evaluation_algorithm:
	//Set of constants from the specificationj
	identifier
rule:
	"rule" identifier "is" "pre-check" condition "check" condition "action" action_id "rule end"
identifier:
	//standard identifier
condition:
	"not" condition
	condition "&" condition
	condition "|" condition
	arit_val arit_op arit_val
	str_val str_op str_val
	bool_val
action_id:
	"PERMIT"
	"DENY"
arit_val:
	//Number
	categ_attr
arit_op:
	"+"
	"-"
	"*"
	"/"
bool_val:
	"TRUE"
	"FALSE"
categ_attr:
	identifier "." identifier
str_val:
	categ_attr
	//String
str_op:
	"="
	"<>"
	