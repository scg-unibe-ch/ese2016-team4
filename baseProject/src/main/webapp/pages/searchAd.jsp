<%@ page language="java" pageEncoding="iso-8859-1"
	contentType="text/html;charset=iso-8859-1"%>
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
			dateFormat : 'dd.mm.yy'
		});
		$("#field-latestMoveInDate").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		$("#field-earliestMoveOutDate").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		$("#field-latestMoveOutDate").datepicker({
			dateFormat : 'dd.mm.yy'
		});

		var price = document.getElementById('prizeInput');
		var radius = document.getElementById('radiusInput');

		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<%-- Need to choose at least 1 propertyType and 1 sellType --%>
<script>
function typesNotEmpty() {
	if(!(document.getElementById('room').checked || document.getElementById('studio').checked 
	|| document.getElementById('flat').checked || document.getElementById('house').checked)){
		document.getElementById('noPropertyType').checked = true;
	}else{document.getElementById('noPropertyType').checked = false;}
	if(!(document.getElementById('buy').checked || document.getElementById('rent').checked 
	|| document.getElementById('auction').checked)){
				document.getElementById('noSellType').checked = true;
	}else{document.getElementById('noSellType').checked = false;}
}
</script>

<script>
$(document).ready( function() {
    var now = new Date();
    var today = now.getDate() + '.' + (now.getMonth() + 1) + '.' + now.getFullYear();
    $('#field-earliestMoveInDate').val(today);
});
</script>


<h1>Search for an ad</h1>
<hr />


<form:form method="get" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off" onsubmit="typesNotEmpty()">

	<div id="searchDiv">
		<h2>Search:</h2>
		<table>
		<tr>
		<td><form:checkbox name="buy" id="buy" path="buy"/><label>Buy</label></td>
		<td><form:checkbox name="rent" id="rent" path="rent"/><label>Rent</label></td>
		<td><form:checkbox name="auction" id="auction" path="auction"/><label>Auction</label></td>
		<form:checkbox name="noSellType" id="noSellType" path="noSellType" hidden="true"/>
		<form:errors path="noSellType" cssClass="validationErrorText" />
		</tr>
		
		<tr>
		<td><form:checkbox name="room" id="room" path="room" /><label>Room</label></td>
		<td><form:checkbox name="studio" id="studio" path="studio" /><label>Studio</label></td>
		<td><form:checkbox name="flat" id="flat" path="flat" /><label>Flat</label></td>
		<td><form:checkbox name="house" id="house" path="house" /><label>House</label></td>
		<form:checkbox name="noPropertyType" id="noPropertyType" path="noPropertyType" hidden="true"/>
		<form:errors path="noPropertyType" cssClass="validationErrorText" />
		</tr>
		</table>


		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" /><br />

		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="1" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="1" />
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
				<td><form:checkbox id="field-dishwasher" path="dishwasher" value="1" /><label>Dishwasher</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-washingMachine" path="washingMachine" value="1" /><label>Washing machine</label></td>
			</tr>
		</table>
		<%-- used to redirect to the previous page if form input was erroneous --%>
		<form:input type="hidden" name="page" value="searchAd" path=""/>

		<button type="submit">Search</button>
		<button type="reset">Cancel</button>
	</div>
</form:form>

<c:import url="template/footer.jsp" />
