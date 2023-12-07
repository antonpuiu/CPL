lexer grammar CoolLexer;

tokens { ERROR } 

@header{
    package cool.lexer;
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

// Keywords
CLASS: 'class';
ELSE: 'else';
FALSE: 'false';
FI: 'fi';
IF: 'if';
IN: 'in';
INHERITS: 'inherits';
ISVOID: 'isvoid';
LET: 'let';
LOOP: 'loop';
POOL: 'pool';
THEN: 'then';
WHILE: 'while';
CASE: 'case';
ESAC: 'esac';
NEW: 'new';
OF: 'of';
NOT: 'not';
TRUE: 'true';

// Primitives
fragment DIGIT: [0-9];
fragment UPPERCASE: [A-Z];
fragment LOWERCASE: [a-z];
fragment LETTER: UPPERCASE | LOWERCASE;

fragment NEWLINE: '\r'? '\n';
fragment ESCAPED: '\\"';

BOOL: (TRUE | FALSE);
INT: DIGIT+;
TYPE: UPPERCASE ('_' | DIGIT | LETTER)*;
ID: LOWERCASE ('_' | DIGIT | LETTER)*;
STRING : '"' (ESCAPED | .)*? '"' {
            String str = getText();

            str = str.substring(1, str.length() - 1);
            str = str.replace("\\\r\n", "\r\n")
                .replace("\\\n", "\n")
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\f", "\f");

            str = str.replaceAll("\\\\(.)", "$1");

            if (str.length() > 1024) {
                raiseError("String constant too long");
                return;
            }

            setText(str);
        };

// Comments
fragment OPEN_COMMENT: '(*';
fragment CLOSE_COMMENT: '*)';
fragment SINGLE_LINE_COMMENT: '--';

COMMENT: OPEN_COMMENT (COMMENT | .)*?
        (
            CLOSE_COMMENT {skip();} |
            EOF { raiseError("EOF in comment"); }
        );

// fragment NEWLINE: '\r'? '\n';
ONE_LINE_COMMENT: SINGLE_LINE_COMMENT .*? (NEWLINE | EOF) -> skip;

// Symbols & Operators
COMMA: ',';
SEMI: ';';
COLON: ':';

LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';

ASSIGN: '<-';
ARROW: '=>';

PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';

LT: '<';
LE: '<=';
EQUAL: '=';

NEGATE: '~';
AT: '@';
DOT: '.';

WS: [ \n\f\r\t]+ -> skip;

// ERROR handling
INVALID_COMMENT: CLOSE_COMMENT { raiseError("Unmatched "+ getText()); };
INVALID_CHARACTER: . { raiseError("Invalid character: " + getText()); };
