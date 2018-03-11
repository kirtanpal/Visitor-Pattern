package ecs160.visitor.astvisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TryStatement;

public class TryCheck extends ASTVisitor {

	private String tryStatement = null;
	
	public String getTryStatement() {
		return tryStatement;
	}
	
	public boolean visit(TryStatement node){
		tryStatement = (node.toString()).replaceAll("\n", " ");
		return true;
	}
}
