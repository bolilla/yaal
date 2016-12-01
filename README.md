# YAAL (Yet Another Authorization Language)

## What is YAAL

YAAL is a DSL (Domain Specific Language) which helps in the definition of authorization policies. It aims to be a simple and expressive language that improves communication between technical and business teams.

## Why a new DSL for authorization?

While defining authorization policies, it seems to be pretty obvious the model which brings most flexibility is [ABAC](https://en.wikipedia.org/wiki/Attribute-Based_Access_Control), and the language of choice to define ABAC authorization policies is XACML (or ALFA). More on these languages in the section "Alternatives to YAAL".

Policy definition in XACML is done using XML, which is certainly a very difficult-to-read language. ALFA maintains the language model and uses JSON as the coding language; this improves the readability over XML, but it is not "business ready".

I believe a language is meant to be used to communicate, but I have not found the right language to communicate. I find XACML very well suited to communicate with machines (it can be "easily" used in authorization engines). I think ALFA also makes a good communication tool among technical people (specially developers). But (IMHO) they both create policies that cannot be easily understood by non-technical people.

I think there is a spot for a language that allows policy definition to feel natural enough as to be read by non-technical people, but rigorous enough so it contains no (or few) ambiguities.

In case you are wondering about the "[the COBOL inference](http://martinfowler.com/bliki/BusinessReadableDSL.html)", let me state it clearly: I do not want non-technical people to write YAAL; I just want them to be able to read it.

## Language description

These are the main characteristics of YAAL:
- External DSL: this means it has its own syntax. It is not based on some other language, such as XML or JSON
- Uses English words for markers. This intends to make it more readable
- The grammar is defined using Antlr4. This makes it easy for language-savvy people to tinker around with the language
- The language model is a simplification of the XACML language model. This means anything defined in YAAL can be implemented in XACML (this is still a work in progress, as _targets_ are treated as _conditions_).
 
The model of YAAL is a simplification of the XACML model. This simplification aims to make YAAL to be easier to be understood by non-tecnical people.

The following is the model of XACML V3 (directly extracted from its documentation):

![XACML model](http://docs.oasis-open.org/xacml/3.0/xacml-3.0-core-spec-os-en_files/image004.gif "XACML model")

The following is the language model of YAAL:

![YAAL model](/img/yaal_model.png "YAAL model")

This means there is only one _Policy Set_, with just one _Policy_, which may have many rules.

YAAL does not define the way attributes are retrieved.

## Example

```
# Example extracted from: Guide to Attribute Based Access Control (ABAC) Definition and Considerations
# All Nurse Practitioners in the Cardiology Department can View the Medical Records of Heart Patients

policy Example begin
  rule CardiologyNurses (PERMIT) begin
    condition 
    
      subject.role = "Nurse"
      AND subject.department = "Cardiology"
      
      AND action.action_id = "View"
      
      AND resource.type ="MedicalRecord"
      AND resource.patientDepartment = "Cardiology"
      
  end 
end
```

## Limitations (a.k.a roadmap)

Even when there may be many other limitations I am not aware of , I can state for sure these:

- **There is no proper YAAL compiler to create an executable authorization policy**: The language model is a subset of XACML, which means any policy written in YAAL can be translated into XACML, but I have not had the time (or will) to implement it.
- **The definition of the _target_ component of the policies is treated in the same way as a _condition_**: This means there is some work to be done regarding the transformation of a _target_ defined in YAAL into a _target_ defined into XACML. May be there is some of the _target_ that has to be transcribed into the _condition_ of the policy.
- **Explanation for conditions**: A definition of condition components that are repeated. e.g. define "administrator" as "user.id = 'admin' OR user.group = 'admin'".
- **Obligations**: Add some means to put answers (PERMIT or DENY) into context by adding obligations as described by XACML. e.g. user cannot perform action (DENY result) unless she agrees to the terms of the service (obligation).
- **Advices**: Add some means to put answers (PERMIT or DENY) into context by adding advises as described by XACML. e.g. user can perform the operation (PERMIT result), but please label this operation as "suspicious" (if the operation cannot be labeled, the operation is permitted anyway).
- **Query (or command) transformations**: One very interesting piece of information that can be returned to the PEP regards the manipulation of the parameters of the operation to be done. e.g. reduce the scope of an hypothetical sql query by transforming `select * from users` into `select * from (select * from users where department = user.department)`.

## Alternatives to YAAL

Maybe YAAL is not what you are looking for. If that is the case, maybe you will find the following list of authorization languages useful:

- XACML (eXtensible Access Control Markup Language) [Wikipedia]([https://en.wikipedia.org/wiki/XACML) [Specification](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml)
- ALFA (Axiomatics Language for Authorization) [Wikipedia](https://en.wikipedia.org/wiki/ALFA_(XACML)) [Specification](https://www.axiomatics.com/solutions/products/authorization-for-applications/developer-tools-and-apis/192-axiomatics-language-for-authorization-alfa.html)

## Disclaimer
There is no official (or extra-official) support for the use of YAAL. If lack of professional support is not an issue for you, feel free to use it and please tell me about your experience ;)
