<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import = "java.util.LinkedList" %>
<%@ page import = "photomanager.beans.Photo, photomanager.beans.PhotoTag" %>

<% String base = (String)application.getAttribute("base"); %>
<jsp:useBean id="dataManager" scope="application"
  class="eshop.model.DataManager"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Welcome to photo Manager</title>
</head>
<body>

<%
  String photoCacheURL = "/eshop/photocache/"; //(String)application.getAttribute("imageURL");
%>

<% 
    String photoTag = request.getParameter("select_tag");

	LinkedList<Photo> photos = dataManager.getAllPhotosWithTag(photoTag); 
	Photo.cache(photos);
	
	LinkedList<PhotoTag> tags = dataManager.getAllPhotoTags();
%>
<%=photos.size() %> photos

<table border="1" style="width:100%">
  <tr>
    <th>Photo</th>
    <th>Photo Name</th>
    <th>Taken on</th>
    <th>Tag</th>	
  </tr>

<%
	for (Photo photo: photos) {
	    String tag = "";
	    if (photo.getTags().size() > 0) {
	        for (String t : photo.getTags()) { tag += t+" "; }
	    }
	    
	    String formSelection = "<select name=\"tag_id\">";
	    for (PhotoTag tagToAdd : tags) {
	        if (!photo.isTagExist(tagToAdd.getTagName())) {
	        formSelection += "<option value=\"" + tagToAdd.getTagId() + "\">" + tagToAdd.getTagName() + "</option>";
	        }
	    }
	    formSelection += "</select>";
	    
        out.println("<tr><td>" + "<a href=\"" + photoCacheURL + photo.getFileName() 
                + "\"><img src=\"" + photoCacheURL + photo.getFileName() 
                + "\" border=\"0\" width=\"100\" height=\"100\"/></a>" + "</td><td>"+ photo.getName() 
                + "</td><td>" + photo.getCreation_date() + "</td><td>" + tag
                + "<form action=\"\" method=\"get\"><input type=\"hidden\" name=\"action\" value=\"addTagForPhoto\"/>" 
                + "<input type=\"hidden\" name=\"photo_id\" value=\"" + photo.getId() + "\"/>" + formSelection 
                + "<input type=\"submit\" value=\"add tag\"/></form>"
                + "</td></tr>");
      }
  %>

</table>
    
<jsp:include page="tags.jsp" flush="true"/>
</body>
</html>