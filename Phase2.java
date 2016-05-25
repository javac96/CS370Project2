
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/*
 * CS370, Project Phase-2, 
 * Phase2.java
 * 
 * Class of the program including main method, it is responsible for taking two parameters on the Command Line, 
 * reading the different URL's from input file that is the first parameter on the Command Line,
 * connect with those URL's, extracts various information and print them in a output file, second parameter on the Command Line. 
 * If the the URL extension is .jpg/.jpeg, it saves the image in system and if the extension is .pdf, 
 * it saves that in system in a output file. Otherwise, it saves every html/htm/txt information in separate file of system.
 * 
 * @name: Mohammad R Hasan
 */
public class Phase2 {

	static BufferedWriter bw;//creates a static instance of BufferedWriter so that other method can use it.

	/**
	 * 
	 * main method takes two arguments passed as parameter, one is input text file and another output text file.
	 * it is also responsible for reading every url, connect them, and also confirm in console that every task
	 * was done successfully.
	 * 
	 */
	public static void main(String[] args) {
		String inputFileName = args[0];//argument passed for input file
		String outputFileName = args[1];//argument passed for output file
		FileWriter fw = null;//initialize the FileWriter object
		try {//if any file exists
			//The file can be appended using FileWriter alone however using BufferedWriter improves the performance as it maintains a buffer.
			fw = new FileWriter(new File(outputFileName), true); //Here true is to append the content to file
			bw = new BufferedWriter(fw);//wrap a BufferedWriter around File Writer for efficient writing
		} catch (IOException e) {
			e.printStackTrace();
		}//catch

		BufferedReader br = null;//initialize BufferedReader
		try {
			String urlLine;//variable for read each line of input file of url's
			//BufferedReader wraps around  FileReader, to buffer the input and improve efficiency.
			br = new BufferedReader(new FileReader(inputFileName));
			int count = 1;//counting the url's that were read
			while ((urlLine = br.readLine()) != null) {//read up to the las line
				URL url = new URL(urlLine);//create a url object with every url from input file
				URLConnection connection = url.openConnection();// get a url connection instance  
				connection.connect();//establish the actual network connection with the url
				getURLinfo(connection);//invoke the method for different url information
				printURLinfo(connection, "url_" + count);//invoke the method to write url image/html codes/pdf/ text 
				System.out.println("Read/Write Successfull for : "
						+ connection.getURL());//confirm in console that the respective url information was read and written
				count++;//counting the url number
			}
			bw.close();//Closing BufferedWriter Stream
		} catch (IOException e) {
			e.printStackTrace();
		}//catch
	}//end of main method

	/**
	 * method responsible for writing every url information starting with the respective url name
	 * in output file that is the second argument passed on the Command Line
	 */
	public static void getURLinfo(URLConnection uc)throws IOException {
		// write in output file - the URL address, and information about it.
		bw.write(uc.getURL().toExternalForm() + ":");//construct a string representation of the url objects and write them in output file
		bw.newLine();//create a new line
		bw.write("Content Type: " + uc.getContentType());//write the content type of url in output file
		bw.newLine();
		//write the content length of the url's, -1 if the content length is not known, or if the content length is greater than Integer.MAX_VALUE.
		bw.write("  Content Length: " + uc.getContentLength());
		bw.newLine();
		//write the date the url was last modified, or 0 if not known.
		bw.write("  Last Modified: " + new Date(uc.getLastModified()));
		bw.newLine();
		//write the expiration date of the url, or 0 if not known
		bw.write("  Expiration: " + uc.getExpiration());
		bw.newLine();
		//write the content encoding of the resource that the URL references, or null if not 
		bw.write("  Content Encoding: " + uc.getContentEncoding());
		bw.newLine();

	}//end of getURLInfo method

	/**
	 * This method deals with regular expression pattern and matcher. It saves the image or pdf file in the 
	 * system if the url matches url is jpg/jpeg/gif, or pdf respectively. Otherwise saves the whole content in output file.
	 * Method also leave a message in output file (second argument) where the content was saved and also total count of 
	 * lines read. If nothing found throw exception.
	 */
	public static void printURLinfo(URLConnection uc, String fileName) throws MalformedURLException, IOException{
		Pattern pattern = Pattern.compile(".*"+getURLExtension(uc)+".*");//compiles the extension of url into a pattern
		Matcher matcherJPG = pattern.matcher("([^\\s]+(\\.(?i)(jpg/jpeg/gif))$)");//creates a matcher that will match jpg/jpeg/gif against the pattern
		Matcher matcherPDF = pattern.matcher("[^\\s]+(\\.(?i)(pdf))$");//creates a matcher that will match pdf against the pattern
		if (matcherJPG.matches()){//if pattern matches to jpg/jpeg/gif 
			bw.write("Content Saved to File: " + fileName + ".jpg");//write a message where content was save referring file name
			getURLImage(uc, fileName + ".jpg");//invoke the method to save image in a file in the system
		} else if (matcherPDF.matches()) {//if pattern matches to pdf
			bw.write("Content Saved to File: " + fileName + ".pdf");//write a message where content was save referring file name
			writePdf(uc, fileName + ".pdf");//invoke the method ot save pdf in a file in the system
		} else {//if url is not jpg/jpeg/gif or pdf
			bw.write("Content Saved to File: " + fileName + ".txt , Lines:"
					+ writeText(uc, fileName + ".txt"));//write the whole content of url in a output file, with total number of lines.
		}//else out
		bw.newLine();//create new line
		bw.newLine();
		
	}//end of method printURLInfo
	

	/**
	 * This method reads the every line of html/htm/ text url in output file, writes them in output file and 
	 * return the total number of lines it read
	 */
	public static int writeText(URLConnection uc, String fileName) {
		try {
			File file = new File(fileName);//create a file instance with file name
			//get the input stream from url and create a input stream and convert to buffered reader object
			BufferedReader brr
	          = new BufferedReader(new InputStreamReader(uc.getInputStream()));

			if (!file.exists()) {//if there is not file, 
				file.createNewFile();//create a new one
			}//if
			// create new instance of a File object representing the file located at the absolute path of the current File object.
			// and then construct a FileWriter object with that instance
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);//convert file write object to buffered writer object
			String everyLine = "";//initialize the string
			int count = 0;//initialize the count that will count the total lines
			while ((everyLine = brr.readLine()) != null) {//read line up to the end of url
				bw.write(everyLine);//write every line in file
				bw.newLine();
				count++;//increment the line number
			}//while
			bw.close();//close the file
			return count;// retrun the total count
		} catch (Exception e) {//if no file found
			return 0;//retrun zero
		}//catch
	}//end of method writeText

	/**
	 * This method read the image of url having jpg/jpeg/gif at the end and save them in 
	 * an output file of system
	 */
	public static void getURLImage(URLConnection uc, String fileName) {
		File outputImageFile = new File(fileName);//create the File instance of output file to be written image
		BufferedImage image = null;//inialize the BufferedImage
		try {
			// Read from a URL
			image = ImageIO.read(uc.getURL());
			//write the image to the filepath specified as a jpg
			ImageIO.write(image, "jpg", outputImageFile);
		} catch (IOException e) {
		}
	}//end of getURLImage

	/**
	 * This method read the content of url having pdf at the end and save them in 
	 * an output file of system
	 * @throws IOException
	 */
	public static void writePdf(URLConnection uc, String fileName) throws IOException{

		//declare the instances and initialize
		BufferedInputStream in = null;
		FileOutputStream fileout = null;
		try {
			//Opens a connection to the URL and returns an InputStream for reading from that connection
			in = new BufferedInputStream(uc.getURL().openStream());//convert that inputStream to Buffered Input Stream
			fileout = new FileOutputStream(fileName);//Creates a file output stream to write to the file with the specified name

			byte buffer[] = new byte[1024];//allocates memory for 1024 bytes
			int numberOfByte;//uses to read number of byte
			//Reads some number of bytes, starting at  offset 0, maximum number of bytes to read 1024 
			//from the input stream and stores them into the buffer array
			while ((numberOfByte = in.read(buffer, 0, 1024)) != -1) {
				//writes total number of bytes from the specified byte array starting at offset 0 to this file output stream.
				fileout.write(buffer, 0, numberOfByte);
			}//while

		} finally {//closing streams in finally block
			if (in != null)
				in.close();//close the input stream
			if (fileout != null)
				fileout.close();//close the file output stream
		}//finally
	}//end of method writePdf

	/**
	 *This method forms the url in a string and returns the extension of url.
	 */
	public static String getURLExtension(URLConnection uc) {
		String url = uc.getURL().toExternalForm();//constructs string representation of url's
		//String extension = url.substring(url.length()-3);//
		if (url == null) {//if there is no url 
			return null;//return nothing
		}//if
		//the index of the last occurrence of the specified substring, or -1 if there is no such occurrence.
		int dot = url.lastIndexOf(".");
		if (dot >= 0) {
			return url.substring(dot);//return the extension of url, add 1 because we do not want to get return with (".")
		} else {
			// No extension.
			return "";
		}//else
	}//end of method getURLExtension
	
}//end of class Phase2
