package ch.unibe.ese.team4.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Bid;
import ch.unibe.ese.team4.model.Gender;
import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AdDao;
import ch.unibe.ese.team4.model.dao.BidHistoryDao;
import ch.unibe.ese.team4.model.dao.MessageDao;
import ch.unibe.ese.team4.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AuctionServiceTest {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private BidService bidService;
	
	@Autowired
	private BidHistoryDao bidHistoryDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Before
	public void setup(){
		Ad ad = new Ad();
		ad.setSellType(3);
		ad.setPropertyType(4);
		ad.setTitle("auctionhouse");
		ad.setZipcode(3012);
		ad.setStreet("Mittelweg");
		ad.setCity("Bern");
		
		Date date = new Date();
		ad.setCreationDate(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, +2);
		date = cal.getTime();
		
		ad.setAuctionEndDate(date);

		ad.setSquareFootage(50);
		ad.setRoomDescription("wowmuchhouse");
		ad.setPreferences("muchwowpreferences");
		ad.setRoommates("ese@unibe.ch");		
		ad.setSmokers(false);
		ad.setAnimals(false);
		ad.setGarden(false);
		ad.setBalcony(false);
		ad.setCellar(false);
		ad.setFurnished(false);
		ad.setCable(false);
		ad.setGarage(false);
		ad.setDishwasher(false);
		ad.setWashingMachine(false);
		ad.setUser(userDao.findByUsername("System"));
		
		ad.setStartOffer(500);
		
		adDao.save(ad);
		User user = userDao.findByUsername("System");
		Ad testAd = adDao.findByUser(user).iterator().next();
		assertEquals(testAd.getUser().getEmail(),"System");
		
		User biduser = new User();
		biduser.setUsername("hanswillbietenn");
		biduser.setPassword("hanspeter");
		biduser.setEmail("hans@peter.dede");
		biduser.setFirstName("hans");
		biduser.setLastName("peter");
		biduser.setGender(Gender.MALE);
		biduser.setEnabled(true);
		
		User bidusertwo = new User();
		bidusertwo.setUsername("hanswillbietenqq");
		bidusertwo.setPassword("hanspeterq");
		bidusertwo.setEmail("hans@peter.deqdeq");
		bidusertwo.setFirstName("hansq");
		bidusertwo.setLastName("peterq");
		bidusertwo.setGender(Gender.MALE);
		bidusertwo.setEnabled(true);
		
		userDao.save(biduser);
		userDao.save(bidusertwo);
		
	}
	@Test
	public void auctionEndWithBidsTest() throws ParseException {
		User user = userDao.findByUsername("System");

		Ad testAd = adDao.findByUser(user).iterator().next();
		User biduser = userDao.findByUsername("hanswillbietenn");
		User bidusertwo = userDao.findByUsername("hanswillbietenqq");

		bidService.addBid(testAd.getId(), biduser.getId(), 1000000);
		bidService.addBid(testAd.getId(), bidusertwo.getId(), 5000000);
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -5);
		date = cal.getTime();
		testAd.setAuctionEndDate(date);
		adDao.save(testAd);
		
		Iterable<Bid> bids = bidHistoryDao.findByAdIdOrderByBidDesc(testAd.getId());
		assertTrue(bids.iterator().hasNext());
		
		auctionService.checkForFinishedAuctions();
		assertTrue(adDao.findByUser(user).iterator().next().getFinished());
		
		cal.setTime(date);
		cal.add(Calendar.YEAR, +10);
		date = cal.getTime();
		testAd.setAuctionEndDate(date);
		adDao.save(testAd);
		
		assertFalse(adDao.findByUser(user).iterator().next().getFinished());
		auctionService.instantBuy(testAd.getId(), biduser);
		assertTrue(adDao.findByUser(user).iterator().next().getFinished());

		Iterable<Message> biduserMessages = messageDao.findByRecipient(biduser);
		assertEquals(biduserMessages.iterator().next().getText().substring(0, 9), "Dear hans");
		
		assertEquals(auctionService.getNoSuccessWithAuctionMessage(testAd).substring(0, 9), ("Dear "+user.getFirstName()).subSequence(0, 9));
	}

}
