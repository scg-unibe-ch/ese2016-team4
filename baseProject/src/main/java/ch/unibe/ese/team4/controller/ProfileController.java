package ch.unibe.ese.team4.controller;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team4.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team4.controller.pojos.forms.GetPremiumForm;
import ch.unibe.ese.team4.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team4.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team4.controller.pojos.forms.UnsubscribePremiumForm;
import ch.unibe.ese.team4.controller.service.AdService;
import ch.unibe.ese.team4.controller.service.SignupService;
import ch.unibe.ese.team4.controller.service.UserService;
import ch.unibe.ese.team4.controller.service.UserUpdateService;
import ch.unibe.ese.team4.controller.service.VisitService;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Gender;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.UserPicture;
import ch.unibe.ese.team4.model.UserRole;
import ch.unibe.ese.team4.model.Visit;
import ch.unibe.ese.team4.model.dao.UserDao;

/**
 * Handles all requests concerning user accounts and profiles.
 */
@Controller
public class ProfileController {

	public static final String IMAGE_DIRECTORY = "/img/ads/";
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private SignupService signupService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserUpdateService userUpdateService;

	@Autowired
	private VisitService visitService;

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserDao userDao;

	/** Returns the login page. */
	@RequestMapping(value = "/login")
	public ModelAndView loginPage() {
		ModelAndView model = new ModelAndView("login");
		return model;
	}

	/** Returns the signup page. */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signupPage() {
		ModelAndView model = new ModelAndView("signup");
		model.addObject("signupForm", new SignupForm());
		return model;
	}

	/** Validates the signup form and on success persists the new user. */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signupResultPage(@Valid SignupForm signupForm,
			BindingResult bindingResult) {
		ModelAndView model;
		if (!bindingResult.hasErrors()) {
			signupService.saveFrom(signupForm);
			model = new ModelAndView("login");
			model.addObject("confirmationMessage", "Signup complete!");

			User user = userDao.findByUsername(signupForm.getEmail());
			signupService.sendWelcomeMessage(user);
		} else {
			model = new ModelAndView("signup");
			model.addObject("signupForm", signupForm);
		}
		return model;
	}

	/** Checks and returns whether a user with the given email already exists. */
	@RequestMapping(value = "/signup/doesEmailExist", method = RequestMethod.POST)
	public @ResponseBody boolean doesEmailExist(@RequestParam String email) {
		return signupService.doesUserWithUsernameExist(email);
	}

	/** Shows the edit profile page. */
	@RequestMapping(value = "/profile/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfilePage(Principal principal) {
		ModelAndView model = new ModelAndView("editProfile");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		model.addObject("editProfileForm", new EditProfileForm());
		model.addObject("currentUser", user);
		return model;
	}
	
	/** Shows the get premium page. */
	@RequestMapping(value = "/profile/getPremium", method = RequestMethod.GET)
	public ModelAndView getPremium(Principal principal) {
		ModelAndView model = new ModelAndView("getPremium");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		model.addObject("getPremiumForm", new GetPremiumForm());
		model.addObject("currentUser", user);
		return model;
	}
	
	/** Shows the unsubscribe premium page. */
	@RequestMapping(value = "/profile/unsubscribePremium", method = RequestMethod.GET)
	public ModelAndView unsubscribePremium(Principal principal) {
		ModelAndView model = new ModelAndView("unsubscribePremium");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		model.addObject("unsubscribePremiumForm", new UnsubscribePremiumForm());
		model.addObject("currentUser", user);
		return model;
	}

	/** Handles the request for editing the user profile. */
	@RequestMapping(value = "/profile/editProfile", method = RequestMethod.POST)
	public ModelAndView editProfileResultPage(
			@Valid EditProfileForm editProfileForm,
			BindingResult bindingResult, Principal principal) {
		ModelAndView model;
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (!bindingResult.hasErrors()) {
			userUpdateService.updateFrom(editProfileForm, username);
			model = new ModelAndView("updatedProfile");
			model.addObject("message", "Your Profile has been updated!");
			model.addObject("currentUser", user);
			return model;
		} else {
			model = new ModelAndView("updatedProfile");
			model.addObject("message",
					"Something went wrong, please contact the WebAdmin if the problem persists!");
			return model;
		}
	}
	
	/** Handles the request for get premium. */
	@RequestMapping(value = "/profile/getPremium", method = RequestMethod.POST)
	public ModelAndView getPremiumResultPage(
			@Valid GetPremiumForm getPremiumForm,
			BindingResult bindingResult, Principal principal) {
		ModelAndView model;
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (!bindingResult.hasErrors()) {
			userUpdateService.updateFrom(getPremiumForm, username);
			model = new ModelAndView("updatedProfile");
			model.addObject("message", "Congrats! You are Premium user now.");
			model.addObject("currentUser", user);
			return model;
		} else {
			model = new ModelAndView("updatedProfile");
			model.addObject("message",
					"Something went wrong, please contact the WebAdmin if the problem persists!");
			return model;
		}
	}
	
	/** Handles the request for unsubscribe premium. */
	@RequestMapping(value = "/profile/unsubscribePremium", method = RequestMethod.POST)
	public ModelAndView unsubscribePremiumResultPage(
			@Valid UnsubscribePremiumForm unsubscribePremiumForm,
			BindingResult bindingResult, Principal principal) {
		ModelAndView model;
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (!bindingResult.hasErrors()) {
			userUpdateService.updateFrom(unsubscribePremiumForm, username);
			model = new ModelAndView("updatedProfile");
			model.addObject("message", "You unsubscribed successfully.");
			model.addObject("currentUser", user);
			return model;
		} else {
			model = new ModelAndView("updatedProfile");
			model.addObject("message",
					"Something went wrong, please contact the WebAdmin if the problem persists!");
			return model;
		}
	}

	/** Displays the public profile of the user with the given id. */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView user(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("user");
		User user = userService.findUserById(id);
		if (principal != null) {
			String username = principal.getName();
			User user2 = userService.findUserByUsername(username);
			long principalID = user2.getId();
			model.addObject("principalID", principalID);
		}
		model.addObject("user", user);
		model.addObject("messageForm", new MessageForm());
		return model;
	}

	/** Displays the schedule page of the currently logged in user. */
	@RequestMapping(value = "/profile/schedule", method = RequestMethod.GET)
	public ModelAndView schedule(Principal principal) {
		ModelAndView model = new ModelAndView("schedule");
		User user = userService.findUserByUsername(principal.getName());
		// visits, i.e. when the user sees someone else's property
		Iterable<Visit> visits = visitService.getVisitsForUser(user);
		model.addObject("visits", visits);

		// presentations, i.e. when the user presents a property
		Iterable<Ad> usersAds = adService.getAdsByUser(user);
		ArrayList<Visit> usersPresentations = new ArrayList<Visit>();

		for (Ad ad : usersAds) {
			try {
				usersPresentations.addAll((ArrayList<Visit>) visitService
						.getVisitsByAd(ad));
			} catch (Exception e) {
			}
		}

		model.addObject("presentations", usersPresentations);
		return model;
	}

	/** Returns the visitors page for the visit with the given id. */
	@RequestMapping(value = "/profile/visitors", method = RequestMethod.GET)
	public ModelAndView visitors(@RequestParam("visit") long id) {
		ModelAndView model = new ModelAndView("visitors");
		Visit visit = visitService.getVisitById(id);
		Iterable<User> visitors = visit.getSearchers();

		model.addObject("visitors", visitors);

		Ad ad = visit.getAd();
		model.addObject("ad", ad);
		return model;
	}
	
	/** Returns the new generated one-time password for a user with a google-login. */
	@RequestMapping(value = "/authenticateGoogleUser", method = RequestMethod.GET)
	public @ResponseBody String authenticateG(
			@RequestParam("userName") String userName,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("email") String email,
			@RequestParam("imageURL") String imageURL,
			@RequestParam("googleId") String googleId) {
		User user = userService.findUserByGoogleId(googleId);
		String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
		String picName = email.replace('@', '_').replace('.','_') + ".jpg";
		try(InputStream in = new URL(imageURL).openStream()){
		    Files.copy(in, Paths.get(realPath,picName),StandardCopyOption.REPLACE_EXISTING);
		}catch(Exception e){
			System.out.println(e.getMessage());
			picName = null;
		}
		if (user == null){
			user = signupService.signupGoogleUser(email, getRandomString(24),
					firstName, lastName, IMAGE_DIRECTORY+picName, 
					Gender.UNDEFINED, false, googleId);
		}else {
			user.setEmail(email);
//			user.setUsername(userName);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(getRandomString(24));
			userDao.save(user);
		}
		return user.getPassword();
	}
	private static String VALID_CHARACHTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789abcdefghijklmnopqrstuvwxyz";
	private static Random rnd = new Random(System.currentTimeMillis());
	/**returns a random string*/
	public static String getRandomString(int length) {
		StringBuffer str = new StringBuffer();
		for (int i =0;i<length;i++){
			str.append(VALID_CHARACHTERS.charAt(rnd.nextInt(VALID_CHARACHTERS.length())));
		}
	    return str.toString();
	}
	
//	/**creates new user with accordingly to the inputs*/
//	public User createUser(String email, String password, String firstName,
//			String lastName, String picPath, Gender gender, boolean premium, String googleID) {
//		User user = new User();
//		user.setUsername(email);
//		user.setPassword(password);
//		user.setEmail(email);
//		user.setFirstName(firstName);
//		user.setLastName(lastName);
//		user.setEnabled(true);
//		user.setGender(gender);
//		user.setPremium(premium);
//		user.setGoogleId(googleID);
//		if(premium){ 
//			user.setCcNumber("1111222233334444");
//			user.setCcMonth(5);
//			user.setCcYear(2020);
//		}
//		Set<UserRole> userRoles = new HashSet<>();
//		UserRole role = new UserRole();
//		role.setRole("ROLE_USER");
//		role.setUser(user);
//		userRoles.add(role);
//		user.setUserRoles(userRoles);
//		if (picPath != null){
//			UserPicture picture = new UserPicture();
//			picture.setUser(user);
//			picture.setFilePath(picPath);
//			user.setPicture(picture);
//		}
//		return user;
//	}
}
