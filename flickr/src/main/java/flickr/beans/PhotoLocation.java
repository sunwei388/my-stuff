package flickr.beans;

import java.io.File;

//TODO get setting from config file

public class PhotoLocation {
	private String path;
	
	public PhotoLocation() {
		
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

}
