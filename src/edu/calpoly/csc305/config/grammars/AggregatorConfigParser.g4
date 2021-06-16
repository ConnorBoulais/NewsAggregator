parser grammar AggregatorConfigParser;

options { tokenVocab=AggregatorConfigLexer; }

/*
   Parser Rules
*/

sources
   :  (sourceType | NEWLINE)* EOF
   ;
sourceType
   :  fileSourceType
   |  urlSourceType
   ;
fileSourceType
   : FILE_SOURCE_TYPE NEWLINE sourceName format source filter delay?
   ;
urlSourceType
   : URL_SOURCE_TYPE NEWLINE sourceName format source filter delay?
   ;
sourceName
   : NAME LINE NEWLINE
   ;
format
   : FORMAT format_option NEWLINE
   ;
format_option
   : NEWSAPI_FORMAT                     # NewsApiFormat
   | SIMPLE_FORMAT                      # SimpleFormat
   ;
source
   : SOURCE LINE NEWLINE
   ;
filter
   : FILTER selector? NL
   ;
selector
   : KEYWORD                            # KeywordExpression
   | LPAREN selector RPAREN             # NestedExpression
   | lft=selector AND rht=selector      # AndExpression
   | lft=selector OR rht=selector       # OrExpression
   ;
delay
   : DELAY NUM NEWLINE
   ;