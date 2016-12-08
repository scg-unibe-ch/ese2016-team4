package ch.unibe.ese.team4.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

;

/**
 * Describes an alert. An alert can be created by a user. If ads matching the
 * criteria of the alert are added to the platform later, the user will be
 * notified.
 */
@Entity
public class Alert {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private User user;

	@Column(nullable = true)
	private int sellType;

	@Column(nullable = true)
	private int propertyType;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date moveInDate;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date moveOutDate;

	@Column(nullable = true)
	private int zipcode;
	
	@Column(nullable = true)
	private int squareFootage;

	@Column(nullable = true)
	private String city;

	@Column(nullable = true)
	private int price;

	@Column(nullable = true)
	private int radius;
	
	@Column(nullable = true)
	private boolean smokers;

	@Column(nullable = true)
	private boolean animals;

	@Column(nullable = true)
	private boolean garden;

	@Column(nullable = true)
	private boolean balcony;

	@Column(nullable = true)
	private boolean cellar;

	@Column(nullable = true)
	private boolean furnished;

	@Column(nullable = true)
	private boolean cable;

	@Column(nullable = true)
	private boolean garage;

	@Column(nullable = true)
	private boolean internet;
	
	@Column(nullable = false)
	private boolean dishwasher;
	
	@Column(nullable = false)
	private boolean washingMachine;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public int getSellType() {
		return sellType;
	}

	public void setSellType(int sellType) {
		this.sellType = sellType;
	}

	public int getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(int propertyType) {
		this.propertyType = propertyType;
	}
	
	public Date getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(Date moveInDate) {
		this.moveInDate = moveInDate;
	}
	
	public Date getMoveOutDate() {
		return moveOutDate;
	}
	

	public void setMoveOutDate(Date moveOutDate) {
		this.moveOutDate = moveOutDate;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}

	public boolean getSmokers() {
		return smokers;
	}

	public void setSmokers(boolean allowsSmokers) {
		this.smokers = allowsSmokers;
	}

	public boolean getAnimals() {
		return animals;
	}

	public void setAnimals(boolean allowsAnimals) {
		this.animals = allowsAnimals;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean hasGarden) {
		this.garden = hasGarden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean hasBalcony) {
		this.balcony = hasBalcony;
	}

	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean hasCellar) {
		this.cellar = hasCellar;
	}

	public boolean getFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean getCable() {
		return cable;
	}

	public void setCable(boolean hasCable) {
		this.cable = hasCable;
	}

	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public boolean getInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}
	
	public boolean getDishwasher() {
		return dishwasher;
	}
	
	public void setDishwasher(boolean dishwasher) {
		this.dishwasher = dishwasher;
	}
	
	public boolean getWashingMachine() {
		return washingMachine;
	}
	
	public void setWashingMachine(boolean washingMachine) {
		this.washingMachine = washingMachine;
	}

}
