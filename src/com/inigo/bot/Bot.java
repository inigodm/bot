package com.inigo.bot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Bot {
	TwitterFactory factory;
	TwitterStream twitterStream;
	Twitter twitter;
	
	public static void main(String args[]) throws Exception{
	    // The factory instance is re-useable and thread safe.
	    Bot bot = new Bot(new TwitterFactory());
	    bot.respondTweet("uno", "dos");
	    while(true);
	    //Twitter twitter = factory.getInstance();
	    //Status status = twitter.updateStatus("@aVorcan marandina!!!! lingotero!!!! unchara!!!!");
	    //System.out.println("Successfully updated the status to [" + status.getText() + "].");
	}
	
	public Bot(TwitterFactory factory){
		this.factory = factory;
		this.twitter = factory.getInstance();
		this.twitterStream = new TwitterStreamFactory().getInstance();
	}

	public Status sendTweet(String tweet) throws TwitterException {
		Status status = twitter.updateStatus(tweet);
		System.out.println("Successfully updated the status to [" + status.getText() + "].");
		return status;
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
		StatusListener listener = new StatusListener() {
			Pattern p = Pattern.compile("ado ");
			public void onStatus(Status status) {
				String text = status.getText();
				Matcher m = p.matcher(text);
				if (m.matches()){
					System.out.println(status.getUser().getName() + " : " + status.getText());
				}
				try {
					Thread.currentThread().sleep(9000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			public void onException(Exception ex) {ex.printStackTrace();}
			public void onScrubGeo(long userId, long upToStatusId) {}
			public void onStallWarning(StallWarning warning) {}
		};
		twitterStream.addListener(listener);
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
}