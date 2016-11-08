<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Alerts</pre>

<script>
function deleteAlert(button) {
	var id = $(button).attr("data-id");
	$.get("/profile/alerts/deleteAlert?id=" + id, function(){
		$("#alertsDiv").load(document.URL + " #alertsDiv");
	});
}
</script>


<script>
	$(document).ready(function() {

		$("#field-city").autocomplete({
			minLength : 2
		});
		$("#field-city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#field-city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		$("#field-moveInDate").datepicker({
			dateFormat : 'dd.mm.yyyy'
		});
		$("#field-moveOutDate").datepicker({
			dateFormat : 'dd.mm.yyyy'
		});

	});
</script>

<h1>Create and manage alerts</h1>
<hr />

<h2>Create new alert</h2><br />

<form:form method="post" modelAttribute="alertForm" action="/profile/alerts"
	id="alertForm" autocomplete="off">
	<fieldset id="alertDiv">
		<table>
			<tr>
				<td>
				<form:select path="propertyType">
				<form:option id="type-room" value="1">Room</form:option>
				<form:option id="type-room" value="2">Studio</form:option>
				<form:option id="type-room" value="3">Flat</form:option>
				<form:option id="type-room" value="4">House</form:option>
				</form:select>
				</td>

				<td>
				<form:select path="sellType">
				<form:option type="type-room" value="1">Rent</form:option>
				<form:option type="type-room" value="2">Buy</form:option>
				<form:option type="type-room" value="3">Auction</form:option>
				</form:select>
				</td>


				<td><form:errors path="propertyType" cssClass="validationErrorText" /></td>
				<td><form:errors path="sellType" cssClass="validationErrorText" /></td>

			</tr>

			<tr>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
				<td><form:input type="text" id="field-city" path="city" placeholder="City"/>
					<form:errors path="city" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td><label for="moveInDate"  id="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate" id="moveOutDate">Move-out date</label></td>
			</tr>

			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" /></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" /></td>
			</tr>

			<tr>
				<td><label for="field-Prize" id="prize">Price (max.):</label></td>
				<td><label for="radius">Within radius of (max.):</label></td>

			</tr>

			<tr>
				<td><form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per month" step="50" /> <form:errors
						path="prize" cssClass="validationErrorText" /></td>
				<td><form:input id="radiusInput" type="number" path="radius"
						placeholder="e.g. 5" step="5" /> <form:errors
						path="radius" cssClass="validationErrorText" /></td>

			</tr>

			<tr>
				<td><label for="field-SquareFootage">Size</label></td>
			</tr>
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage" placeholder="Prize per month" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
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
		</table>

		<button type="submit" tabindex="7" onClick="validateType(this.form)">Subscribe</button>
		<button type="reset" tabindex="8">Cancel</button>

	</fieldset>
</form:form> <br />
<h2>Your active alerts</h2>

<div id="alertsDiv" class="alertsDiv">


<c:choose>
	<c:when test="${empty alerts}">
		<p>You currently aren't subscribed to any alerts.
	</c:when>
	<c:otherwise>
		<table class="styledTable" id="alerts">
			<thead>
			<tr>
				<th>Property Type</th>
				<th>Sale Type</th>
				<th>City</th>
				<th>max. Radius</th>
				<th>max. Price</th>
				<th>Size</th>
				<th>Move-in date</th>
				<th>Move-out date</th>
				<th>Room description</th>
				<th>Action</th>


			</tr>
			</thead>
		<c:forEach var="alert" items="${alerts}">
			<tr>
				<td>
				<c:choose>
					<c:when test="${alert.getPropertyType() == 1}">
						Room
					</c:when>
					<c:when test="${alert.getPropertyType() == 2}">
						Studio
					</c:when>
					<c:when test="${alert.getPropertyType() == 3}">
						Flat
					</c:when>
					<c:when test="${alert.getPropertyType() == 4}">
						House
					</c:when>

				</c:choose>
				</td>
				<td>
				<c:choose>
					<c:when test="${alert.getSellType() == 1}">
						Rent
					</c:when>
					<c:when test="${alert.getSellType() == 2}">
						Buy
					</c:when>
					<c:when test="${alert.getSellType() == 3}">
						Auction
					</c:when>
				</c:choose>

				</td>
				<td>${alert.city}</td>
				<td>${alert.radius} km</td>
				<td>${alert.price} CHF</td>
				<td>${alert.squareFootage} m²</td>
				<td><c:out value="${alert.moveInDate}"/></td>
				<td><c:out value="${alert.moveOutDate}"/></td>
				<td>
					<c:if test="${alert.getSmokers()}">
						Smoking allowed,
					</c:if>
					<c:if test="${alert.getAnimals()}">
						Animals,
					</c:if>
					<c:if test="${alert.getGarden()}">
						Garden,
					</c:if>
					<c:if test="${alert.getBalcony()}">
						Balcony,
					</c:if>
					<c:if test="${alert.getCellar()}">
						Cellar,
					</c:if>
					<c:if test="${alert.getFurnished()}">
						Furnished,
					</c:if>
					<c:if test="${alert.getCable()}">
						Cable TV,
					</c:if>
					<c:if test="${alert.getGarage()}">
						Garage,
					</c:if>
					<c:if test="${alert.getInternet()}">
						WiFi
					</c:if>
				</td>
				<td><button class="deleteButton" data-id="${alert.id}" onClick="deleteAlert(this)">Delete</button></td>
			</tr>
		</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
</div>

<c:import url="template/footer.jsp" />
