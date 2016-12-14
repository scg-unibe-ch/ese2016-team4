package ch.unibe.ese.team4.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Bid;
import ch.unibe.ese.team4.model.Gender;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AdDao;
import ch.unibe.ese.team4.model.dao.BidHistoryDao;
import ch.unibe.ese.team4.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class BidServiceTest {
	
	@Autowired
	AdDao adDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	BidService bidService;
	
	@Autowired
	BidHistoryDao bidHistoryDao;
	
	//tests all bidService methods which require bids and users
	@Test
	public void bidServiceTests(){
		Ad ad = new Ad();
		ad.setId(999999999);
		ad.setSellType(3);
		ad.setPropertyType(4);
		ad.setTitle("auctionhouse");
		ad.setZipcode(3012);
		ad.setStreet("Sidlerstrasse");
		ad.setCity("Bern");
		
		Date date = new Date();
		ad.setCreationDate(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 2050);
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
		biduser.setUsername("hanswillbieten");
		biduser.setPassword("hanspeter");
		biduser.setEmail("hans@peter.de");
		biduser.setFirstName("hans");
		biduser.setLastName("peter");
		biduser.setGender(Gender.MALE);
		biduser.setEnabled(true);
		
		User bidusertwo = new User();
		bidusertwo.setUsername("hanswillbietenq");
		bidusertwo.setPassword("hanspeterq");
		bidusertwo.setEmail("hans@peter.deq");
		bidusertwo.setFirstName("hansq");
		bidusertwo.setLastName("peterq");
		bidusertwo.setGender(Gender.MALE);
		bidusertwo.setEnabled(true);
		
		userDao.save(biduser);
		userDao.save(bidusertwo);
		
		biduser = userDao.findByUsername("hanswillbieten");
		bidusertwo = userDao.findByUsername("hanswillbietenq");
		
		assertEquals(biduser.getEmail(),"hans@peter.de");
		assertEquals(bidusertwo.getEmail(),"hans@peter.deq");
		
		bidService.addBid(testAd.getId(), biduser.getId(), 1000000);
		bidService.addBid(testAd.getId(), bidusertwo.getId(), 5000000);
		
		Iterable<Bid> bids = bidHistoryDao.findByAdIdOrderByBidDesc(testAd.getId());
		assertEquals(bids.iterator().next().getBid(),5000000);

		//allBids()
		Iterable<Bid> allbids = bidService.allBids(testAd.getId());
		assertEquals(allbids.iterator().next().getBid(), 5000000);
		
		//getBids()
		List<Ad> adlist = new ArrayList<Ad>();
		adlist.add(testAd);
		Iterable<Long> bidsofadlist = bidService.getBids(adlist);
		assertEquals(bidsofadlist.iterator().next().longValue(),5000000);
		
		//getBidUsernames()
		Iterable<String> bidusernames = bidService.getBidUsernames(allbids);
		assertTrue(bidusernames.iterator().next().equals("hanswillbietenq"));
		
		//getMyBid()
		long mybid = bidService.getMyBid("hanswillbietenq", testAd.getId());
		assertEquals(mybid,5000000);
		
		//getNextBid()
		long nextbid = bidService.getNextBid(testAd.getId());
		assertEquals(nextbid,5000001);
		
		//getHighestBid()
		long highestBid = bidService.getHighestBid(testAd.getId());
		assertEquals(highestBid, 5000000);
		
		//getHighestBids()
		Iterable<Long> highestBids = bidService.getHighestBids(adlist);
		assertEquals(highestBids.iterator().next().longValue(), 5000000);
		
		//isBidden()
		assertTrue(bidService.isBidden(testAd.getId()));

		//areBidden()
		Iterable<Boolean> areBidden = bidService.areBidden(adlist);
		assertTrue(areBidden.iterator().next().booleanValue());
		
		//getAllBids()
		Iterable<Bid> allBidsOfAd = bidService.getAllBids(testAd.getId());
		List<Bid> allBidsOfAdList = new ArrayList<Bid>();
		for(Bid bid : allBidsOfAd){
			allBidsOfAdList.add(bid);
		}
		assertEquals(allBidsOfAdList.size(),2);
		assertEquals(allBidsOfAdList.get(0).getBid(),5000000);
		assertEquals(allBidsOfAdList.get(1).getBid(),1000000);
	}
	
		@Test
		public void getUserNameTest(){
			User user = userDao.findByUsername("System");
			assertEquals(bidService.getUserName(user.getId()),"System");
			
			assertEquals(bidService.getUserName(999999999),"ghost user");
			
			
		}
		
		@Test
		public void getNextBid(){
			Ad ad = new Ad();
			ad.setId(999999999);
			ad.setSellType(3);
			ad.setPropertyType(4);
			ad.setTitle("auctionhouse");
			ad.setZipcode(3012);
			ad.setStreet("Sidlerstrasse");
			ad.setCity("Bern");
			
			Date date = new Date();
			ad.setCreationDate(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, 2050);
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
			ad = adDao.findByUser(userDao.findByUsername("System")).iterator().next();
			
			long nextBidOfAdWithoutBids = bidService.getNextBid(ad.getId());
			assertEquals(nextBidOfAdWithoutBids, ad.getStartOffer());
			
			long nextBidOfNullAd = bidService.getNextBid(999999999);
			assertEquals(nextBidOfNullAd, 1);
		}
		
		@Test
		public void getHighestBidTest(){
			Ad ad = new Ad();
			ad.setId(999999999);
			ad.setSellType(3);
			ad.setPropertyType(4);
			ad.setTitle("auctionhouse");
			ad.setZipcode(3012);
			ad.setStreet("Sidlerstrasse");
			ad.setCity("Bern");
			
			Date date = new Date();
			ad.setCreationDate(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, 2050);
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
			ad = adDao.findByUser(userDao.findByUsername("System")).iterator().next();
			
			long highestBidNullAd = bidService.getHighestBid(99999999);
			assertEquals(highestBidNullAd, 0);
			
			long highestBidAdWithoutBid = bidService.getHighestBid(ad.getId());
			assertEquals(highestBidAdWithoutBid, ad.getStartOffer());
		}
		
		@Test
		public void isBidCreatorTest(){
			Bid bid = new Bid();
			bid.setAdId(999999999);
			assertFalse(bidService.isBidCreator(bid));
		}
		
}
