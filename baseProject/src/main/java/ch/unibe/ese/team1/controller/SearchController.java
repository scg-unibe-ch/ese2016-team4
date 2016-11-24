package ch.unibe.ese.team1.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.BidService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;

/** Handles all requests concerning the search for ads. */
@Controller
public class SearchController {

	@Autowired
	private AdService adService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private BidService bidService;

	/**
	 * The search form that is used for searching. It is saved between request
	 * so that users don't have to enter their search parameters multiple times.
	 */
	private SearchForm searchForm;

	/** Shows the search ad page. */
	@RequestMapping(value = "/searchAd", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView searchAd() {
		ModelAndView model = new ModelAndView("searchAd");
		return model;
	}

	
	@RequestMapping(value="/indexSearch", method = {RequestMethod.POST})
	public ModelAndView indexSearch(@RequestParam("city") String city,
			@Valid SearchForm searchForm,
			BindingResult result){
		searchForm.deleteLists();
		searchForm.setCity(city);
		searchForm.setPrize(1000000);
		
		searchForm.setFlat(searchForm.getFlat());
		searchForm.setHouse(searchForm.getHouse());
		searchForm.setStudio(searchForm.getStudio());
		searchForm.setRoom(searchForm.getRoom());
		searchForm.setRent(true);
		searchForm.setAuction(true);
		searchForm.setBuy(true);
		searchForm.setRadius(2000);
		
		if (!result.hasErrors()) {
			ModelAndView model = new ModelAndView("results");
			Iterable<Ad> searchFormResults = adService.queryResults(searchForm);
			Iterable<String> googleMapCoords = adService.findCoords(searchFormResults);
			model.addObject("results", searchFormResults);
			model.addObject("coords", googleMapCoords);
			//bad, only temporary fix
			model.addObject("bidService", bidService);
			searchForm.deleteLists();
			return model;
		} else {
			// go back
			ModelAndView model = new ModelAndView("index");
			model.addObject("newest", adService.getNewestAds(4));
			model.addObject("bidService", bidService);
			return model;
		}
	}

	/**
	 * Gets the results when filtering the ads in the database by the parameters
	 * in the search form.
	 */
	@RequestMapping(value = "/results", method = {RequestMethod.POST})
	public ModelAndView results(@Valid SearchForm searchForm,
			BindingResult result) {
		if (!result.hasErrors()) {
			ModelAndView model = new ModelAndView("results");
			Iterable<Ad> searchFormResults = adService.queryResults(searchForm);
			Iterable<String> googleMapCoords = adService.findCoords(searchFormResults);
			model.addObject("results", searchFormResults);
			model.addObject("coords", googleMapCoords);
			//bad, only temporary fix
			model.addObject("bidService", bidService);
			return model;
		} else {
			// go backs
			return searchAd();
		}
	}

	@ModelAttribute
	public SearchForm getSearchForm() {
		if (searchForm == null) {
			searchForm = new SearchForm();
		}
		return searchForm;
	}
}