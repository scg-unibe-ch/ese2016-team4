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

<form:form method="get" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off">

	<div id="filterDiv">
	
		<h2>Filter results:</h2>

		<table>
		<tr>
		<td><form:checkbox name="buy" id="buy" path="buy"/><label>Buy</label></td>
		<td><form:checkbox name="rent" id="rent" path="rent"/><label>Rent</label></td>
		<td><form:checkbox name="auction" id="auction" path="auction"/><label>Auction</label></td>


		</tr>
		<tr>
		<td><form:checkbox name="room" id="room" path="room" /><label>Room</label></td>
		<td><form:checkbox name="studio" id="studio" path="studio" /><label>Studio</label></td>
		<td><form:checkbox name="flat" id="flat" path="flat" /><label>Flat</label></td>
		<td><form:checkbox name="house" id="house" path="house" /><label>House</label></td>


		</tr>
		</table>

		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" /><br />

		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="prize" cssClass="validationErrorText" /><br />

		<hr class="slim">

		<table style="width: 80%">
			<tr>
				<td><label for="earliestMoveInDate">Earliest move-in date</label></td>
				<td><label for="earliestMoveOutDate">Earliest move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-earliestMoveInDate"
						path="earliestMoveInDate" /></td>
				<td><form:input type="text" id="field-earliestMoveOutDate"
						path="earliestMoveOutDate" /></td>
			</tr>
			<tr>
				<td><label for="latestMoveInDate">Latest move-in date</label></td>
				<td><label for="latestMoveOutDate">Latest move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-latestMoveInDate"
						path="latestMoveInDate" /></td>
				<td><form:input type="text" id="field-latestMoveOutDate"
						path="latestMoveOutDate" /></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Smoking inside
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Animals
						inside allowed</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-garden" path="garden" value="1" /><label>Garden
						(co-use)</label></td>
				<td><form:checkbox id="field-balcony" path="balcony" value="1" /><label>Balcony
						or Patio</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cellar" path="cellar" value="1" /><label>Cellar
						or Attic</label></td>
				<td><form:checkbox id="field-furnished" path="furnished"
						value="1" /><label>Furnished</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cable" path="cable" value="1" /><label>Cable
						TV</label></td>
				<td><form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label>
				</td>
			</tr>
			<tr>
				<td><form:checkbox id="field-internet" path="internet" value="1" /><label>WiFi</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-dishwasher" path="dishwasher" value="1" /><label>Dishwasher</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-washingMachine" path="washingMachine" value="1" /><label>Washing machine</label></td>
			</tr>
		</table>


		<button type="submit">Filter</button>
		<button type="reset">Cancel</button>
	</div>
</form:form>

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