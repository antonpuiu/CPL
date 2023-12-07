parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program: (classes+=class SEMI)+ EOF;

class: CLASS name=TYPE (INHERITS parent=TYPE)? LBRACE (features+=feature SEMI)* RBRACE;

feature:
      name=ID LPAREN (formals+=formal (COMMA formals+=formal)*)? RPAREN
        COLON returnType=TYPE LBRACE content=expr RBRACE                            # method
    | variable=var                                                                  # attribute
    ;

formal: name=ID COLON type=TYPE;
var: name=ID COLON type=TYPE (ASSIGN value=expr)?;
case: name=ID COLON type=TYPE ARROW value=expr;

expr: object=expr (AT staticType=TYPE)? DOT methodName=ID
        LPAREN (args+=expr (COMMA args+=expr)*)? RPAREN                             # dispatch
    | methodName=ID LPAREN (args+=expr (COMMA args+=expr)*)? RPAREN                 # methodCall
    | IF cond=expr THEN thenBranch=expr ELSE elseBranch=expr FI                     # if
    | WHILE cond=expr LOOP body=expr POOL                                           # while
    | LBRACE (instructions+=expr SEMI)+ RBRACE                                      # block
    | LET vars+=var (COMMA vars+=var)* IN context=expr                              # let
    | CASE object=expr OF (cases+=case SEMI)+ ESAC                                  # switch
    | NEW type=TYPE                                                                 # new
    | ISVOID value=expr                                                             # isvoid
    | left=expr op=(MULT | DIV) right=expr                                          # multDiv
    | left=expr op=(PLUS | MINUS) right=expr                                        # plusMinus
    | left=expr op=(LT | LE | EQUAL) right=expr                                     # relational
    | NEGATE value=expr                                                             # negate
    | NOT value=expr                                                                # not
    | LPAREN value=expr RPAREN                                                      # paren
    | name=ID ASSIGN value=expr                                                     # assign
    | name=ID                                                                       # id
    | value=INT                                                                     # int
    | value=STRING                                                                  # string
    | value=(TRUE | FALSE)                                                          # bool
    ;
