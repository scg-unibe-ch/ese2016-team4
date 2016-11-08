<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<script src="/js/pictureUploadEditAd.js"></script>

<script src="/js/editAd.js"></script>


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
			dateFormat : 'dd.mm.yy'
		});
		$("#field-moveOutDate").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		
		$("#field-visitDay").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		$("#field-auctionEndDate").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		
		$("#addbutton").click(function() {
			var text = $("#roomFriends").val();
			var alreadyAdded = $("#addedRoommates").html();
			if(validateForm(text)) {
				$.post("/profile/placeAd/validateEmail",{email: text, alreadyIn: alreadyAdded}, function(data) {
					if(validateForm(data)) {
						// length gibt die Anzahl der Elemente im input.roommateInput an. Dieser wird in index geschrieben und iteriert.
						var index = $("#roommateCell input.roommateInput").length;
						$("#roommateCell").append("<input class='roommateInput' type='hidden' name='registeredRoommateEmails[" + index + "]' value='" + data + "' />");
						$("#addedRoommates").append(data + "; ");
					} else {
						alert(data);
					}});
			}
			else {
				alert("Please enter an e-mail adress");
			}
			 
			// Validates the input for Email Syntax
			function validateForm(text) {
			    var positionAt = text.indexOf("@");
			    var positionDot = text.lastIndexOf(".");
			    if (positionAt< 1 || positionDot<positionAt+2 || positionDot+2>=text.length) {
			        return false;
			    } else {
			    	return true;
			    }
			}
		});
		
		$("#addVisitButton").click(function() {
			var date = $("#field-visitDay").val();
			if(date == ""){
				return;
			}
			
			var startHour = $("#startHour").val();
			var startMinutes = $("#startMinutes").val();
			var endHour = $("#endHour").val();
			var endMinutes = $("#endMinutes").val();
			
			if (startHour > endHour) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			} else if (startHour == endHour && startMinutes >= endMinutes) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			}
			
			var newVisit = date + ";" + startHour + ":" + startMinutes + 
				";" + endHour + ":" + endMinutes; 
			var newVisitLabel = date + " " + startHour + ":" + startMinutes + 
			" to " + endHour + ":" + endMinutes; 
			
			var index = $("#addedVisits input").length;
			
			var label = "<p>" + newVisitLabel + "</p>";
			var input = "<input type='hidden' value='" + newVisit + "' name='visits[" + index + "]' />";
			
			$("#addedVisits").append(label + input);
		});
		
		$(".deleteRoommateButton").click(function()  {
			var userId = $(this).attr("data-user-id");
			var adId = $(this).attr("data-ad-id");
			var row = $(this).parent().parent();
			$.post("/profile/editAd/deleteRoommate", {userId: userId, adId: adId}, function() {
				$(row).animate({opacity: 0}, 300, function() {$(row).remove(); } );
			});
		
		});
	});
</script>

<!-- format the dates -->
<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd.MM.yyyy" />
<fmt:formatDate value="${ad.moveOutDate}" var="formattedMoveOutDate"
	type="date" pattern="dd.MM.yyyy" />
	
<pre><a href="/">Home</a>   &gt;   <a href="/profile/myRooms">My Rooms</a>   &gt;   <a href="/ad?id=${ad.id}">Ad Description</a>   &gt;   Edit Ad</pre>


<h1>Edit Ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/editAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

<input type="hidden" name="adId" value="${ad.id }" />

	<fieldset>
		<legend>Change General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				
				<td>
				<form:select path="sellType" id="myselect">
				<c:choose>
					<c:when test="${ad.getSellType() == 1}">
						<form:option id="type-room" value="1">Rent</form:option>
						<form:option id="type-room" value="2">Buy</form:option>
						<form:option id="type-room" value="3">Auction</form:option>				
					</c:when>
					<c:when test="${ad.getSellType() == 2}">
						<form:option id="type-room" value="2">Buy</form:option>
						<form:option id="type-room" value="1">Rent</form:option>
						<form:option id="type-room" value="3">Auction</form:option>
					</c:when>
					<c:when test="${ad.getSellType() == 3}">
						<form:option id="type-room" value="3">Auction</form:option>
						<form:option id="type-room" value="1">Rent</form:option>
						<form:option id="type-room" value="2">Buy</form:option>
					</c:when>
				</c:choose>
				</form:select>
				</td>
				
				<td><form:errors path="sellType" cssClass="validationErrorText" /></td>

				<script>
					document.getElementById("myselect").onchange = function() {
						if(document.getElementById("myselect").value == 3) {
							document.getElementById("auctionEndDate").hidden = false;
							document.getElementById("field-auctionEndDate").hidden = false;
							document.getElementById("moveInDate").hidden = true;
							document.getElementById("moveOutDate").hidden = true;							
							document.getElementById("prize").hidden = true;
							document.getElementById("prizeOfSale").hidden = true;
							document.getElementById("startOffer").hidden = false;
							document.getElementById("field-moveInDate").hidden = true;
							document.getElementById("field-moveOutDate").hidden = true;
							document.getElementById("field-Prize").hidden = true;
							document.getElementById("field-PrizeOfSale").hidden = true;
							document.getElementById("field-startOffer").hidden = false;



						} else if(document.getElementById("myselect").value == 2) {	
							document.getElementById("auctionEndDate").hidden = true;
							document.getElementById("field-auctionEndDate").hidden = true;
							document.getElementById("moveInDate").hidden = true;
							document.getElementById("moveOutDate").hidden = true;							
							document.getElementById("prize").hidden = true;
							document.getElementById("prizeOfSale").hidden = false;
							document.getElementById("startOffer").hidden = true;
							document.getElementById("field-moveInDate").hidden = true;
							document.getElementById("field-moveOutDate").hidden = true;
							document.getElementById("field-Prize").hidden = true;
							document.getElementById("field-PrizeOfSale").hidden = false;
							document.getElementById("field-startOffer").hidden = true;

						} else if(document.getElementById("myselect").value == 1){
							document.getElementById("auctionEndDate").hidden = true;
							document.getElementById("field-auctionEndDate").hidden = true;
							document.getElementById("moveInDate").hidden = false;
							document.getElementById("moveOutDate").hidden = false;							
							document.getElementById("prize").hidden = false;
							document.getElementById("prizeOfSale").hidden = true;
							document.getElementById("startOffer").hidden = true;
							document.getElementById("field-moveInDate").hidden = false;
							document.getElementById("field-moveOutDate").hidden = false;
							document.getElementById("field-Prize").hidden = false;
							document.getElementById("field-PrizeOfSale").hidden = true;
							document.getElementById("field-startOffer").hidden = true;
						}
					};
				</script>		
			</tr>

			<tr>
				<td><form:input id="field-title" path="title"
						placeholder="Ad Title" /></td>
				
				<td>
				<form:select path="propertyType">
				<c:choose>
					<c:when test="${ad.getPropertyType() == 1}">
						<form:option id="type-room" value="1">Room</form:option>
						<form:option id="type-room" value="2">Studio</form:option>
						<form:option id="type-room" value="3">Flat</form:option>
						<form:option id="type-room" value="4">House</form:option>

					</c:when>
					<c:when test="${ad.getPropertyType() == 2}">
						<form:option id="type-room" value="2">Studio</form:option>
						<form:option id="type-room" value="1">Room</form:option>
						<form:option id="type-room" value="3">Flat</form:option>
						<form:option id="type-room" value="4">House</form:option>
					</c:when>

					<c:when test="${ad.getPropertyType() == 3}">
						<form:option id="type-room" value="3">Flat</form:option>
						<form:option id="type-room" value="1">Room</form:option>
						<form:option id="type-room" value="2">Studio</form:option>
						<form:option id="type-room" value="4">House</form:option>

					</c:when>
					<c:when test="${ad.getPropertyType() == 4}">
						<form:option id="type-room" value="4">House</form:option>
						<form:option id="type-room" value="1">Room</form:option>
						<form:option id="type-room" value="2">Studio</form:option>
						<form:option id="type-room" value="3">Flat</form:option>
					</c:when>
				</c:choose>
				</form:select>
				</td>
				<td><form:errors path="propertyType" cssClass="validationErrorText" /></td>
			</tr>

		<c:choose>
		<c:when test="${ad.getSellType() == 1}">
			<tr>
				<td><label for="field-street"s>Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
				<td><label for="field-PrizeBuy" id="startOffer" hidden="true">Start offer</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="${ad.street}" /></td>
					<!--<form:errors path="street" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-city" path="city" placeholder="${ad.city}" />
					<!--<form:errors path="city" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-startOffer" type="number" path="startOffer"
						placeholder="Start offer" step="50" hidden="true" /> 
					<!--<form:errors path="startOffer" cssClass="validationErrorText" /></td>-->
			</tr>

			
			<tr>
				<td><label for="moveInDate"  id="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate" id="moveOutDate">Move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate"/></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate"/></td>
			</tr>

			<tr>
				<td><label for="field-Prize" id="prize">Prize per month</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per Month" step="50"/>
					<!--<form:errors path="prize" cssClass="validationErrorText" /></td>-->
			</tr>
			
			<tr>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-PrizeBuy" id="prizeOfSale" hidden="true">Prize of sale</label></td>
				<td><label for="auctionEndDate"  id="auctionEndDate" hidden="true">Auction End Date</label></td>
			</tr>
			<tr>
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage"/> 
					<!--<form:errors path="squareFootage" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-PrizeOfSale" type="number" path="prizeOfSale" 
						placeholder="Prize of sale" step="50" hidden="true" />
					<!--<form:errors path="prizeOfSale" cssClass="validationErrorText" /></td>-->
					<td><form:input type="text" id="field-auctionEndDate" 
						path="auctionEndDate" hidden="true"/></td>
					<!--<td><form:errors path="auctionEndDate" cssClass="validationErrorText" /> </td>-->		
			</tr>							
		</c:when>
		<c:when test="${ad.getSellType() == 2}">
			<tr>
				<td><label for="field-street"s>Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
				<td><label for="field-PrizeBuy" class="startOffer" hidden="true">Start offer</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="${ad.street}" /></td>
					<!--<form:errors path="street" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-city" path="city" placeholder="${ad.city}" />
					<form:errors path="city" cssClass="validationErrorText" /></td>
				<td><form:input id="field-startOffer" type="number" path="startOffer"
						placeholder="Start offer" step="50" hidden="true"/> 
					<!--<form:errors path="startOffer" cssClass="validationErrorText" /></td>-->
			</tr>

			
			<tr>
				<td><label for="moveInDate"  id="moveInDate" hidden="true">Move-in date</label></td>
				<td><label for="moveOutDate" id="moveOutDate" hidden="true">Move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" hidden="true"/></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" hidden="true"/></td>
			</tr>

			<tr>
				<td><label for="field-Prize" id="prize" hidden="true">Prize per month</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per Month" step="50" hidden="true"/>
					<!--<form:errors path="prize" cssClass="validationErrorText" /></td>-->
			</tr>
			
			<tr>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-PrizeBuy" id="prizeOfSale">Prize of sale</label></td>
				<td><label for="auctionEndDate"  id="auctionEndDate" hidden="true">Auction End Date</label></td>
			</tr>
			<tr>
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage"/> 
					<!--<form:errors path="squareFootage" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-PrizeOfSale" type="number" path="prizeOfSale"
						placeholder="Prize of sale" step="50" />
					<!--<form:errors path="prizeOfSale" cssClass="validationErrorText" /></td>-->
					<td><form:input type="text" id="field-auctionEndDate"
						path="auctionEndDate" hidden="true"/></td>
					<!--<td><form:errors path="auctionEndDate" cssClass="validationErrorText" /> </td>-->	
			</tr>
		</c:when>
		<c:when test="${ad.getSellType() == 3}">
			<tr>
				<td><label for="field-street"s>Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
				<td><label for="field-PrizeBuy" id="startOffer">Start offer</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="${ad.street}" /></td>
					<!--<form:errors path="street" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-city" path="city" placeholder="${ad.city}" />
					<form:errors path="city" cssClass="validationErrorText" /></td>
				<td><form:input id="field-startOffer" type="number" path="startOffer"
						placeholder="Start offer" step="50" /> 
					<!--form:errors path="startOffer" cssClass="validationErrorText" /></td>-->
			</tr>

			
			<tr>
				<td><label for="moveInDate"  id="moveInDate" hidden="true">Move-in date</label></td>
				<td><label for="moveOutDate" id="moveOutDate" hidden="true">Move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" hidden="true"/></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" hidden="true"/></td>
			</tr>

			<tr>
				<td><label for="field-Prize" id="prize" hidden="true">Prize per month</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per Month" step="50" hidden="true"/>
					<!--<form:errors path="prize" cssClass="validationErrorText" /></td>-->
			</tr>
			
			<tr>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-PrizeBuy" id="prizeOfSale" hidden="true">Prize of sale</label></td>
				<td><label for="auctionEndDate"  id="auctionEndDate">Auction End Date</label></td>
			</tr>
			<tr>
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage"/> 
					<!--<form:errors path="squareFootage" cssClass="validationErrorText" /></td>-->
				<td><form:input id="field-PrizeOfSale" type="number" path="prizeOfSale"
						placeholder="Prize of sale" step="50" hidden="true"/>
					<!--<form:errors path="prizeOfSale" cssClass="validationErrorText" /></td>-->
					<td><form:input type="text" id="field-auctionEndDate"
						path="auctionEndDate" /></td>
					<!--<td><form:errors path="auctionEndDate" cssClass="validationErrorText" /> </td>-->	
			</tr>
		
		</c:when>
		</c:choose>
		</table>

	</fieldset>

	<br />
	<fieldset>
		<legend>Change Room Description</legend>

		<table class="placeAdTable">
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.smokers}">
							<form:checkbox id="field-smoker" path="smokers" checked="checked" /><label>Smoking
							inside allowed</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-smoker" path="smokers" /><label>Smoking
							inside allowed</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.animals}">
							<form:checkbox id="field-animals" path="animals"  checked="checked" /><label>Animals
						allowed</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-animals" path="animals" /><label>Animals
						allowed</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.garden}">
							<form:checkbox id="field-garden" path="garden" checked="checked" /><label>Garden
							(co-use)</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garden" path="garden" /><label>Garden
							(co-use)</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.balcony}">
							<form:checkbox id="field-balcony" path="balcony"  checked="checked" /><label>Balcony
						or Patio</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-balcony" path="balcony" /><label>Balcony
						or Patio</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.cellar}">
							<form:checkbox id="field-cellar" path="cellar" checked="checked" /><label>Cellar
						or Attic</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-cellar" path="cellar" /><label>Cellar
						or Atticd</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.furnished}">
							<form:checkbox id="field-furnished" path="furnished"  checked="checked" /><label>Furnished
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-furnished" path="furnished" /><label>Furnished</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.cable}">
							<form:checkbox id="field-cable" path="cable" checked="checked" /><label>Cable TV</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-cable" path="cable" /><label>Cable TV</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.garage}">
							<form:checkbox id="field-garage" path="garage"  checked="checked" /><label>Garage
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garage" path="garage" /><label>Garage</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.internet}">
							<form:checkbox id="field-internet" path="internet"  checked="checked" /><label>WiFi available
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-internet" path="internet" /><label>WiFi available</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>

		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" cols="100" value="${ad.roomDescription}" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	</fieldset>


	<br />
	<fieldset>
		<legend>Change roommates</legend>
		
		<h3>Add new roommates</h3>
		<br />
		<p>If your roommates have an account, simply add them by email.</p>

		<table class="placeAdTable">
			<tr>
				<td><label for="roomFriends">Add by email</label></td>
			</tr>

			<tr>
				<td id="roommateCell"><form:input type="text" id="roomFriends"
						path="roomFriends" placeholder="email" /> 

				<div id="addbutton" class="smallPlusButton">+</div></td>
			</tr>
			
			<tr>
				<td><p id="addedRoommates" path="addedRoommates">Newly added roommates: </p></td>
			</tr>
		</table>


		<p>Edit the description of the roommates:</p>
		<br />
		<form:textarea path="roommates" rows="10" cols="100"
			placeholder="Roommates" />
		<form:errors path="roommates" cssClass="validationErrorText" />
		<hr />
		<h3>Delete existing roommates</h3>
		<br />
		<table class="styledTable">
					<tr>
						<th>Username</th>
						<th>Delete</th>
					</tr>
					
					<c:forEach var="user" items="${ad.registeredRoommates}">
							<tr>
								<td>${user.username}</td>
								<td><button type="button" data-user-id="${user.id}" data-ad-id="${ad.id}" class="deleteRoommateButton">Delete</button></td>
							</tr>
							<tr>
					</c:forEach>
		</table>
	</fieldset>

	<br />
	<fieldset>
		<legend>Change preferences</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			value="${ad.preferences}" ></form:textarea>
	</fieldset>

	
	<fieldset>
		<legend>Add visiting times</legend>
		
		<table>
			<tr>
				<td>
					<input type="text" id="field-visitDay" />
					
					<select id="startHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="startMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
					
					<span>to&thinsp; </span>
					
					<select id="endHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="endMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
			

					<div id="addVisitButton" class="smallPlusButton">+</div>
					
					<div id="addedVisits"></div>
				</td>
				
			</tr>
			
		</table>
		<br>
	</fieldset>

	<br />

	<fieldset>
		<legend>Change pictures</legend>
		<h3>Delete existing pictures</h3>
		<br />
		<div>
			<c:forEach items="${ad.pictures }" var="picture">
				<div class="pictureThumbnail">
					<div>
					<img src="${picture.filePath}" />
					</div>
					<button type="button" data-ad-id="${ad.id }" data-picture-id="${picture.id }">Delete</button>
				</div>
			</c:forEach>
		</div>
		<p class="clearBoth"></p>
		<br /><br />
		<hr />
		<h3>Add new pictures</h3>
		<br />
		<label for="field-pictures">Pictures</label> <input
			type="file" id="field-pictures" accept="image/*" multiple="multiple" />
		<table id="uploaded-pictures" class="styledTable">
			<tr>
				<th id="name-column">Uploaded picture</th>
				<th>Size</th>
				<th>Delete</th>
			</tr>
		</table>
		<br>
	</fieldset>

	<div>
		<button type="submit">Submit</button>
		<a href="<c:url value='/ad?id=${ad.id}' />"> 
			<button type="button">Cancel</button>
		</a>
	</div>

</form:form>


<c:import url="template/footer.jsp" />
