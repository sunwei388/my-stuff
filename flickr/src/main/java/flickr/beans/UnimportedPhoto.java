package flickr.beans;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class UnimportedPhoto {
	private String name;
	private String ext;
	private long size;
	private Date created;
	
	public UnimportedPhoto() {
		
	}
	
	public UnimportedPhoto(String name, String ext, long size, Date created) {
		this.name = name;
		this.ext = ext;
		this.size = size;
		this.created = created;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	//TODO move to repository
	static public List<UnimportedPhoto> getAllUnimportedPhotos(PhotoLocation location) {
		List<UnimportedPhoto> photoList = new LinkedList<UnimportedPhoto>();
		
		File[] files = location.getAllFiles();
		for (File f : files) {
			if (f.isFile()) {
				UnimportedPhoto up = new UnimportedPhoto(FilenameUtils.getBaseName(f.getName())
						                                 , FilenameUtils.getExtension(f.getName()), f.length()
						                                 , new Date(f.lastModified()));
				photoList.add(up);
			}
		}
		return photoList;
	}
	

}
