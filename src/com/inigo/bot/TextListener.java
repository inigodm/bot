package com.inigo.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;

public class TextListener implements StatusListener{
	Pattern p;
	Bot bot;
	private String text;
	
	public TextListener(Bot bot, String regex, String text) {
		this.bot = bot;
		this.text = text;
		this.p = Pattern.compile("(^|[\\w\\W]*[\\s]{1})([a-z]+" + regex + "(?:[^a-z]+|$)[^\\n]*");
	}

	public void onStatus(Status status) {
		try {
			// if is not friend of mine or if is mine, don't do nothing
			if (!bot.isFriend(status.getUser().getId()) || status.getUser().getId() == bot.userID()){
				System.out.println(status.getUser().getId() + "No amigo ");
				return;
			}
			String tweet = status.getText().toLowerCase();
			Matcher m = p.matcher(tweet);
			if (m.matches()){
				bot.reply(status, String.format(text, m.group(2)));
				System.out.println(String.format(text, m.group(2)));
			}
		} catch (IllegalStateException | TwitterException e1) {
			e1.printStackTrace();
		}
		
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		System.out.println("tracklimitation notice");
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Scrub geo notice");
	}

	public void onStallWarning(StallWarning warning) {
		System.out.println("Stall warning");
	}
}
