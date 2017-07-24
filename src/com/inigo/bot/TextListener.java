package com.inigo.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

public class TextListener implements StatusListener{
	Pattern p = Pattern.compile("(^|[\\w\\W]*[\\s]{1})([a-z]*ado)[^\\n]*");
	Bot bot;
	
	public TextListener(Bot bot) {
		this.bot = bot;
	}

	public void onStatus(Status status) {
		try {
			// if is not friend of mine or if is mine, don't do nothing
			if (!bot.isFriend(status.getUser().getId()) || status.getUser().getId() == bot.userID()){
				return;
			}
			String text = status.getText();
			Matcher m = p.matcher(text);
			if (m.matches()){
				System.out.println(m.group(2));
				bot.reply(status, " Pues para "+ m.group(2) + " el que tengo aquí colgado");
			}else{
				System.out.println("No match");
			}
		} catch (IllegalStateException | TwitterException e1) {
			e1.printStackTrace();
		}
		
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		System.out.println("deletion notice");
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
