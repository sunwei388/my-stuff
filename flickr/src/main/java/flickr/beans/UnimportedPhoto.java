package flickr.beans;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class UnimportedPhoto {
	
	public UnimportedPhoto() {
		
	}
	
	static public List<UnimportedPhoto> getAllUnimportedPhotos(PhotoLocation location) {
		List<UnimportedPhoto> photoList = new LinkedList<UnimportedPhoto>();
		
		File[] files = location.getAllFiles();
		
		return photoList;
	}
	

}
