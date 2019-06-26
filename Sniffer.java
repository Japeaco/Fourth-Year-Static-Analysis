package codeSmells;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Sniffer {
	
	//counts total number of lines in a class
	public int countLinesInClass(String fileName) throws IOException {
		
		int lines = 0;
		String name = "";
		String line = "";
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null){
			if(line.contains("class")){
				name = line;
				name = reduceClassName(name);
				while((line = br.readLine()) != null){
					lines++;
				}
				lines--;
			}
		}		
		return lines;
	}
	
	//counts number of fields in a class
	public int countFieldsInClass(String fileName) throws IOException {
		
		int fields = 0;
		String name = "";
		String line = "";
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null){
			if(line.contains("class")){
				name = line;
				name = reduceClassName(name);
			}
			else if(line.contains("{")  && line.contains("class") == false || line.contains("}")){
				break;
			}
			else if(line.contains("int") || line.contains("String") || line.contains("bool") 
					|| line.contains("long") || line.contains("long") || line.contains("float") 
					|| line.contains("double")) {
				fields++;
			}
		}	
		return fields;
	}
	
	//counts number of methods in a class
	public int countMethodsInClass(String fileName) throws IOException {
		
		String line = "";
		String name = "";
		int methods = 0;
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null) {
			if(line.contains("class")){
				name = line;
				name = reduceClassName(name);
			}
			else if((line.contains("public") || line.contains("private") || line.contains("protected"))
					&& line.contains(";") == false && line.contains("class") == false) {
				methods++;
			}
		}	
		return methods;
	}
	
	public void largeClass(String fileName) throws IOException {
		
		String className = "";
		String line = "";
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null){
			if(line.contains("class")){
				className = line;
				className = reduceClassName(className);
				break;
			}
		}
		
		int lines = countLinesInClass(fileName);
		int methods = countMethodsInClass(fileName);
		int fields = countFieldsInClass(fileName);
		
		System.out.print("\n" + className + "has " + methods + " methods");
		if (methods >= 15) {
			System.out.print(" (This class has 15 or more methods and is possibly smelly!)");
		}
		
		System.out.print("\n" + className + "has " + fields + " fields");
		if (fields >= 10) {
			System.out.print(" (This class has 10 or more fields and is possibly smelly!)");
		}
		
		System.out.print("\n" + className + "has " + lines + " LoC");
		if (lines >= 200) {
			System.out.print(" (This class has 200 or more loC and is possibly smelly!)");
		}
		
	}
	
	//counts number of comments in a class
	public void countCommentsInClass(String fileName) throws IOException {
		
		String line = "";
		String name = "";
		int comments = 0;	
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null) {
			if(line.contains("class")){
				name = line;
				name = reduceClassName(name);
			}
			else if(line.contains("//")) {
				comments++;
			}
		}
		if (comments < 15) {
			System.out.println("\n" + name + "has " + comments + " comments");
		}
		else {
			System.out.println("\n" + name + "has " + comments 
					+ " comments (This class has 15 or more comments and is possibly smelly!)");
		}
	}
	
	//counts number of lines in each method and stores them in a map
	public Map<String,Integer> countLinesInMethods(String fileName) throws IOException {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		String line = "";
		String name = "";
		int smellyMethods = 0;
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null){
			int lines = 0;
			int closeBrackets = 0;
			int openBrackets = 0;
			if((line.contains("public") || line.contains("private") || line.contains("protected")) 
					&& line.contains(";") == false && line.contains("class") == false){
				name = line;
				name = reduceMethodName(name);
				openBrackets++;
				while((line = br.readLine()) != null) {
					lines++;
					if(line.contains("{")){
						openBrackets++;
					}
					else if(line.contains("}") && closeBrackets < openBrackets) {
						closeBrackets++;	
						if(openBrackets == closeBrackets){
							lines--;
							map.put(name, lines);
							break;
						}
					}
				}
			}
		}
		
		Iterator it = map.entrySet().iterator();
		System.out.println("\nList of methods and LoC: ");
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        if((int) pair.getValue() >= 10) {
	        	System.out.println(pair.getKey() + " = " + pair.getValue() 
	        	+ " (This method has 10 or more loC and is possibly smelly!)");
	        	smellyMethods++;
	        }
	        else{
	        	System.out.println(pair.getKey() + " = " + pair.getValue());
	        }
	    }
		System.out.println("No. of smelly methods = " + smellyMethods);
		
		return map;
	}
	
	//counts number of parameters for each method and stores them in a map
	public void countParameters(String fileName) throws IOException {
		
		Map<String, Integer> map = new HashMap<String, Integer>();	
		String line = "";
		String name = "";
		int smellyMethods = 0;	
		BufferedReader br = read(fileName);
			
		while((line = br.readLine()) != null) {
			int wordCount = 0;
			if((line.contains("public") || line.contains("private") || line.contains("protected")) 
					&& line.contains(";") == false && line.contains("class") == false) {
				name = line;
				name = reduceMethodName(name);
				for(int i = 0; i < line.length(); ++i) {
			         char currentChar = line.charAt(i);
			         if(line.contains("()")) {
			        	 break;
			         }
			         else if(currentChar == ',' || currentChar == ')') {
			           wordCount += 1;
			         }
				}	
				map.put(name, wordCount);
			}
		}
			
		Iterator it = map.entrySet().iterator();
		System.out.println("\nList of methods and number of parameters: ");
		while (it.hasNext()) {
		       Map.Entry pair = (Map.Entry)it.next();
		       if((int) pair.getValue() >= 4) {
		       	System.out.println(pair.getKey() + " = " + pair.getValue() 
		       		+ " (This method has 4 or more parameters and is possibly smelly!)");
		       	smellyMethods++;
		       }
		       else {
		       	System.out.println(pair.getKey() + " = " + pair.getValue());
		       }
	    }
		System.out.println("No. of smelly methods = " + smellyMethods);
	}
	
	//counts number of duplicated methods (with different names) in a class
	public void countDuplicateMethods(String fileName) throws IOException {
			
		String line = "";
		String methodName = "";
		String className = "";
		String value = "";
		int duplicatedMethods = 0;
		BufferedReader br = read(fileName);
		
		Map<String, String> map = storeMethods(fileName);
		
		while((line = br.readLine()) != null){
			if(line.contains("class")){
				className = line;
				className = reduceClassName(className);
				break;
			}
		}
		
		Iterator it1 = map.entrySet().iterator();
		while (it1.hasNext()) {
			Map.Entry pair1 = (Map.Entry)it1.next();
			String stringPair1 = (String) pair1.getValue();
			Iterator it2 = map.entrySet().iterator();
		    while (it2.hasNext()) {
				Map.Entry pair2 = (Map.Entry)it2.next();
				String stringPair2 = (String) pair2.getValue();
				if(stringPair1.equals(stringPair2) == true && pair1.getKey() != pair2.getKey()){
			    	duplicatedMethods++;
			    	System.out.println("\n" + pair1.getKey() + " and " + pair2.getKey() + " are duplicate methods!");
				}
			}
		}
		if(duplicatedMethods > 0) {
			System.out.println("\n" + className + "has " + duplicatedMethods + " duplicated methods and is possibly smelly!");
		}
		else {
			System.out.println("\n" + className + "has " + duplicatedMethods + " duplicated methods");
		}
	}
	
	public void dataClass(String fileName) throws IOException {
		
		Map<String,Integer> linesMap = countLinesInMethods(fileName);
		Map<String,String> bodyMap = storeMethods(fileName);
		String className = "";
		String line = "";
		int numMethods = countMethodsInClass(fileName);
		int setMethods = 0;
		int getMethods = 0;
		int totalMethods = 0;
		BufferedReader br = read(fileName);

		while((line = br.readLine()) != null){
			if(line.contains("class")){
				className = line;
				className = reduceClassName(className);
				break;
			}
		}
		
		Iterator it = linesMap.entrySet().iterator();

		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        if(pair.getValue().equals(1)){
	        	Iterator it2 = bodyMap.entrySet().iterator();
		    	while (it2.hasNext()) {
		    		Map.Entry pair2 = (Map.Entry)it2.next();
		    		if(pair.getKey().equals(pair2.getKey())) {
		    			String stringPair = (String) pair2.getValue();
		    			if(stringPair.contains("this")){
		    				setMethods++;
		    			}
		    			else if(stringPair.contains("return")){
		    				getMethods++;
		    			}
		    		}
		    	}
	        }
		}
		
		totalMethods = setMethods + getMethods;
		
		System.out.println("\n set Methods: " + setMethods);
		System.out.println("Get Methods: " + getMethods);
		
		if(totalMethods == numMethods){
			System.out.println(className + "has only set and/or get methods and is possibly smelly!");
		}
		
	}
	
	public void unusedFields(String fileName) throws IOException {
		
		List<String> fields = getFields(fileName);
		Map<String,String> methods = storeMethods(fileName);
		List<String> unusedFields = new ArrayList();
		int NumUnusedFields = 0;
		int used = 0;
		
		for (int i = 0; i < fields.size(); i++) {
			used = 0;
			Iterator it = methods.entrySet().iterator();
			while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if(((String) pair.getValue()).contains(fields.get(i))){
		        	used++;
		        }
		    }
	        if(used == 0) {
	        	unusedFields.add(fields.get(i));
	        	NumUnusedFields++;
	        }
		}	
		
		if(unusedFields.size() > 0) {
			System.out.println("\n unused fields: ");
			for (int i = 0; i < unusedFields.size(); i++) {
				System.out.println(unusedFields.get(i));
			}
		}
		else {
			System.out.println("\n No unused fields");
		}
	}
	
	public void unusedParameters(String fileName) throws IOException {
		
		//get all parameters
		Map<String, List<String>> parametersMap = getParameters(fileName);
		//get all methods
		Map<String, String> methodsMap = storeMethods(fileName);
		List<String> parameters = new ArrayList();
		List<String> unusedParameters = new ArrayList();
		BufferedReader br = read(fileName);
		int parameterUsed = 0;
			
		Iterator it = parametersMap.entrySet().iterator();
		while (it.hasNext()) {
			parameterUsed = 0;
	        Map.Entry pair = (Map.Entry)it.next();
	        parameters = (List<String>) pair.getValue();
	        Iterator it2 = methodsMap.entrySet().iterator();
		    while (it2.hasNext()) {
		    	Map.Entry pair2 = (Map.Entry)it2.next();
		    	String stringPair = (String) pair2.getValue();
		    	if(pair.getKey().equals(pair2.getKey())) {
		    		for (int i = 0; i < parameters.size(); i++) {
		    			if(stringPair.contains(parameters.get(i))) {
		    				parameterUsed++;
		    			}
		    		}
		    	}
		   	}
		    if(parameterUsed < parameters.size()) {
		    	unusedParameters.add((String) pair.getKey());
		    }
		}
		
		for (int i = 0; i < unusedParameters.size(); i++) {
			System.out.println("\n" + unusedParameters.get(i) + " has unused parameters!");
		}
	}
	
	public void unusedMethods(String fileName, List<String> filenames) throws IOException {
		
		Map<String,String> method1 = storeMethods(fileName);
		List<String> unusedMethods = new ArrayList();
		List<Map> methodsMap = new ArrayList();
		int numUnusedMethods = 0;
		int usedInClass = 0;
		
		for (int i = 0; i < filenames.size(); i++) {
			Map<String,String> method = storeMethods(filenames.get(i));
			methodsMap.add(method);
		}
		
		Iterator It = method1.entrySet().iterator();
		while (It.hasNext()) {
			usedInClass = 0;
	        Map.Entry pair = (Map.Entry)It.next();
	        for (int i = 0; i < methodsMap.size(); i++) {
	        	Iterator it2 = methodsMap.get(i).entrySet().iterator();
	        	while (it2.hasNext()) {
	        		Map.Entry pair2 = (Map.Entry)it2.next();
	        		String stringPair = (String) pair2.getValue();
	        		if(stringPair.contains((String) pair.getKey())){
	        			usedInClass++;
	        		}
	        	}
	        	if(usedInClass == 0){
	        		unusedMethods.add((String) pair.getKey());
	        		numUnusedMethods++;
	        	}
	        }
		}		
		
		if(unusedMethods.size() > 0) {
			System.out.println("\n unused methods: ");
			for (int i = 0; i < unusedMethods.size(); i++) {
				System.out.println(unusedMethods.get(i));
			}
		}
		else {
			System.out.println("\n No unused methods");
		}
	}
	
	public void unusedMethodss(String fileName, String fileName2) throws IOException {
		
		Map<String,String> methods1 = storeMethods(fileName);
		Map<String,String> methods2 = storeMethods(fileName2);
		List<String> unusedMethods = new ArrayList();
		int numUnusedMethods = 0;
		
		int usedInFirstClass = 0;
		int usedInSecondClass = 0;
		
		Iterator It = methods1.entrySet().iterator();
		while (It.hasNext()) {
			usedInFirstClass = 0;
			usedInSecondClass = 0;
	        Map.Entry pair = (Map.Entry)It.next();
	        Iterator it2 = methods1.entrySet().iterator();
		    while (it2.hasNext()) {
		    	Map.Entry pair2 = (Map.Entry)it2.next();
		    	if(pair.getKey().equals(pair2.getKey())) {
		    		String stringPair = (String) pair2.getValue();
		    		if(stringPair.contains((String) pair.getKey())){
		    			usedInFirstClass++;
		    		}
		    	}
		   	}
		    Iterator it3 = methods2.entrySet().iterator();
		    while (it3.hasNext()) {
		    	Map.Entry pair2 = (Map.Entry)it3.next();
		    	if(pair.getKey().equals(pair2.getKey())) {
		    		String stringPair = (String) pair2.getValue();
		    		if(stringPair.contains((String) pair.getKey())){
		    			usedInSecondClass++;
		    		}
		    	}
		   	}
		    if(usedInFirstClass == 0 && usedInSecondClass == 0){
		    	unusedMethods.add((String) pair.getKey());
		    	numUnusedMethods++;
		    }
		}		
		
		if(unusedMethods.size() > 0) {
			System.out.println("unused methods: ");
			for (int i = 0; i < unusedMethods.size(); i++) {
				System.out.println(unusedMethods.get(i));
			}
		}
		else {
			System.out.println("No unused methods");
		}
	}
	
	public List<String> getFields(String fileName) throws IOException {
		
		String className = "";
		String fieldName = "";
		String line = "";
		List<String> fields = new ArrayList<String>();
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null){
			if(line.contains("class")){
				className = line;
				className = reduceClassName(className);
			}
			else if(line.contains("{")  && line.contains("class") == false || line.contains("}")){
				break;
			}
			else if(line.contains("int") || line.contains("String") || line.contains("bool") 
					|| line.contains("long") || line.contains("long") || line.contains("float") 
					|| line.contains("double")) {
				fieldName = line;
				fieldName = reduceFieldName(fieldName);
				fieldName = fieldName.toLowerCase();
				fields.add(fieldName);
			}
		}	
		
		return fields;
	}
	
	public Map<String, List<String>> getParameters(String fileName) throws IOException{
	
		Map<String, List<String>> map = new HashMap<String, List<String>>();	
		String line = "";
		String name = "";
		String parameters = "";
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null) {
			List<String> pl = new ArrayList();
			if((line.contains("public") || line.contains("private") || line.contains("protected")) 
					&& !line.contains(";") && !line.contains("class")) {
				name = line;
				name = reduceMethodName(name);
				if(!line.contains("()")){
					parameters = line.substring(line.indexOf("(")+1,line.indexOf(")"));
					parameters = reduceParameters(parameters);
					parameters = parameters.toLowerCase();
					parameters = parameters.replace(" ", "");
					for(String parameter : parameters.split(",")) {
						pl.add(parameter);
					}
					map.put(name, pl);
				}
				else {
					map.put(name, pl);
				}
			}
		}
		return map;
	}
	
	public Map<String,String> storeMethods(String fileName) throws IOException{
		
		Map<String, String> map = new HashMap<String, String>();
		String line = "";
		String methodName = "";
		String value = "";
		BufferedReader br = read(fileName);
		
		while((line = br.readLine()) != null){
			int closeBrackets = 0;
			int openBrackets = 0;
			if((line.contains("public") || line.contains("private") || line.contains("protected")) 
				&& line.contains(";") == false && line.contains("class") == false){
				methodName = line;
				methodName = reduceMethodName(methodName);
				openBrackets++;
				map.put(methodName, "");
				while((line = br.readLine()) != null) {
					if(line.contains("{")){
						openBrackets++;
					}
					else if(line.contains("}") && closeBrackets < openBrackets) {
						closeBrackets++;	
						if(openBrackets == closeBrackets){
							break;
						}
					}
					value = map.get(methodName);
					value = value + line.toLowerCase();
					value = value.replace(" ", "");
					value = value.replace("	", "");
					map.put(methodName, value);
				}
			}
		}
		
		return map;
	}
	
	public String reduceParameters(String name) {
		
		name = name.replace("String", "");
		name = name.replace("double", "");
		name = name.replace("int", "");
		name = name.replace("boolean", "");
		name = name.replace("bool", "");
		name = name.replace("long", "");
		name = name.replace("float", "");
		
		return name;
	}
	
	public String reduceFieldName(String name){
		
		name = name.replace("=", "");
		name = name.replace("0", "");
		name = name.replace("String", "");
		name = name.replace("double", "");
		name = name.replace("int", "");
		name = name.replace("boolean", "");
		name = name.replace("bool", "");
		name = name.replace("long", "");
		name = name.replace("float", "");
		name = name.replace("public", "");
		name = name.replace("private", "");
		name = name.replace("protected", "");
		name = name.replace(";", "");
		name = name.replace(" ", "");
		name = name.replace("	", "");
		
		return name;
	}
	
	public String reduceMethodName(String name) {
		
		name = name.replaceAll("\\(.*\\)", "");
		name = name.replace("public", "");
		name = name.replace("private", "");
		name = name.replace("protected", "");
		name = name.replace("static", "");
		name = name.replace("class", "");
		name = name.replace("void", "");
		name = name.replace("String", "");
		name = name.replace("double", "");
		name = name.replace("int", "");
		name = name.replace("bool", "");
		name = name.replace("long", "");
		name = name.replace("float", "");
		name = name.replace("()", "");
		name = name.replace("{", "");
		name = name.replace("throws IOException", "");
		name = name.replace(" ", "");
		name = name.replace("	", "");
		name = name.toLowerCase();
		
		return name;
	}
	
	public String reduceClassName(String name) {
		
		name = name.replace("{", "");
		name = name.replace("	", "");
		
		return name;
	}
	
	public BufferedReader read(String fileName) throws IOException {

		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		return br;
	}

}
