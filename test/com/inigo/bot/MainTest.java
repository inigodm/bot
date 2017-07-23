package com.inigo.bot;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;

public class MainTest {

	/*@Test
	public void test() throws Exception{
		Bot.main(new String[] {""});
	}
	
	@Test
	public void testSendMessage() throws TwitterException, InterruptedException{
		TwitterFactory tf = makeFactory();
		Bot bot = new Bot(tf);
		Status tweet = bot.sendTweet("Test");
		assertTrue(tweet.getId() > 0);
		Thread.currentThread().sleep(1000);
		bot.deleteTweet(tweet);
	}
	
	@Test
	public void testGetTimeline() throws TwitterException{
		TwitterFactory tf = makeFactory();
		Bot bot = new Bot(tf);
		List<Status> tl = bot.getTimeLine();
		assertTrue(tl.size() > 0);
	}
	
	@Test
	public void respondTweet(String str) throws TwitterException{
		TwitterFactory tf = makeFactory();
		Bot bot = new Bot(tf);
		bot.respondTweet("eso", "adooooo");
	}
	
	private TwitterFactory makeFactory(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("JidjbDyNWxtVp0Fyh7MVJgJDD")
		  .setOAuthConsumerSecret("LZ8omYs78Bl7mzWyxirocO2TuHAUZaxe4zpyKVn0dYvM91zwGb")
		  .setOAuthAccessToken("889071297346621440-iGttUqngnqRCyCC3zlDoFvGegAQpARz")
		  .setOAuthAccessTokenSecret("r8Wx9LQkMdYfTSNwFdawpqLYhjSHxJtpPJLd15hSixeH2");
		return new TwitterFactory(cb.build());
	}*/
	
	@Test
	public void testListener(){
		TestListener tl = new TestListener();
		Status st = new Status() {
			
			@Override
			public UserMentionEntity[] getUserMentionEntities() {return null;}
			@Override
			public URLEntity[] getURLEntities() {
				return null;
			}
			@Override
			public SymbolEntity[] getSymbolEntities() {
				return null;
			}
			@Override
			public MediaEntity[] getMediaEntities() {
				return null;
			}
			@Override
			public HashtagEntity[] getHashtagEntities() {
				return null;
			}
			@Override
			public RateLimitStatus getRateLimitStatus() {
				return null;
			}
			@Override
			public int getAccessLevel() {
				return 0;
			}
			@Override
			public int compareTo(Status o) {
				return 0;
			}
			@Override
			public boolean isTruncated() {
				return false;
			}
			@Override
			public boolean isRetweetedByMe() {
				return false;
			}
			@Override
			public boolean isRetweeted() {
				return false;
			}
			@Override
			public boolean isRetweet() {
				return false;
			}
			@Override
			public boolean isPossiblySensitive() {
				return false;
			}
			@Override
			public boolean isFavorited() {
				return false;
			}
			@Override
			public String[] getWithheldInCountries() {
				return null;
			}
			@Override
			public User getUser() {
				return null;
			}
			@Override
			public String getText() {
				return "alalalala ado asdasdas";
			}
			@Override
			public String getSource() {
				return null;
			}
			@Override
			public Scopes getScopes() {
				return null;
			}
			@Override
			public Status getRetweetedStatus() {
				return null;
			}
			
			@Override
			public int getRetweetCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getQuotedStatusId() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Status getQuotedStatus() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Place getPlace() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getLang() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getInReplyToUserId() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getInReplyToStatusId() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getInReplyToScreenName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getId() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public GeoLocation getGeoLocation() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getFavoriteCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getDisplayTextRangeStart() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getDisplayTextRangeEnd() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getCurrentUserRetweetId() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Date getCreatedAt() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long[] getContributors() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		tl.onStatus(st);
	}
}
