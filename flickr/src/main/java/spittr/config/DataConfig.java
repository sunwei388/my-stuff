package spittr.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import flickr.app.Flickr;
import flickr.app.FlickrMode;

@Configuration
public class DataConfig {

  @Bean
  public DataSource dataSource() {
	  
	  if (Flickr.getMode() == FlickrMode.dev) {
		  System.out.println("Running in dev mode");
		  return new EmbeddedDatabaseBuilder()
	            .setType(EmbeddedDatabaseType.H2)
	            .addScript("schema.sql")
	            .build();
	  } else {
		    return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("schema.sql")
            .build();		  
	  }
  }
  
  @Bean
  public JdbcOperations jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

}
