package ch.unibe.ese.team1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** Describes an advertisement that users can place and search for. */
@Entity
public class BidHistory {

	@Id
	@GeneratedValue
	private long id;
}
