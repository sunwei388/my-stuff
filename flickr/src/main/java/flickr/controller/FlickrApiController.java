package flickr.controller;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import spittr.Spittle;
import spittr.data.SpittleRepository;
import flickr.app.Flickr;
import flickr.app.FlickrMode;
import flickr.beans.Photo;
import flickr.beans.PhotoLocation;
import flickr.beans.UnimportedPhoto;
import flickr.beans.storage.PhotoBackup;
import flickr.beans.storage.S3Backup;

@RestController
@RequestMapping("/api")

public class FlickrApiController {

	  private static final String MAX_LONG_AS_STRING = "9223372036854775807";

	  private SpittleRepository spittleRepository;

	  @Autowired
	  public FlickrApiController(SpittleRepository spittleRepository) {
	    this.spittleRepository = spittleRepository;
	  }

	  @RequestMapping(value="main_backup", method=RequestMethod.GET, produces="application/json")
	  public String[] listMainBackup(
	      @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
	      @RequestParam(value="count", defaultValue="20") int count) {
		  
		  PhotoBackup s3 = S3Backup.getS3Backup();
		  return s3.getAllFileNames();
	  }
	  
	  @RequestMapping(value="unimported", method=RequestMethod.GET, produces="application/json")
	  public List<UnimportedPhoto> findUnimportedPhotos(
	      @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
	      @RequestParam(value="count", defaultValue="20") int count) {
	    return UnimportedPhoto.getAllUnimportedPhotos(spittleRepository.getUnimportedPhotoLocation());
	  }
	  
	  @RequestMapping(value="import_all_photos", method=RequestMethod.POST, consumes="application/json")
	  @ResponseStatus(HttpStatus.CREATED)
	  public ResponseEntity<List<Photo>> importAllPhotos(@RequestBody List<UnimportedPhoto> photos_to_import, UriComponentsBuilder ucb) {
		  System.out.println("Import photos");
		  List<Photo> photos = new LinkedList<Photo>();

		  for (UnimportedPhoto p : photos_to_import) {
			  Photo photo = new Photo(-1, "", p.getExt(), p.getName(),
					                  p.getSize(), p.getCreated());
			  Photo saved = spittleRepository.savePhoto(photo);

			  // copy photo
			  p.copy(spittleRepository.getUnimportedPhotoLocation(), saved.getPhotoFileName(),
					 spittleRepository.getPhotoRepository());
			  
			  // backup photo
			  PhotoBackup backup = S3Backup.getS3Backup();
			  backup.copyInto(saved.getPhotoFileName() + ".zip"
					          , spittleRepository.getPhotoRepository()
					          , null);
			  
			  // rename photo in unimported
			  if (Flickr.getMode() != FlickrMode.dev) {
				  spittleRepository.getUnimportedPhotoLocation().rename(p.getName()+"."+p.getExt(), 
						  p.getName()+"."+p.getExt()+".imported");
			  }
			  
			  photos.add(saved);
		  }

		  HttpHeaders headers = new HttpHeaders();
		  URI locationUri = ucb.path("/photos/")
		        .build()
		        .toUri();
		  headers.setLocation(locationUri);
	    
		  ResponseEntity<List<Photo>> responseEntity = new ResponseEntity<List<Photo>>(photos, headers, HttpStatus.CREATED);
		  return responseEntity;
	  }

	  @RequestMapping(value="import_photo", method=RequestMethod.POST, consumes="application/json")
	  @ResponseStatus(HttpStatus.CREATED)
	  public ResponseEntity<Photo> importPhoto(@RequestBody UnimportedPhoto p, UriComponentsBuilder ucb) {
		  System.out.println("Import one photo " + p.getName());

		  Photo photo = new Photo(-1, "", p.getExt(), p.getName(),
					                  p.getSize(), p.getCreated());
	      Photo saved = spittleRepository.savePhoto(photo);

			  // copy photo
	      p.copy(spittleRepository.getUnimportedPhotoLocation(), saved.getPhotoFileName(),
					 spittleRepository.getPhotoRepository());
			  
			  // backup photo
		  PhotoBackup backup = S3Backup.getS3Backup();
		  backup.copyInto(saved.getPhotoFileName() + ".zip"
				  , spittleRepository.getPhotoRepository()
			      , null);
			  
			  // rename photo in unimported
	      if (Flickr.getMode() != FlickrMode.dev) {
			  spittleRepository.getUnimportedPhotoLocation().rename(p.getName()+"."+p.getExt(), 
						  p.getName()+"."+p.getExt()+".imported");
		  }
			  
		  HttpHeaders headers = new HttpHeaders();
		  URI locationUri = ucb.path("/photos/")
		        .build()
		        .toUri();
		  headers.setLocation(locationUri);
	    
		  ResponseEntity<Photo> responseEntity = new ResponseEntity<Photo>(saved, headers, HttpStatus.CREATED);
		  return responseEntity;
	  }

	  @RequestMapping(value="photos", method=RequestMethod.GET, produces="application/json")
	  public List<Photo> findPhotos(
	      @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
	      @RequestParam(value="count", defaultValue="20") int count) {
		  
		  List<Photo> photos = spittleRepository.findPhotos(max, count);
		  
		  // cleanup the cache
		  
		  // cache the photo
		  for (Photo p : photos) {
			  p.cache(spittleRepository.getPhotoRepository(), 
					  spittleRepository.getPhotoCache());
		  }
		  
		  return photos;
	  }
	  
	  // TODO temporary function used for testing purpose only
	  
	  @RequestMapping(value="photo", method=RequestMethod.POST, consumes="application/json")
	  @ResponseStatus(HttpStatus.CREATED)
	  public ResponseEntity<Photo> savePhoto(@RequestBody Photo photo, UriComponentsBuilder ucb) {
		  System.out.println("Add photo");

		  Photo saved = spittleRepository.savePhoto(photo);

		  HttpHeaders headers = new HttpHeaders();
		  URI locationUri = ucb.path("/photo/")
		        .path(String.valueOf(saved.getId()))
		        .build()
		        .toUri();
		  headers.setLocation(locationUri);
	    
		  ResponseEntity<Photo> responseEntity = new ResponseEntity<Photo>(saved, headers, HttpStatus.CREATED);
		  return responseEntity;
	  }
	  
	  @RequestMapping(value="photo/{id}", method=RequestMethod.GET, produces="application/json")
	  public Photo findPhotoById(@PathVariable Long id) {
	    return spittleRepository.findPhotoById(id);
	  }

	  
	  @RequestMapping(method=RequestMethod.POST, consumes="application/json")
	  @ResponseStatus(HttpStatus.CREATED)
	  public ResponseEntity<Spittle> importPhoto(@RequestBody Spittle spittle, UriComponentsBuilder ucb) {
	    Spittle saved = spittleRepository.save(spittle);
	    
	    HttpHeaders headers = new HttpHeaders();
	    URI locationUri = ucb.path("/spittles/")
	        .path(String.valueOf(saved.getId()))
	        .build()
	        .toUri();
	    headers.setLocation(locationUri);
	    
	    ResponseEntity<Spittle> responseEntity = new ResponseEntity<Spittle>(saved, headers, HttpStatus.CREATED);
	    return responseEntity;
	  }


}
