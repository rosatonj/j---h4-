package jminusminus;

public class JEnhancedForStatement extends JStatement {

	/** Initialize a variable. */
	private JVariableDeclarator initialize;

	/** Statement that holds the array */
	private JExpression array;

	/** Statement that occurs on every loop */
	private JBlock consequent;
	
	public JEnhancedForStatement(int line, JVariableDeclarator initialize, JExpression array, JBlock consequent) {
		super(line);
		this.initialize = initialize;
		this.array = array;
		this.consequent = consequent;
	}

	public JAST analyze(Context context) {
		return this;
	}

	public void codegen(CLEmitter output) {

	}

	public void writeToStdOut(PrettyPrinter p) {
		p.printf("<JEnhancedForStatement line=\"%d\">\n", line());
		p.indentRight();
		p.printf("<Initialization>\n");
		p.indentRight();
		initialize.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Initialization>\n");
		p.printf("<Array>\n");
		p.indentRight();
		array.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Array>\n");
		p.indentRight();
		p.printf("<Consequent>\n");
		p.indentRight();
		consequent.writeToStdOut(p);
		p.indentLeft();
		p.printf("</Consequent>\n");
		p.indentLeft();
		p.printf("</JEnhancedForStatement>\n");
	}

}
