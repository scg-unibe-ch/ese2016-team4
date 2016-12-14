package ch.unibe.ese.team4.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Lists;

import ch.unibe.ese.team4.controller.pojos.PictureUploader;
import ch.unibe.ese.team4.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team4.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Gender;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.UserRole;
import ch.unibe.ese.team4.model.dao.AdDao;
import ch.unibe.ese.team4.model.dao.UserDao;
import javax.servlet.ServletContext;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class EditAdServiceTest {

	@Autowired
	private AdService adService;
	
	@Autowired
	private EditAdService editAdService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private AdDao adDao;
	
	
	/**
	 * In order to test the saved ad, I need to get it back from the DB again, so these
	 * two methods need to be tested together, normally we want to test things isolated of
	 * course. Testing just the returned ad from saveFrom() wouldn't answer the question 
	 * whether the ad has been saved correctly to the db.
	 * @throws ParseException 
	 */
	@Test
	public void saveFromAndGetById() throws ParseException {
		//Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setPreferences("Test preferences");
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setRoommates("Test Roommate description");
		placeAdForm.setPrize(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27.02.2015");
		placeAdForm.setMoveOutDate("27.04.2015");
		
		placeAdForm.setSmokers(true);
		placeAdForm.setAnimals(false);
		placeAdForm.setGarden(true);
		placeAdForm.setBalcony(false);
		placeAdForm.setCellar(true);
		placeAdForm.setFurnished(false);
		placeAdForm.setCable(false);
		placeAdForm.setGarage(true);
		placeAdForm.setInternet(false);
		
		List<String> visits = new ArrayList<String>();
		String visit = "28-02-2050;10:02;14:15";
		visits.add(visit);
		placeAdForm.setVisits(visits);
		
		
		List<String> regRoommates = new ArrayList<String>();
		regRoommates.add("System");
		placeAdForm.setRegisteredRoommateEmails(regRoommates);
		
		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("klaus@kanns.ch", "password", "Klaus", "Klausi",
				Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);
		
		adService.saveFrom(placeAdForm, filePaths, hans);
		
		
		Iterable<Ad> newCreatedAd = adDao.findByUser(hans);
		assertTrue(newCreatedAd.iterator().hasNext());
		Ad ad = new Ad();
		ad = newCreatedAd.iterator().next();
		
		//Testing
		assertTrue(ad.getSmokers());
		assertFalse(ad.getAnimals());
		assertEquals("Bern", ad.getCity());
		assertEquals(3018, ad.getZipcode());
		assertEquals("Test preferences", ad.getPreferences());
		assertEquals("Test Room description", ad.getRoomDescription());
		assertEquals("Test Roommate description", ad.getRoommates());
		assertEquals(600, ad.getPrizePerMonth());
		assertEquals(50, ad.getSquareFootage());
		assertEquals("title", ad.getTitle());
		assertEquals("Hauptstrasse 13", ad.getStreet());
		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	    Date result =  df.parse("27.02.2015");
		
		assertEquals(0, result.compareTo(ad.getMoveInDate()));
		
		PlaceAdForm editAdForm = new PlaceAdForm(); 
		
		editAdForm.setCity("4500 - Solothurn");
		editAdForm.setPreferences("Edit preferences");
		editAdForm.setRoomDescription("Edit Room description");
		editAdForm.setRoommates("Edit Roommate description");
		editAdForm.setPrize(800);
		editAdForm.setSquareFootage(80);
		editAdForm.setTitle("edit");
		editAdForm.setStreet("Langstrasse 15");
		editAdForm.setMoveInDate("30.03.2015");
		editAdForm.setMoveOutDate("27.04.2015");
		
		editAdForm.setSmokers(false);
		editAdForm.setAnimals(true);
		editAdForm.setGarden(false);
		editAdForm.setBalcony(true);
		editAdForm.setCellar(false);
		editAdForm.setFurnished(true);
		editAdForm.setCable(true);
		editAdForm.setGarage(false);
		editAdForm.setInternet(true);
		editAdForm.setAuctionEndDate("30.01.2050");

		editAdForm.setRegisteredRoommateEmails(regRoommates);
		
		editAdForm.setVisits(visits);
		
		editAdService.saveFrom(editAdForm, filePaths, hans, ad.getId());
	}
	
	private User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
	
}
