package flickr.beans.storage;

import java.io.File;

import flickr.beans.PhotoLocation;

public interface PhotoStorage {
	
	boolean isBackup();
	
	String getType();
	
	String getURL();
	
	String getContainer();
	
	void copyInto(String srcName, PhotoLocation src, String dstName);
	
	void copyTo(String srcName, PhotoLocation dst, String dstName);
	
	File getFile(String fileName);
	
	String[] getAllFileNames();
	
	PhotoStorageStats getStats();
	
}
