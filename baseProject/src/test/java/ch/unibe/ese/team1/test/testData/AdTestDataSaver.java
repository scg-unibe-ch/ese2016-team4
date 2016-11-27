package ch.unibe.ese.team1.test.testData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/** This inserts several ad elements into the database. */
@Service
public class AdTestDataSaver {

	@Autowired
	private AdDao adDao;
	@Autowired
	private UserDao userDao;

	@Transactional
	public void saveTestData() throws Exception {
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		User oprah = userDao.findByUsername("oprah@winfrey.com");
		User jane = userDao.findByUsername("jane@doe.com");
		User hans = userDao.findByUsername("hans@unibe.ch");
		User mathilda = userDao.findByUsername("mathilda@unibe.ch");
		
		List<User> regRoommatesAdBern = new LinkedList<User>();
		regRoommatesAdBern.add(hans);
		regRoommatesAdBern.add(mathilda);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat formatterTime = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
		
		Date creationDate1 = formatter.parse("03.10.2014");
		Date creationDate2 = formatter.parse("11.10.2014");
		Date creationDate3 = formatter.parse("25.10.2014");
		Date creationDate4 = formatter.parse("02.11.2014");
		Date creationDate5 = formatter.parse("25.11.2013");
		Date creationDate6 = formatter.parse("01.12.2014");
		Date creationDate7 = formatter.parse("16.11.2014");
		Date creationDate8 = formatter.parse("27.11.2014");
		Date creationDate9 = formatterTime.parse("03.11.2016, 10:05:30");
		Date creationDate10 = formatter.parse("26.11.2016");

		
		Date moveInDate1 = formatter.parse("15.12.2017");
		Date moveInDate2 = formatter.parse("21.12.2017");
		Date moveInDate3 = formatter.parse("01.01.2017");
		Date moveInDate4 = formatter.parse("15.01.2017");
		Date moveInDate5 = formatter.parse("01.02.2017");
		Date moveInDate6 = formatter.parse("01.03.2017");
		Date moveInDate7 = formatter.parse("15.03.2017");
		Date moveInDate8 = formatter.parse("16.02.2017");
		Date moveInDate9 = formatter.parse("18.10.2017");
		
		Date moveOutDate1 = formatter.parse("31.03.2018");
		Date moveOutDate2 = formatter.parse("30.04.2018");
		Date moveOutDate3 = formatter.parse("31.03.2018");
		Date moveOutDate4 = formatter.parse("01.07.2018");
		Date moveOutDate5 = formatter.parse("30.09.2018");
		Date moveOutDate6 = formatter.parse("10.11.2018");
		
		Date auctionEndDate1 = formatterTime.parse("30.11.2016, 14:00:00");
		Date auctionEndDate2 = formatterTime.parse("30.11.2016, 19:40:00");
		Date auctionEndDate3 = formatterTime.parse("30.11.2016, 19:40:00");

		
		String roomDescription1 = "The room is a part of 3.5 rooms apartment completely renovated"
				+ "in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ "Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ "transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ "charges are separate. If you are interested, feel free to drop me a message"
				+ "to have an appointment for a visit or can write me for any further information";
		String preferences1 = "Uncomplicated, open minded and easy going person (m / w),"
				+ "non-smoker, can speak English, which of course fits in the WG, and who likes dogs."
				+ "Cleanliness is must. Apart from personal life, sometimes glass of wine,"
				+ "eat and cook together and go out in the evenings.";

		Ad adBern = new Ad();
		adBern.setZipcode(3011);
		adBern.setMoveInDate(moveInDate1);
		adBern.setCreationDate(creationDate1);
		adBern.setMoveOutDate(moveOutDate1);
		adBern.setPrizePerMonth(400);
		adBern.setSquareFootage(50);
		adBern.setPropertyType(1);
		adBern.setSellType(1);
		adBern.setSmokers(false);
		adBern.setAnimals(true);
		adBern.setRoomDescription(roomDescription1);
		adBern.setPreferences(preferences1);
		adBern.setRoommates("One roommate");
		adBern.setUser(bernerBaer);
		adBern.setRegisteredRoommates(regRoommatesAdBern);
		adBern.setTitle("Roommate wanted in Bern");
		adBern.setStreet("Kramgasse 22");
		adBern.setLatitude(46.9167);
		adBern.setLongitude(7.4667);
		adBern.setCity("Bern");
		adBern.setGarden(true);
		adBern.setBalcony(true);
		adBern.setCellar(true);
		adBern.setFurnished(true);
		adBern.setCable(true);
		adBern.setGarage(true);
		adBern.setInternet(true);
		List<AdPicture> pictures = new ArrayList<>();
		pictures.add(createPicture(adBern, "/img/test/ad1_1.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_3.jpg"));
		adBern.setPictures(pictures);
		adDao.save(adBern);

		String studioDescription2 = "It is small studio close to the"
				+ "university and the bahnhof. The lovely neighbourhood"
				+ "Langgasse makes it an easy place to feel comfortable."
				+ "The studio is close to a Migross, Denner and the Coop."
				+ "The studio is 60m2. It has it own Badroom and kitchen."
				+ "Nothing is shared. The studio is fully furnished. The"
				+ "studio is also provided with a balcony. So if you want to"
				+ "have a privat space this could totally be good place for you."
				+ "Be aware it is only till the end of March. The price is from"
				+ "550- 700 CHF, But there is always room to talk about it.";
		String roomPreferences2 = "I would like to have an easy going person who"
				+ "is trustworthy and can take care of the flat. No animals please."
				+ "Non smoker preferred.";
		
		Ad adBern2 = new Ad();
		adBern2.setZipcode(3012);
		adBern2.setMoveInDate(moveInDate2);
		adBern2.setCreationDate(creationDate2);
		adBern2.setMoveOutDate(moveOutDate4);
		adBern2.setPrizePerMonth(700);
		adBern2.setSquareFootage(60);
		adBern2.setPropertyType(2);
		adBern2.setSellType(1);
		adBern2.setSmokers(false);
		adBern2.setAnimals(true);
		adBern2.setRoomDescription(studioDescription2);
		adBern2.setPreferences(roomPreferences2);
		adBern2.setRoommates("None");
		adBern2.setUser(ese);
		adBern2.setTitle("Cheap studio in Bern!");
		adBern2.setStreet("Länggassstrasse 40");
		adBern2.setLatitude(46.9167);
		adBern2.setLongitude(7.4667);
		adBern2.setCity("Bern");
		adBern2.setGarden(false);
		adBern2.setBalcony(false);
		adBern2.setCellar(false);
		adBern2.setFurnished(false);
		adBern2.setCable(false);
		adBern2.setGarage(false);
		adBern2.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBern2, "/img/test/ad2_1.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_3.jpg"));
		adBern2.setPictures(pictures);
		adDao.save(adBern2);

		String studioDescription3 = " In the center of Gundeli (5 Min. away from the"
				+ "station, close to Tellplatz) there is a lovely house, covered with"
				+ "savage wine stocks, without any neighbours but with a garden. The"
				+ "house has two storey, on the first floor your new room is waiting"
				+ "for you. The house is totally equipped with everything a household "
				+ ": washing machine, kitchen, batroom, W-Lan...if you don´t have any"
				+ "furniture, don´t worry, I am sure, we will find something around"
				+ "the house. The price for the room and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ "new female flatmate from December on.";
		String roomPreferences3 = "smoking female flatmate";
		
		Ad adLuzern1 = new Ad();
		adLuzern1.setZipcode(6000);
		adLuzern1.setCreationDate(creationDate10);
		adLuzern1.setStartOffer(500);
		adLuzern1.setSquareFootage(100);
		adLuzern1.setPropertyType(2);
		adLuzern1.setSellType(3);
		adLuzern1.setAuctionEndDate(auctionEndDate2);
		adLuzern1.setRoomDescription("TEST");
		adLuzern1.setPreferences(preferences1);
		adLuzern1.setRoommates("None");
		adLuzern1.setUser(ese);
		adLuzern1.setTitle("Studio in Luzern!");
		adLuzern1.setStreet("Bahnhofstr. 40");
		adLuzern1.setLatitude(47.0833);
		adLuzern1.setLongitude(8.2667);
		adLuzern1.setCity("Luzern");
		adLuzern1.setAnimals(true);
		adLuzern1.setGarden(true);
		adLuzern1.setBalcony(false);
		adLuzern1.setCellar(false);
		adLuzern1.setFurnished(false);
		adLuzern1.setCable(false);
		adLuzern1.setGarage(false);
		adLuzern1.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLuzern1, "/img/test/ad2_1.jpg"));
		pictures.add(createPicture(adLuzern1, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adLuzern1, "/img/test/ad2_3.jpg"));
		adLuzern1.setPictures(pictures);
		adDao.save(adLuzern1);
		
		Ad adLuzern2 = new Ad();
		adLuzern2.setZipcode(6000);
		adLuzern2.setCreationDate(creationDate10);
		adLuzern2.setStartOffer(500);
		adLuzern2.setSquareFootage(100);
		adLuzern2.setPropertyType(3);
		adLuzern2.setSellType(3);
		adLuzern2.setAuctionEndDate(auctionEndDate3);
		adLuzern2.setRoomDescription("TEST");
		adLuzern2.setPreferences(preferences1);
		adLuzern2.setRoommates("None");
		adLuzern2.setUser(ese);
		adLuzern2.setTitle("Flat in Luzern!");
		adLuzern2.setStreet("Bahnhofstr. 42");
		adLuzern2.setCity("Luzern");
		adLuzern2.setLatitude(47.0833);
		adLuzern2.setLongitude(8.2667);
		adLuzern2.setAnimals(true);
		adLuzern2.setGarden(true);
		adLuzern2.setBalcony(false);
		adLuzern2.setCellar(false);
		adLuzern2.setFurnished(false);
		adLuzern2.setCable(false);
		adLuzern2.setGarage(false);
		adLuzern2.setInternet(true);
		pictures= new ArrayList<>();
		pictures.add(createPicture(adLuzern2, "/img/test/ad2_1.jpg"));
		pictures.add(createPicture(adLuzern2, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adLuzern2, "/img/test/ad2_3.jpg"));
		adLuzern2.setPictures(pictures);
		adDao.save(adLuzern2);
		
		Ad adBasel = new Ad();
		adBasel.setZipcode(4053);
		adBasel.setMoveInDate(moveInDate3);
		adBasel.setMoveOutDate(moveOutDate2);
		adBasel.setCreationDate(creationDate3);
		adBasel.setPrizePerMonth(480);
		adBasel.setSquareFootage(10);
		adBasel.setPropertyType(2);
		adBasel.setSellType(1);
		adBasel.setSmokers(true);
		adBasel.setAnimals(false);
		adBasel.setRoomDescription(studioDescription3);
		adBasel.setPreferences(roomPreferences3);
		adBasel.setRoommates("None");
		adBasel.setUser(bernerBaer);
		adBasel.setTitle("Nice, bright studio in the center of Basel");
		adBasel.setStreet("Bruderholzstrasse 32");
		adBasel.setCity("Basel");
		adBasel.setLatitude(47.5667);
		adBasel.setLongitude(7.6);
		adBasel.setGarden(false);
		adBasel.setBalcony(false);
		adBasel.setCellar(false);
		adBasel.setFurnished(false);
		adBasel.setCable(false);
		adBasel.setGarage(false);
		adBasel.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBasel, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_3.jpg"));
		adBasel.setPictures(pictures);
		adDao.save(adBasel);
		
		String studioDescription4 = "Flatshare of 3 persons. Flat with 5 rooms"
				+ "on the second floor. The bedroom is about 60 square meters"
				+ "with access to a nice balcony. In addition to the room, the"
				+ "flat has: a living room, a kitchen, a bathroom, a seperate WC,"
				+ "a storage in the basement, a balcony, a laundry room in the basement."
				+ "The bedroom is big and bright and has a nice parquet floor."
				+ "Possibility to keep some furnitures like the bed.";
		String roomPreferences4 = "an easy going flatmate man or woman between 20 and 30";
		
		Ad adOlten = new Ad();
		adOlten.setZipcode(4600);
		adOlten.setMoveInDate(moveInDate4);
		adOlten.setCreationDate(creationDate4);
		adOlten.setPrizePerMonth(430);
		adOlten.setSquareFootage(60);
		adOlten.setPropertyType(1);
		adOlten.setSellType(1);
		adOlten.setSmokers(true);
		adOlten.setAnimals(false);
		adOlten.setRoomDescription(studioDescription4);
		adOlten.setPreferences(roomPreferences4);
		adOlten.setRoommates("One roommate");
		adOlten.setUser(ese);
		adOlten.setTitle("Roommate wanted in Olten City");
		adOlten.setStreet("Zehnderweg 5");
		adOlten.setCity("Olten");
		adOlten.setLatitude(47.35);
		adOlten.setLongitude(7.9167);
		adOlten.setGarden(false);
		adOlten.setBalcony(true);
		adOlten.setCellar(true);
		adOlten.setFurnished(true);
		adOlten.setCable(true);
		adOlten.setGarage(false);
		adOlten.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adOlten, "/img/test/ad4_1.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_3.png"));
		adOlten.setPictures(pictures);
		adDao.save(adOlten);

		String studioDescription5 = "Studio meublé au 3ème étage, comprenant"
				+ "une kitchenette entièrement équipée (frigo, plaques,"
				+ "four et hotte), une pièce à vivre donnant sur un balcon,"
				+ "une salle de bains avec wc. Cave, buanderie et site satellite"
				+ "à disposition.";
		String roomPreferences5 = "tout le monde est bienvenu";
		
		Ad adNeuchâtel = new Ad();
		adNeuchâtel.setZipcode(2000);
		adNeuchâtel.setMoveInDate(moveInDate5);
		adNeuchâtel.setMoveOutDate(moveOutDate3);
		adNeuchâtel.setCreationDate(creationDate5);
		adNeuchâtel.setPrizePerMonth(410);
		adNeuchâtel.setSquareFootage(40);
		adNeuchâtel.setPropertyType(2);
		adNeuchâtel.setSellType(1);
		adNeuchâtel.setSmokers(true);
		adNeuchâtel.setAnimals(false);
		adNeuchâtel.setRoomDescription(studioDescription5);
		adNeuchâtel.setPreferences(roomPreferences5);
		adNeuchâtel.setRoommates("None");
		adNeuchâtel.setUser(bernerBaer);
		adNeuchâtel.setTitle("Studio extrèmement bon marché à Neuchâtel");
		adNeuchâtel.setStreet("Rue de l'Hôpital 11");
		adNeuchâtel.setCity("Neuchâtel");
		adNeuchâtel.setLatitude(46.95);
		adNeuchâtel.setLongitude(6.85);
		adNeuchâtel.setGarden(true);
		adNeuchâtel.setBalcony(false);
		adNeuchâtel.setCellar(true);
		adNeuchâtel.setFurnished(true);
		adNeuchâtel.setCable(false);
		adNeuchâtel.setGarage(false);
		adNeuchâtel.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_1.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_3.jpg"));
		adNeuchâtel.setPictures(pictures);
		adDao.save(adNeuchâtel);

		String studioDescription6 = "A place just for yourself in a very nice part of Biel."
				+ "A studio for 1-2 persons with a big balcony, bathroom, kitchen and furniture already there."
				+ "It's quiet and nice, very close to the old city of Biel.";
		String roomPreferences6 = "A nice and easy going person. Minimum rent is two months";
		
		Ad adBiel = new Ad();
		adBiel.setZipcode(2503);
		adBiel.setMoveInDate(moveInDate6);
		adBiel.setMoveOutDate(moveOutDate5);
		adBiel.setCreationDate(creationDate6);
		adBiel.setPrizePerMonth(480);
		adBiel.setSquareFootage(10);
		adBiel.setPropertyType(2);
		adBiel.setSellType(1);
		adBiel.setSmokers(true);
		adBiel.setAnimals(false);
		adBiel.setRoomDescription(studioDescription6);
		adBiel.setPreferences(roomPreferences6);
		adBiel.setRoommates("None");
		adBiel.setUser(ese);
		adBiel.setTitle("Direkt am Quai: hübsches Studio");
		adBiel.setStreet("Oberer Quai 12");
		adBiel.setCity("Biel/Bienne");
		adBiel.setLatitude(47.1324);
		adBiel.setLongitude(7.2441);
		adBiel.setGarden(false);
		adBiel.setBalcony(false);
		adBiel.setCellar(false);
		adBiel.setFurnished(false);
		adBiel.setCable(false);
		adBiel.setGarage(false);
		adBiel.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBiel, "/img/test/ad6_1.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_3.png"));
		adBiel.setPictures(pictures);
		adDao.save(adBiel);
		
		
		String roomDescription7 = "The room is a part of 3.5 rooms apartment completely renovated"
				+ "in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ "Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ "transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ "charges are separate. If you are interested, feel free to drop me a message"
				+ "to have an appointment for a visit or can write me for any further information";
		String preferences7 = "Uncomplicated, open minded and easy going person (m / w),"
				+ "non-smoker, can speak English, which of course fits in the WG, and who likes dogs."
				+ "Cleanliness is must. Apart from personal life, sometimes glass of wine,"
				+ "eat and cook together and go out in the evenings.";

		Ad adZurich = new Ad();
		adZurich.setZipcode(8001);
		adZurich.setMoveInDate(moveInDate7);
		adZurich.setCreationDate(creationDate7);
		adZurich.setMoveOutDate(moveOutDate5);
		adZurich.setPrizePerMonth(480);
		adZurich.setSquareFootage(32);
		adZurich.setPropertyType(1);
		adZurich.setSellType(1);
		adZurich.setSmokers(false);
		adZurich.setAnimals(false);
		adZurich.setRoomDescription(roomDescription7);
		adZurich.setPreferences(preferences7);
		adZurich.setRoommates("One roommate");
		adZurich.setUser(oprah);
		adZurich.setTitle("Roommate wanted in Zürich");
		adZurich.setStreet("Lintheschergasse 21");
		adZurich.setCity("Zürich");
		adZurich.setLatitude(47.3667);
		adZurich.setLongitude(8.55);
		adZurich.setGarden(false);
		adZurich.setBalcony(true);
		adZurich.setCellar(false);
		adZurich.setFurnished(true);
		adZurich.setCable(true);
		adZurich.setGarage(true);
		adZurich.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adZurich, "/img/test/ad1_3.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_1.jpg"));
		adZurich.setPictures(pictures);
		adDao.save(adZurich);
	
		
		String studioDescription8 = "It is small studio close to the"
				+ "university and the bahnhof. The lovely neighbourhood"
				+ "Langgasse makes it an easy place to feel comfortable."
				+ "The studio is close to a Migross, Denner and the Coop."
				+ "The studio is 60m2. It has it own Badroom and kitchen."
				+ "Nothing is shared. The studio is fully furnished. The"
				+ "studio is also provided with a balcony. So if you want to"
				+ "have a privat space this could totally be good place for you."
				+ "Be aware it is only till the end of March. The price is from"
				+ "550- 700 CHF, But there is always room to talk about it.";
		String roomPreferences8 = "I would like to have an easy going person who"
				+ "is trustworthy and can take care of the flat. No animals please."
				+ "Non smoker preferred.";
		
		Ad adLuzern = new Ad();
		adLuzern.setZipcode(6004);
		adLuzern.setMoveInDate(moveInDate8);
		adLuzern.setCreationDate(creationDate2);
		adLuzern.setPrizePerMonth(700);
		adLuzern.setSquareFootage(60);
		adLuzern.setPropertyType(2);
		adLuzern.setSellType(1);
		adLuzern.setSmokers(false);
		adLuzern.setAnimals(false);
		adLuzern.setRoomDescription(studioDescription8);
		adLuzern.setPreferences(roomPreferences8);
		adLuzern.setRoommates("None");
		adLuzern.setUser(oprah);
		adLuzern.setTitle("Elegant Studio in Lucerne");
		adLuzern.setStreet("Schweizerhofquai 1");
		adLuzern.setCity("Luzern");
		adLuzern.setLatitude(47.0833);
		adLuzern.setLongitude(8.2667);
		adLuzern.setGarden(false);
		adLuzern.setBalcony(false);
		adLuzern.setCellar(false);
		adLuzern.setFurnished(false);
		adLuzern.setCable(false);
		adLuzern.setGarage(false);
		adLuzern.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLuzern, "/img/test/ad2_3.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_1.jpg"));
		adLuzern.setPictures(pictures);
		adDao.save(adLuzern);

		String studioDescription9 = " In the center of Gundeli (5 Min. away from the"
				+ "station, close to Tellplatz) there is a lovely house, covered with"
				+ "savage wine stocks, without any neighbours but with a garden. The"
				+ "house has two storey, on the first floor your new room is waiting"
				+ "for you. The house is totally equipped with everything a household "
				+ ": washing machine, kitchen, batroom, W-Lan...if you don´t have any"
				+ "furniture, don´t worry, I am sure, we will find something around"
				+ "the house. The price for the room and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ "new female flatmate from December on.";
		String roomPreferences9 = "smoking female flatmate";
		
		Ad adAarau = new Ad();
		adAarau.setZipcode(5000);
		adAarau.setMoveInDate(moveInDate3);
		adAarau.setMoveOutDate(moveOutDate4);
		adAarau.setCreationDate(creationDate8);
		adAarau.setPrizePerMonth(800);
		adAarau.setSquareFootage(26);
		adAarau.setPropertyType(2);
		adAarau.setSellType(1);
		adAarau.setSmokers(true);
		adAarau.setAnimals(false);
		adAarau.setRoomDescription(studioDescription9);
		adAarau.setPreferences(roomPreferences9);
		adAarau.setRoommates("None");
		adAarau.setUser(oprah);
		adAarau.setTitle("Beautiful studio in Aarau");
		adAarau.setStreet("Jurastrasse 9");
		adAarau.setCity("Aarau");
		adAarau.setLatitude(47.3833);
		adAarau.setLongitude(8.05);
		adAarau.setGarden(false);
		adAarau.setBalcony(true);
		adAarau.setCellar(false);
		adAarau.setFurnished(true);
		adAarau.setCable(false);
		adAarau.setGarage(false);
		adAarau.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adAarau, "/img/test/ad3_3.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_3.jpg"));
		
		adAarau.setPictures(pictures);
		adDao.save(adAarau);
		
		String studioDescription10 = "Flatshare of 3 persons. Flat with 5 rooms"
				+ "on the second floor. The bedroom is about 60 square meters"
				+ "with access to a nice balcony. In addition to the room, the"
				+ "flat has: a living room, a kitchen, a bathroom, a seperate WC,"
				+ "a storage in the basement, a balcony, a laundry room in the basement."
				+ "The bedroom is big and bright and has a nice parquet floor."
				+ "Possibility to keep some furnitures like the bed.";
		String roomPreferences10 = "an easy going flatmate man or woman between 20 and 30";
		
		Ad adDavos = new Ad();
		adDavos.setZipcode(7270);
		adDavos.setMoveInDate(moveInDate2);
		adDavos.setCreationDate(creationDate4);
		adDavos.setPrizePerMonth(1100);
		adDavos.setSquareFootage(74);
		adDavos.setPropertyType(1);
		adDavos.setSellType(1);
		adDavos.setSmokers(true);
		adDavos.setAnimals(false);
		adDavos.setRoomDescription(studioDescription10);
		adDavos.setPreferences(roomPreferences10);
		adDavos.setRoommates("One roommate");
		adDavos.setUser(oprah);
		adDavos.setTitle("Free room in Davos City");
		adDavos.setStreet("Scalettastrasse 8");
		adDavos.setCity("Davos");
		adDavos.setLatitude(46.8001);
		adDavos.setLongitude(9.8309);
		adDavos.setGarden(false);
		adDavos.setBalcony(true);
		adDavos.setCellar(true);
		adDavos.setFurnished(true);
		adDavos.setCable(true);
		adDavos.setGarage(false);
		adDavos.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adDavos, "/img/test/ad4_3.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_1.png"));
		adDavos.setPictures(pictures);
		adDao.save(adDavos);

		String studioDescription11 = "Studio meublé au 3ème étage, comprenant"
				+ "une kitchenette entièrement équipée (frigo, plaques,"
				+ "four et hotte), une pièce à vivre donnant sur un balcon,"
				+ "une salle de bains avec wc. Cave, buanderie et site satellite"
				+ "à disposition.";
		String roomPreferences11 = "tout le monde est bienvenu";
		
		Ad adLausanne = new Ad();
		adLausanne.setZipcode(1004);
		adLausanne.setMoveInDate(moveInDate5);
		adLausanne.setMoveOutDate(moveOutDate3);
		adLausanne.setCreationDate(creationDate5);
		adLausanne.setPrizePerMonth(360);
		adLausanne.setSquareFootage(8);
		adLausanne.setPropertyType(1);
		adLausanne.setSellType(1);
		adLausanne.setSmokers(true);
		adLausanne.setAnimals(false);
		adLausanne.setRoomDescription(studioDescription11);
		adLausanne.setPreferences(roomPreferences11);
		adLausanne.setRoommates("None");
		adLausanne.setUser(oprah);
		adLausanne.setTitle("Studio extrèmement bon marché à Lausanne");
		adLausanne.setStreet("Avenue Recordon 46");
		adLausanne.setCity("Lausanne");
		adLausanne.setLatitude(46.5333);
		adLausanne.setLongitude(6.6667);
		adLausanne.setGarden(true);
		adLausanne.setBalcony(false);
		adLausanne.setCellar(true);
		adLausanne.setFurnished(true);
		adLausanne.setCable(false);
		adLausanne.setGarage(false);
		adLausanne.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLausanne, "/img/test/ad5_3.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_1.jpg"));
		adLausanne.setPictures(pictures);
		adDao.save(adLausanne);

		String studioDescription12 = "A place just for yourself in a very nice part of Biel."
				+ "A studio for 1-2 persons with a big balcony, bathroom, kitchen and furniture already there."
				+ "It's quiet and nice, very close to the old city of Biel.";
		String roomPreferences12 = "A nice and easy going person. Minimum rent is two months";
		
		Ad adLocarno = new Ad();
		adLocarno.setZipcode(6600);
		adLocarno.setMoveInDate(moveInDate6);
		adLocarno.setMoveOutDate(moveOutDate5);
		adLocarno.setCreationDate(creationDate6);
		adLocarno.setPrizePerMonth(960);
		adLocarno.setSquareFootage(42);
		adLocarno.setPropertyType(1);
		adLocarno.setSellType(1);
		adLocarno.setSellType(1);
		adLocarno.setSmokers(true);
		adLocarno.setAnimals(false);
		adLocarno.setRoomDescription(studioDescription12);
		adLocarno.setPreferences(roomPreferences12);
		adLocarno.setRoommates("None");
		adLocarno.setUser(jane);
		adLocarno.setTitle("Malibu-style Beachhouse");
		adLocarno.setStreet("Via Giovanni Antonio Orelli 14");
		adLocarno.setCity("Locarno");
		adLocarno.setLatitude(46.1667);
		adLocarno.setLongitude(8.8);
		adLocarno.setGarden(false);
		adLocarno.setBalcony(false);
		adLocarno.setCellar(false);
		adLocarno.setFurnished(false);
		adLocarno.setCable(false);
		adLocarno.setGarage(false);
		adLocarno.setInternet(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLocarno, "/img/test/ad6_3.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_1.png"));
		adLocarno.setPictures(pictures);
		adDao.save(adLocarno);
		
		String HouseDescription13 = "This is a huge house with several kitchens, bathrooms and a large living room. "
				+ "Several 1000 square meters of space should be enough for even the most severe cases of claustrophobia. "
				+ "It's located at Güterstrasse 14, 3550 Langnau, only about 200 meters away from the train station. "
				+ "The only downside is that some of the floor is covered in ice, so it might get a bit frosty sometimes. "
				+ "As you can see in the third picture, it was just newly renovated in 2012. ";
		String preferences13 = "I will auction this off to literally anyone";

		Ad adLangnau = new Ad();
		adLangnau.setZipcode(3550);
		adLangnau.setMoveInDate(moveInDate9);
		adLangnau.setMoveOutDate(moveOutDate6);
		adLangnau.setCreationDate(creationDate9);
		adLangnau.setAuctionEndDate(auctionEndDate1);
		adLangnau.setStartOffer(100);
		adLangnau.setPrizePerMonth(100);
		adLangnau.setSquareFootage(10000);
		adLangnau.setPropertyType(4);
		adLangnau.setSellType(3);
		adLangnau.setSmokers(true);
		adLangnau.setAnimals(true);
		adLangnau.setRoomDescription(HouseDescription13);
		adLangnau.setPreferences(preferences13);
		adLangnau.setRoommates("Sometimes up to 6000");
		adLangnau.setUser(bernerBaer);
		adLangnau.setTitle("Huge mansion to sell by auction");
		adLangnau.setStreet("Güterstrasse 14");
		adLangnau.setCity("Langnau");
		adLangnau.setLatitude(46.9333);
		adLangnau.setLongitude(7.8444);
		adLangnau.setGarden(false);
		adLangnau.setBalcony(true);
		adLangnau.setCellar(true);
		adLangnau.setFurnished(true);
		adLangnau.setCable(true);
		adLangnau.setGarage(true);
		adLangnau.setInternet(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLangnau, "/img/test/ad9_1.png"));
		pictures.add(createPicture(adLangnau, "/img/test/ad9_2.jpg"));
		pictures.add(createPicture(adLangnau, "/img/test/ad9_3.jpg"));
		adLangnau.setPictures(pictures);
		adDao.save(adLangnau);

	}

	private AdPicture createPicture(Ad ad, String filePath) {
		AdPicture picture = new AdPicture();
		picture.setFilePath(filePath);
		return picture;
	}

}