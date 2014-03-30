package jminusminus;

public class JBreakStatement extends JStatement {

	private JExpression expr;

	protected JBreakStatement(int line, JExpression expr) {
		super(line);
		this.expr = expr;
		// TODO Auto-generated constructor stub
	}

	@Override
	public JAST analyze(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void codegen(CLEmitter output) {
		// TODO Auto-generated method stub

	}

    public void writeToStdOut(PrettyPrinter p) {
        if (expr != null) {
            p.printf("<JBreakStatement line=\"%d\">\n", line());
            p.indentRight();
            expr.writeToStdOut(p);
            p.indentLeft();
            p.printf("</JBreakStatement>\n");
        } else {
            p.printf("<JBreakStatement line=\"%d\"/>\n", line());
        }
    }
	
}
