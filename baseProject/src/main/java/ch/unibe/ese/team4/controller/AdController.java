package ch.unibe.ese.team4.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team4.controller.pojos.forms.BidForm;
import ch.unibe.ese.team4.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team4.controller.service.AdService;
import ch.unibe.ese.team4.controller.service.AuctionService;
import ch.unibe.ese.team4.controller.service.BidService;
import ch.unibe.ese.team4.controller.service.BookmarkService;
import ch.unibe.ese.team4.controller.service.MessageService;
import ch.unibe.ese.team4.controller.service.UserService;
import ch.unibe.ese.team4.controller.service.VisitService;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Bid;
import ch.unibe.ese.team4.model.User;

/**
 * This controller handles all requests concerning displaying ads and
 * bookmarking them.
 */
@Controller
public class AdController {

	@Autowired
	private AdService adService;
	
	@Autowired
	private BidService bidService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private VisitService visitService;
	
	@Autowired
	private BidService bidHistoryService;

	/** Gets the ad description page for the ad with the given id. */
	@RequestMapping(value = "/ad", method = RequestMethod.GET)
	public ModelAndView ad(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		
		//replacing bidService
		long userBid = -1;
		if(principal != null){
		String username = principal.getName();
		userBid = bidHistoryService.getMyBid(username, id);
		}
		long highestBid = bidHistoryService.getHighestBid(id);
		long nextBid = bidHistoryService.getNextBid(id);
		boolean isBidden = bidHistoryService.isBidden(id);
		boolean adExists = ad != null;
		boolean adDeletable = adService.adDeletable(id);
		Iterable<Bid> allBids = bidHistoryService.getAllBids(id);
		Iterable<String> bidNames = bidHistoryService.getBidUsernames(allBids);
		
		model.addObject("userBid", userBid);
		model.addObject("highestBid", highestBid);
		model.addObject("nextBid", nextBid);
		model.addObject("isBidden", isBidden);
		model.addObject("allBids", allBids);
		model.addObject("bidNames", bidNames);
		
		model.addObject("adDeletable", adDeletable);
		model.addObject("shownAd", ad);
		model.addObject("adExists", adExists);
		model.addObject("messageForm", new MessageForm());

		String loggedInUserEmail = (principal == null) ? "" : principal.getName();
		model.addObject("loggedInUserEmail", loggedInUserEmail);

		model.addObject("visits", visitService.getVisitsByAd(ad));

		model.addObject("bidForm", new BidForm());
		
		return model;
	}

	/**
	 * Gets the ad description page for the ad with the given id and also
	 * validates and persists the message passed as post data.
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView messageSent(@RequestParam("id") long id,@Valid BidForm bidForm,
			@Valid MessageForm messageForm, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) {		
		
//		ModelAndView model = new ModelAndView("adDescription");
////		ModelAndView model = new ModelAndView("redirect:/qSearch");
		Ad ad = adService.getAdById(id);
//		model.addObject("shownAd", ad);
//
//		model.addObject("bidForm", new BidForm());
//		model.addObject("allBids", bidHistoryService.allBids(ad.getId()));
		
		//principal == null should never happen, because the form only gets displayed when you're logged in
		if(principal != null){
			String username = principal.getName();
			User user = userService.findUserByUsername(username);
			bidHistoryService.addBid(ad.getId(), user.getId(), bidForm.getBid());
		}
		if (!bindingResult.hasErrors()) {
			messageService.saveFrom(messageForm);
		}
		ModelAndView redirModel = new ModelAndView("redirect:/ad?id=" + ad.getId());
		redirectAttributes.addFlashAttribute("confirmationMessage",
				"Bid placed successfully. You can take a look at it below.");
		/*
		redirModel.addObject("destination", "/ad?id="+ad.getId());
		
		
		*/
		return redirModel;
	}
	
	
	/** Deletes the ad with the given id */
	@RequestMapping(value = "/ad/deleteAd", method = RequestMethod.GET)
	public @ResponseBody ModelAndView deleteAd(@RequestParam("id") long id, Principal principal, RedirectAttributes redirectAttributes) {
		ModelAndView redirModel = new ModelAndView("redirect:/");
		if(principal != null){
			String username = principal.getName();
			User loggedUser = userService.findUserByUsername(username);
			User adUser = adService.getAdById(id).getUser();
			if(loggedUser.getId() == adUser.getId() && adService.adDeletable(id) && !(bidHistoryService.isBidden(id))){
				adService.deleteAd(id);
				redirectAttributes.addFlashAttribute("confirmationMessage",
						"Ad successfully deleted");
			}else{
				redirectAttributes.addFlashAttribute("errorMessage",
						"The Ad couldn't be deleted. Make sure you're owner of the Ad, "
						+ "there are no more planned visits and, in case of an auction, "
						+ "there haven't been made any bids yet");
	
			}
		}else{
			redirectAttributes.addFlashAttribute("errorMessage",
					"The Ad couldn't be deleted. Make sure you're logged in");
		}
		
		
		return redirModel;
	}


	/**
	 * Checks if the adID passed as post parameter is already inside user's
	 * List bookmarkedAds. In case it is present, true is returned changing
	 * the "Bookmark Ad" button to "Bookmarked". If it is not present it is
	 * added to the List bookmarkedAds.
	 * 
	 * @return 0 and 1 for errors; 3 to update the button to bookmarked 3 and 2
	 *         for bookmarking or undo bookmarking respectively 4 for removing
	 *         button completly (because its the users ad)
	 */
	@RequestMapping(value = "/bookmark", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int isBookmarked(@RequestParam("id") long id,
			@RequestParam("screening") boolean screening,
			@RequestParam("bookmarked") boolean bookmarked, Principal principal) {
		// should never happen since no bookmark button when not logged in
		if (principal == null) {
			return 0;
		}
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			// that should not happen...
			return 1;
		}
		List<Ad> bookmarkedAdsIterable = user.getBookmarkedAds();
		if (screening) {
			for (Ad ownAdIterable : adService.getAdsByUser(user)) {
				if (ownAdIterable.getId() == id) {
					return 4;
				}
			}
			for (Ad adIterable : bookmarkedAdsIterable) {
				if (adIterable.getId() == id) {
					return 3;
				}
			}
			return 2;
		}

		Ad ad = adService.getAdById(id);

		return bookmarkService.getBookmarkStatus(ad, bookmarked, user);
	}

	/**
	 * Fetches information about bookmarked rooms and own ads and attaches this
	 * information to the myRooms page in order to be displayed.
	 */
	@RequestMapping(value = "/profile/myRooms", method = RequestMethod.GET)
	public ModelAndView myRooms(Principal principal) {
		ModelAndView model;
		User user;
		if (principal != null) {
			model = new ModelAndView("myRooms");
			String username = principal.getName();
			user = userService.findUserByUsername(username);

			Iterable<Ad> ownAds = adService.getAdsByUser(user);
			Iterable<Long> highestBids = bidService.getHighestBids(ownAds);

			model.addObject("bookmarkedAdvertisements", user.getBookmarkedAds());
			model.addObject("ownAdvertisements", ownAds);
			model.addObject("highestBids", highestBids);
			return model;
		} else {
			model = new ModelAndView("home");
		}

		return model;
	}

}