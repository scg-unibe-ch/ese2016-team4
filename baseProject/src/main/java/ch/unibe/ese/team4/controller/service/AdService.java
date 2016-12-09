package ch.unibe.ese.team4.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import ch.unibe.ese.team4.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team4.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.AdPicture;
import ch.unibe.ese.team4.model.Location;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.Visit;
import ch.unibe.ese.team4.model.dao.AdDao;

/** Handles all persistence operations concerning ad placement and retrieval. */
/**
 * @author enicath
 *
 */
/**
 * @author enicath
 *
 */
@Service
public class AdService {

	@Autowired
	private AdDao adDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private BidService bidService;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Handles persisting a new ad to the database.
	 *
	 * @param placeAdForm
	 *            the form to take the data from
	 * @param a
	 *            list of the file paths the pictures are saved under
	 * @param the
	 *            currently logged in user
	 */
	@Transactional
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths,
			User user) {

		Ad ad = new Ad();

		Date now = new Date();
		ad.setCreationDate(now);

		ad.setSellType(placeAdForm.getSellType());
		ad.setPropertyType(placeAdForm.getPropertyType());

		ad.setTitle(placeAdForm.getTitle());

		ad.setStreet(placeAdForm.getStreet());

		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));


		if(stringToDate(placeAdForm.getAuctionEndDate()) != null){
			Date auctionEndDate = stringToDate(placeAdForm.getAuctionEndDate());
			ad.setAuctionEndDate(auctionEndDate);
		}

		if(stringToDate(placeAdForm.getMoveInDate()) != null){
			Date moveInDate = stringToDate(placeAdForm.getMoveInDate());
			ad.setMoveInDate(moveInDate);
		}
		if(stringToDate(placeAdForm.getMoveOutDate()) != null){
			Date moveOutDate = stringToDate(placeAdForm.getMoveOutDate());
			ad.setMoveOutDate(moveOutDate);
		}

		ad.setPrizePerMonth(placeAdForm.getPrize());

		// new for buy and auction
		ad.setPrizeOfSale(placeAdForm.getPrizeOfSale());
		ad.setStartOffer(placeAdForm.getStartOffer());

		ad.setSquareFootage(placeAdForm.getSquareFootage());

		ad.setRoomDescription(placeAdForm.getRoomDescription());
		ad.setPreferences(placeAdForm.getPreferences());
		ad.setRoommates(placeAdForm.getRoommates());
		
		// ad coordinates
		double[] coordinates = findCoords(ad);
		ad.setLatitude(coordinates[0]);
		ad.setLongitude(coordinates[1]);		
		
		// ad description values
		ad.setSmokers(placeAdForm.isSmokers());
		ad.setAnimals(placeAdForm.isAnimals());
		ad.setGarden(placeAdForm.getGarden());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setCellar(placeAdForm.getCellar());
		ad.setFurnished(placeAdForm.isFurnished());
		ad.setCable(placeAdForm.getCable());
		ad.setGarage(placeAdForm.getGarage());
		ad.setInternet(placeAdForm.getInternet());
		ad.setDishwasher(placeAdForm.getDishwasher());
		ad.setWashingMachine(placeAdForm.getWashingMachine());

		/*
		 * Save the paths to the picture files, the pictures are assumed to be
		 * uploaded at this point!
		 */
		List<AdPicture> pictures = new ArrayList<>();
		for (String filePath : filePaths) {
			AdPicture picture = new AdPicture();
			picture.setFilePath(filePath);
			pictures.add(picture);
		}
		ad.setPictures(pictures);

		/*
		 * Roommates are saved in the form as strings. They need to be converted
		 * into Users and saved as a List which will be accessible through the
		 * ad object itself.
		 */
		List<User> registeredUserRommates = new LinkedList<>();
		if (placeAdForm.getRegisteredRoommateEmails() != null) {
			for (String userEmail : placeAdForm.getRegisteredRoommateEmails()) {
				User roommateUser = userService.findUserByUsername(userEmail);
				registeredUserRommates.add(roommateUser);
			}
		}
		ad.setRegisteredRoommates(registeredUserRommates);

		// visits
		List<Visit> visits = new LinkedList<>();
		List<String> visitStrings = placeAdForm.getVisits();
		if (visitStrings != null) {
			for (String visitString : visitStrings) {
				Visit visit = new Visit();
				// format is 28-02-2014;10:02;13:14
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				String[] parts = visitString.split(";");
				String startTime = parts[0] + " " + parts[1];
				String endTime = parts[0] + " " + parts[2];
				Date startDate = null;
				Date endDate = null;
				try {
					startDate = dateFormat.parse(startTime);
					endDate = dateFormat.parse(endTime);
				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				visit.setStartTimestamp(startDate);
				visit.setEndTimestamp(endDate);
				visit.setAd(ad);
				visits.add(visit);
			}
			ad.setVisits(visits);
		}

		ad.setUser(user);
		System.out.println(ad.getCreationDate());
		adDao.save(ad);

		return ad;
	}

	/**
	 * Gets the ad that has the given id.
	 *
	 * @param id
	 *            the id that should be searched for
	 * @return the found ad or null, if no ad with this id exists
	 */
	@Transactional
	public Ad getAdById(long id) {
		return adDao.findOne(id);
	}

	/** Returns all ads in the database */
	@Transactional
	public Iterable<Ad> getAllAds() {
		return adDao.findAll();
	}

	/**
	 * Returns the newest ads from the database
	 * @param number - how many ads
	 * @param premium - return only premium or all ads
	 * @return iterable of ads
	 */
	@Transactional
	public Iterable<Ad> getNewestAds(int number, boolean premium) {
		Iterable<Ad> allAds = adDao.findAll();
		List<Ad> ads = new ArrayList<Ad>();
		for (Ad ad : allAds)
			ads.add(ad);
		Collections.sort(ads, new Comparator<Ad>() {
			@Override
			public int compare(Ad ad1, Ad ad2) {
				return ad2.getCreationDate().compareTo(ad1.getCreationDate());
			}
		});
		if(premium)
			ads = (List<Ad>) sortByPremiumFirst(ads);
		List<Ad> fourNewest = new ArrayList<Ad>();
		for (int i = 0; i < number; i++)
			fourNewest.add(ads.get(i));
		return fourNewest;
	}

	/**
	 * Returns all ads that match the parameters given by the form. This list
	 * can possibly be empty.
	 *
	 * @param searchForm
	 *            the form to take the search parameters from
	 * @return an Iterable of all search results
	 */
	@Transactional
	public Iterable<Ad> queryResults(SearchForm searchForm) {


		// filter out zipcode
		String city = searchForm.getCity().substring(7);

		// get the location that the user searched for and take the one with the
		// lowest zip code
		Location searchedLocation = geoDataService.getLocationsByCity(city)
				.get(0);

		// create a list of the results and of their locations. Adds together the lists of the different properties
		List<Ad> locatedResults = new ArrayList<>();

		//loop through every propertytype/selltype combination, add the matching ads to the list
		for (int property : searchForm.getPropertyType()){
			for (int sell : searchForm.getSellType()){
				for (Ad ad : adDao.findByPropertyTypeAndSellType(property, sell)){
					int price = -1;
					switch(ad.getSellType()){
						case Ad.RENT:
							if (ad.getPrizePerMonth()<searchForm.getPrize()+1){
								price = ad.getPrizePerMonth();
							}
							break;
						case Ad.BUY:
							if (ad.getPrizePerMonth()<searchForm.getPrize()+1){
								price = ad.getPrizePerMonth();
							}
							break;
						case Ad.AUCTION:
							if (bidService.getHighestBid(ad.getId())<searchForm.getPrize()+1){
								price = (int) bidService.getHighestBid(ad.getId());
							}
							break;
					}
					if (price >= 0)
						locatedResults.add(ad);
				}
			}

		}

		final int earthRadiusKm = 6380;
		List<Location> locations = geoDataService.getAllLocations();
		double radSinLat = Math.sin(Math.toRadians(searchedLocation
				.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(searchedLocation
				.getLatitude()));
		double radLong = Math.toRadians(searchedLocation.getLongitude());

		/*
		 * calculate the distances (Java 8) and collect all matching zipcodes.
		 * The distance is calculated using the law of cosines.
		 * http://www.movable-type.co.uk/scripts/latlong.html
		 */
		List<Integer> zipcodes = locations
				.parallelStream()
				.filter(location -> {
					double radLongitude = Math.toRadians(location
							.getLongitude());
					double radLatitude = Math.toRadians(location.getLatitude());
					double distance = Math.acos(radSinLat
							* Math.sin(radLatitude) + radCosLat
							* Math.cos(radLatitude)
							* Math.cos(radLong - radLongitude))
							* earthRadiusKm;
					return distance < searchForm.getRadius();
				}).map(location -> location.getZip())
				.collect(Collectors.toList());

		locatedResults = locatedResults.stream()
				.filter(ad -> zipcodes.contains(ad.getZipcode()))
				.collect(Collectors.toList());

			// prepare date filtering - by far the most difficult filter
			Date earliestInDate = null;
			Date latestInDate = null;
			Date earliestOutDate = null;
			Date latestOutDate = null;

			// parse move-in and move-out dates
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				earliestInDate = formatter.parse(searchForm.getEarliestMoveInDate());

			} catch (Exception e) {
			}
			try {
				latestInDate = formatter
						.parse(searchForm.getLatestMoveInDate());
			} catch (Exception e) {
			}
			try {
				earliestOutDate = formatter.parse(searchForm
						.getEarliestMoveOutDate());
			} catch (Exception e) {
			}
			try {
				latestOutDate = formatter.parse(searchForm
						.getLatestMoveOutDate());
			} catch (Exception e) {
			}

			// filtering by dates, this removes the ads that don't match the searchdates
			locatedResults = validateDate(locatedResults, earliestInDate, latestInDate, earliestOutDate, latestOutDate);

			// filtering for the rest
			// smokers
			if (searchForm.getSmokers()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getSmokers())
						iterator.remove();
				}
			}

			// animals
			if (searchForm.getAnimals()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getAnimals())
						iterator.remove();
				}
			}

			// garden
			if (searchForm.getGarden()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getGarden())
						iterator.remove();
				}
			}

			// balcony
			if (searchForm.getBalcony()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getBalcony())
						iterator.remove();
				}
			}

			// cellar
			if (searchForm.getCellar()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getCellar())
						iterator.remove();
				}
			}

			// furnished
			if (searchForm.getFurnished()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getFurnished())
						iterator.remove();
				}
			}

			// cable
			if (searchForm.getCable()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getCable())
						iterator.remove();
				}
			}

			// garage
			if (searchForm.getGarage()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getGarage())
						iterator.remove();
				}
			}

			// internet
			if (searchForm.getInternet()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getInternet())
						iterator.remove();
				}
			}
			
			// dishwasher
			if (searchForm.getDishwasher()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getInternet())
						iterator.remove();
				}
			}
			
			// washing machine
			if (searchForm.getWashingMachine()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getInternet())
						iterator.remove();
				}
			}
		searchForm.deleteLists();
		return locatedResults;
	}
	
	/**
	 * @param ads - list with all the ads currently matching the search
	 * @param earliestIn - earliest move-in-date
	 * @param latestIn - lastest move-in-date
	 * @param earliestOut - earliest move-out-date
	 * @param latestOut - lastest move-out-date
	 * @return ads - list with all the ads removed, that don't match the search dates
	 */
	private List<Ad> validateDate(List<Ad> ads, Date earliestIn, Date latestIn, Date earliestOut, Date latestOut) {

		if (ads.size() > 0) {
			Iterator<Ad> adIterator = ads.iterator();
			//iterate through all the ads in the list
			while (adIterator.hasNext()) {
				Ad ad = adIterator.next();
				if(ad.getMoveInDate() != null){
					if(earliestIn != null){
						if(ad.getMoveInDate().compareTo(earliestIn) < 0){ 
							adIterator.remove(); 
							//jump to the next iterator, since this one has already been removed
							continue;
						}
					}
					if(latestIn != null){
						if(ad.getMoveInDate().compareTo(latestIn) > 0) {
							adIterator.remove(); 
							continue;
						}
					}
				}
				if(ad.getMoveOutDate() != null){
					if(earliestOut != null){
						if(ad.getMoveOutDate().compareTo(earliestOut) < 0){ 
							adIterator.remove(); 
							continue;
						}
					}
					if(latestOut != null){
						if(ad.getMoveOutDate().compareTo(latestOut) > 0) adIterator.remove();
					}	
				}
			}
		}
		return ads;
	}

	/** Returns all ads that were placed by the given user. */
	public Iterable<Ad> getAdsByUser(User user) {
		return adDao.findByUser(user);
	}

	/**
	 * Checks if the email of a user is already contained in the given string.
	 *
	 * @param email
	 *            the email string to search for
	 * @param alreadyAdded
	 *            the string of already added emails, which should be searched
	 *            in
	 *
	 * @return true if the email has been added already, false otherwise
	 */
	public Boolean checkIfAlreadyAdded(String email, String alreadyAdded) {
		email = email.toLowerCase();
		alreadyAdded = alreadyAdded.replaceAll("\\s+", "").toLowerCase();
		String delimiter = "[:;]+";
		String[] toBeTested = alreadyAdded.split(delimiter);
		for (int i = 0; i < toBeTested.length; i++) {
			if (email.equals(toBeTested[i])) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * gets the coordinations of an Ad by a google geocode api request
	 * @param ad The Ad to get the coords from
	 * @return size 2 double array with longitude [0] and latitude [1] of the Ad
	 */
	public double[] findCoords(Ad ad){
		double coords[] = new double[2];
		
		String id = Long.toString(ad.getId());
		if(ad.getId() < 10) id = "0" + id; 
		String adAddress = id + " " + ad.getStreet() +" "+ ad.getZipcode() + " CH";
		
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCdNwB8auysJ8k7gqiKOpLwFyV2L7iBneo");
		GeocodingResult[] result = null;
		double latitude;
		double longitude;
		try {
			result =  GeocodingApi.geocode(context,
				    adAddress.substring(3)).await();
		} catch (Exception e) {
			coords[0] = 0;
			coords[1] = 0;
			return coords;
		}
			
		System.out.println(result.toString());
		if(result.length > 0 && result[0] != null && !result[0].partialMatch){
			
			latitude = result[0].geometry.location.lat;
			longitude = result[0].geometry.location.lng;
			
			ad.setLatitude(latitude);
			ad.setLongitude(longitude);
			
			coords[0] = latitude; 
			coords[1] = longitude;	
		}
		
		else{
			coords[0] = 0;
			coords[1] = 0;
		}
			
		return coords;
	}
	

	/** converts a String to a dd.MM.yyyy HH:mm:ss Date*/
	public Date stringToDate(String stringDate){
			SimpleDateFormat formatterTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			Date dateNow = new Date();
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(dateNow);
			int HH = calendar.get(Calendar.HOUR_OF_DAY);
			int mm = calendar.get(Calendar.MINUTE);
			int ss = calendar.get(Calendar.SECOND);
			Date date;
			try {
				date = formatterTime.parse(stringDate + " "+ HH + ":" + mm + ":" + ss);
				return date;
			} catch (ParseException e) {
				return null;
			}
	}
	
	/**
	 * sorts an Iterable by placing premium Ads in the first place
	 * @param addressesToSort The Iterable to get sorted
	 * @return sorted Iterable with Premium Ads in the first place
	 */
	public Iterable<Ad> sortByPremiumFirst(Iterable<Ad> addressesToSort){
		ArrayList<Ad> premiumFirst = new ArrayList<>();
		for(Ad adToSort : addressesToSort){
			if (adToSort.getUser().isPremium()){
				premiumFirst.add(adToSort);
			}
		}
		for(Ad adToSort : addressesToSort){
			if (!adToSort.getUser().isPremium()){
				premiumFirst.add(adToSort);
			}
		}
		return premiumFirst;
	}
}
