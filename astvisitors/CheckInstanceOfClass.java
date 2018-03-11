package ecs160.visitor.astvisitors;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CheckInstanceOfClass extends ASTVisitor {
	private String className;
	private String instanceName;
	private boolean isTestPassed = false;
	
	public CheckInstanceOfClass(String className) {
		this.className = className;
		instanceName = "";
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public boolean getResult() {
		return isTestPassed;
	}
	
	public boolean visit(VariableDeclarationFragment node)
	{
		boolean isPrivate = false, isStatic = false;
		ASTNode AST_node = node.getParent();
		String type = (((FieldDeclaration) AST_node).getType()).toString();
		List<ASTNode> mods = (List<ASTNode>) ((BodyDeclaration) AST_node).modifiers();

		if(type.equals(className))
		{
			for (ASTNode m : mods)
			{
				if((m.toString()).equals("static")) {isStatic = true;}
				if((m.toString()).equals("public")) {isPrivate = false;}
				if((m.toString()).equals("private")) {isPrivate = true;}
			}
			if(isPrivate && isStatic) {
				isTestPassed = true;
				return true;
			}
		}
		return false;
	}
}
