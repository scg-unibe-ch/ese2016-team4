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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team4.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team4.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Gender;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.UserRole;
import ch.unibe.ese.team4.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AdServiceTest {

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserDao userDao;
	
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
		
		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("hans@kanns.ch", "password", "Hans", "Kanns",
				Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);
		
		adService.saveFrom(placeAdForm, filePaths, hans);
		
		Ad ad = new Ad();
		Iterable<Ad> ads = adService.getAllAds();
		Iterator<Ad> iterator = ads.iterator();
		
		while (iterator.hasNext()) {
			ad = iterator.next();
		}
		
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
	
	@Test
	public void compareEmailString(){
		String email = "hans@glueck.de";
		String toCompareTrue = "nanana@batman.de;apfel@baum.com:hans@glueck.de";
		assertTrue(adService.checkIfAlreadyAdded(email, toCompareTrue));
		
		String toCompareFalse = "nanana@batman.de;apfel@baum.com:bohne@glueck.de";
		assertFalse(adService.checkIfAlreadyAdded(email, toCompareFalse));
	}
	
	//checks optional and date filtering by resulting in no found ads by unrealistic dates
	@Test
	public void queryResultTestNoResults(){
		SearchForm searchForm = new SearchForm();
		
		searchForm.setAnimals(true);
		searchForm.setBalcony(true);
		searchForm.setCable(true);
		searchForm.setCellar(true);
		searchForm.setDishwasher(true);
		searchForm.setFurnished(true);
		searchForm.setGarage(true);
		searchForm.setGarden(true);
		searchForm.setInternet(true);
		searchForm.setSmokers(true);
		searchForm.setWashingMachine(true);
		
		searchForm.setRoom(true);
		searchForm.setStudio(true);
		searchForm.setFlat(true);
		searchForm.setHouse(true);
		
		searchForm.setAuction(true);
		searchForm.setBuy(true);
		searchForm.setRent(true);
		
		searchForm.setCity("3012 - Bern");
		searchForm.setRadius(5000);
		searchForm.setPrize(1);
		
		searchForm.setEarliestMoveInDate("12.01.1950");
		searchForm.setEarliestMoveOutDate("14.01.1950");
		searchForm.setLatestMoveInDate("13.01.1950");
		searchForm.setLatestMoveOutDate("15.01.1950");
		
		assertEquals(adService.queryResults(searchForm).iterator().hasNext(),false);		
	}
	
	@Test
	public void queryResultTestAllResults(){
		SearchForm searchForm = new SearchForm();
		
		searchForm.setAnimals(false);
		searchForm.setBalcony(false);
		searchForm.setCable(false);
		searchForm.setCellar(false);
		searchForm.setDishwasher(false);
		searchForm.setFurnished(false);
		searchForm.setGarage(false);
		searchForm.setGarden(false);
		searchForm.setInternet(false);
		searchForm.setSmokers(false);
		searchForm.setWashingMachine(false);
		
		searchForm.setRoom(true);
		searchForm.setStudio(true);
		searchForm.setFlat(true);
		searchForm.setHouse(true);
		
		searchForm.setAuction(true);
		searchForm.setBuy(true);
		searchForm.setRent(true);
		
		searchForm.setCity("3012 - Bern");
		searchForm.setRadius(5000);
		searchForm.setPrize(999999999);
		
		List<Ad> adsByForm = new ArrayList<Ad>();
		List<Ad> allAds = new ArrayList<Ad>();
		
		for(Ad ad : adService.queryResults(searchForm)){
			adsByForm.add(ad);
		}
		
		for(Ad ad : adService.getAllAds()){
			allAds.add(ad);
		}
		assertEquals(adsByForm.size(),allAds.size()-1);
	}
	
}
