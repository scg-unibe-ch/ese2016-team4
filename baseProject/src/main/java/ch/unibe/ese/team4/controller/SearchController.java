package ch.unibe.ese.team4.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team4.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team4.controller.service.AdService;
import ch.unibe.ese.team4.controller.service.BidService;
import ch.unibe.ese.team4.model.Ad;

/** Handles all requests concerning the search for ads. */
@Controller
public class SearchController {

	@Autowired
	private AdService adService;
	
	@Autowired
	private BidService bidService;

	/**
	 * The search form that is used for searching. It is saved between request
	 * so that users don't have to enter their search parameters multiple times.
	 */
	private SearchForm searchForm;

	/** Displays the homepage */
	@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView index() {
		ModelAndView model = new ModelAndView("index");
		// returns the 4 newest premium ads
		model.addObject("newest", adService.getNewestAds(4, true));
		model.addObject("bidService", bidService);
		return model;
	}
	
	/** Shows the search ad page. */
	@RequestMapping(value = "/searchAd", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView searchAd() {
		ModelAndView model = new ModelAndView("searchAd");
		return model;
	}

	/**
	 * Gets the results when filtering the ads in the database by the parameters
	 * in the search form.
	 */
	@RequestMapping(value = "/results", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView results(@Valid SearchForm searchForm,
			BindingResult result, @RequestParam(value="page", required=false) String page) {
		if (!result.hasErrors()) {
			ModelAndView model = new ModelAndView("results");
			Iterable<Ad> searchFormResults = adService.queryResults(searchForm);
			//placing Premium Ads in searchFormResults to the head
			Iterable<Ad> sortedSearchFormResults = adService.sortByPremiumFirst(searchFormResults);
			//replacing bidService
			Iterable<Long> bidPrices = bidService.getBids(sortedSearchFormResults);
			model.addObject("bidPrices", bidPrices);
			
			model.addObject("results", sortedSearchFormResults);

			return model;
		} else {
			// go backs
			if(page != null && page.equalsIgnoreCase("index")) {
				return index();
			}else {
				return searchAd();
			}
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