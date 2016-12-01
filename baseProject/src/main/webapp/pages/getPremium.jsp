<%@page import="ch.unibe.ese.team1.model.Ad"%>
<%@ page language="java" pageEncoding="iso-8859-1"
	contentType="text/html;charset=iso-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
	
<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   <a href="/user?id=${currentUser.id}">Public Profile</a> &gt;   Get Premium</pre>

<form:form id="signupForm" method="post" modelAttribute="getPremiumForm"
	action="getPremium">
	<fieldset>
		<legend>Get Premium!</legend>
			<p><b>What are the advantages of a premium User?</b><br>
			As a premium User you benefit from multiple things:
			<br>- Your ads are shown on the front page
			<br>- In the search results, premium user ads get placed before the ones of cheapskate users
			<br>- You can place a bid before any penny-pincher user
			<br>- You get notified immediately (as opposed to 1 hour later like a skinflint user) if a new ad gets placed that 
			matches your search alerts
			<br><br><b>How to become a premium user?</b><br>
			To access this feature, you only have to tick the premium user box and fill in your credit card info below. 
			If you're unsure whether or not you want to become a premium user, you can always upgrade your normal account to premium later.
			<br><br><b>How much does a premium account cost?</b><br>
			A premium membership costs only 235.60* CHF per month.
			We automatically attempt to charge your credit card on the 1st of each month.
			Users who can not pay their premium membership will be prosecuted.<br>
			<sub><i>*plus taxes</i></sub>
			<br>
			<br>
						
			<label>Validation-Date:</label>
			<form:input id="field-validationmonth" maxlength="2" type="number" style="width: 40px;" placeholder="02" path="ccMonth"/>
			<form:errors path="ccMonth" cssClass="validationErrorText" />
			/ 
			<form:input id="field-validationyear" maxlength="4" type="number" style="width: 60px;" placeholder= "2018" path="ccYear"/>
			<form:errors path="ccYear" cssClass="validationErrorText" />	 
			<br>
			<label for="field-creditcard">16 digit credit card number:</label>
			<form:input id="field-creditcard" placeholder="1111222233334444" path="ccNumber"/>										
			<form:errors path="ccNumber" cssClass="validationErrorText" />
			<br>
			<sub><i>If you leave the credit card info blank, a normal user account will be created</i></sub>						
			</p>
			
		<div>
			<button type="submit">Get Premium</button>
			<button onClick="history.go(-1);">Cancel</button>
		</div>
	</fieldset>
</form:form>


<c:import url="template/footer.jsp" />