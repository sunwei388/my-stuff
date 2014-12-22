package flickr.beans.storage;

public class PhotoStorageStats {
	long numItems;
	long totalSize;
	
	final long itemLimit;
	final long sizeLimit;
	
	public PhotoStorageStats(long itemLimit, long sizeLimit) {
		this.itemLimit = itemLimit;
		this.sizeLimit = sizeLimit;
	}

	public long getNumItems() {
		return numItems;
	}

	public void setNumItems(long numItems) {
		this.numItems = numItems;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getItemLimit() {
		return itemLimit;
	}

	public long getSizeLimit() {
		return sizeLimit;
	}
	

}
