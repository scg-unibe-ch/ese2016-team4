<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script>
	// Validate the email field
	$(document).ready(function() {
		$("#field-email").focusout(function() {
			var text = $(this).val();
			$.post("/signup/doesEmailExist", {email: text}, function(data){
				if(data){
					alert("This username is taken. Please choose another one!");
					$("#field-email").val("");
				}
			});
		});
		
		if ($('#field-premium').prop('checked')) {
			$(function() {
				$("#accordionSignUp").accordion({
					collapsible : false,
					active : false,
					heightStyle : "content"
				});
			});
		} else {
			$(function() {
				$("#accordionSignUp").accordion({
					collapsible : true,
					active : false,
					heightStyle : "content"
				});
			});
		}	
	});

</script>


<pre>
	<a href="/">Home</a>   &gt;   Sign up</pre>

<h1>Sign up</h1>
<form:form id="signupForm" method="post" modelAttribute="signupForm"
	action="signup">
	<fieldset>
		<legend>Enter Your Information</legend>

		<table>

			<tr>
				<td class="signupDescription"><label for="field-firstName">First Name:</label></td>
				<td><form:input path="firstName" id="field-firstName" /> <form:errors
						path="firstName" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td class="signupDescription"><label for="field-lastName">Last Name:</label></td>
				<td><form:input path="lastName" id="field-lastName" /> <form:errors
						path="lastName" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td class="signupDescription"><label for="field-password">Password:</label></td>
				<td><form:input path="password" id="field-password"
						type="password" /> <form:errors path="password"
						cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td class="signupDescription"><label for="field-email">Email:</label></td>
				<td><form:input path="email" id="field-email" /> <form:errors
						path="email" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td class="signupDescription"><label for="field-gender">Gender:</label></td>
				<td><form:select path="gender">
						<form:option value="FEMALE" label="Female" />
						<form:option value="MALE" label="Male" />
					</form:select></td>
			</tr>
			<tr>
				<td class="premiumUser"><label for="field-premium">Premium User</label></td>
				<td><form:checkbox id="field-premium" path="premiumUser" value="0" /></td>
			</tr>
		</table>
		<table>
			<tr>
			 <td>
				<div id="accordionSignUp">
					<h2 class="panel">Premium user</h2>
						<p><b>What are the advantages of a premium User?</b><br>
						As a premium User you benefit from multiple things:
						<br>- Your ads are shown on the front page
						<br>- In the search results, premium user ads get placed before the ones of cheapskate users
						<br>- You can place a bid before any penny-pincher user
						<br>- You get notified immediately (as opposed to 1 hour later like a skinflint user) if a new ad gets placed that 
						matches your search alerts
						<br><b>How to become a premium user?</b><br>
						To access this feature, you only have to tick the premium user box and fill in your credit card info below. 
						If you're unsure whether or not you want to become a premium user, you can always upgrade your normal account to premium later.
						<br><b>How much does a premium account cost?</b><br>
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
				</div>
			 </td>
			</tr>
		</table>
		

		
		<button type="submit">Sign up</button>
		
	</fieldset>
</form:form>

<script>

$('#field-premium').change(function() {
	if ($(this).prop('checked')) {
		$(function() {
			$("#accordionSignUp").accordion({
				collapsible : false,
				active : false,
				heightStyle : "content"
			});
		});
	} else {
		$(function() {
			$("#accordionSignUp").accordion({
				collapsible : true,
				active : false,
				heightStyle : "content"
			});
		});
	}
});	
  
</script>

<c:import url="template/footer.jsp" />