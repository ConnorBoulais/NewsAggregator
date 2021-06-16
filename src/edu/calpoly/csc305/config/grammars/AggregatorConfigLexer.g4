lexer grammar AggregatorConfigLexer;

FILE_SOURCE_TYPE    : 'file' ;
URL_SOURCE_TYPE     : 'url' ;
FORMAT              : 'format:' ;
NEWSAPI_FORMAT      : 'newsapi' ;
SIMPLE_FORMAT       : 'simple' ;
NAME                : 'name:' -> pushMode(lineMode) ;
SOURCE              : 'source:' -> pushMode(lineMode) ;
FILTER              : 'filter:' -> pushMode(filterMode);
DELAY               : 'delay:' ;
NUM                 : [0-9]+ ;
NEWLINE             : '\r'? '\n' | '\r' ;
WS                  :  [ \t]+ -> skip ;
COMMENT             :  '#' .*? '\n' -> skip ;


mode lineMode;

LINE           : ~[\r\n]+ -> popMode ;


mode filterMode;

AND                 : '&' ;
OR                  : '|' ;
LPAREN              : '(' ;
RPAREN              : ')' ;
KEYWORD             : ~[\r\n ]+ ;
NL                  : ('\r'? '\n' | '\r') -> popMode;
SPACE               :  [ \t]+ -> skip ;