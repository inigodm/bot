package test.inigo.bot;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.IDs;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.SymbolEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;
import com.inigo.bot.Bot;
import com.inigo.bot.TextListener;

public class MainTest {
	@Mock
	static TwitterFactory tf;
	@Mock
	static Twitter twitter;
	@Mock
	static User user;
	@Mock
	static Status tweet;
	@Mock
	static IDs ids;
	AuxList statuses;
	static String tweetText ="tweet text";
	
	Bot b;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	@Before
	public void setup() throws IllegalStateException, TwitterException{
		statuses = new AuxList();
		statuses.add(tweet);
		when(ids.getIDs()).thenReturn(new long[]{444444});
		when(tweet.getId()).thenReturn(new Long(333333));
		when(tweet.getUser()).thenReturn(user);
		when(tweet.getText()).thenReturn(tweetText);
		when(user.getId()).thenReturn(new Long(222222));
		when(user.getScreenName()).thenReturn("respondido");
		when(twitter.getId()).thenReturn(new Long(111111));
		when(twitter.getFriendsIDs(-1)).thenReturn(ids);
		when(twitter.updateStatus(tweetText)).thenReturn(tweet);
		when(twitter.getHomeTimeline()).thenReturn(statuses);
		//when(twitter.updateStatus(stsup)).thenReturn(sts);
		twitter.destroyStatus(tweet.getId());
		when(tf.getInstance()).thenReturn(twitter);
	}
	
	@Test
	public void testSendMessage() throws TwitterException, InterruptedException{
		b = new Bot(tf);
		Status tweet = b.sendTweet(tweetText);
		assertTrue(tweet.getId() == 333333);
		Thread.currentThread().sleep(1000);
		b.deleteTweet(tweet);
	}
	
	@Test
	public void testGetTimeline() throws TwitterException{
		b = new Bot(tf);
		List<Status> tl = b.getTimeLine();
		assertTrue(tl.size() > 0);
	}
	
	@Test
	public void startService() throws TwitterException{
		b = new Bot(tf);
		//b.startService();
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
	public void testListener() throws TwitterException{
		b = new Bot(tf);
		TextListener tl = new TextListener(b, "ado)[s]*", "Pues para %s el que tengo aqui colgado");
		tl.onStatus(tweet);
		System.out.println("No ha respondido nada");
		when(tweet.getText()).thenReturn("ya he acabados tu");
		tl.onStatus(tweet);
		when(user.getId()).thenReturn(new Long(444444));
		when(tweet.getText()).thenReturn("todos terminados");
		tl.onStatus(tweet);
	}
}

class AuxList extends ArrayList<Status> implements ResponseList<Status> {
	@Override
	public int getAccessLevel() {
		return 0;
	}

	@Override
	public RateLimitStatus getRateLimitStatus() {
		return null;
	}
}
