/*
 * Created on 11-10-2003 by jesper
 * This class should 
 */
package visitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import syntaxtree.*;

/**
 * @author jesper
 */
public class CodeGenVisitor extends DepthFirstVisitor {
	
	private StringBuffer code = new StringBuffer();
	private Environment env = new Environment();
	
	
	public void writeToFile(){
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File("Generated.java"));
			writer.write(code.toString());
		} catch (IOException e) {
			System.err.println("Couldnt write to file:" + e);
		}finally{
			if(writer != null){
				try {
					writer.flush();
					writer.close();	
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	   * f0 -> Expression()
	   * f1 -> ";"
	   */
	  public void visit(Start n) {
		 code.append("import visitor.Environment;\n");
		code.append("//NB:this class is generated\n");
	  	 code.append("public class Generated{\n\n");
	  	 code.append("	public static void evalExpr(Environment e) {\n");
	  	 code.append("		Integer result = ");
		 n.f0.accept(this);
		 code.append(";//end of result");
		 code.append("\n	}");
		 n.f1.accept(this);
		 code.append("\n\n	" +		 	"public static Integer lookup(Environment env, String x) {\n	" +		 	"	return env.get(x);\n" +		 	"	}\n");
		code.append("}");
		 
		System.out.println("Generated Code:\n" + code.toString());
		writeToFile();
		System.out.println("Wrote file...");
		try {
			Runtime.getRuntime().exec(code.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }



	  /**
	   * f0 -> AdditiveExpression()
	   */
	  public void visit(Expression n) {
		 n.f0.accept(this);
	  }

	  /**
	   * f0 -> MultiplicativeExpression()
	   * f1 -> ( ( "+" | "-" ) MultiplicativeExpression() )*
	   */
	  public void visit(AdditiveExpression n) { 	
		 n.f0.accept(this);
		 if(n.f1.present()){
			for (Enumeration e = n.f1.elements(); e.hasMoreElements();) {
				Object element = (Object)e.nextElement();
				if(element instanceof NodeSequence){
					NodeSequence seq = (NodeSequence) element;
					Vector v = seq.nodes;
					for (Iterator iter = v.iterator(); iter.hasNext();) {
						Object o = (Object) iter.next();
						if(o instanceof NodeChoice){
							NodeChoice choice = (NodeChoice)o;
							Node node = choice.choice;
							code.append(node.toString());
						}					
					}
				}	
			}	
		 }
		 n.f1.accept(this);
	  }

	/**
	 * f0 -> UnaryExpression()
	 * f1 -> ( ( "*" | "/" | "%" ) UnaryExpression() )*
	 */
	public void visit(MultiplicativeExpression n) {
		n.f0.accept(this);
		if (n.f1.present()) {
			Vector vec = n.f1.nodes;
			for (Iterator iter = vec.iterator(); iter.hasNext();) {
				Object el = (Object) iter.next();
				NodeSequence sq = (NodeSequence) el;
				Vector v1 = sq.nodes;
				for (Iterator iterator = v1.iterator(); iterator.hasNext();) {
					Object no = (Object) iterator.next();
					if (no instanceof NodeChoice) {
						NodeChoice ch = (NodeChoice) no;
						code.append(ch.choice);
					}
				}
			}
		}
		n.f1.accept(this);
	}

	  /**
	   * f0 -> "(" Expression() ")"
	   *       | Identifier()
	   *       | MyInteger()
	   */
	  public void visit(UnaryExpression n) {
	  	/*
	  	 * A cleaner way than switch would be to check if choice was one - otherwise
	  	 * just  n.f0.accept(this);
	  	 */
	  	 
	  	switch(n.f0.which){
	  		case 0:
	  			Node ex = (Node)n.f0.choice;
	  			if(ex instanceof NodeSequence){
					NodeSequence ns = (NodeSequence)ex;
					Vector v = ns.nodes;
					for (Iterator iter = v.iterator(); iter.hasNext();) {
						Object element = (Object) iter.next();
						if(element instanceof NodeToken){
							NodeToken token = (NodeToken)element;
							code.append(token.tokenImage); //appends the "()"
							//token.accept(this); //which accept dosent achieve..
						}
						if(element instanceof Expression){
							Expression e = (Expression)element;
							e.accept(this);
						}
					}
	  			}
	  		break;
	  		case 1:
	  			Identifier i  = (Identifier)n.f0.choice;
	  			i.accept(this);
	  		break;
	  		case 2:
	  			MyInteger mi = (MyInteger)n.f0.choice;
	  			mi.accept(this);
	  		break;
	  	}
	  }

	  /**
	   * f0 -> <IDENTIFIER>
	   */
	  public void visit(Identifier n) {
		code.append("lookup(e, \"" + n.f0.tokenImage +"\")");
		n.f0.accept(this);
	  }

	  /**
	   * f0 -> <INTEGER_LITERAL>
	   */
	  public void visit(MyInteger n) {
	  	 code.append(n.f0.tokenImage);
		 n.f0.accept(this);
	  }
}
