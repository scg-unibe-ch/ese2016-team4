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

<%-- Need to chose at least 1 propertyType --%>
<script>
function typesNotEmpty() {
	if(!(document.getElementById('room').checked || document.getElementById('studio').checked 
	|| document.getElementById('flat').checked || document.getElementById('house').checked)){
		document.getElementById('noPropertyType').checked = true;
	}else{document.getElementById('noPropertyType').checked = false;}
}
</script>

<%-- We don't want to cache this forminput --%>
<script>
$(document).ready(function() {
	document.getElementById('room').checked = false;
	document.getElementById('studio').checked = false;
	document.getElementById('flat').checked = false;
	document.getElementById('house').checked = false;

});
</script>

<pre>Home</pre>

<h1>Welcome to FlatFindr!</h1>

<form:form method="get" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off" onsubmit="typesNotEmpty()">

	    <div id="indexDiv">
	    <div align="center">
    		<h1>Fast Search</h1> <br>
        </div>
        
        <font size="5"><b>What?</b></font>
        <table>
        <tr>
        
        <td><form:checkbox name="room" id="room" path="room" /><label><b>Room</b></label></td>
		<td><form:checkbox name="studio" id="studio" path="studio" /><label><b>Studio</b></label></td>
		<td><form:checkbox name="flat" id="flat" path="flat" /><label><b>Flat</b></label></td>
		<td><form:checkbox name="house" id="house" path="house" /><label><b>House</b></label></td>
		<form:checkbox name="noPropertyType" id="noPropertyType" path="noPropertyType" hidden="true"/>
		<form:errors path="noPropertyType" cssClass="validationErrorText" />
        </tr>
        </table>

        <label for="city"><font size="5"><b>Where?</b></font></label>
        <input type="text" name="city" id="city" required="required"
            placeholder="e.g. Bern" tabindex="3" />
        <form:errors path="city" cssClass="validationErrorText" />
    	
		<%-- Hidden fields for price, radius and auctiontypes --%>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" value="10" hidden="true"/>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" value="1000000" hidden="true"/>
		<form:checkbox name="buy" id="buy" path="buy" checked="true" hidden="true"/>
		<form:checkbox name="rent" id="rent" path="rent" checked="true" hidden="true"/>
		<form:checkbox name="auction" id="auction" path="auction" checked="true" hidden="true"/>
		
        <br />
        <%-- used to redirect to the previous page if form input was erroneous --%>
		<form:input type="hidden" name="page" value="index" path=""/>
	
        <button type="submit" class="buttonDiv" >Go!</button>
    </div>
</form:form>

<c:choose>
	<c:when test="${empty newest}">
		<h2>No ads placed yet</h2>
	</c:when>
	<c:otherwise>
		<div class="resultAd">
			<h2>Our newest ads:</h2>		
			<c:forEach var="ad" items="${newest}">
				<div id="IndexResultsDiv" class="IndexResultsDiv">	
				<%--<c:choose>
					<c:when test="${ad.getUser().isPremium()==true}"><p><IMG SRC="/img/premium.png" ALT="Premium User" style="width:60px;height:60px;" class="premiumRight"></p></c:when>
				</c:choose>--%>
					<div class="resultLeft">
						<h2>
							<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
						</h2>
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>

						<c:choose>
							<c:when test="${ad.getSellType() == 1}"><h2>Monthly Rent ${ad.prizePerMonth } CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 2}"><h2>Sale Price ${ad.prizeOfSale } CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 3 && !bidService.isBidden(ad.getId())}"><h2>Auction Price ${ad.startOffer} CHF</h2></c:when>
							<c:when test="${ad.getSellType() == 3 && bidService.isBidden(ad.getId())}"><h2>Auction Price ${bidService.getHighestBid(ad.getId())} CHF</h2></c:when>
						</c:choose>
					</div>
					
					
					<div class="resultRight">

						<br /> 
						<c:choose>
							<c:when test="${ad.getUser().isPremium()==true}"><p><IMG SRC="/img/premium.png" ALT="Premium User" style="width:60px;height:60px;" class="premiumRight"></p></c:when>
						</c:choose>
						<p>
							
							<i><c:choose>
									<c:when test="${ad.getPropertyType() == 1}">Room</c:when>
									<c:when test="${ad.getPropertyType() == 2}">Studio</c:when>
									<c:when test="${ad.getPropertyType() == 3}">Flat</c:when>
									<c:when test="${ad.getPropertyType() == 4}">House</c:when>
								</c:choose></i>
							<br /> 
						
						</p>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />
						<c:choose>
							<c:when test="${ad.getSellType() == 1}"><p>Move-in date: ${formattedMoveInDate }</p></c:when>
							<c:when test="${ad.getSellType() == 2 || ad.getSellType() == 3}"><p> </p></c:when>
						</c:choose>
						
					</div>
				</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
<c:import url="template/footer.jsp" /><br />
</body>



