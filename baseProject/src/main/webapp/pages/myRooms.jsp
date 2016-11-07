<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
	});

</script>


<pre><a href="/">Home</a>   &gt;   My Rooms</pre>

<c:choose>
	<c:when test="${empty ownAdvertisements}">
		<h1>My Advertisements</h1>
		<hr />
		<p>You have not advertised anything yet.</p>
		<br /><br />
	</c:when>
	<c:otherwise>
	
		<div id="resultsDiv" class="resultsDiv">
		<h1>My Advertisements</h1>
		<hr />			
			<c:forEach var="ad" items="${ownAdvertisements}">
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
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
							<c:when test="${ad.getSellType() == 2}"><h2>Sale Prize ${ad.prizeOfSale }</h2></c:when>
							<c:when test="${ad.getSellType() == 3}"><h2>Current Bid ${ad.startOffer }</h2></c:when>
						</c:choose>						
						<br /> <br />

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />

						<c:choose>
							<c:when test="${ad.getSellType() == 1}"><p>Move-in date: ${formattedMoveInDate }</p></c:when>
						</c:choose>					
					</div>
				</div>
			</c:forEach>
			<br /> <br />
		</div>		
	</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${empty bookmarkedAdvertisements}">
		<h1>My Bookmarks</h1>
		<hr />
		<p>You have not bookmarked anything yet.</p><br /><br />
	</c:when>
	<c:otherwise>
		
		<div id="resultsDiv" class="resultsDiv">
		<h1>My Bookmarks</h1>
		<hr />			
			<c:forEach var="ad" items="${bookmarkedAdvertisements}">
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
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
							<c:when test="${ad.getSellType() == 2}"><h2>Sale Prize ${ad.prizeOfSale }</h2></c:when>
							<c:when test="${ad.getSellType() == 3}"><h2>Current Bid ${ad.startOffer }</h2></c:when>
						</c:choose>						
						<br /> <br />

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />

						<c:choose>
							<c:when test="${ad.getSellType() == 1}"><p>Move-in date: ${formattedMoveInDate }</p></c:when>
						</c:choose>					
					</div>
				</div>
			</c:forEach>
		</div>		
	</c:otherwise>
</c:choose>


<c:import url="template/footer.jsp" />