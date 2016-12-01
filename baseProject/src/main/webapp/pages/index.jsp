<%@ page language="java" pageEncoding="iso-8859-1"
	contentType="text/html;charset=iso-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to FlatFindr</title>
</head>

<body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#city").autocomplete({
			minLength : 2
		});
		$("#city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
	});
	
</script>

<pre>Home</pre>

<h1>Welcome to FlatFindr!</h1>

<form method="post" action="/indexSearch"
	id="filterForm" autocomplete="off" >
	<div id="indexDiv">
	<h1>Fast Search:</h1> <br>
		
		
		<font size="5"><b>What?</b></font>
		<table>
		<tr>
		<td><input type="checkbox" name="room" id="room"  /><label><b>Room</b></label></td>
		<td><input type="checkbox" name="studio" id="studio"  /><label><b>Studio</b></label></td>
		<td><input type="checkbox" name="flat" id="flat" /><label><b>Flat</b></label></td>
		<td><input type="checkbox" name="house" id="house"  /><label><b>House</b></label></td>

		</tr>
		</table>

		<label for="city"><font size="5"><b>Where?</b></font></label>
		<input type="text" name="city" id="city" required="required"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" />
	
		<br /> 

		<button type="submit" class="buttonDiv" >Go!</button>
		<button type="reset" class="buttonDiv" >Reset</button>
	</div>
</form>

<c:choose>
	<c:when test="${empty newest}">
		<h2>No ads placed yet</h2>
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">	
			<h2>Our newest ads:</h2>		
			<c:forEach var="ad" items="${newest}">
				<div class="resultAd">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							
							<i><c:choose>
									<c:when test="${ad.getPropertyType() == 1}">Room</c:when>
									<c:when test="${ad.getPropertyType() == 2}">Studio</c:when>
									<c:when test="${ad.getPropertyType() == 3}">Flat</c:when>
									<c:when test="${ad.getPropertyType() == 4}">House</c:when>
								</c:choose></i>
							<br /><br />
							<i><c:choose>
									<c:when test="${ad.getSellType() == 1}">Rent</c:when>
									<c:when test="${ad.getSellType() == 2}">Buy</c:when>
									<c:when test="${ad.getSellType() == 3}">Auction</c:when>
								</c:choose></i>
						
						</p>
					</div>
					
					
					<div class="resultRight">
						<c:choose>
							<c:when test="${ad.getSellType() == 1}"><h2>Monthly Rent ${ad.prizePerMonth } CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 2}"><h2>Sale Price ${ad.prizeOfSale } CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 3 && !bidService.isBidden(ad.getId())}"><h2>Auction Price ${ad.startOffer} CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 3 && bidService.isBidden(ad.getId())}"><h2>Auction Price ${bidService.getHighestBid(ad.getId())} CHF</h2></c:when>
						</c:choose>
						<br /> 

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />
						<c:choose>
							<c:when test="${ad.getSellType() == 1}"><p>Move-in date: ${formattedMoveInDate }</p></c:when>
						</c:choose>
						<c:choose>
							<c:when test="${ad.getUser().isPremium()==true}"><p>>>>Premium<<<</p></c:when>
						</c:choose>

						
					</div>
				</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>

<c:import url="template/footer.jsp" /><br />