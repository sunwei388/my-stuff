package flickr.beans;

import java.io.File;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;

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
	
	public String getURL(String fileName) {
		return path + "/" + fileName;
	}
	
	public void copy(String name, PhotoLocation dst) {
		
		File src = new File(path + "/" + name);
		File target = new File(dst.getPath() + "/" + name);

		try {
			Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
		    throw new RuntimeException("Copy failed" + e);
		}
	}

}
