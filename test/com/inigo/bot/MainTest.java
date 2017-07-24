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

	@Test
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
	}
	
	@Test
	public void testListener(){
		//TextListener tl = new TextListener(null);
		//tl.onStatus(st);
	}
}
