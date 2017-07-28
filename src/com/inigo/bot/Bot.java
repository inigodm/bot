package com.inigo.bot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.FilterQuery;
import twitter4j.IDs;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Bot {
	TwitterFactory factory;
	TwitterStream twitterStream;
	Twitter twitter;
	IDs friends;
	
	
	public static void main(String args[]) throws Exception{
	    // The factory instance is re-useable and thread safe.
	    Bot bot = new Bot(new TwitterFactory());
	    bot.startService();
	}
	
	public Bot(TwitterFactory factory) throws TwitterException{
		this.factory = factory;
		this.twitter = factory.getInstance();
		this.twitterStream = new TwitterStreamFactory().getInstance();
		this.friends = twitter.getFriendsIDs(-1);
	}

	public boolean isFriend(long idUser){
		long[] amics = friends.getIDs();
		for (int i = 0; i < amics.length; i++){
			if (idUser == amics[i]){
				return true;
			}
		}
		return false;
		
	}
	
	public Status sendTweet(String tweet) throws TwitterException {
		Status status = twitter.updateStatus(tweet);
		return status;
	}
	
	public String reply(Status inReplyTo, String text) throws TwitterException{
		System.out.println("in reply to " + inReplyTo.getText());
		StatusUpdate stat= new StatusUpdate("@" + inReplyTo.getUser().getScreenName() + " " + text);
	    stat.setInReplyToStatusId(inReplyTo.getId());
	    twitter.updateStatus(stat);
	    return stat.getStatus();
	 }

	public void deleteTweet(Status tweet) {
		try {
			if (tweet.getUser().getId() == twitter.getId()) {
				twitter.destroyStatus(tweet.getId());
			}
		} catch (TwitterException e) {
			System.out.println(String.format("Not able to delete tweet with id: %s. reason: %s", tweet, e.getMessage()));
			e.printStackTrace();
		}
	}

	public List<Status> getTimeLine() throws TwitterException {
		List<Status> statuses = twitter.getHomeTimeline();
	    for (Status status : statuses) {
	        System.out.println(status.getUser().getName() + "(@" + status.getUser().getScreenName() + "):" + status.getText());
	    }
		return statuses;
	}

	public void startService() throws TwitterException {
		System.out.println("Starting stream listener....");
		MultiPatternListener mpl = new MultiPatternListener(this);
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ente)[s]?(?:[^a-z]+|$)[^\\n]*", "Sabes?, para %s mi polla en tu frente...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ar)(?:[^a-z]+|$)[^\\n]*", "Sabes quien va a %s? mi polla en tu paladar...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ado)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s el que tengo aqui colgado");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ada)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s la que tengo aqui colgada");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+al)(?:[^a-z]+|$)[^\\n]*", "Para %s mi polla en tu ojal...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+enta)[s]?(?:[^a-z]+|$)[^\\n]*", "%s?? pues come de aqui que alimenta!!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ino)[s]?(?:[^a-z]+|$)[^\\n]*", "%s??? en tu culo mi pepino!!!");
		twitterStream.addListener(mpl);
		twitterStream.filter(tweetFilterCreator());
		System.out.println("Started.");
		while(true);
	}
	
	private FilterQuery tweetFilterCreator() throws TwitterException{
		long[] friendsIDs = twitter.getFriendsIDs(-1).getIDs();
		System.out.println("Following: " + friendsIDs.length);
		FilterQuery tweetFilterQuery = new FilterQuery(friendsIDs); // See 
		//tweetFilterQuery.track(new String[]{"*ado"}); // OR on keywords
		return tweetFilterQuery;
	}

	public long userID() throws IllegalStateException, TwitterException {
		return twitter.getId();
	}
}
