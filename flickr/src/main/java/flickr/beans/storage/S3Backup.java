package flickr.beans.storage;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import flickr.beans.PhotoLocation;


public class S3Backup extends PhotoBackup {

	public S3Backup(long id, String container) {
		super(id, "S3", "s3.amazonaws.com", container);
	}
	
	@Override
	public void copyInto(String srcName, PhotoLocation src, String dstName) {
		AmazonS3 s3 = getS3();
		File f = src.getFile(srcName);
		
		if (dstName == null) dstName = srcName;
		
        s3.putObject(new PutObjectRequest(container, dstName, f));		
	}

	@Override
	public void copyTo(String srcName, PhotoLocation dst, String dstName) {
	}

	public File getFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String[] getAllFileNames() {
		AmazonS3 s3 = getS3();
		List<String> fileNameList = new LinkedList<String>();
		
	    ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
        			.withBucketName(container));
	    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
	    	System.out.println(" - " + objectSummary.getKey() + "  " +
                       "(size = " + objectSummary.getSize() + ")");
	    	fileNameList.add(objectSummary.getKey());
	    }
	    
	    return fileNameList.toArray(new String[fileNameList.size()]);
	}
	
	private AmazonS3 getS3() {
		
	    /*
	     * The ProfileCredentialsProvider will return your [default]
	     * credential profile by reading from the credentials file located at
	     * (~/.aws/credentials).
	     */
	    AWSCredentials credentials;
		
	    try {
	        credentials = new ProfileCredentialsProvider().getCredentials();
	    } catch (Exception e) {
	        throw new AmazonClientException(
	                "Cannot load the credentials from the credential profiles file. " +
	                "Please make sure that your credentials file is at the correct " +
	                "location (~/.aws/credentials), and is in valid format.",
	                e);
	    }
	
	    AmazonS3 s3 = new AmazonS3Client(credentials);
	    return s3;
	}


}
