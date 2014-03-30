package jminusminus;

public class JForStatement extends JStatement {

	/** Initialize a variable. */
	private JStatement initialize;
	
	/** Statement that terminates the loop */
	private JStatement terminate;

	/** Statement that updates the variable for next loop */
	private JExpression update;

	/** Statement that occurs on every loop */
	private JStatement consequent;
	
//	/** Statement that checks for initial that is a statement */
//	private JStatement initialize;
	
	public JForStatement(int line, JStatement initialize, JStatement terminate,
			JExpression update, JStatement consequent) {
		super(line);
		this.initialize = initialize;
		this.terminate = terminate;
		this.update = update;
		this.consequent = consequent;
	}

//	public JForStatement(int line, JStatement initialize2, JStatement terminate,
//			JExpression update, JStatement consequent) {
//		super(line);
//		this.initialize2 = initialize2;
//		this.terminate = terminate;
//		this.update = update;
//		this.consequent = consequent;
//	}
	
	
	public JAST analyze(Context context) {
		return this;
	}

	public void codegen(CLEmitter output) {

	}

	public void writeToStdOut(PrettyPrinter p) {
		p.printf("<JForStatement line=\"%d\">\n", line());
		p.indentRight();
		p.printf("<Initialization>\n");
		p.indentRight();
		initialize.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Initialization>\n");
		p.printf("<Termination>\n");
		p.indentRight();
		terminate.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Termination>\n");
		p.printf("<Update>\n");
		p.indentRight();
		update.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Update>\n");
		p.printf("<Consequent>\n");
		p.indentRight();
		consequent.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Consequent>\n");
		p.indentLeft();
		p.printf("</JForStatement>\n");
	}

}
