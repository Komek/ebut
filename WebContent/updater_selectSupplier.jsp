<%@ page session="true"
	import="de.htwg_konstanz.ebus.framework.wholesaler.api.bo.*,de.htwg_konstanz.ebus.framework.wholesaler.api.boa.*,java.math.BigDecimal,de.htwg_konstanz.ebus.framework.wholesaler.vo.util.PriceUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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

	<div>
		<form method="post" action="controllerservlet">
		<input type="hidden" name="action" value="showUpdates">
			<div>
		<select name="<%=Constants.PARAM_SUPPLIER%>">
					<c:forEach var="supplier" items="${sessionScope.supplierList}">
						<jsp:useBean id="supplier"
							type="de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier" />
						<option value="<%=supplier.getSupplierNumber()%>"><%=supplier.getCompanyname()%></option>
					</c:forEach>
				</select>
			</div>
			<br>
			<div>
				<input type="submit" value="Show Updates">
			</div>

		</form>
	</div>

</body>
</html>