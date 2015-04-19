import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class CaptionConverter {

	public void getCaptionsAsText(String filename){
		try {
            File file = new File("captionsText.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            for(CaptionBlock cb : convertCaptionsIntoBlocks(filename))
            	output.write(cb.getCaption());
            output.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
	}
	
	private ArrayList<CaptionBlock> convertCaptionsIntoBlocks(String filename){
		ArrayList<CaptionBlock> captionBlocks = new ArrayList<CaptionBlock>();
		
		try{
			FileReader inputFile = new FileReader(filename);
			Scanner fileScanner = new Scanner(inputFile);
			String line;
			
			while(fileScanner.hasNextLine()){
				line = fileScanner.nextLine();
				StringBuffer sb = new StringBuffer();
				
				//when line.length == 0, create new CaptionBlock
				while(line.length() != 0){
					sb.append(fileScanner.nextLine());
				}
				captionBlocks.add(createCaptionBlock(sb.toString()));
				fileScanner.nextLine(); //throw away the empty line
			}
		}catch(Exception e){
			System.out.println("Error while processing file: " + e.getMessage());
		}
		
		return captionBlocks;
	}
	
	private CaptionBlock createCaptionBlock(String block){
		String text;
		int number;
		String time;
		Scanner blockScanner = new Scanner(block);
		
		
		while(blockScanner.hasNextLine()){
			
		}
		
	}
	
	private class CaptionBlock{
		private String captionText;
		private int lineNumber;
		private String time;
		
		public CaptionBlock(String caption, int lineNumber, String time){
			this.captionText = caption;
			this.lineNumber = lineNumber;
			this.time = time;
		}
		
		public String getCaption(){
			return captionText;
		}
		
		public String getTime(){
			return time;
		}
		
		public int getLineNumber(){
			return lineNumber;
		}
	
		
	}
	
}
