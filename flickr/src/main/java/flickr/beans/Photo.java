package flickr.beans;

import java.util.Date;

public class Photo {
	
	// Unique id for each photo
	long id;
	
	// Photo name, can be dup or empty
	String name;
	
	// File name extension, can't be empty
	String ext;
	
	// File name before importing, excluding ext
	String originalName;
	
	long size;
	
	Date created;
	
	public Photo() {
		
	}
	
	public Photo(long id, String name, String ext, String originalName,
			     long size, Date created) {
		this.id = id;
		this.name = name;
		this.ext = ext;
		this.originalName = originalName;
		this.size = size;
		this.created = created;
	}
	
	@Override
	public String toString() {
		return id + "." + ext;
	}
	
	public String getPhotoFileName() {
		return id + "." + ext;
	}
	
	public String getDisplayName() {
		if (name.equals("")) {
			return Long.toString(id);
		} else {
			return name;
		}		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
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
	
	public void cache(PhotoLocation repository, PhotoLocation cache) {
		repository.copy(getPhotoFileName(), cache);
	}
}
