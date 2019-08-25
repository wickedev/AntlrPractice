grammar Rows;

@parser::members { // add members to generated RowsParser
    int col;
    public ArrayList<String> list = new ArrayList<>();
    public RowsParser(TokenStream input, int col) {
        this(input);
        this.col = col;
    }
}

file: (row NL)+;

row
locals [int i = 0]
    : (STUFF
        {
            $i++;
            if ($i == col) list.add($STUFF.text);
        }
    )+
    ;

TAB: '\t' -> skip;
NL: '\r' ? '\n';
STUFF: ~[\t\r\n]+;
