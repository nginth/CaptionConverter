import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class CaptionConverter {
	
//	public static void main(String[] args){
//		CaptionConverter cc = new CaptionConverter();
//		String filename = "captionTest.srt";
//		StringBuffer sb = new StringBuffer();
//		try{
//			Scanner sc = new Scanner(new File(filename));
//			while(sc.hasNextLine()){
//				sb.append(sc.nextLine());
//				sb.append("\n");
//			}
//			String block = sb.toString();
//			System.out.println(block);
//			CaptionBlock cb = cc.createCaptionBlock(block);
//			System.out.println(cb.getCaption());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}

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
					sb.append("\n");
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
		
		number = blockScanner.nextInt();
		blockScanner.nextLine();
		time = blockScanner.nextLine();
		
		StringBuffer sb = new StringBuffer();
		while(blockScanner.hasNextLine()){
			sb.append(blockScanner.nextLine());
			sb.append(" ");
		}
		text = sb.toString();
		
		if(text.charAt(0) == '>'){ //specific to my .srt files, each sentence starts with ">> ", this removes it
			text = text.substring(3);
		}
		
		return new CaptionBlock(text, number, time);
		
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
