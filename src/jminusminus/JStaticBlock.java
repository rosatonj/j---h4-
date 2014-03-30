package jminusminus;

import java.util.ArrayList;

class JStaticBlock extends JAST implements JMember {

	private JBlock body;
	private ArrayList<String> mods;

	public JStaticBlock(int line, ArrayList<String> mods, JBlock body) {
		// TODO Auto-generated constructor stub
		super(line);
		this.mods = mods;
		this.body = body;
	}

	@Override
	public void preAnalyze(Context context, CLEmitter partial) {
		// TODO Auto-generated method stub

	}
	
    public JStaticBlock analyze(Context context) {
        return this;
    }
	
    public void codegen(CLEmitter output) {
    	
        }
    
    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JStaticBlock line=\"%d\">\n", line());
        p.indentRight();
        if (mods != null) {
            p.println("<Modifiers>");
            p.indentRight();
            for (String mod : mods) {
                p.printf("<Modifier name=\"%s\"/>\n", mod);
            }
            p.indentLeft();
            p.println("</Modifiers>");
        }
        p.printf("<Body>\n");
        p.indentRight();
        body.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Body>\n");
        p.indentLeft();
        p.printf("</JStaticBlock>\n");
    }

}
