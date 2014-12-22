package spittr.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import spittr.Spittle;
import flickr.beans.Photo;
import flickr.beans.PhotoLocation;

@Repository
public class JdbcSpittleRepository implements SpittleRepository {

  private JdbcOperations jdbc;

  @Autowired
  public JdbcSpittleRepository(JdbcOperations jdbc) {
    this.jdbc = jdbc;
  }

  public List<Photo> findPhotos(long max, int count) {
      return jdbc.query(
    		  "select id, name, ext, original_name, size, created"
 	          + " from photos" 
    	      + " order by id desc limit 50",
	          new PhotoRowMapper());
  }
  
  public Photo findPhotoById(long id) {
	  try {
		  return jdbc.queryForObject(
               "select id, name, ext, original_name, size, created" 
		       + " from photos" 
               + " where id = ?",
               new PhotoRowMapper(), id);
    } catch (EmptyResultDataAccessException e) {
      throw new SpittleNotFoundException(id);
    }
  }
  
  public Photo savePhoto(Photo photo) {
	
	  String sql = "insert into photos (name, ext, original_name, size, created)" +
	" values (?, ?, ?, ?, ?)";
	PreparedStatementCreatorFactory statementCreatorFactory = new PreparedStatementCreatorFactory(sql, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP);
	statementCreatorFactory.setReturnGeneratedKeys(true);
	PreparedStatementCreator creator = statementCreatorFactory.newPreparedStatementCreator(new Object[] {
	    photo.getName(),
	    photo.getExt(),
	    photo.getOriginalName(),
	    photo.getSize(),
	    photo.getCreated()
	});
	
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	jdbc.update(creator, keyHolder);
	return new Photo(
	    keyHolder.getKey().longValue(), 
	    photo.getName(),
	    photo.getExt(),
	    photo.getOriginalName(),
	    photo.getSize(),
	    photo.getCreated());
  }

  public List<Spittle> findRecentSpittles() {
    return jdbc.query(
        "select id, message, created_at, latitude, longitude" +
        " from Spittle" +
        " order by created_at desc limit 20",
        new SpittleRowMapper());
  }

  public List<Spittle> findSpittles(long max, int count) {
    return jdbc.query(
        "select id, message, created_at, latitude, longitude" +
        " from Spittle" +
        " where id < ?" +
        " order by created_at desc limit 20",
        new SpittleRowMapper(), max);
  }

  public Spittle findOne(long id) {
    try {
    return jdbc.queryForObject(
        "select id, message, created_at, latitude, longitude" +
        " from Spittle" +
        " where id = ?",
        new SpittleRowMapper(), id);
    } catch (EmptyResultDataAccessException e) {
      throw new SpittleNotFoundException(id);
    }
  }

  public Spittle save(Spittle spittle) {
    
    System.out.println("--> " + spittle.getMessage());
    
    
    String sql = "insert into Spittle (message, created_at, latitude, longitude)" +
        " values (?, ?, ?, ?)";
    PreparedStatementCreatorFactory statementCreatorFactory = new PreparedStatementCreatorFactory(sql, Types.VARCHAR, Types.TIMESTAMP, Types.DOUBLE, Types.DOUBLE);
    statementCreatorFactory.setReturnGeneratedKeys(true);
    PreparedStatementCreator creator = statementCreatorFactory.newPreparedStatementCreator(new Object[] {
        spittle.getMessage(),
        spittle.getTime(),
        spittle.getLatitude(),
        spittle.getLongitude()
    });

    
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(creator, keyHolder);
    return new Spittle(
        keyHolder.getKey().longValue(), 
        spittle.getMessage(), 
        spittle.getTime(), 
        spittle.getLongitude(), 
        spittle.getLatitude());
    
    
  }

  private static class SpittleRowMapper implements RowMapper<Spittle> {
    public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Spittle(
          rs.getLong("id"),
          rs.getString("message"), 
          rs.getDate("created_at"), 
          rs.getDouble("longitude"), 
          rs.getDouble("latitude"));
    }
  }
  
  private static class PhotoRowMapper implements RowMapper<Photo> {
	    public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
	      return new Photo(
	          rs.getLong("id"),
	          rs.getString("name"), 
	          rs.getString("ext"), 
	          rs.getString("original_name"), 
	          rs.getLong("size"), 
	          rs.getDate("created"));
	    }
	  }

  public PhotoLocation getUnimportedPhotoLocation() {
	  return new PhotoLocation("/home/sunwei/flickr-unimported");
  }
  
  public PhotoLocation getPhotoRepository() {
	  return new PhotoLocation("/home/sunwei/flickr-repository");	  
  }
  
  public PhotoLocation getPhotoCache() {
	  return new PhotoLocation("/home/sunwei/tools/apache-tomcat-7.0.57/webapps/flickr/photo-cache/");	   
  }
  
}
