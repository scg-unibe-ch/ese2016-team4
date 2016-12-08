<%@ page language="java" pageEncoding="iso-8859-1"
	contentType="text/html;charset=iso-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<script src="/js/pictureUpload.js"></script>

<script>
$(document).ready( function() {
    var today = new Date();
    var tomorrow = new Date(today.getTime() + (24 * 60 * 60 * 1000));
    
    function getFormattedDate(date){
    	var dd = date.getDate();
    	var mm = date.getMonth()+1;
    	var yyyy = date.getFullYear();
    	
    	if(dd<10){
    	    dd='0'+dd
    	} 
    	if(mm<10){
        	mm='0'+mm
    	} 
    	
    	return d = dd+'.'+mm+'.'+yyyy;
    }
    
    $('#field-moveInDate').val(getFormattedDate(today));
    $('#field-auctionEndDate').val(getFormattedDate(tomorrow));
    
});
</script>

<script>
	$(document).ready(function() {
		
		// Go to controller take what you need from user
		// save it to a hidden field
		// iterate through it
		// if there is id == x then make "Bookmark Me" to "bookmarked"
		
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
		$("#field-auctionEndDate").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		
		$("#field-visitDay").datepicker({
			dateFormat : 'dd.mm.yy'
		});
		

		$("#addbutton").click(function() {
			var text = $("#roomFriends").val();
			var alreadyAdded = $("#addedRoommates").html();
			if(validateForm(text)) {
				$.post("/profile/placeAd/validateEmail",{email: text, alreadyIn: alreadyAdded}, function(data) {
					if(validateForm(data)) {
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
	});
</script>

<script>
function updateType() {
		if(document.getElementById("myselect").value == 3) {
			$('#field-moveInDate').val(null);
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
			$('#field-moveInDate').val(null);
			$('#field-auctionEndDate').val(null);
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
		} else {
			$('#field-auctionEndDate').val(null);
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
}

$('document').ready(function(){
	document.getElementById('myselect').onchange = updateType;
});
window.onload = updateType;
</script>

<pre>
	<a href="/">Home</a>   &gt;   Place ad</pre>

<h1>Place an ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/placeAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

	<fieldset>
		<legend>General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				
				<td>
				<form:select path="sellType" id="myselect">
				<form:option value ="0">Select a Sale Type</form:option>
				<form:option id="type-room" value="1">Rent</form:option>
				<form:option id="type-room" value="2">Buy</form:option>
				<form:option id="type-room" value="3">Auction</form:option>				
				</form:select>
				</td>
				
				<td><form:errors path="sellType" cssClass="validationErrorText" /></td>

				
				
			</tr>

			<tr>
				<td><form:input id="field-title" path="title"
						placeholder="Ad Title" /></td>
				
				<td>
				<form:select path="propertyType">
				<form:option value ="0">Select a Property Type</form:option>
				<form:option id="type-room" value="1">Room</form:option>
				<form:option id="type-room" value="2">Studio</form:option>
				<form:option id="type-room" value="3">Flat</form:option>
				<form:option id="type-room" value="4">House</form:option>
				</form:select>
				</td>
				<td><form:errors path="propertyType" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
				<td><label for="field-PrizeBuy" id="startOffer" hidden="true">Start offer</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="Street" /></td>
					<form:errors path="street" cssClass="validationErrorText" /></td>
				<td><form:input id="field-city" path="city" placeholder="City" />
					<form:errors path="city" cssClass="validationErrorText" /></td>
				<td><form:input id="field-startOffer" type="number" path="startOffer" hidden="true"
						placeholder="Start offer" step="50" min="0" />
					<form:errors path="startOffer" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td><label for="moveInDate"  id="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate" id="moveOutDate">Move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" /><form:errors path="moveInDate" cssClass="validationErrorText" /></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" /><form:errors path="moveOutDate" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td><label for="field-Prize" id="prize">Prize per month</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per month" step="50" min="0" /> <form:errors
						path="prize" cssClass="validationErrorText" /></td>

			</tr>
			
			<tr>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-PrizeBuy" id="prizeOfSale" hidden="true">Prize of sale</label></td>
				<td><label for="auctionEndDate"  id="auctionEndDate" hidden="true">Auction End Date</label></td>
			</tr>
			<tr>
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage" min="0"/> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
						
				<td><form:input id="field-PrizeOfSale" type="number" path="prizeOfSale" hidden="true"
						placeholder="Prize of sale" step="100" min="0" /> <form:errors
						path="prizeOfSale" cssClass="validationErrorText" /></td>
								
				<td><form:input type="text" id="field-auctionEndDate" hidden="true"
						path="auctionEndDate" /><form:errors path="auctionEndDate" cssClass="validationErrorText" /> </td>									
			</tr>
		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>Room Description</legend>

		<table class="placeAdTable">
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Smoking
						inside allowed</label></td>
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
				<td><form:checkbox id="field-internet" path="internet"
						value="1" /><label>WiFi available</label></td>
				<td><form:checkbox id="field-dishwasher" path="dishwasher"
						value="1" /><label>Dishwasher</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-washingMachine" path="washingMachine"
						value="1" /><label>Washing machine</label></td>
			</tr>

		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" cols="100"
			placeholder="Room Description" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	</fieldset>

	<br />
	<fieldset>
		<legend>Roommates (optional)</legend>
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
				<td><p id="addedRoommates" path="addedRoommates">Added
						roommates:</p></td>
			</tr>
		</table>

		<br />
		<p>If the roommates do not have accounts or you wish to give
			further information, you can add a text in which you describe the
			roommates.</p>
		<br/>
		<form:textarea path="roommates" rows="10" cols="100"
			placeholder="Roommates" />
		<form:errors path="roommates" cssClass="validationErrorText" />
	</fieldset>

	<br />
	<fieldset>
		<legend>Preferences (optional)</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			placeholder="Preferences"></form:textarea>
	</fieldset>

	<fieldset>
		<legend>Pictures (optional)</legend>
		<br /> <label for="field-pictures">Pictures</label> <input
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

	<fieldset>
		<legend>Visiting times (optional)</legend>

		<table>
			<tr>
				<td><input type="text" id="field-visitDay" /> <select
					id="startHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="startMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select> <span>to&thinsp; </span> <select id="endHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="endMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select>



					<div id="addVisitButton" class="smallPlusButton">+</div>

					<div id="addedVisits"></div></td>

			</tr>

		</table>
		<br>
	</fieldset>



	<br />
	<div>
		<button type="submit">Submit</button>
		<a href="/"><button type="button">Cancel</button></a>
	</div>

</form:form>

<c:import url="template/footer.jsp" />
