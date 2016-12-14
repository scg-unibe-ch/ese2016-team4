<%@ page language="java" pageEncoding="utf-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile"/>

<c:import url="template/header.jsp" />
<script src="https://apis.google.com/js/platform.js" async defer ></script>
<meta name="google-signin-client_id" content="1001465174850-12k4bmfds5no3qp9hebmch197rdls106.apps.googleusercontent.com">


<script>
var logged = ("${loggedIn}"=='true');
function onSignIn(googleUser) {
    console.log("signed in: " + logged);
    console.log(!logged);
    if (!logged){
        console.log("im here!");
        googleUser.reloadAuthResponse()
        var id_token = googleUser.getAuthResponse().id_token;
        var profile = googleUser.getBasicProfile();
        console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
        console.log('Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log('Image URL: ' + profile.getImageUrl());
        console.log('Email: ' + profile.getEmail());
        var email = profile.getEmail()?profile.getEmail():"Undefined";
        if (email == 'Undefined'){
            return;
        }
        var values = {
            userName : profile.getName() ? profile.getName() : "Glögglifrösch",
            firstName : profile.getGivenName() ? profile.getGivenName() : "Blöd",
            lastName : profile.getFamilyName() ? profile.getFamilyName() : "Gange",
            email : email,
            imageURL : profile.getImageUrl()?profile.getImageUrl():"https://thumbs.dreamstime.com/z/indian-head-profile-vector-image-traditional-headdress-feathers-caricature-symbol-sign-logo-element-68779563.jpg",
            googleId: profile.getId(),
        };
        var get = $.get("/authenticateGoogleUser", values);

        console.log("startaAuth");
        get.done(function(pw) {
            console.log("pw: "+pw);
            var form = createElement("form",{action:"/j_spring_security_check", 
                                             method: "post",
                                             id:"login-form",
                                             autocomplete:"off"});		
            form.appendChild(createElement("input",{name:"j_username",id:"field-email", value:email,autocomplete:"off"}));
            form.appendChild(createElement("input",{name:"j_password",id:"field-password", value:pw, type:"text",autocomplete:"off"}));
            logged = true;
            document.body.appendChild(form);
            form.submit();
            document.body.removeChild(form);
        });
    }
}
	</script>
	<script> 
		// prevent google from auto-login!
		window.onbeforeunload = function(e){
		  gapi.auth2.getAuthInstance().signOut();
		};
	</script>

	<script>
	var createElement = (function()
			{
			    // Detect IE using conditional compilation
			    if (/*@cc_on @*//*@if (@_win32)!/*@end @*/false)
			    {
			        // Translations for attribute names which IE would otherwise choke on
			        var attrTranslations =
			        {
			            "class": "className",
			            "for": "htmlFor"
			        };

			        var setAttribute = function(element, attr, value)
			        {
			            if (attrTranslations.hasOwnProperty(attr))
			            {
			                element[attrTranslations[attr]] = value;
			            }
			            else if (attr == "style")
			            {
			                element.style.cssText = value;
			            }
			            else
			            {
			                element.setAttribute(attr, value);
			            }
			        };

			        return function(tagName, attributes)
			        {
			            attributes = attributes || {};

			            // See http://channel9.msdn.com/Wiki/InternetExplorerProgrammingBugs
			            if (attributes.hasOwnProperty("name") ||
			                attributes.hasOwnProperty("checked") ||
			                attributes.hasOwnProperty("multiple"))
			            {
			                var tagParts = ["<" + tagName];
			                if (attributes.hasOwnProperty("name"))
			                {
			                    tagParts[tagParts.length] =
			                        ' name="' + attributes.name + '"';
			                    delete attributes.name;
			                }
			                if (attributes.hasOwnProperty("checked") &&
			                    "" + attributes.checked == "true")
			                {
			                    tagParts[tagParts.length] = " checked";
			                    delete attributes.checked;
			                }
			                if (attributes.hasOwnProperty("multiple") &&
			                    "" + attributes.multiple == "true")
			                {
			                    tagParts[tagParts.length] = " multiple";
			                    delete attributes.multiple;
			                }
			                tagParts[tagParts.length] = ">";

			                var element =
			                    document.createElement(tagParts.join(""));
			            }
			            else
			            {
			                var element = document.createElement(tagName);
			            }

			            for (var attr in attributes)
			            {
			                if (attributes.hasOwnProperty(attr))
			                {
			                    setAttribute(element, attr, attributes[attr]);
			                }
			            }

			            return element;
			        };
			    }
			    // All other browsers
			    else
			    {
			        return function(tagName, attributes)
			        {
			            attributes = attributes || {};
			            var element = document.createElement(tagName);
			            for (var attr in attributes)
			            {
			                if (attributes.hasOwnProperty(attr))
			                {
			                    element.setAttribute(attr, attributes[attr]);
			                }
			            }
			            return element;
			        };
			    }
			})();
</script>

<script>
	function signOut() {
		var auth2 = gapi.auth2.getAuthInstance();

		/*auth2.disconnect().then(function(){
		    console.log('Access revoked.');        	
		});*/
		auth2.signOut().then(function() {
			console.log('User signed out.');
		});
	}
</script>

<pre>
	<a href="/">Home</a>   &gt;   Login</pre>

<h1>Login</h1>

<c:choose>
	<c:when test="${loggedIn}">
		<p>You are already logged in!</p>
	</c:when>
	<c:otherwise>
		<c:if test="${!empty param.error}">
			<p>Incorrect email or password. Please retry using correct email
				and password.</p>
			<br />
		</c:if>
		<form id="login-form" method="post" action="/j_spring_security_check">
			<label for="field-email">Email:</label>
				<input name="j_username" id="field-email" />
			<label for="field-password">Password:</label>
				<input name="j_password" id="field-password" type="password" />
			<button type="submit">Login</button>
			<div class="g-signin2" data-onsuccess="onSignIn"></div>
		</form>
		<br />
		<h2>Test users</h2>

		<ul class="test-users">
			<li>Email: <i>ese@unibe.ch</i>, password: <i>ese</i>, <b>✪premium</b></li>
			<li>Email: <i>jane@doe.com</i>, password: <i>password</i>, <b>✪premium</b></li>
			<li>Email: <i>user@bern.com</i>, password: <i>password</i></li>
			<li>Email: <i>oprah@winfrey.com</i>, password: <i>password</i></li>
		</ul>
		<br />

		<h2>Roommates for AdBern</h2>
		<ul class="test-users">
			<li>Email: <i>hans@unibe.ch</i>, password: <i>password</i></li>
			<li>Email: <i>mathilda@unibe.ch</i>, password: <i>password</i></li>
		</ul>
		<br />
		
			<p>Or <a class="signup" href="<c:url value="/signup" />">sign up</a> as a new user.</p>
		
	</c:otherwise>
</c:choose>


<c:import url="template/footer.jsp" />