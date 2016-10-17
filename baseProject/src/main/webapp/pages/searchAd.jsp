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
	var neither = document.getElementById('neither');
	var all = document.getElementById('all');
	var studioAndFlat = document.getElementById('studioAndFlat');
	var roomAndFlat = document.getElementById('roomAndFlat');
	var roomAndStudio = document.getElementById('roomAndStudio');
	var type = document.getElementById('type');
	var filtered = document.getElementById('filtered');
	
	if(room.checked && studio.checked && flat.checked) {
		all.checked = true;
		neither.checked = false;
	}
	else if(!room.checked && !studio.checked && !flat.checked) {
		all.checked = false;
		neither.checked = true;
	}
	else if(!room.checked && !studio.checked) {
		all.checked = false;
		neither.checked = false;
		flat.checked = true;
	}
	else if(!room.checked && !flat.checked) {
		all.checked = false;
		neither.checked = false;
		studio.checked = true;
	}
	else if(!flat.checked && !studio.checked) {
		all.checked = false;
		neither.checked = false;
		room.checked = true;
	}
	else if(!flat.checked) {
		all.checked = false;
		neither.checked = false;
		roomAndStudio.checked = true;
	}
	else if(!room.checked) {
		all.checked = false;
		neither.checked = false;
		studioAndFlat.checked = true;
	}
	else if(!studio.checked) {
		all.checked = false;
		neither.checked = false;
		roomAndFlat.checked = true;
	}
	filtered.checked = false;
}
</script>

<h1>Search for an ad</h1>
<hr />

<form:form method="post" modelAttribute="searchForm" action="/results"
	id="searchForm" autocomplete="off">

	<fieldset>
		<form:checkbox name="room" id="room" path="roomHelper" /><label>Room</label>
		<form:checkbox name="studio" id="studio" path="studioHelper" /><label>Studio</label>
		<form:checkbox name="flat" id="flat" path="flatHelper" /><label>Flat</label>

		
		<form:checkbox style="display:none" name="neither" id="neither" path="noRoomNoStudioNoFlat" />
		<form:checkbox style="display:none" name="all" id="all" path="allRoomAndStudioAndFlat" />
		<form:checkbox style="display:none" name="studioAndFlat" id="studioAndFlat" path="bothStudioAndFlat" />
		<form:checkbox style="display:none" name="roomAndFlat" id="roomAndFlat" path="bothRoomAndFlat" />
		<form:checkbox style="display:none" name="roomAndStudio" id="roomAndStudio" path="bothRoomAndStudio" />

		<form:checkbox style="display:none" name="filtered" id="filtered" path="filtered" />
		<form:errors path="noRoomNoStudioNoFlat" cssClass="validationErrorText" />
		
		<br />
		
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" />
		

		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="prize" cssClass="validationErrorText" />
		<br />

		<button type="submit" tabindex="7" onClick="validateType(this.form)">Search</button>
		<button type="reset" tabindex="8">Cancel</button>
	</fieldset>

</form:form>

<c:import url="template/footer.jsp" />