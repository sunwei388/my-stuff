package spittr.data;

import java.util.List;

import spittr.Spittle;
import flickr.beans.Photo;
import flickr.beans.PhotoLocation;

public interface SpittleRepository {

  List<Spittle> findRecentSpittles();

  List<Spittle> findSpittles(long max, int count);
  
  Spittle findOne(long id);

  Spittle save(Spittle spittle);
  
  List<Photo> findPhotos(long max, int count);
  
  Photo findPhotoById(long id);
  
  Photo savePhoto(Photo phone);
  
  PhotoLocation getUnimportedPhotoLocation();
  
  PhotoLocation getPhotoRepository();
  
  PhotoLocation getPhotoCache();

}
