package ch.unibe.ese.team4.controller.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team4.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.MessageState;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.MessageDao;
import ch.unibe.ese.team4.model.dao.UserDao;

/** Handles all persistence operations concerning messaging. */
@Service
public class MessageService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private MessageDao messageDao;

	// Pattern for recognizing a URL, based off RFC 3986
	private static final Pattern urlPattern = Pattern.compile(
			//"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
			//		+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + ".*",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	/**
	 * Gets all messages in the inbox of the given user, sorted newest to oldest
	 */
	@Transactional
	public Iterable<Message> getInboxForUser(User user) {
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		List<Message> messages = new ArrayList<Message>();
		for (Message message : usersMessages)
			messages.add(message);
		Collections.sort(messages, new Comparator<Message>() {
			@Override
			public int compare(Message message1, Message message2) {
				return message2.getDateSent().compareTo(message1.getDateSent());
			}
		});
		if (!messages.isEmpty()){
			messages.get(0).setState(MessageState.READ);
			messageDao.save(messages.get(0));
		}
		return messages;
	}

	/** Gets all messages in the sent folder for the given user. */
	@Transactional
	public Iterable<Message> getSentForUser(User user) {
		return messageDao.findBySender(user);
	}

	/** Gets the message with the given id. */
	@Transactional
	public Message getMessage(long id) {
		return messageDao.findOne(id);
	}

	/**
	 * Handles persisting a new message to the database.
	 * 
	 * @param messageForm
	 *            the form to take the data from
	 */
	@Transactional
	public Message saveFrom(MessageForm messageForm) {
		Message message = new Message();

		message.setRecipient(userDao.findByUsername(messageForm.getRecipient()));
		message.setSubject(messageForm.getSubject());
		message.setText(getUrl(messageForm.getText()));
		message.setState(MessageState.UNREAD);

		// get logged in user as the sender
		org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		User loggedInUser = userDao.findByUsername(securityUser.getUsername());

		message.setSender(loggedInUser);

		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		calendar.setTimeInMillis(System.currentTimeMillis());
		message.setDateSent(calendar.getTime());

		messageDao.save(message);

		return message;
	}

	/**
	 * Saves a new message with the given parameters in the DB.
	 * 
	 * @param sender
	 *            the user who sends the message
	 * @param recipient
	 *            the user who should receive the message
	 * @param subject
	 *            the subject of the message
	 * @param text
	 *            the text of the message
	 */
	public void sendMessage(User sender, User recipient, String subject, String text) {
		Message message = new Message();
		message.setDateSent(new Date());
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setSubject(subject);
		message.setText(getUrl(text));

		message.setState(MessageState.UNREAD);

		messageDao.save(message);
	}

	public String getUrl(String text) {

		// separate input by spaces (since URLs don't have spaces... )
		// if a link gets copied without a space (e.g. check this out:www.google.ch) it doesn't work
		String[] parts = text.split("\\s+");
		String linkedText = "";
		// get every part
		for (String item : parts) {
			if (urlPattern.matcher(item).matches()) {
				// it's a url, so we link it and concatenate it after
				String name = item;
				if (item.length()>35){
					name = item.substring(0, 35);
					name = name.concat("...");
				}
				linkedText = linkedText.concat("<a href=\"" + item + "\">" + name + "</a> ");
			} else {
				// no url, so normal concatenating
				linkedText = linkedText.concat(item + " ");
			}
		}
		return linkedText;
	}

	/**
	 * Sets the MessageState of a given Message to "READ".
	 * 
	 * @param id
	 */
	@Transactional
	public void readMessage(long id) {
		Message message = messageDao.findOne(id);
		message.setState(MessageState.READ);
		messageDao.save(message);
	}

	/** Returns the number of unread messages a user has. */
	@Transactional
	public int unread(long id) {
		User user = userDao.findOne(id);
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		int i = 0;
		for (Message message : usersMessages) {
			if (message.getState().equals(MessageState.UNREAD))
				i++;
		}
		return i;
	}

}
