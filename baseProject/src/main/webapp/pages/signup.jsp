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
	});
	
	  $( function() {
		    $( "#accordionSignUp" ).accordion({
		      collapsible: true,
		      active: false,
		      heightStyle: "content"
		    });
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
		</table>
		<table>
			<tr>
			 <td>
				<div id="accordionSignUp">
					<h2 class="panel">Premium user?</h2>
						<p><b>What is a premium User?</b><br>
						As a premium User you benefit from multiple things. First your ad's are shown 
						higher on the homepage and are one of the first result when a user search for them.
						Second, as a premium user you can place a bid before any normal user and you get earlier 
						alerted when there is a new ad you probably are searching for.
						<b>How to get a premium user?</b><br>
						To access this feature, you only have to fill in the boxes below. In the first one, you 
						have to add the validation date of your credit card that means how long your credit card
						is valid (date). In the second one you just have to type in your last 5 digits of your 
						IBAN-Number.<br>
						
						<label><b>Validation-Date:</b></label>
						<form:input maxlength="2" typ="number" style="width: 60px;" value="eg. 02" path=""/><label> / 
						</label> <form:input maxlength="4" typ="number" style="width: 80px;" value= "eg. 2018" path=""/> 
						<br>
						<label><b>Last 5 digits of credit card:</b></label>
						<form:input maxlength="5" typ="number" style="width: 50px;" value="12345" path=""/><label>											
												
						</p>
				</div>
			 </td>
			</tr>
		</table>
		

		
		<button type="submit">Sign up</button>
	</fieldset>
</form:form>

<c:import url="template/footer.jsp" />