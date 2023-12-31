lexer grammar CPLangLexer;

/* Reguli de funcționare:
 * 
 * * Se ia în considerare cel mai lung lexem recunoscut, indiferent de ordinea
 *   regulilor din specificație (maximal munch).
 * 
 * * Dacă există mai multe cele mai lungi lexeme, se ia în considerare prima
 *   regulă din specificație.
 */

/* Cuvânt cheie.
 */
IF : 'if';
FI : 'fi';
THEN : 'then';
ELSE : 'else';
TRUE : 'true';
FALSE : 'false';

/* Număr întreg.
 * 
 * fragment spune că acea categorie este utilizată doar în interiorul
 * analizorului lexical, nefiind trimisă mai departe analizorului sintactic.
 */
fragment DIGIT : [0-9];
INT : DIGIT+;

/* Identificator.
 */
fragment UPPERCASE: [A-Z];
fragment LOWERCASE: [a-z];
fragment LETTER: UPPERCASE | LOWERCASE;
TYPE: UPPERCASE(LETTER | '_' | DIGIT)*;
ID : (LOWERCASE | '_')(LETTER | '_' | DIGIT)*;

fragment VAR_SPEC: TYPE ID;

// VAR_DEF : VAR_SPEC SC;
// FUNC_CALL : ID LPAREN (ID? | ID (COMMA ID)*) RPAREN SC;

/* Număr real.
 */
fragment DIGITS : DIGIT+;
fragment FRACTION : (DOT DIGITS+);
fragment EXPONENT : ('e' (PLUS | MINUS)? DIGITS)?;
REAL : (DIGITS FRACTION? EXPONENT) | (DIGITS DOT) | (FRACTION EXPONENT);

SC : ';';
ASSIGN : '=';

/* Operators */
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';

LPAREN : '(';
RPAREN : ')';
LBRACKET : '{';
RBRACKET : '}';
COMMA : ',';
DOT : '.';

LT : '<';
GT : '>';
LE : '<=';
GE : '>=';
EQUAL : '==';

NEWLINE :'\r'? '\n';

/* Șir de caractere.
 * 
 * Poate conține caracterul '"', doar precedat de backslash.
 * . reprezintă orice caracter în afară de EOF.
 * *? este operatorul non-greedy, care încarcă să consume caractere cât timp
 * nu a fost întâlnit caracterul ulterior, '"'.
 * 
 * Acoladele de la final pot conține secvențe arbitrare de cod Java,
 * care vor fi executate la întâlnirea acestui token.
 */
STRING : '"' ('\\"' | .)*? '"'
    { System.out.println("there are no strings in CPLang, but shhh.."); };

// BLOCK_COMMENT : '/*' (BLOCK_COMMENT | .)*? '*/' -> skip;

COMMENT : '//' .*? '\n' -> skip;
BLOCK_COMMENT : NEWLINE (BLOCK_COMMENT | .)*? (NEWLINE {skip(); } | EOF {System.err.println("Wrong comment."); skip();});

/* Spații albe.
 * 
 * skip spune că nu este creat niciun token pentru lexemul depistat.
 */
WS : [ \n\r\t]+ -> skip;

/* Modalitate alternativă de recunoaștere a șirurilor de caractere, folosind
 * moduri lexicale.
 * 
 * Un mod lexical, precum cel implicit (DEFAULT_MODE) sau IN_STR, de mai jos,
 * reprezintă stări ale analizorului. Când analizorul se află într-un anumit
 * mod, numai regulile din acel mod se pot activa.
 * 
 * Folosim pushMode și popMode pentru intra și ieși din modurile lexicale,
 * în regim de stivă.
 * 
 * more spune că deocamdată nu este construit un token, dar lexemul identificat
 * va face parte, cumulativ, din lexemul recunoscut de următoarea regulă.
 * 
 * De-abia la recunoașterea caracterului '"' de sfârșit de șir de către regula
 * STR, se va construi un token cu categoria STR și întregul conținut al șirului
 * drept lexem.
 */
/*
STR_OPEN : '"' -> pushMode(IN_STR), more;

mode IN_STR;

STR : '"' -> popMode;
CHAR : ('\\"' | ~'"') -> more;  // ~ = complement
*/
