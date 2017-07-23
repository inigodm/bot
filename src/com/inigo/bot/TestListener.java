package com.inigo.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import twitter4j.StatusListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

public class TestListener implements StatusListener{
				Pattern p = Pattern.compile("\\w*ado\\b");

	public void onStatus(Status status) {
		String text = status.getText();
		Matcher m = p.matcher(text);
		System.out.println(status.getUser().getName() + " : " + status.getText());
		System.out.println("matches = " + m.matches());
		if (m.matches()){
			System.out.println("------" + status.getUser().getName() + " : " + status.getText());
		}
		try {
			Thread.currentThread().sleep(9000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	public void onScrubGeo(long userId, long upToStatusId) {
	}

	public void onStallWarning(StallWarning warning) {
	}
}
