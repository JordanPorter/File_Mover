package FileMover;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class FileCopy extends Thread	{
	private File destinationFile;
	private File sourceFile;
	private long timeTaken = 0;

	public FileCopy(File destination, File source)	{
		this.destinationFile = destination;
		this.sourceFile = source;
	}
	
	public void run()	{
	    long start = System.nanoTime();
	    
	    FileChannel source = null;
	    FileChannel destination = null;
	    
	    if(!destinationFile.exists())	{
	    	try {
				destinationFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    try {
	    	source = new FileInputStream(sourceFile).getChannel();
	    	destination = new FileOutputStream(destinationFile).getChannel();
	    	destination.transferFrom(source, 0, source.size());
	    	source.close();
	    	destination.close();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    long end = System.nanoTime();
	    timeTaken = (end - start) / 1000000000L;
	}
	
	@Override
	public String toString()	{
		return sourceFile.getName();
	}
	
	public File getSourceFile()	{
		return sourceFile;
	}
	
	public File getDestinationFile()	{
		return destinationFile;
	}
	
	public long getTimeTaken()	{
		return timeTaken;
	}
}