package ch.unibe.ese.team4.controller.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team4.controller.pojos.forms.AlertForm;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Alert;
import ch.unibe.ese.team4.model.Location;
import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.MessageState;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AlertDao;
import ch.unibe.ese.team4.model.dao.MessageDao;
import ch.unibe.ese.team4.model.dao.UserDao;

/**
 * Provides and handles persistence operations for adding, editing and deleting
 * alerts.
 */
@Service
public class AlertService {

	@Autowired
	UserDao userDao;

	@Autowired
	AlertDao alertDao;

	@Autowired
	MessageDao messageDao;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Persists a new alert with the data from the alert form to the database.
	 * 
	 * @param alertForm
	 *            the form to take the data from
	 * @param user
	 *            the user to associate the new alert to
	 */
	@Transactional
	public void saveFrom(AlertForm alertForm, User user) {
		Alert alert = new Alert();

		String zip = alertForm.getCity().substring(0, 4);
		alert.setZipcode(Integer.parseInt(zip));
		alert.setCity(alertForm.getCity().substring(7));
		alert.setPrice(alertForm.getPrize());
		alert.setRadius(alertForm.getRadius());
		alert.setSquareFootage(alertForm.getSquareFootage());
		alert.setUser(user);
		alert.setPropertyType(alertForm.getPropertyType());
		alert.setSellType(alertForm.getSellType());
		
		// ad description values

		alert.setBalcony(alertForm.getBalcony());
		alert.setCable(alertForm.getCable());
		alert.setCellar(alertForm.getCellar());
		alert.setGarage(alertForm.getGarage());
		alert.setGarden(alertForm.getGarden());
		alert.setInternet(alertForm.getInternet());
		alert.setAnimals(alertForm.getAnimals());
		alert.setFurnished(alertForm.getFurnished());
		alert.setSmokers(alertForm.getSmokers());
		alert.setDishwasher(alertForm.getDishwasher());
		alert.setWashingMachine(alertForm.getWashingMachine());
		
		
		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12

		try {
			if (alertForm.getMoveInDate().length() >= 1) {
				int dayMoveIn = Integer.parseInt(alertForm.getMoveInDate()
						.substring(0, 2));
				int monthMoveIn = Integer.parseInt(alertForm.getMoveInDate()
						.substring(3, 5));
				int yearMoveIn = Integer.parseInt(alertForm.getMoveInDate()
						.substring(6, 10));
				calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn);
				alert.setMoveInDate(calendar.getTime());
				} 
				
			if (alertForm.getMoveOutDate().length() >= 1) {
				int dayMoveOut = Integer.parseInt(alertForm.getMoveOutDate()
						.substring(0, 2));
				int monthMoveOut = Integer.parseInt(alertForm
						.getMoveOutDate().substring(3, 5));
				int yearMoveOut = Integer.parseInt(alertForm.getMoveOutDate()
						.substring(6, 10));
				calendar.set(yearMoveOut, monthMoveOut - 1, dayMoveOut);
				alert.setMoveOutDate(calendar.getTime());
			}
		} catch (NumberFormatException e) {
		}


		alertDao.save(alert);
	}

	/**
	 * Returns all alerts that belong to the given user.
	 */
	@Transactional
	public Iterable<Alert> getAlertsByUser(User user) {
		return alertDao.findByUser(user);
	}

	/** Deletes the alert with the given id. */
	@Transactional
	public void deleteAlert(Long id) {
		alertDao.delete(id);
	}

	/**
	 * Triggers all alerts that match the given ad. For every user, only one
	 * message is sent.
	 */
	@Transactional
	public void triggerAlerts(Ad ad) {
		
		if(ad.getSellType() == 1) {
			int adPrice = ad.getPrizePerMonth();
			Iterable<Alert> alerts = alertDao.findByPriceGreaterThan(adPrice-1);

			// loop through all ads with matching city and price range, throw out
			// mismatches
			Iterator<Alert> alertIterator = alerts.iterator();
			while (alertIterator.hasNext()) {
				Alert alert = alertIterator.next();
				if (typeMismatchWith(ad, alert) || radiusMismatchWith(ad, alert)
						|| sizeMismatchWith(ad, alert) || propertyMismatchWith(ad, alert)
						|| dateMismatchWith(ad, alert)
						|| ad.getUser().equals(alert.getUser()))
					alertIterator.remove();
			}
			
			// send only one message per user, no matter how many alerts were
			// triggered
			List<User> users = new ArrayList<User>();
			for (Alert alert : alerts) {
				User user = alert.getUser();
				if (!users.contains(user)) {
					users.add(user);
				}
			}

			// send messages to all users with matching alerts
			for (User user : users) {
					Date now = new Date();
					Message message = new Message();
					message.setSubject("It's a match!");
					message.setText(getAlertText(ad));
					message.setSender(userDao.findByUsername("System"));
					message.setRecipient(user);
					message.setState(MessageState.UNREAD);
					message.setDateSent(now);
					if(user.isPremium()){
						messageDao.save(message);
					}
					else{
						PremiumService.setMessage(message);
					}

			}
		} else if(ad.getSellType() == 2) {
			int adPrice = ad.getPrizeOfSale();
			Iterable<Alert> alerts = alertDao.findByPriceGreaterThan(adPrice-1);

			// loop through all ads with matching city and price range, throw out
			// mismatches
			Iterator<Alert> alertIterator = alerts.iterator();
			while (alertIterator.hasNext()) {
				Alert alert = alertIterator.next();
				if (typeMismatchWith(ad, alert) || radiusMismatchWith(ad, alert)
						|| sizeMismatchWith(ad, alert) || propertyMismatchWith(ad, alert)
						|| dateMismatchWith(ad, alert)
						|| ad.getUser().equals(alert.getUser()))
					alertIterator.remove();
			}
			
			// send only one message per user, no matter how many alerts were
			// triggered
			List<User> users = new ArrayList<User>();
			for (Alert alert : alerts) {
				User user = alert.getUser();
				if (!users.contains(user)) {
					users.add(user);
				}
			}

			// send messages to all users with matching alerts
			for (User user : users) {
				Date now = new Date();
				Message message = new Message();
				message.setSubject("It's a match!");
				message.setText(getAlertText(ad));
				message.setSender(userDao.findByUsername("System"));
				message.setRecipient(user);
				message.setState(MessageState.UNREAD);
				message.setDateSent(now);
				if(user.isPremium()){
					messageDao.save(message);
				}
				else{
					PremiumService.setMessage(message);
				}
			}
		} 
		else {
			int adPrice = ad.getStartOffer();
			Iterable<Alert> alerts = alertDao.findByPriceGreaterThan(adPrice-1);

			// loop through all ads with matching city and price range, throw out
			// mismatches
			Iterator<Alert> alertIterator = alerts.iterator();
			while (alertIterator.hasNext()) {
				Alert alert = alertIterator.next();
				if (typeMismatchWith(ad, alert) || radiusMismatchWith(ad, alert)
						|| sizeMismatchWith(ad, alert) || propertyMismatchWith(ad, alert)
						|| dateMismatchWith(ad, alert)
						|| ad.getUser().equals(alert.getUser()))
					alertIterator.remove();
			}
			
			// send only one message per user, no matter how many alerts were
			// triggered
			List<User> users = new ArrayList<User>();
			for (Alert alert : alerts) {
				User user = alert.getUser();
				if (!users.contains(user)) {
					users.add(user);
				}
			}

			// send messages to all users with matching alerts
			for (User user : users) {
				Date now = new Date();
				Message message = new Message();
				message.setSubject("It's a match!");
				message.setText(getAlertText(ad));
				message.setSender(userDao.findByUsername("System"));
				message.setRecipient(user);
				message.setState(MessageState.UNREAD);
				message.setDateSent(now);
				if(user.isPremium()){
					messageDao.save(message);
				}
				else{
					PremiumService.setMessage(message);
				}
			}
		}
	}
		
	/**
	 * Returns the text for an alert message with the properties of the given
	 * ad.
	 */
	private String getAlertText(Ad ad) {
		return "Dear user,<br>good news. A new ad matching one of your alerts has been "
				+ "entered into our system. You can visit it here:<br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "Good luck and enjoy,<br>"
				+ "Your FlatFindr crew";
	}

	/** Checks if an ad is conforming to the criteria in an alert. */
	private boolean typeMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if (ad.getPropertyType() != alert.getPropertyType() || ad.getSellType() != alert.getSellType()) {
			mismatch = true;
		}
		return mismatch;
	}
	

	/**
	 * Checks whether an ad is for a place too far away from the alert.
	 * 
	 * @param ad
	 *            the ad to compare to the alert
	 * @param alert
	 *            the alert to compare to the ad
	 * 
	 * @return true in case the alert does not match the ad (the ad is too far
	 *         away), false otherwise
	 */
	private boolean radiusMismatchWith(Ad ad, Alert alert) {
		final int earthRadiusKm = 6380;
		Location adLocation = geoDataService.getLocationsByCity(ad.getCity())
				.get(0);
		Location alertLocation = geoDataService.getLocationsByCity(
				alert.getCity()).get(0);

		double radSinLat = Math.sin(Math.toRadians(adLocation.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(adLocation.getLatitude()));
		double radLong = Math.toRadians(adLocation.getLongitude());
		double radLongitude = Math.toRadians(alertLocation.getLongitude());
		double radLatitude = Math.toRadians(alertLocation.getLatitude());
		double distance = Math.acos(radSinLat * Math.sin(radLatitude)
				+ radCosLat * Math.cos(radLatitude)
				* Math.cos(radLong - radLongitude))
				* earthRadiusKm;
		return (distance > alert.getRadius());
	}
	
	/** Checks if an ad is conforming to the size in an alert. */
	private boolean sizeMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		double sizeAd = ad.getSquareFootage();
		double desiredSize = alert.getSquareFootage();
		
		double sizeDifference = Math.abs(sizeAd - desiredSize);
		if(sizeDifference >= 20) {
			mismatch = true;
		}
		return  mismatch;
	}
	
	/** Checks if an ad is conforming to the properties in an alert. */
	private boolean propertyMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if(ad.getAnimals() != alert.getAnimals() || ad.getBalcony() != alert.getBalcony() 
				|| ad.getCable() != alert.getCable() || ad.getCellar() != alert.getCellar()
				|| ad.getFurnished() != alert.getFurnished() || ad.getGarage() != alert.getGarage()
				|| ad.getGarden() != alert.getGarden() || ad.getInternet() != alert.getInternet()
				|| ad.getSmokers() != alert.getSmokers() || ad.getDishwasher() != alert.getDishwasher()
				|| ad.getWashingMachine() != alert.getWashingMachine()) {
			mismatch = true;
		}
		return mismatch;
	}
	
	/** Checks if an ad is conforming to the date in an alert. */
	private boolean dateMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		
		if(ad.getMoveInDate() != null & alert.getMoveInDate() != null) {
			long moveInDateAd = ad.getMoveInDate().getTime();
			long moveInDateAlert = alert.getMoveInDate().getTime();
			
			if (ad.getMoveOutDate() != null & alert.getMoveOutDate() != null ) {
				long moveOutDateAd = ad.getMoveOutDate().getTime();
				long moveOutDateAlert = alert.getMoveOutDate().getTime();

				if(moveOutDateAlert < moveOutDateAd && moveInDateAlert > moveInDateAd) {
					mismatch = true;
				}
			}
			if (moveInDateAlert > moveInDateAd) {
				mismatch = true;
			}
		} 
		return mismatch;
	}

	
	//for testing
	public boolean radiusMismatch(Ad ad, Alert alert) {
		return radiusMismatchWith(ad, alert);
	}
	
	//for testing
	public boolean typeMismatch(Ad ad, Alert alert) {
		return typeMismatchWith(ad, alert);
	}
	
	
}