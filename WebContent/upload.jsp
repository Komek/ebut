<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true"
	import="de.htwg_konstanz.ebus.wholesaler.demo.util.Constants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Upload</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>

<script>
   function validate(){
	   
	   if (document.getElementById('file').value !== '') {
	         document.getElementById('upload-form').submit();
	      }   
	      else {
	         alert('No File Attached, please select you catalog xml file!');
	      }


 }
</script>

	<%@ include file="header.jsp"%>
	<%@ include file="error.jsp"%>
	<%@ include file="authentication.jsp"%>
	<%@ include file="navigation.jspfragment"%>

	<h1>Upload File</h1>
	<center>

		<form name="upload-form" id="upload-form" method="post"
			action="<%=response.encodeURL("controllerservlet") + "?action="
					+ Constants.ACTION_UPLOAD_CATALOG%>"
			enctype="multipart/form-data">
			<span class="label">Select XML document:</span> <input name="file" id="file" type="file"
				name="<%=Constants.PARAM_NAME_FILE%>" /> <br> <input
				type="button" value="Send" onclick="validate()"/>
		</form>

	</center>
</body>
</html>
