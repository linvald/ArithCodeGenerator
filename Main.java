//
// A modified version of the main() method found in the Java 1.1 grammar.
// To add your visitors to the program, go to near the end, and copy or
// replace the line "root.accept(new Visitor());" with your own visitor
//

import javacc_generated.*;
import syntaxtree.*;
import visitor.*;

public class Main {
   public static void main(String args[]) {
	  ArithExpr parser;
	  if (args.length == 0) {
		 System.err.println("Reading from standard " +
							"input . . .");
		 parser = new ArithExpr(System.in);
	  }
	  else if (args.length == 1) {
		 System.err.println("Reading from file " +
							args[0] + " . . .");
		 try { parser = new ArithExpr(new java.io.FileInputStream(args[0])); }
		 catch (java.io.FileNotFoundException e) {
			System.err.println("File " + args[0] +
							   " not found.");
			return;
		 }
	  }
	  else {
		 System.err.println("Usage is one of:");
		 System.err.println("         java Main < inputfile");
		 System.err.println("OR");
		 System.err.println("         java Main inputfile");
		 return;
	  }

	  try {
		 //
		 // Here's where the AST actions and visiting take place.
		 //
		 Start root = ArithExpr.Start();
		 System.out.println("ArithExpr parsed " +
							"successfully.");


		System.out.println();
			
			System.out.println("Calling the visitor from inner anonymous class:");
				root.accept(new CodeGenVisitor() {
				   public void visit(Start n) {
					  //dumper.startAtNextToken();
					  n.accept(new CodeGenVisitor());
				   }	  	
				});
				

	  }
	  catch (ParseException e) {
		 System.err.println(e.getMessage());
		 System.err.println("Encountered errors " +
							"during parse.");
	  }
   }
}
