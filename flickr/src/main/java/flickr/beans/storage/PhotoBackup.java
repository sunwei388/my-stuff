package flickr.beans.storage;

public abstract class PhotoBackup implements PhotoStorage {
	protected PhotoStorageStats stats;
	public long id;
	public String type;
	public String url;
	public String container;
	
	public PhotoBackup(long id, String type, String url, String container) {
		stats = new PhotoStorageStats(1000, 10000000);
		
		this.id = id;
		this.type = type;
		this.url = url;
		this.container = container;
	}

	@Override
	public boolean isBackup() {
		return true;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public String getContainer() {
		return container;
	}

	@Override
	public PhotoStorageStats getStats() {
		return stats;
	}

	
}
