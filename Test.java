package codeSmells;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Test {
	
	public static void main(String args[]) throws IOException {
		
		Sniffer sniffer = new Sniffer();
		
		sniffer.largeClass("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.countCommentsInClass("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.countLinesInMethods("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.countParameters("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.countDuplicateMethods("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.dataClass("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.unusedFields("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
		
		sniffer.unusedParameters("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt");
				
		//List<String> list = new ArrayList<String>();
		//String filename1 = "C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test2.txt";
		//list.add(filename1);
		
		//sniffer.unusedMethods("C:\\Users\\japea\\OneDrive\\Pictures\\University\\Year 4\\test.txt", list);
	}

}
