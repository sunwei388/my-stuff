<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
  <head>
    <title>Flickr</title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value="/resources/style.css" />" >
<script type="text/javascript" src="../scripts/jquery-1.11.1.js"></script>
<script type="text/javascript">

jQuery["postJSON"] = function( url, data, callback ) {
    // shift arguments if data argument was omitted
    if ( jQuery.isFunction( data ) ) {
        callback = data;
        data = undefined;
    }

    return jQuery.ajax({
        url: url,
        type: "POST",
        contentType:"application/json; charset=utf-8",
        dataType: "json",
        data: data,
        success: callback
    });
};

$(function(){

    console.log("java script starts.");

// prepare tags

    $.getJSON('../spittles/tags/', '', function(response) {
        console.log("tags:");
        console.log(response);
        $.each(response, function (index, value) {
            $("#photo_tags_to_add").append($('<option/>', { 
                value: value.id,
                text : value.name 
            }));
        } );
    } ); 

    var photos = [ ];

    $.getJSON('../spittles/photos', '',
    function(response) {
        photos = response;

        photos.forEach(function(element) {
            console.log(element);
            $('<tr>',
             {
                 id: element.id
             })
            .appendTo('#photo_table');

            $('<td><input type="checkbox" name="" id="photo_checkbox_' + element.id + '"></td>').appendTo('#' + element.id);

            $('<td>', 
             {
                 id: element.id + "_image"
             } ).appendTo('#' + element.id);

            $('<td>',
              {
                  id: "tag" + element.id
              } ).appendTo('#' + element.id);

            $.getJSON('../spittles/photo_photo_tags/photo/' + (element.id), '', function(resp) {
            	console.log(resp);
                $('#tag' + element.id).html(resp.toString());
            } ); // function response for tags
            
            $('<img>',
              {
                  src: '../photo_cache/' + element.id + '.' +'png', //+ element.ext,
                  alt: element.name,
                  height: '100',
                  width: '130'
              })
             .appendTo('#' + element.id + "_image");
        } );  // function foreach
    } );  // function response

    $('#add_photo_tag').click(function(e) {
        e.preventDefault();

        console.log("selection is " + $("#photo_tags_to_add").val());
 
        for (var i=0; i<photos.length; i++) {
            if ($('#photo_checkbox_' + (i+1)).attr('checked') == true) {
                console.log((i+1) + ' is checked');
                var ppt = {
                     photo_id: i+1,
                     photo_tag_id: $("#photo_tags_to_add").val()
                };

                // Logic to prevent Async call to use wrong i value
                // if the code in postJSON/getJSON is using i+1 instead of id
                // the callback for getJSON is called, the i value is different to i value when postJSON is called

                var id = i+1;

                $.postJSON('../spittles/photo_photo_tags/photo', JSON.stringify(ppt), function() {
                    $.getJSON('../spittles/photo_photo_tags/photo/' + id, function(resp) {
                        console.log(resp);
                        $('#tag' + id).html(resp.toString());
                    } ); // function response for tags
                });
            }
        }
    });

  });    

</script>
  </head>
  <body>
    <h1>Welcome to Flickr</h1>

    <form>
        <table id="photo_table" border="1px solid green">
            <thead>
                <tr><td>Image</td><td>Name</td><td>Tags</td><td>Size</td></tr>
            </thead>
        </table>
        <select id="photo_tags_to_add"> </select>
        <input type="submit" value="Add Tags" id="add_photo_tag">
    </form>
    
  </body>
</html>
