package ecs160.visitor.astvisitors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import ecs160.visitor.utilities.UtilReader;

public class SingletonCheckerVisitor extends ASTVisitor  {
	
	private String className;
	private boolean isTestPassed = false;
	private String testName = null;
	
	private File file;
	private CompilationUnit comp_unit;
	
	private String openFile(String path) {
		file = new File(path);
		String text = "";
		try {
			text = UtilReader.read(file);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public SingletonCheckerVisitor(String path, String className) {

		this.className = className;
		String fulltext = openFile(path);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8); 
		parser.setCompilerOptions(options); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT); 
    		parser.setResolveBindings(true);
    		parser.setBindingsRecovery(true);
    		String[] classpath = { System.getProperty("java.home") + "/lib/rt.jar" };
    		parser.setEnvironment(classpath, new String[] { "" }, new String[] { "UTF-8" }, true);
	    	parser.setSource(fulltext.toCharArray()); 
	    	parser.setUnitName(file.getAbsolutePath()); 
	    	comp_unit = (CompilationUnit) parser.createAST(null);
	}
	public static SingletonCheckerVisitor setUpGrader(String path, String className){		
		
		SingletonCheckerVisitor single_chkr = new SingletonCheckerVisitor(path,className);
		return single_chkr;
	}
	
	public boolean gradeA() {
		testName = "Question1a";
		isTestPassed = false;
		comp_unit.accept(this);
		return isTestPassed;
	}
	
	public boolean gradeB() {
		testName = "Question1b";
		isTestPassed = false;
		comp_unit.accept(this);
		return isTestPassed;
	}
	
	public boolean gradeC() {
		testName = "Question1c";
		CheckInstanceOfClass Inst_check = new CheckInstanceOfClass(className);
		comp_unit.accept(Inst_check);
		return Inst_check.getResult();
	}
	
	public boolean gradeD() {
		testName = "Question1d";
		isTestPassed = false;
		comp_unit.accept(this);
		return isTestPassed;
	}
	
	public boolean visit(MethodDeclaration node)
	{
		
		switch(testName) {
		case "Question1a": 
			List<ASTNode> mods0 = (List<ASTNode>) node.modifiers();
			if(node.isConstructor()) {
				for (ASTNode m : mods0) {
					if((m.toString()).equals("private")) {
						isTestPassed = true;
						return true;
					}
				}
				return false;
			}
			return false;
			
			
		case "Question1b":
			List<ASTNode> mods1 = (List<ASTNode>) node.modifiers();
			boolean isPublic = false, isStatic = false;
			if(isTestPassed){return false;}
			if( ((node.getReturnType2()).toString()).equals(className)) {
				for (ASTNode m : mods1)
				{
					if((m.toString()).equals("static")) {isStatic = true;}
					if((m.toString()).equals("public")) {isPublic = true;}
					if((m.toString()).equals("private")) {isPublic = false;}
				}
				if(isPublic && isStatic) {
					isTestPassed = true;
					return true;
				}
				
			}
			return false;
			
			
		case "Question1c":
			/*
			 * Not visiting this function, instead visit of CheckInstanceOfClass is called
			 * from the gradeC method of this class
			 */
			
		case "Question1d":
			if(isTestPassed) return false;
			if( ((node.getReturnType2()).toString()).equals(className)) {
				IfPrinter if_print = new IfPrinter(className);
				node.accept(if_print);
				isTestPassed = true;
				return if_print.getResult();
			}
			return false;
		}
		return false;
	}
}
