package ch.unibe.ese.team1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.BidHistoryService;

/**
 * This controller handles request concerning the home page and several other
 * simple pages.
 */
@Controller
public class IndexController {

	@Autowired
	private AdService adService;

	@Autowired
	private BidHistoryService bidHistoryService;
	/** Displays the home page. */
	@RequestMapping(value = "/")
	public ModelAndView index() {
		ModelAndView model = new ModelAndView("index");
		model.addObject("newest", adService.getNewestAds(4));
		model.addObject("bidService", bidHistoryService);
		return model;
	}

	/** Displays the about us page. */
	@RequestMapping(value = "/about")
	public ModelAndView about() {
		return new ModelAndView("about");
	}

	/** Displays the disclaimer page. */
	@RequestMapping(value = "/disclaimer")
	public ModelAndView disclaimer() {
		return new ModelAndView("disclaimer");
	}
}