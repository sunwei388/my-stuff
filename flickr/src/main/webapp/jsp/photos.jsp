<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Photos</title>

	<!-- Third Party javascripts -->
	<script type="text/javascript" src="../scripts/jquery-1.11.1.js"></script>
	<!-- Internal javascripts -->
	<script type="text/javascript" src="../app/flick.js"></script>

</head>
<body>
 
    <h1>Photos</h1>
    <form id="form_photos">
        <table id="table_photos" border="1">
          <tr>
              <td/><td>Image</td><td>Name</td><td>Ext</td><td>Created</td><td>Size</td>
          </tr>
        </table>
        <p id="p_selected_photos">0 photo selected</p>
        <input type="submit" id="button_select_all" value="Select All"/>
        <input type="submit" id="button_select_none" value="Select None"/>
    </form>

	<script type="text/javascript">

function load_all_photos() {

    $.getJSON('../api/photos/', '', function(response) {
        console.log("photos:");
        console.log(response);
        $.each(response, function (index, value) {
            $("#table_photos").append($('<tr><td><input type="checkbox" class="photo_selection" value="' + value.id + '"></td>' 
                + '<td><img src="../photo-cache/' + value.photoFileName + '" height="100" width="100"></td>' 
                + '<td>' + value.displayName + '</td><td>' + value.ext + '</td>'
                + '<td>' + value.created + '</td><td>' + value.size + '</td></tr>'));
        } );


        $('.photo_selection').change(function() {
            var n = $( ".photo_selection:checked" ).length; 
            console.log(n + " photos selected");
            $("#p_selected_photos").html(n + " photos selected");  
        } );

    } ); 
}


$(document).ready(function() {

    $('#button_select_all').click(function(e) {
        console.log("Select all");
        $('.photo_selection').prop('checked', true);    
        var n = $( ".photo_selection:checked" ).length; 
        console.log(n + " photos selected");
        $("#p_selected_photos").html(n + " photos selected");  
        e.preventDefault();
    } );

    $('#button_select_none').click(function(e) {
        console.log("Select none");
        $('.photo_selection').prop('checked', false);    
        var n = $( ".photo_selection:checked" ).length; 
        console.log(n + " photos selected");
        $("#p_selected_photos").html(n + " photos selected");  
        e.preventDefault();
    } );

    load_all_photos();
});

	</script>

</body>
</html>
