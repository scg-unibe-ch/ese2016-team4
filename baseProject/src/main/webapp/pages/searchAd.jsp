<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Search</pre>


<script>
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
		
		$("#field-earliestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-earliestMoveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		var price = document.getElementById('prizeInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>


<script>
function validateType(form)
{
	var room = document.getElementById('room');
	var studio = document.getElementById('studio');
	var flat = document.getElementById('flat');
	var house = document.getElementById('house');
	var onlyone = document.getElementById('onlyone')
	
	if(room.checked) onlyone.value = "1";
	if(studio.checked) onlyone.value = "2";
	if(flat.checked) onlyone.value = "3";
	if(house.checked) onlyone.value = "4";

}
</script>

<h1>Search for an ad</h1>
<hr />


<form:form method="post" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off">

	<div id="searchDiv">
		<h2>Search:</h2>
		<table>
		<tr>
		<td><form:checkbox id="buy" path="buy" value="1" /><label>Buy</label></td>
		<td><form:checkbox id="rent" path="rent" value="1" /><label>Rent</label></td>
		<td><form:checkbox id="auction" path="auction" value="1" /><label>Auction</label></td>

		</tr>
		<tr>
		<td><form:checkbox name="room" id="room" path="roomHelper" /><label>Room</label></td>
		<td><form:checkbox name="studio" id="studio" path="studioHelper" /><label>Studio</label></td>
		<td><form:checkbox name="flat" id="flat" path="flat" /><label>Flat</label></td>
		<td><form:checkbox name="house" id="house" path="house" /><label>House</label></td>
		<form:input style="display:none" id="onlyone" type="number" path="propertyType"/>
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
		</table>
			
		
		<button type="submit" onClick="validateType(this.form)">Search</button>	
		<button type="reset">Cancel</button>
	</div>
</form:form>

<c:import url="template/footer.jsp" />