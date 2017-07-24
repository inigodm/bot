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
	    bot.respondTweet("uno", "dos");
	    while(true);
	    //Twitter twitter = factory.getInstance();
	    //Status status = twitter.updateStatus("@aVorcan marandina!!!! lingotero!!!! unchara!!!!");
	    //System.out.println("Successfully updated the status to [" + status.getText() + "].");
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
		System.out.println("Successfully updated the status to [" + status.getText() + "].");
		return status;
	}
	
	public void reply(Status inReplyTo, String text) throws TwitterException{
		System.out.println("in reply to " + inReplyTo.getText());
		StatusUpdate stat= new StatusUpdate("@" + inReplyTo.getUser().getScreenName() + text);
	    stat.setInReplyToStatusId(inReplyTo.getId());
	    twitter.updateStatus(stat);
	    System.out.println("done");
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

	public void respondTweet(String string, String string2) throws TwitterException {
		System.out.println("Starting stream listener....");
		twitterStream.addListener(new TextListener(this));
		twitterStream.filter(tweetFilterCreator());
		//twitterStream.sample();
	}
	
	private FilterQuery tweetFilterCreator() throws TwitterException{
		long[] friendsIDs = twitter.getFriendsIDs(-1).getIDs();
		System.out.println("amics! " + friendsIDs.length);
		FilterQuery tweetFilterQuery = new FilterQuery(friendsIDs); // See 
		//tweetFilterQuery.track(new String[]{"*ado"}); // OR on keywords
		/*tweetFilterQuery.locations(new double[][]{new double[]{-126.562500,30.448674},
		                new double[]{-61.171875,44.087585
		                }}); // See https://dev.twitter.com/docs/streaming-apis/parameters#locations for proper location doc. 
		//Note that not all tweets have location metadata set.
		tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly on Norwegian tweets */
		return tweetFilterQuery;
	}

	public long userID() throws IllegalStateException, TwitterException {
		return twitter.getId();
	}
}