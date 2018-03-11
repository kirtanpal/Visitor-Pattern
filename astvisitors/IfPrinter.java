package ecs160.visitor.astvisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import ecs160.visitor.utilities.ASTNodeTypePrinter;

/**
 * A visitor to print out information about if statements
 * including the expression, the body of the if, and the body of the 'else'.
 *
 */
public class IfPrinter extends ASTVisitor{
	private String className;
	private boolean isTestPassed = false;
	
	public IfPrinter(String className) {
		this.className = className;
	}

	public int subStringCount(String string, String substring)
	  {
	     int count = 0;
	     int idx = 0;

	     while ((idx = string.indexOf(substring, idx)) != -1)
	     {
	        idx++;
	        count++;
	     }

	     return count;
	  }
	
	public boolean getResult() {
		return isTestPassed;
	}
	
	public boolean visit(IfStatement node)
	{
		String Substring = className + "()";
		String if_Statement;
		String else_body = null, then_body;
		then_body = node.getThenStatement().toString().replaceAll("\n", " ");
		Statement elseStmt = node.getElseStatement();
		if(elseStmt != null)
		{
			else_body = node.getElseStatement().toString().replaceAll("\n", " ");
		}
		
		if_Statement = then_body + else_body;
		if(subStringCount(if_Statement,Substring) == 1) {
			isTestPassed = true;
			return true;
		}
		return false; 
	}

}
