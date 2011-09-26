package me.jack.AutoUpdater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoUpdater {
	
public static void main(String[] args){
	try {
		new AutoUpdater().start();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
/** URL of the remote resource to be downloaded */
private URL url;
/** target object to be populated */
// again just a test download target
private Object target = new File("C:\\autoupdater test\\Download.jar");
private static final int BUFFER_SIZE = 1024;
public void download()
{
    try {
    	if(exists((File) target)){
    		backup(((File) target).getPath(),((File) target).getPath() + "backup");
    		System.out.println("Backed up");
    	}
    	else{
		deleate((File) target);
    	}
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    BufferedReader br = null;
    try
    {
        /* open connection to the URL */
        final URLConnection link;

        try
        {
            link = url.openConnection();
            link.connect();
        }
        catch (Exception e)
        {
          
            throw e;
        }
        /* get size of webpage in bytes; -1 if unknown */
        final int length = link.getContentLength();
        try
        {
            final InputStream input = link.getInputStream();

            if (target instanceof File)
            {
                bis = new BufferedInputStream(input);
            }
            else if (target instanceof StringBuilder)
            {
                final String contentType = link.getContentType().toLowerCase(Locale.ENGLISH);

                /* look for charset, if specified */
                String charset = null;
                final Matcher m = Pattern.compile(".*charset[\\s]*=([^;]++).*").matcher(contentType);

                if (m.find())
                {
                    charset = m.group(1).trim();
                }

                if ((charset != null) && !charset.isEmpty())
                {
                    try
                    {
                        br = new BufferedReader(new InputStreamReader(input, charset));
                    }
                    catch (Exception e)
                    {
                        br = null;
                    }
                }

                if (br == null)
                {
                    br = new BufferedReader(new InputStreamReader(input));
                }
            }
        }
        catch (Exception e)
        {
          
            throw e;
        }

        /* open output stream, if necessary */
        if (target instanceof File)
        {
            try
            {
                /* create parent directories, if necessary */
                final File f = (File) target;
                final File parent = f.getParentFile();

                if ((parent != null) && !parent.exists())
                {
                    parent.mkdirs();
                }

                bos = new BufferedOutputStream(new FileOutputStream(f));
            }
            catch (Exception e)
            {
          
                throw e;
            }
        }
        try
        {
            if (target instanceof File)
            {
                final byte[] byteBuffer = new byte[BUFFER_SIZE];
                System.out.println("File!");
                while (true)
                {
                   
                    final int byteCount = bis.read(byteBuffer, 0, BUFFER_SIZE);

                    /* check for end-of-stream */
                    if (byteCount == -1)
                    {
                        break;
                    }

                    bos.write(byteBuffer, 0, byteCount);
                } 
            }
            else if (target instanceof StringBuilder)
            {
                final char[] charBuffer = new char[BUFFER_SIZE];
                final StringBuilder sb = (StringBuilder) target;

                while (true)
                {
                    final int charCount = br.read(charBuffer, 0, BUFFER_SIZE);

                    /* check for end-of-stream */
                    if (charCount == -1)
                    {
                        break;
                    }

                    sb.append(charBuffer, 0, charCount);
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        /* clean-up */
        for (Closeable c : new Closeable[] {bis, br, bos})
        {
            if (c != null)
            {
                try
                {
                    c.close();
                }
                catch (Exception e)
                {
                    /* ignore */
                }
            }
        }

    }
}
public void start(){
	try {
		//hard coded,this was just a test download
		this.url = new URL("http://dl.dropbox.com/u/33874811/AutoFurnace/AutoFurnace.jar");
		download();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void backup(String fromFileName,String toFileName) throws IOException{
		    File fromFile = new File(fromFileName);
		    File toFile = new File(toFileName);
		    if (!fromFile.exists())
		      throw new IOException("FileCopy: " + "no such source file: "
		          + fromFileName);
		    if (!fromFile.isFile())
		      throw new IOException("FileCopy: " + "can't copy directory: "
		          + fromFileName);
		    if (!fromFile.canRead())
		      throw new IOException("FileCopy: " + "source file is unreadable: "
		          + fromFileName);
		    if (toFile.isDirectory())
		      toFile = new File(toFile, fromFile.getName());
		    if (toFile.exists()) {
		    } else {
		      String parent = toFile.getParent();
		      if (parent == null)
		        parent = System.getProperty("user.dir");
		      File dir = new File(parent);
		      if (!dir.exists())
		        throw new IOException("FileCopy: "
		            + "destination directory doesn't exist: " + parent);
		      if (dir.isFile())
		        throw new IOException("FileCopy: "
		            + "destination is not a directory: " + parent);
		      if (!dir.canWrite())
		        throw new IOException("FileCopy: "
		            + "destination directory is unwriteable: " + parent);
		    }
		    FileInputStream from = null;
		    FileOutputStream to = null;
		    try {
		      from = new FileInputStream(fromFile);
		      to = new FileOutputStream(toFile);
		      byte[] buffer = new byte[4096];
		      int bytesRead;
		      while ((bytesRead = from.read(buffer)) != -1)
		        to.write(buffer, 0, bytesRead); // write
		    } finally {
		      if (from != null)
		        try {
		          from.close();
		        } catch (IOException e) {
		          ;
		        }
		      if (to != null)
		        try {
		          to.close();
		        } catch (IOException e) {
		          e.printStackTrace();
		        }
		    }  
}
public boolean deleate(String file){
	File f = new File(file);
    return f.delete();		
}
public boolean deleate(File f){
	return f.delete();
}
public boolean exists(File f){
	return f.exists();
}
}
