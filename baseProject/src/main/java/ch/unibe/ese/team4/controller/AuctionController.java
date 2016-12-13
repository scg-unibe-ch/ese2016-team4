package ch.unibe.ese.team4.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team4.controller.service.AdService;
import ch.unibe.ese.team4.controller.service.AuctionService;
import ch.unibe.ese.team4.controller.service.UserService;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.User;

@Controller
public class AuctionController {
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuctionService auctionService;
	
	/**
     * Ends auction
     * Sends messages to the guy who bought the estate
	 * @param redirectAttributes 
     */
	@RequestMapping(value = "/instantBuy", method = RequestMethod.GET)
	@ResponseBody
    public  ModelAndView instantBuy(@RequestParam("id") long id, Principal principal, RedirectAttributes redirectAttributes){
		
		Ad ad = adService.getAdById(id);

		ModelAndView model = new ModelAndView("redirect:/ad?id=" + ad.getId());
		redirectAttributes.addFlashAttribute("confirmationMessage",
				"Instant buy successful!");
		
        User user = userService.findUserByUsername(principal.getName());
        auctionService.instantBuy(id, user);
        
        return model;
    }

	
}
