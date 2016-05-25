This is CS370 PROJECT Phase 2

********************************************************************************************************************************************
How to run from Command Line:

1. Create a directory named CS370Project. Save the emailed file " Phase2.java " and "input.txt" files in the directory
2. Output file need not be created at this time.
3. Navigate to the directory where CS370Project is stored in Command Line.
4. Type javac Phase2.java
6. Type java Phase2 inputFileName.txt outputfileName.txt

Note: inputFileName.txt and outputFileName.txt can be substituted for the name of input and output file that you want.
    
 
*********************************************************************************************************************************************
How it works:
1.  The prodram takes two parameters on the Command Line, one for input text file and another for output text file. It
    reads the different URL's from input file that is the first parameter on the Command Line,connect with those URL's, extracts various 
    information and print them in a output file that is second parameter on the Command Line. If the the URL extension is .jpg/.jpeg, 
    it saves the image in system and if the extension is .pdf, it saves that in system in a output file. Otherwise, 
    it saves every html/htm/txt information in separate file of system. It also prints total number of lines read from the url with the 
    extension .html/.htm/.txt .

Note: Several files for pdf/jpg or for html lines will be generated in system after program execution.
 
*********************************************************************************************************************************************
                                                ***********END***********