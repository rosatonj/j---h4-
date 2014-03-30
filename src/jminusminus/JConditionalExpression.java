// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

/**
 * The AST node for an ternary if-statement.
 */

class JConditionalExpression extends JExpression {

    /** Test expression. */
    private JExpression condition;

    /** Then clause. */
    private JStatement thenPart;

    /** Else clause. */
    private JStatement elsePart;
    
    
    public JConditionalExpression(int line, JExpression condition, 
    		JStatement thenPart, JStatement elsePart) {
    	super(line);
        this.condition = condition;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }

    public JExpression analyze(Context context) {
        return this;
    }

    public void codegen(CLEmitter output) {

    }

    /**
     * @inheritDoc
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JTerIfElseStatement line=\"%d\">\n", line());
        p.indentRight();
        p.printf("<TestExpression>\n");
        p.indentRight();
        condition.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        p.printf("<ThenClause>\n");
        p.indentRight();
        thenPart.writeToStdOut(p);
        p.indentLeft();
        p.printf("</ThenClause>\n");
            p.printf("<ElseClause>\n");
            p.indentRight();
            elsePart.writeToStdOut(p);
            p.indentLeft();
            p.printf("</ElseClause>\n");
        p.indentLeft();
        p.printf("</JTerIfElseStatement>\n");
    }

}
