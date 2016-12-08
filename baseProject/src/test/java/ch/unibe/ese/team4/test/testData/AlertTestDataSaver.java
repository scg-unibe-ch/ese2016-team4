package ch.unibe.ese.team4.test.testData;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team4.model.Alert;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AlertDao;
import ch.unibe.ese.team4.model.dao.UserDao;

/**
 * This inserts some alert test data into the database.
 */
@Service
public class AlertTestDataSaver {

	@Autowired
	private AlertDao alertDao;
	
	@Autowired
	private UserDao userDao;


	@Transactional
	public void saveTestData() throws Exception {
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Date moveInDate1 = formatter.parse("24.12.2016");
		Date moveInDate2 = formatter.parse("01.01.2017");
		Date moveOutDate1 = formatter.parse("01.01.2020");
		Date moveOutDate2 = formatter.parse("01.01.2025");


		// 2 Alerts for the ese test-user
		Alert alert = new Alert();
		alert.setUser(ese);
		alert.setPropertyType(1);
		alert.setSellType(1);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setMoveInDate(moveInDate1);
		alert.setMoveOutDate(moveOutDate1);
		alert.setAnimals(true);
		alert.setRadius(30);
		alert.setSquareFootage(100);
		alertDao.save(alert);
		
		alert = new Alert();
		alert.setUser(ese);
		alert.setPropertyType(2);
		alert.setSellType(2);
		alert.setCity("Zürich");
		alert.setZipcode(8000);
		alert.setPrice(1000);
		alert.setMoveInDate(moveInDate2);
		alert.setMoveOutDate(moveOutDate2);
		alert.setInternet(true);
		alert.setGarden(true);
		alert.setRadius(25);
		alert.setSquareFootage(120);
		alertDao.save(alert);
		
		
		// One alert for Jane Doe
		alert = new Alert();
		alert.setUser(jane);
		alert.setPropertyType(3);
		alert.setSellType(3);
		alert.setCity("Luzern");
		alert.setZipcode(6003);
		alert.setPrice(900);
		alert.setRadius(22);
		alertDao.save(alert);
	}

}
