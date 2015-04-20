import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class CaptionConverter {
	private boolean printTimes;
	private String fileName;
	private String targetFilename;
	
	public static void main(String[] args){
		CaptionConverter cc;
		
		if(args.length < 1)
			throw new IllegalArgumentException("Too few arguments.");
		else if(args[0].equals("-help"))
			printHelp();
		if(!checkFileExtension(args[0])) //check to see if args[0] is a .srt
			throw new IllegalArgumentException("File must be a .srt in the correct format. See http://en.wikipedia.org/wiki/SubRip for format examples.");
		else if(args.length == 3){ //has a third argument that may or may not be -t
			if(!args[2].equals("-t"))
				throw new IllegalArgumentException("-help for all arguments");
			else{
				cc = new CaptionConverter(args[0], args[1], true);
			}
		}
		else if(args.length == 2){
			cc = new CaptionConverter(args[0], args[1], false);
		}
		else
			throw new IllegalArgumentException("Too few or too many arguments.");
		
		cc.getCaptionsAsText();
	}
	
	public CaptionConverter(String fileName, String targetFilename, boolean printTimes){
		this.printTimes = printTimes;
		this.fileName = fileName;
		this.targetFilename = targetFilename;
	}
	
	private void getCaptionsAsText(){
		try {
            File file = new File(targetFilename);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            ArrayList<CaptionBlock> captionBlocks = convertCaptionsIntoBlocks(fileName);
            if(printTimes)
            	for(CaptionBlock cb : captionBlocks)
            		output.write(cb.toString());
            else
            	for(CaptionBlock cb : captionBlocks){
            		output.write(cb.getCaption());
            		output.newLine();	
            	}
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
				while(line.length() != 0 && fileScanner.hasNextLine()){
					sb.append(line);
					sb.append("\n");
					line = fileScanner.nextLine();
				}
				CaptionBlock cb = createCaptionBlock(sb.toString());
				System.out.println(cb);
				captionBlocks.add(cb);
			}
		}catch(Exception e){
			System.out.println("Error while processing file: " + e.getMessage());
			e.printStackTrace();
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
		
		if(text.contains(">")){ //specific to my .srt files, each sentence starts with ">> "
			sb.insert(sb.indexOf(">"), "\n\n");
		}
		
		return new CaptionBlock(text, number, time);
		
	}
	
	private static boolean checkFileExtension(String file){
		//is file an .srt?
		return file.substring(file.lastIndexOf("."), file.length()).equals(".srt");
	}
	
	private static void printHelp(){
		System.out.println("CaptionConverter converts .srt subtitle files to a more readable format.");
		System.out.println();
		System.out.println("How to use:");
		System.out.println("CaptionConverter.java [<fileName>.srt] [<targetFileName>.txt]");
		System.out.println("Where <fileName>.srt is the file containing the captions "
				+ "and <targetFileName>.txt is the file to which you would like the output to be written to.");
		System.out.println("If <targetFileName>.txt does not exist it will be created. If it does exist it will be overwritten!");
		System.out.println();
		System.out.println("Arguments: \n------------");
		System.out.println("-help      Shows this help page.");
		System.out.println("-t         Outputs timestamps and line numbers");
		
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
		
		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append(lineNumber);
			sb.append(" ");
			sb.append(time);
			sb.append(" ");
			sb.append(captionText);
			return sb.toString();
		}
			
	}
	
}
