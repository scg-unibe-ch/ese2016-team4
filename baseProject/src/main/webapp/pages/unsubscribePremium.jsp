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

<pre><a href="/">Home</a>   &gt;   <a href="/user?id=${currentUser.id}">Public Profile</a> &gt;   Unsubscribe Premium</pre>


<form:form id="signupForm" method="post" modelAttribute="unsubscribePremiumForm"
	action="unsubscribePremium">
	<fieldset>
		<legend>Unsubscribe</legend>
			<p><b>We are interested in your reasons</b><br>
			Please let us know why you would like to unsubscribe. 
						
			<table>
			<tr><form:checkbox name="expensive" id="expensive" path="expensive"/><label>Too expensive</label></tr><br>
			<tr><form:checkbox name="noUse" id="noUse" path="noUse"/><label>I do not use FlatFindr anymore</label></tr><br>
			<tr><form:checkbox name="badService" id="badService" path="badService"/><label>Not enough advantages</label></tr><br>
			<tr><form:checkbox name="otherReasons" id="otherReasons" path="otherReasons" /><label>Other reasons</label></tr>
			</table>
			
		<div>
			<button type="submit">Unsubscribe</button>
			<button onClick="history.go(-1);">Cancel</button>
		</div>
	</fieldset>
</form:form>


<c:import url="template/footer.jsp" />