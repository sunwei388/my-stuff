package flickr.app;

public class Flickr {
	
	static private FlickrMode mode;
	
	static {
		  String modeStr = System.getProperty("flickr.mode");
		  System.out.println("flickr.mode " + modeStr);
			  
		  if (modeStr.equals("dev")) mode = FlickrMode.dev;
		  else if (modeStr.equals("svt")) mode = FlickrMode.svt;
		  else if (modeStr.equals("sandbox")) mode = FlickrMode.sandbox;
		  else if (modeStr.equals("prod")) mode = FlickrMode.prod;
		  else mode = FlickrMode.dev;
	}
	
	public static FlickrMode getMode() {
		return mode;
	}

}
