<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Photos to Import</title>

	<!-- Third Party javascripts -->
	<script type="text/javascript" src="../scripts/jquery-1.11.1.js"></script>
	<!-- Internal javascripts -->
	<script type="text/javascript" src="../app/flick.js"></script>

</head>
<body>
 
    <h1>Unimported Photo List</h1>
    <form id="form_unimported">
        <table id="table_unimported" border="1">
          <tr>
              <td/><td>Image</td><td>Name</td><td>Ext</td><td>Created</td><td>Size</td>
          </tr>
        </table>
        <p id="p_selected_photos">0 photo selected</p>
        <input type="submit" id="button_select_all" value="Select All"/>
        <input type="submit" id="button_select_none" value="Select None"/>
        <input type="submit" id="button_import_all_photos" value="IMPORT"/>
    </form>

	<script type="text/javascript">

function load_all_unimported_photos() {

    $.getJSON('../api/unimported/', '', function(response) {
        console.log("unimported:");
        console.log(response);
        $.each(response, function (index, value) {
            $("#table_unimported").append($('<tr><td><input type="checkbox" class="photo_selection" value="' + value.name + "." + value.ext + '"></td>' 
                + '<td></td>' 
                + '<td>' + value.name + '</td><td>' + value.ext + '</td>'
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

    $('#button_import_all_photos').click(function(e) {
        console.log("Import all photos");
        var photo_to_import = [];
        $( ".photo_selection:checked" ).each(function() {
            photo_to_import.push(this.value);
        } );
        console.log(photo_to_import);

	jQuery.ajax({
            url: "../api/import_photo",
            type: "POST",
            contentType:"application/json; charset=utf-8",
	    dataType: "json",
	    data: JSON.stringify(photo_to_import),
            success: function(data) {
        	console.log(data);
            }			
        } );

        e.preventDefault();
    } );

    load_all_unimported_photos();
});

	</script>

</body>
</html>
