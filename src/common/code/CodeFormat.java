package common.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CodeFormat {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String filePath="";
		String[] separator = new String[] { ":" };
		BufferedReader fReader = new BufferedReader(
				new FileReader(filePath));
		String tmpline="";
		
		while ((tmpline = fReader.readLine()) != null) {
			
		}
		fReader.close();		
	}

}
