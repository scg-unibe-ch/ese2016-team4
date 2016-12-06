<%@ page language="java" pageEncoding="iso-8859-1"
	contentType="text/html;charset=iso-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />
<pre><a href="/">Home</a>   &gt;   <a href="/searchAd/">Search</a>   &gt;   Results</pre>

<script>
/*
 * This script takes all the resultAd divs and sorts them by a parameter specified by the user.
 * No arguments need to be passed, since the function simply looks up the dropdown selection.
 */
function sort_div_attribute() {
    //determine sort modus (by which attribute, asc/desc)
    var sortmode = $('#modus').find(":selected").val();

    //only start the process if a modus has been selected
    if(sortmode.length > 0) {
    	var attname;

    	//determine which variable we pass to the sort function
		if(sortmode == "price_asc" || sortmode == "price_desc"){
			attname1 = 'data-rentPrice';
    		attname2 = 'data-buyPrice';
    		attname3 = 'data-auctionPrice';
		}
	    else if(sortmode == "moveIn_asc" || sortmode == "moveIn_desc"){
			attname1 = 'data-moveIn';
			attname2 = 'data-moveIn';
			attname3 = 'data-moveIn';
	    }
	    else{
			attname1 = 'data-age';
    		attname2 = 'data-age';
    		attname3 = 'data-age';
	    }
		//copying divs into an array which we're going to sort
	    var divsbucket = new Array();
	    var divslist = $('div.resultAd');
	    var divlength = divslist.length;
	    
	    for (a = 0; a < divlength; a++) {
			divsbucket[a] = new Array();
			if(parseInt(divslist[a].getAttribute('data-sellType'))==1)
				divsbucket[a][0] = divslist[a].getAttribute(attname1);
			else if(parseInt(divslist[a].getAttribute('data-sellType'))==2)
				divsbucket[a][0] = divslist[a].getAttribute(attname2);
			else
				divsbucket[a][0] = divslist[a].getAttribute(attname3);
			divsbucket[a][1] = divslist[a];
			divsbucket[a][2] = divslist[a].getAttribute('data-premium');
			divslist[a].remove();
	    }
	   

	    
	    //sort the array
		divsbucket.sort(function(a, b) {
	    if (parseInt(a[0]) == parseInt(b[0])){
	    	//document.write("Equals: a[0]: "+a[0]+", b[0]: "+b[0]+"<br>");
			return 0;
	    }
	    else if (parseInt(a[0]) > parseInt(b[0])){
	    	//document.write("A bigger: a[0]: "+a[0]+", b[0]: "+b[0]+"<br>");
			return 1;
	    }
        else{
        	//document.write("B bigger: a[0]: "+a[0]+", b[0]: "+b[0]+"<br>");
			return -1;
        }
		});

	    //invert sorted array for certain sort options
		if(sortmode == "price_desc" || sortmode == "moveIn_asc" || sortmode == "dateAge_asc")
			divsbucket.reverse();
	    

		var premiumsort = [];
		
		for(a=0; a < divlength; a++){
			if(divsbucket[a][2] == 'true'){
				premiumsort.push(divsbucket[a][1]);
			}
		}
		
		for(a=0; a < divlength; a++){
			if(divsbucket[a][2] == 'false'){
				premiumsort.push(divsbucket[a][1]);
			}
		}
		
		var premiumLength = premiumsort.length;
		
	    //insert sorted divs into document again
		for(a = 0; a < divlength; a++)
        	$("#resultsDiv").append($(premiumsort[a]));
	}
}
</script>

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
	});
</script>

<%-- Need to chose at least 1 propertyType and 1 sellType --%>
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
function updateType() {
	if(document.getElementById("enableMaps").checked){
		document.getElementById("resultsDiv").hidden = true;
		document.getElementById("resultsDivMaps").hidden = false;
	}else{
		document.getElementById("resultsDiv").hidden = false;
		document.getElementById("resultsDivMaps").hidden = true;
	}
}

$('document').ready(function(){
	document.getElementById('enableMaps').onchange = updateType;
});
window.onload = updateType;
</script>

<%-- Script for the API key --%>
<script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCdNwB8auysJ8k7gqiKOpLwFyV2L7iBneo&callback=initMap">
</script>

<%-- Main script for the google map --%>
<script>
var coordinates = new Array();

<c:forEach var="coord" items="${coords}">
	coordinates.push("${coord}");
</c:forEach>

var arrayLength = coordinates.length;

var infowindow = null;
function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 8,
		//this is the center of switzerland (älgialp, 6072 sachseln)
		center: {lat: 46.801111, lng: 8.226667}
	});
    //content gets overwritten for each new marker later
    infowindow = new google.maps.InfoWindow({
	    content: "couldnt load info"
	});
    
    for (var i = 0; i < arrayLength; i++) {
    	var splitCoords = coordinates[i].split(" ");
    	
    	var content = "<a href='ad?id="+splitCoords[0]+"'>"+splitCoords[0]+"</a>";
    	var latitude = parseFloat(splitCoords[1]);
    	var longitude = parseFloat(splitCoords[2]);
    	
		var marker = new google.maps.Marker({
			position: {lat: latitude, lng: longitude},
			map: map
		});
		
		//eventlistener for the marker, so the infowindow gets openend when clicked on
		google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
		    return function() {
		        infowindow.setContent(content);
		        infowindow.open(map,marker);
		    };
		})(marker,content,infowindow));
	}
}

</script>

<h1>Search results:</h1>

<hr />

<table>
  <tr>
  	<td style="width:600px;">
	<select id="modus">
    	<option value="">Sort by:</option>
    	<option value="price_asc">Price (ascending)</option>
    	<option value="price_desc">Price (descending)</option>
    	<option value="moveIn_desc">Move-in date (earliest to latest)</option>
    	<option value="moveIn_asc">Move-in date (latest to earliest)</option>
    	<option value="dateAge_asc">Date created (youngest to oldest)</option>
    	<option value="dateAge_desc">Date created (oldest to youngest)</option>
	</select>
  	</td>
    <td><b>Switch between list and google maps</b></td>
  </tr>
  <tr>
    <td>
		<button onClick="sort_div_attribute()">Sort</button>
    </td>
    <td>
		<label class="switch">
  		<input type="checkbox" id="enableMaps">
  		<div class="slider round"></div>
		</label>
	</td>
  </tr>
</table>

<c:choose>
	<c:when test="${empty results}">
		<p>No results found!
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">
			<c:forEach var="ad" items="${results}">
				<div class="resultAd" data-rentPrice="${ad.prizePerMonth}"
								data-moveIn="${ad.moveInDate}" data-age="${ad.creationDate}"
								data-auctionPrice="${bidService.getHighestBid(ad.id)}"
								data-buyPrice="${ad.prizeOfSale}"
								data-sellType="${ad.sellType}"
								data-premium="${ad.getUser().isPremium()}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
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
							<c:when test="${ad.getSellType() == 1}"><h2>CHF ${ad.prizePerMonth }</h2></c:when>
							<c:when test="${ad.getSellType() == 2}"><h2>Sale Price ${ad.prizeOfSale } CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 3}"><h2>Auction Price ${bidService.getHighestBid(ad.id)} CHF</h2></c:when>
						</c:choose>						<br /> <br />

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
		<div id="resultsDivMaps">
    		<div id="map"></div>		 
		</div>
	</c:otherwise>
</c:choose>

<form:form method="get" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off" onsubmit="typesNotEmpty()">

	<div id="filterDiv">
	
		<h2>Filter results:</h2>

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
			</tr>
			<tr>
				<td><form:checkbox id="field-dishwasher" path="dishwasher" value="1" /><label>Dishwasher</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-washingMachine" path="washingMachine" value="1" /><label>Washing machine</label></td>
			</tr>
		</table>
		<%-- used to redirect to the previous page if form input was erroneous --%>
		<form:input type="hidden" name="page" value="results" path=""/>

		<button type="submit">Filter</button>
		<button type="reset">Cancel</button>
	</div>
</form:form>

<c:import url="template/footer.jsp" />
