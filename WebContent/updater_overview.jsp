<%@ page session="true"
	import="de.htwg_konstanz.ebus.framework.wholesaler.api.bo.*,de.htwg_konstanz.ebus.framework.wholesaler.api.boa.*,java.math.BigDecimal,de.htwg_konstanz.ebus.framework.wholesaler.vo.util.PriceUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>eBusiness Framework Demo - Welcome</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>

	<%@ include file="header.jsp"%>
	<%@ include file="error.jsp"%>
	<%@ include file="authentication.jsp"%>
	<%@ include file="navigation.jspfragment"%>

	<h1>Article Updater</h1>

	<h2>Updated Articles:</h2>
	<table class="dataTable">
		<thead>
			<tr>
				<th><b>Order No.</b></th>
				<th><b>Title</b></th>
				<th><b>Description</b></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="updatedProduct" items="${sessionScope.listOfUpdatedProducts}">
				<jsp:useBean id="updatedProduct"
					type="org.example.update_catalog_webservice.SupplierProduct" />
				<tr valign="top">
					<td>
					<%=updatedProduct.getSupplierAID()%>
					</td>
					<td><%=updatedProduct.getShortDescription()%></td>
					<td width="400px"><%=updatedProduct.getLongDescription()%></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h2>Deleted Articles</h2>
	
	<table class="dataTable">
		<thead>
			<tr>
				<th><b>Order No.</b></th>
				<th><b>Title</b></th>
				<th><b>Description</b></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="unavailableProduct" items="${sessionScope.listOfUnavailableProducts}">
				<jsp:useBean id="unavailableProduct"
					type="org.example.update_catalog_webservice.SupplierProduct" />
				<tr valign="top">
					<td>
					<%=unavailableProduct.getSupplierAID()%>
					</td>
					<td><%=unavailableProduct.getShortDescription()%></td>
					<td width="400px"><%=unavailableProduct.getLongDescription()%></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<br>
	
	<div>
		<form method="post" action="controllerservlet">
			<input type="hidden" name="action" value="updateProducts">
			<input type="submit" value="Perform Update">
		</form>
	</div>

</body>
</html>
