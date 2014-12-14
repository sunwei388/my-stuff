package flickr.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import spittr.Spittle;
import spittr.data.SpittleRepository;

@RestController
@RequestMapping("/api")

public class FlickrApiController {

	  private static final String MAX_LONG_AS_STRING = "9223372036854775807";

	  private SpittleRepository spittleRepository;

	  @Autowired
	  public FlickrApiController(SpittleRepository spittleRepository) {
	    this.spittleRepository = spittleRepository;
	  }

	  @RequestMapping(method=RequestMethod.GET, produces="application/json")
	  public List<Spittle> testFunc(
	      @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
	      @RequestParam(value="count", defaultValue="20") int count) {
	    return spittleRepository.findSpittles(max, count);
	  }

	  @RequestMapping(value="unimported", method=RequestMethod.GET, produces="application/json")
	  public List<Spittle> getUnimportedPhotos(
	      @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
	      @RequestParam(value="count", defaultValue="20") int count) {
	    return spittleRepository.findSpittles(max, count);
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
