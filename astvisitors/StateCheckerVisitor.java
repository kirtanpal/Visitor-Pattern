package ecs160.visitor.astvisitors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

public class StateCheckerVisitor extends ASTVisitor {
	
	private String context_path;
	private String context_className;
	private String abstract_path;
	private String abstract_className;
	private String instanceName = null;
	private int methodCount = 0;
	private List <String> context_methods;
	private List <String> abstract_methods;

	private boolean isTestPassed = false;
	private boolean isTestFailed = false;
	private String testName = null;
	
	
	private File abstract_file;
	private File context_file;
	
	CompilationUnit context_comp_unit;
	CompilationUnit abstract_comp_unit;
	
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
	
	private String openFile(String path, String fileName) {
		String text = "";
		if(fileName == "context") {
			context_file = new File(path);

			try {
				text = UtilReader.read(context_file);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			abstract_file = new File(path);
			try {
				text = UtilReader.read(abstract_file);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	public StateCheckerVisitor(String context_path, String context_className, String abstract_path, String abstract_className) {
		this.context_path = context_path;
		this.context_className = context_className;
		this.abstract_path = abstract_path;
		this.abstract_className = abstract_className;
		context_methods = new ArrayList<String>();
		abstract_methods = new ArrayList<String>();
		
		String fulltext1 = openFile(context_path,"context");
		ASTParser parser1 = ASTParser.newParser(AST.JLS8);
	 	Map<String, String> options1 = JavaCore.getOptions();
		options1.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser1.setCompilerOptions(options1);
		parser1.setKind(ASTParser.K_COMPILATION_UNIT); 
		parser1.setResolveBindings(true);
		parser1.setBindingsRecovery(true);
		String[] classpath1 = { System.getProperty("java.home") + "/lib/rt.jar" };
		parser1.setEnvironment(classpath1, new String[] { "" }, new String[] { "UTF-8" }, true);
		parser1.setSource(fulltext1.toCharArray());
		parser1.setUnitName(context_file.getAbsolutePath());
		context_comp_unit = (CompilationUnit) parser1.createAST(null);
		
		String fulltext2 = openFile(abstract_path,"abstract");
		ASTParser parser2 = ASTParser.newParser(AST.JLS8);
	 	Map<String, String> options2 = JavaCore.getOptions();
		options2.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser2.setCompilerOptions(options2);
		parser2.setKind(ASTParser.K_COMPILATION_UNIT); 
		parser2.setResolveBindings(true);
		parser2.setBindingsRecovery(true);
		String[] classpath2 = { System.getProperty("java.home") + "/lib/rt.jar" };
		parser2.setEnvironment(classpath2, new String[] { "" }, new String[] { "UTF-8" }, true);
		parser2.setSource(fulltext2.toCharArray());
		parser2.setUnitName(abstract_file.getAbsolutePath());
		abstract_comp_unit = (CompilationUnit) parser2.createAST(null);
		
	}
	
	public static StateCheckerVisitor setUpGrader(String context_path, String context_className, String abstract_path, String abstract_className){		
		
		StateCheckerVisitor state_chkr = new StateCheckerVisitor(context_path, context_className, abstract_path, abstract_className);
		return state_chkr;
	}
	
	public boolean gradeA() {
		testName = "Question2a_context";
		methodCount = 0;
		isTestPassed = false;
		isTestFailed = false;
		context_comp_unit.accept(this);
		testName = "Question2a_abstract";
		abstract_comp_unit.accept(this);
		return !isTestFailed;
	}
	
	public int gradeB() {
		testName = "Question2b_abstract";
		methodCount = 0;
		isTestPassed = false;
		isTestFailed = false;
		abstract_comp_unit.accept(this);
		
		InstPrinter Inst_check = new InstPrinter(abstract_className);
		context_comp_unit.accept(Inst_check);
		
		instanceName = Inst_check.getInstanceName();
		testName = "Question2b_context";
		context_comp_unit.accept(this);
		if (!isTestFailed && isTestPassed) {
			return methodCount;
		};
		return 0;
	}
	
	
	
	public boolean visit(MethodDeclaration node)
	{
		switch(testName) {
		
		case "Question2a_context":
			context_methods.add((node.getName()).toString());	
			return true;
		
		case "Question2a_abstract":
			boolean methodFound = false;
			String curr_method1 = (node.getName()).toString();
			for (String method : context_methods) {
				if( (method.toString()).equals(curr_method1)) {
					methodFound = true;
				}
			}
			if(!methodFound) {
				isTestFailed = true;
				return false;
			}	
		return true;
		
		case "Question2b_abstract":
			abstract_methods.add((node.getName()).toString());	
			return true;
			
		case "Question2b_context":
			TryCheck tryState = new TryCheck();
			String curr_method2 = (node.getName()).toString();
			String method_call = instanceName + "." + curr_method2;
			for(String method : abstract_methods) {
				if( (method.toString()).equals(curr_method2)) {
					node.accept(tryState);
					if(subStringCount(tryState.getTryStatement(),method_call) == 1) {
						methodCount++;
					}
					else {
						isTestFailed = true;
					}
				}
				if(methodCount > abstract_methods.size()) {
					isTestFailed = true;
				}
				if(methodCount == abstract_methods.size()) {
					isTestPassed = true;
				}
			}
			return false;
	}
		return false;
	}
}
