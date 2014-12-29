package flickr.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

//TODO get setting from config file

public class PhotoLocation {
	private String path;
	
	public PhotoLocation() {
		
	}

	@Override
	public String toString() {
		return "Location:" + path;
	}
	
	public PhotoLocation(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public File[] getAllFiles() {

        final File folder = new File(path);
        if (folder != null) return folder.listFiles();
        else return null;
		
	}
	
	public File getFile(String fileName) {
		return new File(path + "/" + fileName);
	}
	
	public String getURL(String fileName) {
		return path + "/" + fileName;
	}
	
	public void copy(String srcName, String dstName, PhotoLocation dst) {
		copy(srcName, dstName, dst, PhotoCopyMode.plain);
	}
	
	
	/* Copy with compression option
	 * Note: the caller should never add .zip into file name. If it's zip, the dst file 
	 *       name is appended with zip automatically. If it's unzip, the src file 
	 *       name is appended with zip automatically.
	 * 
	 * 
	 * 1. plain mode, copy file 
	 * 2. zip mode, zip the file to src.zip. Copy it to dst.zip
	 * 3. unzip mode, unzip the src.zip to src. Copy it to dst
	 */
	
	public void copy(String srcName, String dstName, PhotoLocation dst, PhotoCopyMode mode) {
		File src = null;
		File target = null;
		
		System.out.println("Copy " + srcName + " to " + dstName + " " + dst + " mode " + mode);
		
		if (mode == PhotoCopyMode.plain) {
			src = new File(path + "/" + srcName);
			target = new File(dst.getPath() + "/" + dstName);
		} else if (mode == PhotoCopyMode.zip) {
			String zipFileName = zip(srcName, dstName);
			src = new File(path + "/" + zipFileName);
			target = new File(dst.getPath() + "/" + dstName + ".zip");		
		} else if (mode == PhotoCopyMode.unzip) {
			srcName = unzip(srcName);
			src = new File(path + "/" + srcName);
			target = new File(dst.getPath() + "/" + dstName);			
		}

		try {
			Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
		    throw new RuntimeException("Copy failed" + e);
		}
		
		if (mode == PhotoCopyMode.zip || mode == PhotoCopyMode.unzip) {
			src.delete();
		}
	}
	
	private String zip(String srcName, String dstName) {
		String zipFileName = srcName + ".zip";
		
		byte[] buffer = new byte[1024];
		 
    	try{
    		
    		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path + "/" + zipFileName));
    		ZipEntry ze= new ZipEntry(dstName);
    		zos.putNextEntry(ze);
    		FileInputStream in = new FileInputStream(path + "/" + srcName);
 
    		int len;
    		while ((len = in.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}
 
    		in.close();
    		zos.closeEntry();
 
    		//remember close it
    		zos.close();
    		
    	} catch(IOException ex){
    	   ex.printStackTrace();
    	}
		
		return zipFileName; 
	}
	
	private String unzip(String srcName) {
		String unzipFileName = srcName;
		
	    byte[] buffer = new byte[1024];
		   
		try {

		    ZipInputStream zis = new ZipInputStream(
		    		new FileInputStream(path + File.separator + srcName + ".zip"));
		    ZipEntry ze = zis.getNextEntry();
		    
		    if (ze == null) {
            	zis.closeEntry();
            	zis.close();
		    	throw new RuntimeException("No content in zip file " + srcName + ".zip");
		    }
		 
		    String fileName = ze.getName();
		    // check fileName equals unzipFilename
		    
		    File newFile = new File(path + File.separator + fileName);
		 
		    System.out.println("file unzip : "+ newFile.getAbsoluteFile());
		 
		    FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
            	fos.write(buffer, 0, len);
            }
 
            fos.close();   

            ze = zis.getNextEntry();
            if (ze != null) {
            	zis.closeEntry();
            	zis.close();
            	throw new RuntimeException("More than one entry in " + srcName);
            }
		 
		    zis.closeEntry();
		    zis.close();
		 
		} catch(IOException ex){
		   ex.printStackTrace(); 
		}

		return unzipFileName;
	}
	
	public void rename(String name, String newName) {
		File src = new File(path + "/" + name);
		File target = new File(path + "/" + newName);

		src.renameTo(target);
	}

}
