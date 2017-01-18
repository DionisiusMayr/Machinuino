grammar Machinuino;

options {
	language = Java;
}

@members {
    public void stop(String msg) {
        throw new ParseCancellationException(msg);
    }
}

moore           : 'moore' NAME '{' states input output '}' EOF;

states          : 'states' '{' (NAME (',' NAME)*)? '}';

input           : 'input' '{' pinsInput transition '}';

pinsInput       : 'pins' '{' 'clock' ':' NUMBER (',' NAME ':' NUMBER)* '}';

transition      : 'transition' '{' (NAME '{' transBlock '}')* '}';

transBlock      : (partialTrans (',' partialTrans)*)?;

partialTrans    : logicExp '->' NAME;

logicExp        : extName ('&' extName)*;

extName         : '!'? NAME;

output          : 'output' '{' pinsOutput function '}';

pinsOutput      : 'pins' '{' (NAME ':' NUMBER (',' NAME ':' NUMBER)*)? '}';

function        : 'function' '{' (NAME '{' funcBlock '}')* '}';

funcBlock       : (extName (',' extName)*)?;

WS	            : (' ' | '\t' | '\r' | '\n') -> skip;

NUMBER          : DIGIT+;

NAME			: LETTER (LETTER | DIGIT)+;

COMMENT         : '/*' (.)*? '*/' -> skip;

fragment
LETTER			: ('a' .. 'z')
				| ('A' .. 'Z');

fragment
DIGIT			: ('0' .. '9');

ERROR           : . {stop(getLine() + ": Lexical Error: " + getText() + " - invalid symbol" + System.lineSeparator());};
