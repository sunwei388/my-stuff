<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Unimported Photo List</title>

	<!-- Third Party javascripts -->
	<script type="text/javascript" src="../scripts/jquery-1.11.1.js"></script>
	<!-- Internal javascripts -->
	<script type="text/javascript" src="../app/flick.js"></script>

</head>
<body>

    <form id="form_unimported">
        <table id="table_unimported" border="1">
          <tr>
              <td/><td>Image</td><td>Name</td><td>Ext</td><td>Created</td><td>Size</td>
          </tr>
        </table>
        <p id="p_selected_photos">0 photo selected</p>
        <input type="submit" id="button_import_all_photos" value="IMPORT"/>
    </form>

	<script type="text/javascript">
		$(document).ready(function() {
                    $("#p_selected_photos").html("Started");  
           });
	</script>

</body>
</html>
