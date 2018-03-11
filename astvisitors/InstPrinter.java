package ecs160.visitor.astvisitors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class InstPrinter extends ASTVisitor  {

	private String className;
	private String instanceName;
	private boolean foundInstance;
	
	public InstPrinter(String className) {
		this.className = className;
		instanceName = "";
		foundInstance = false;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public boolean visit(VariableDeclarationFragment node)
	{
		if(!foundInstance) {
			ASTNode AST_node = node.getParent();
			String type = (((FieldDeclaration) AST_node).getType()).toString();
			if(type.equals(className)) {
				foundInstance = true;
				instanceName = (node.getName()).toString();
			}
		}
		return true;
	}
}
