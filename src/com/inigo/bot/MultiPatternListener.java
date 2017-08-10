package com.inigo.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;

/**
 * @author inigo
 *
 */
public class MultiPatternListener  implements StatusListener{
	Bot bot;
	Map<Pattern, String> patterns;
	
	public MultiPatternListener(Bot bot) {
		this.bot = bot;
		this.patterns = new HashMap<>();
	}

	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(Status status) {
		try {
			if (!bot.isFriend(status.getUser().getId()) || status.getUser().getId() == bot.userID() || status.isRetweet()){
				System.out.println(status.getUser().getId() + "No amigo ");
				return;
			}
			Matcher m;
			for (Pattern patt : patterns.keySet()){
				if (replyIfPattern(patt, status)){
					// if replied we don't want to reply more to the same tweet
					break;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean replyIfPattern(Pattern patt, Status status) throws TwitterException {
		String tweet = status.getText().toLowerCase();
		Matcher m = patt.matcher(tweet);
		if (m.matches()){
			List<String> strings = new ArrayList<>();
			for (int i = 1; i <= m.groupCount(); i++){
				strings.add(m.group(i));
			}
			bot.reply(status, String.format(patterns.get(patt), strings.toArray()));
			return true;
		}
		return false;
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning warning) {
		// TODO Auto-generated method stub
		
	}

	/** Adds a pattern to be search in the tweet and substituted in th reply
	 * @param regex the regex to find. There must be ONLY the necessary groups<bR>
	 * ex: ^|[\\w\\W]*[\\s]{1})([a-z]+ente)[s]?(?:[^a-z]+|$)[^\\n]*
	 * @param replacement A string to be formated, with the same number of '%s' than groups the regex
	 * ex: Sabes?, para %s mi polla en tu frente...
	 */
	public void addPattern(String regex, String replacement) {
		patterns.put(Pattern.compile(regex), replacement);		
	}

}
