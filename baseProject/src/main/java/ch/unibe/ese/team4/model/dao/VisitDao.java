package ch.unibe.ese.team4.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Visit;

public interface VisitDao extends CrudRepository<Visit, Long> {
	public Iterable<Visit> findByAd(Ad ad);
}
