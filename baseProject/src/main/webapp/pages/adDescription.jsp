<%@page import="ch.unibe.ese.team1.model.Ad"%>
<%@ page language="java" pageEncoding="iso-8859-1"
	contentType="text/html;charset=iso-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<c:import url="template/header.jsp" />

<pre>
	<a href="/">Home</a>   &gt;  <c:choose><c:when test="${loggedIn}"><a href="/profile/myRooms">My Properties</a>   &gt;	</c:when></c:choose>Ad Description</pre>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script src="/js/image_slider.js"></script>
<script src="/js/adDescription.js"></script>
<script src="/js/flipclock.js"></script>

<script>
  $( function() {
    $( "#accordion" ).accordion({
      collapsible: true,
      active: false,
      heightStyle: "content"
    });
	});

</script>

<script>
	var shownAdvertisementID = "${shownAd.id}";
	var shownAdvertisement = "${shownAd}";
	var today = new Date();

	function attachBookmarkClickHandler(){
		$("#bookmarkButton").click(function() {

			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: false}, function(data) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 3:
					$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");
				}

				attachBookmarkedClickHandler();
			});
		});
	}

	function attachBookmarkedClickHandler(){
		$("#bookmarkedButton").click(function() {
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: true}, function(data) {
				$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 2:
					$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");

				}
				attachBookmarkClickHandler();
			});
		});
	}

	$(document).ready(function() {
		attachBookmarkClickHandler();
		attachBookmarkedClickHandler();

		$.post("/bookmark", {id: shownAdvertisementID, screening: true, bookmarked: true}, function(data) {
			if(data == 3) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				attachBookmarkedClickHandler();
			}
			if(data == 4) {
				$('#shownAdTitle').replaceWith($('<h1>' + "${shownAd.title}" + '</h1>'));
			}
		});

		$("#newMsg").click(function(){
			$("#content").children().animate({opacity: 0.4}, 300, function(){
				$("#msgDiv").css("display", "block");
				$("#msgDiv").css("opacity", "1");
			});
		});

		$("#messageCancel").click(function(){
			$("#msgDiv").css("display", "none");
			$("#msgDiv").css("opacity", "0");
			$("#content").children().animate({opacity: 1}, 300);
		});

		$("#messageSend").click(function (){
			if($("#msgSubject").val() != "" && $("#msgTextarea").val() != ""){
				var subject = $("#msgSubject").val();
				var text = $("#msgTextarea").val();
				var recipientEmail = "${shownAd.user.username}";
				$.post("profile/messages/sendMessage", {subject : subject, text: text, recipientEmail : recipientEmail}, function(){
					$("#msgDiv").css("display", "none");
					$("#msgDiv").css("opacity", "0");
					$("#msgSubject").val("");
					$("#msgTextarea").val("");
					$("#content").children().animate({opacity: 1}, 300);
				})
			}
		});
	});

</script>

<script>
function copyTextToClipboard(text) {
  var textArea = document.createElement("textarea");

  //
  // *** This styling is an extra step which is likely not required. ***
  //
  // Why is it here? To ensure:
  // 1. the element is able to have focus and selection.
  // 2. if element was to flash render it has minimal visual impact.
  // 3. less flakyness with selection and copying which **might** occur if
  //    the textarea element is not visible.
  //
  // The likelihood is the element won't even render, not even a flash,
  // so some of these are just precautions. However in IE the element
  // is visible whilst the popup box asking the user for permission for
  // the web page to copy to the clipboard.
  //

  // Place in top-left corner of screen regardless of scroll position.
  textArea.style.position = 'fixed';
  textArea.style.top = 0;
  textArea.style.left = 0;

  // Ensure it has a small width and height. Setting to 1px / 1em
  // doesn't work as this gives a negative w/h on some browsers.
  textArea.style.width = '2em';
  textArea.style.height = '2em';

  // We don't need padding, reducing the size if it does flash render.
  textArea.style.padding = 0;

  // Clean up any borders.
  textArea.style.border = 'none';
  textArea.style.outline = 'none';
  textArea.style.boxShadow = 'none';

  // Avoid flash of white box if rendered for any reason.
  textArea.style.background = 'transparent';


  textArea.value = text;

  document.body.appendChild(textArea);

  textArea.select();

  try {
    var successful = document.execCommand('copy');
    var msg = successful ? 'successful' : 'unsuccessful';
    console.log('Copying text command was ' + msg);
  } catch (err) {
    console.log('Oops, unable to copy');
  }

  document.body.removeChild(textArea);
}

function CopyLink() {
  copyTextToClipboard(location.href);
}
</script>


<!-- Handles the auction date infos and the remaining time -->

<script>

function getFormattedDate(date){
	var min = date.getMinutes();
	var hh = date.getHours();
	var dd = date.getDate();
	var mm = date.getMonth()+1; //January is 0!
	var yyyy = date.getFullYear();

	if(dd<10){
	    dd='0'+dd
	}
	if(mm<10){
    	mm='0'+mm
	}
	if(hh<10){
	    hh='0'+hh
	}
	if(min<10){
    	min='0'+min
	}

	return d = dd+'.'+mm+'.'+yyyy+' '+hh+':'+min;
}



// get start and end date in ms
var auctionEndMs = ${shownAd.getTimeToAuctionEnd()};
var auctionDuration = Math.max(0, auctionEndMs);
//var highestBid =  Math.max("${shownAd.startOffer}" ,"${allBids[0].bid}");
var highestBid = ${bidService.getHighestBid(shownAd.getId())};

// create real dates
var creationDate = new Date(${shownAd.getCreationMs()});
var auctionEnd = new Date(${shownAd.getAuctionEndTime()});
var dateNow = new Date();

// load the info into the paragraphs, while formatting the date
window.onload = function() {
	document.getElementById('datetoday').innerHTML = "Today: " + getFormattedDate(dateNow).toString();
	document.getElementById('auctionstart').innerHTML = getFormattedDate(creationDate).toString();
	document.getElementById('auctionend').innerHTML = getFormattedDate(auctionEnd).toString();
	document.getElementById('highestBid').innerHTML = "Price: " + highestBid.toString() + " CHF";
}

window.ready = function() {
	auctionEndMs = ${shownAd.getTimeToAuctionEnd()};
	auctionDuration = Math.max(0, auctionEndMs);
	//var highestBid =  Math.max("${shownAd.startOffer}" ,"${allBids[0].bid}");
	highestBid = ${bidService.getHighestBid(shownAd.getId())};
	document.getElementById('datetoday').innerHTML = "Today: " + getFormattedDate(dateNow).toString();
	document.getElementById('auctionstart').innerHTML = getFormattedDate(creationDate).toString();
	document.getElementById('auctionend').innerHTML = getFormattedDate(auctionEnd).toString();
	document.getElementById('highestBid').innerHTML = "Highest Bid: " + highestBid.toString() + " CHF";
}


// set timer to refresh every second
setInterval(auctionTimer, 1000);

// calculate the remaining of the auction. This gets refreshed every second
function auctionTimer() {
	var dateNow = new Date();

	var remaining = auctionEnd - dateNow;
	var remaining_days = Math.floor(remaining / (1000 * 3600 * 24));
	var remaining_hours = Math.floor((remaining/(1000*3600))%24);
	var remaining_minutes = Math.floor((remaining/(1000*60))%60);
	var remaining_seconds = Math.floor((remaining/(1000))%60);

	if(${shownAd.getSellType()} == 3) {

		if(remaining > 0) {
			document.getElementById('timeTilEnd').innerHTML = "Remaining time: "
			+ remaining_days + "d " + remaining_hours + "h " + remaining_minutes + "m " + remaining_seconds + "s";
		} else {
			document.getElementById('timeTilEnd').innerHTML = "Auction expired!"
		}
	}
}


//the mighty timer from http://www.dwuser.com/education/content/easy-javascript-jquery-countdown-clock-builder/
$(function(){
	FlipClock.Lang.Custom = { days:'Days', hours:'Hours', minutes:'Minutes', seconds:'Seconds' };
	var opts = {
		clockFace: 'DailyCounter',
		countdown: true,
		language: 'Custom'
	};
	opts.classes = {
			active: 'flip-clock-active',
			before: 'flip-clock-before',
			divider: 'flip-clock-divider',
			dot: 'flip-clock-dot',
			label: 'flip-clock-label',
			flip: 'flip',
			play: 'play',
			wrapper: 'flip-clock-small-wrapper'
		};

	//there is a 2 second delay before the timer starts ticking, which gets adjusted here
	var countdown = (auctionDuration/1000);
	countdown = Math.max(0, countdown);
	$('.clock-builder-output').FlipClock(countdown, opts);
});

</script>
<%-- The mighty timer script, stolen from http://www.dwuser.com/education/content/easy-javascript-jquery-countdown-clock-builder/ --%>
<script type="text/javascript">

</script>


<!-- format the dates -->
<fmt:formatDate value="${shownAd.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd.MM.yyyy" />
<fmt:formatDate value="${shownAd.creationDate}"
	var="formattedCreationDate" type="date" pattern="dd.MM.yyyy" />


<c:choose>
	<c:when test="${empty shownAd.moveOutDate }">
		<c:set var="formattedMoveOutDate" value="unlimited" />
	</c:when>
	<c:otherwise>
		<fmt:formatDate value="${shownAd.moveOutDate}"
			var="formattedMoveOutDate" type="date" pattern="dd.MM.yyyy" />
	</c:otherwise>
</c:choose>


<h1 id="shownAdTitle">${shownAd.title}
	<c:choose>
		<c:when test="${loggedIn}">
			<a class="right" id="bookmarkButton">Bookmark Ad</a>
		</c:when>
	</c:choose>
</h1>


<hr />



	<form:form action="/ad?id=${shownAd.id}"
			method="post" modelAttribute="bidForm"
			id="bidForm" autocomplete="off">
  <table style="width: 100%; vertical-align: center;">
  <c:choose>
	<c:when test="${shownAd.getSellType() == 3 && shownAd.getFinished() == false}">
    <tr>
      <td style="text-indent:50px;"><img src="/img/test/auct_live.gif">
      </td>
      <td valign="bottom">
        <h2 id="highestBid">Highest Bid: 0</h2>
      </td>
    </tr>
    <tr>
      <td>
        <div class="clock-builder-output"></div>
      </td>
    </tr>

	</c:when>
</c:choose>
 <c:choose>
  <c:when test="${shownAd.getSellType() == 3 && loggedIn && shownAd.getFinished() == false}">
   <tr>
   <td></td>
  	<td>
  	<c:choose>
  	<c:when test="${bidService.getMyBid(loggedInUserEmail,shownAd.getId()) != bidService.getHighestBid(shownAd.getId()) 
  				&& loggedInUserEmail != shownAd.user.username}">
          <label for="bid" >Your bid:</label>

          <form:input type="number" value="${bidService.getNextBid(shownAd.getId())}"
            path="bid" placeholder="e.g. 150" step="1" min="${bidService.getNextBid(shownAd.getId())}" />

          <button type="submit" >Place bid</button>
	</c:when>
	</c:choose>
  	</td>
   </tr>
  </c:when>
  </c:choose>
  <c:choose>
  	<c:when test="${shownAd.getSellType() == 3 && shownAd.getFinished() == true}">
		<td>
        	<h1>Auction expired!</h1>        	
      	</td>
	</c:when>
 </c:choose>
 <c:choose>
  <c:when test="${shownAd.getSellType() == 3 && loggedIn == false && shownAd.getFinished() == false}">
  <tr>
  <td></td>
   <td><p><b>You have to be logged in to place a bid</b></p></td>
  </tr>

  </c:when>
 </c:choose>
  </table>
</form:form>

<section>
	<c:choose>
		<c:when test="${loggedIn}">
			<c:if test="${loggedInUserEmail == shownAd.user.username }">
				<a href="<c:url value='/profile/editAd?id=${shownAd.id}' />">
					<button type="button">Edit Ad</button>
				</a>
			</c:if>
		</c:when>
	</c:choose>
	<br> <br>
<div class="adDescDiv" style="width: 37.5%;">

<button onclick="CopyLink()">Get the link!</button>

<button id="SendEmail">Share with</button>
<td><input path="email" id="field-email" placeholder="example@example.com"/> <form:errors
	path="email" cssClass="validationErrorText" /></td>
</div>
<script>
document.getElementById("SendEmail").onclick = function() {
	var subject = document.getElementById("field-email").value;
	var string = 'Hi, I found an interesting ad on FlatFindr! Please open the link: ' + window.location.href;   
	window.location = "mailto:"+subject+"?&body="+string;
}
</script>
<br> <br>
	<table id="adDescTable" class="adDescDiv">
		<tr>
			<td><h2>Type</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.getPropertyType() == 1}">Room</c:when>
					<c:when test="${shownAd.getPropertyType() == 2}">Studio</c:when>
					<c:when test="${shownAd.getPropertyType() == 3}">Flat</c:when>
					<c:when test="${shownAd.getPropertyType() == 4}">House</c:when>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Purpose</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.getSellType() == 1}">Rent</c:when>
					<c:when test="${shownAd.getSellType() == 2}">Buy</c:when>
					<c:when test="${shownAd.getSellType() == 3}">Auction</c:when>
				</c:choose></td>
		</tr>

		<c:choose>
			<c:when test="${shownAd.getSellType() == 3}">
				<tr>
					<td><h2>Auction start</h2></td>
					<td><p id="auctionstart"></p></td>
				</tr>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${shownAd.getSellType() == 3}">
				<tr>
					<td><h2>Auction end</h2></td>
					<td><p id="auctionend"></p></td>
				</tr>
			</c:when>
		</c:choose>

		<tr>
			<td><h2>Address</h2></td>
			<td><a class="link"
				href="http://maps.google.com/?q=${shownAd.street}, ${shownAd.zipcode}, ${shownAd.city}">${shownAd.street},
					${shownAd.zipcode} ${shownAd.city}</a></td>
		</tr>

		<tr>
			<c:choose>
				<c:when test="${shownAd.getSellType() == 1}">
					<td><h2>Available from</h2></td>
					<td>${formattedMoveInDate}</td>
				</c:when>
			</c:choose>
		</tr>

		<tr>
			<c:choose>
				<c:when test="${shownAd.getSellType() == 1}">
					<td><h2>Move-out Date</h2></td>
					<td>${formattedMoveOutDate}</td>
				</c:when>
			</c:choose>
		</tr>

		<tr>
			<c:choose>
				<c:when test="${shownAd.getSellType() == 1}">
					<td><h2>Monthly Rent</h2></td>
					<td>${shownAd.prizePerMonth}&#32;CHF</td>
				</c:when>
			</c:choose>
		</tr>

		<tr>
			<c:choose>
				<c:when test="${shownAd.getSellType() == 3}">
					<td><h2>Starting Price</h2></td>
					<td>${shownAd.startOffer}&#32;CHF</td>
				</c:when>
			</c:choose>
		</tr>

		<tr>
			<c:choose>
				<c:when test="${shownAd.getSellType() == 2}">
					<td><h2>Sale Price</h2></td>
					<td>${shownAd.prizeOfSale}&#32;CHF</td>
				</c:when>
			</c:choose>
		</tr>

		<tr>
			<td><h2>Square Meters</h2></td>
			<td>${shownAd.squareFootage}m&sup2;</td>
		</tr>
		<tr>
			<td><h2>Ad created on</h2></td>
			<td>${formattedCreationDate}</td>
		</tr>
	</table>
    
<div id="image-slider">
	<div id="left-arrow">
		<img src="/img/left-arrow.png" />
	</div>
	<div id="images">
		<c:forEach items="${shownAd.pictures}" var="picture">
			<img src="${picture.filePath}" />
		</c:forEach>
	</div>
	<div id="right-arrow">
		<img src="/img/right-arrow.png" />
	</div>
</div>

	<div id="map" style="height:295px;width:545px;position: relative;
	border-color: #2dc16d; border-radius: 5px; border-width: 3px; border-style: solid;"></div>

    <script type="text/javascript">
	var map;
	function initMap() {
		map = new google.maps.Map(document.getElementById('map'), {
			center: {lat: ${shownAd.getLatitude()}, lng: ${shownAd.getLongitude()}},
			zoom: 11
		});

		var latLng = new google.maps.LatLng(${shownAd.getLatitude()},${shownAd.getLongitude()});
          var marker = new google.maps.Marker({
            position: latLng,
            map: map
          });  
          
        window.eqfeed_callback = function(results) {
        for (var i = 0; i < results.features.length; i++) {
          var coords = results.features[i].geometry.coordinates;
          var latLng = new google.maps.LatLng(coords[1],coords[0]);
          var marker = new google.maps.Marker({
            position: latLng,
            map: map
          });
        }
      }
	}
	</script>
    <script async defer
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCdNwB8auysJ8k7gqiKOpLwFyV2L7iBneo&callback=initMap">
    </script>

</section>

<hr class="clearBoth" />



<section>
	<div id="accordion">
		<h2 class="panel">Room Description</h2>
		<div>
			<p>${shownAd.roomDescription}</p>
		</div>

		<h2 class="panel">Roommates</h2>
		<div>
			<p>${shownAd.roommates}</p>
			<c:forEach var="mate" items="${shownAd.registeredRoommates}">
				<div class="roommate">
					<table id="mate">
						<tr>
							<td><a href="/user?id=${mate.id}"> <c:choose>
										<c:when test="${mate.picture.filePath != null}">
											<img src="${mate.picture.filePath}">
										</c:when>
										<c:otherwise>
											<img src="/img/avatar.png">
										</c:otherwise>
									</c:choose>
							</a></td>
							<td>${mate.firstName}${mate.lastName}</td>
							<td>${mate.username}</td>
							<td><c:choose>
									<c:when test="${mate.gender == 'MALE'}">
									male
								</c:when>
									<c:otherwise>
									female
								</c:otherwise>
								</c:choose></td>
						</tr>
					</table>
				</div>
			</c:forEach>
		</div>

		<h2 class="panel">Preferences</h2>
		<div>
			<p>${shownAd.preferences}</p>
		</div>
		<c:choose>
			<c:when test="${shownAd.getSellType() == 3}">
				<h2>Bidding (Auction)</h2>
				<div id="helperDiv">
					<p id="datetoday"></p>
					<p class="timeTilEnd" id="timeTilEnd"></p>

					<br />
					<table id="bidTable">
						<tr>
							<c:choose>
								<c:when test="${!bidService.isBidden(shownAd.getId())}">
									<td>Start Offer:</td>
								</c:when>
								<c:otherwise>
									<td>Highest Bid:</td>
								</c:otherwise>
							</c:choose>
							<td colspan="2">
								${bidService.getHighestBid(shownAd.getId())} CHF
							</td>
						</tr>
						<tr>
							<td>Your Bid:</td>
							<td colspan="2">
								<c:choose> 
									<c:when test="${bidService.getMyBid(loggedInUserEmail,shownAd.getId())==-1}">
										<c:choose>
											<c:when test="${loggedInUserEmail == shownAd.user.username }">
												<p>You are the owner of the auction and
													therefore cannot place a bid!</p>
											</c:when>
											<c:when test="${loggedIn == false}">
												<p>You have to be logged in to place a bid!</p>
											</c:when>
											<c:otherwise>
												<p>You haven't made a bid yet</p>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										${bidService.getMyBid(loggedInUserEmail,shownAd.getId())} CHF
									</c:otherwise>
								</c:choose>
							</td>
						</tr>

					</table>

					<br />

					<table id="bidTable">
						<tr>
							<th>Username</th>
							<th>Bid</th>
							<th>Date</th>
							<th>Time</th>
						</tr>
						<c:forEach var="bid" items="${bidService.getAllBids(shownAd.getId())}">
							<tr>
								<td><a href="/user?id=${bid.userId}"><font color="blue"><u>${bidService.getUserName(bid.userId)}</u></font></a></td>
								<td>${bid.bid}</td>
								<td><fmt:formatDate value="${bid.bidTime.time}" type="date" pattern="dd.MM.yyyy" /></td>
								<td><fmt:formatDate value="${bid.bidTime.time}" type="date" pattern="HH:mm:ss" /></td>
							</tr>
						</c:forEach>
					</table>

				</div>
			</c:when>
		</c:choose>

	</div>

	<table id="checkBoxTable" class="adDescDiv">
		<tr>
			<td><h2>Smoking inside allowed</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.smokers}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Animals allowed</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.animals}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Furnished Room</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.furnished}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>WiFi available</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.internet}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Cable TV</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.cable}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Garage</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.garage}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Cellar</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.cellar}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Balcony</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.balcony}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

		<tr>
			<td><h2>Garden</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.garden}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>
		<tr>
			<td><h2>Dishwasher</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.dishwasher}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>
		<tr>
			<td><h2>Washing machine</h2></td>
			<td><c:choose>
					<c:when test="${shownAd.washingMachine}">
						<img src="/img/check-mark.png">
					</c:when>
					<c:otherwise>
						<img src="/img/check-mark-negative.png">
					</c:otherwise>
				</c:choose></td>
		</tr>

	</table>
</section>

<div class="clearBoth"></div>
<br>
<div id="visitList" class="adDescDiv">
	<h2>Visiting times</h2>
	<table>
		<c:forEach items="${visits }" var="visit">
			<tr>
				<td><fmt:formatDate value="${visit.startTimestamp}"
						pattern="dd.MM.yyyy " /> &nbsp; from <fmt:formatDate
						value="${visit.startTimestamp}" pattern=" HH:mm " /> until <fmt:formatDate
						value="${visit.endTimestamp}" pattern=" HH:mm" /></td>
				<td><c:choose>
						<c:when test="${loggedIn}">
							<c:if test="${loggedInUserEmail != shownAd.user.username}">
								<button class="thinButton" type="button" data-id="${visit.id}">Send
									enquiry to advertiser</button>
							</c:if>
						</c:when>
						<c:otherwise>
							<a href="/login"><button class="thinInactiveButton"
									type="button" data-id="${visit.id}">Login to send
									enquiries</button></a>
						</c:otherwise>
					</c:choose></td>
			</tr>
		</c:forEach>
	</table>
</div>
<br>

<table id="advertiserTable" class="adDescDiv">
	<tr>
		<td><h2>Advertiser</h2>
			<br /></td>
	</tr>

	<tr>
		<td><c:choose>
				<c:when test="${shownAd.user.picture.filePath != null}">
					<img src="${shownAd.user.picture.filePath}">
				</c:when>
				<c:otherwise>
					<img src="/img/avatar.png">
				</c:otherwise>
			</c:choose></td>

		<td>${shownAd.user.username}</td>

		<td id="advertiserEmail"><c:choose>
				<c:when test="${loggedIn}">
					<a href="/user?id=${shownAd.user.id}"><button type="button">Visit
							profile</button></a>
				</c:when>
				<c:otherwise>
					<a href="/login"><button class="thinInactiveButton"
							type="button">Login to visit profile</button></a>
				</c:otherwise>
			</c:choose>
		<td>
			<form>
				<c:choose>
					<c:when test="${loggedIn}">
						<c:if test="${loggedInUserEmail != shownAd.user.username }">
							<button id="newMsg" type="button">Contact Advertiser</button>
						</c:if>
					</c:when>
					<c:otherwise>
						<a href="/login"><button class="thinInactiveButton"
								type="button">Login to contact advertiser</button></a>
					</c:otherwise>
				</c:choose>
			</form>
		</td>
	</tr>
</table>

<div id="msgDiv">
	<form class="msgForm">
		<h2>Contact the advertiser</h2>
		<br> <br> <label>Subject: <span>*</span></label> <input
			class="msgInput" type="text" id="msgSubject" placeholder="Subject" />
		<br>
		<br> <label>Message: </label>
		<textarea id="msgTextarea" placeholder="Message"></textarea>
		<br />
		<button type="button" id="messageSend">Send</button>
		<button type="button" id="messageCancel">Cancel</button>
	</form>
</div>

<div id="confirmationDialog">
	<form>
		<p>Send enquiry to advertiser?</p>
		<button type="button" id="confirmationDialogSend">Send</button>
		<button type="button" id="confirmationDialogCancel">Cancel</button>
	</form>
</div>



<c:import url="template/footer.jsp" />
