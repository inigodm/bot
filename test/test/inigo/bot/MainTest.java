package test.inigo.bot;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
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
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;
import com.inigo.bot.Bot;
import com.inigo.bot.GIFFinderListener;
import com.inigo.bot.MultiPatternListener;
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
	@Mock
	static UploadedMedia um;
	AuxList statuses;
	static String tweetText ="tweet text";
	
	Bot b;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	@Before
	public void setup() throws IllegalStateException, TwitterException{
		statuses = new AuxList();
		statuses.add(tweet);
		when(ids.getIDs()).thenReturn(new long[]{444444, 222222});
		when(tweet.getId()).thenReturn(new Long(333333));
		when(tweet.getUser()).thenReturn(user);
		when(tweet.getText()).thenReturn(tweetText);
		when(user.getId()).thenReturn(new Long(222222));
		when(user.getScreenName()).thenReturn("respondido");
		when(twitter.getId()).thenReturn(new Long(111111));
		when(twitter.getFriendsIDs(-1)).thenReturn(ids);
		when(twitter.updateStatus(tweetText)).thenReturn(tweet);
		when(twitter.getHomeTimeline()).thenReturn(statuses);
		when(twitter.uploadMedia(any(File.class))).thenReturn(um);
		when(um.getMediaId()).thenReturn(new Long(11111111));
		//when(twitter.updateStatus(stsup)).thenReturn(sts);
		twitter.destroyStatus(tweet.getId());
		when(tf.getInstance()).thenReturn(twitter);
	}
	
	@Test
	public void testSendMessage() throws TwitterException, InterruptedException{
		b = new Bot(tf);
		Status tweet = b.sendTweet(tweetText);
		assertTrue(tweet.getId() == 333333);
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
	public void testMultiPatternListener() throws TwitterException{
		b = new Bot(tf);
		MultiPatternListener mpl = new MultiPatternListener(b);
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ente)[s]?(?:[^a-z]+|$)[^\\n]*", "Sabes?, para %s mi polla en tu frente...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ar)(?:[^a-z]+|$)[^\\n]*", "Sabes quien va a %s? mi polla en tu paladar...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ado)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s el que tengo aqui colgado");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ada)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s la que tengo aqui colgada");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+al)(?:[^a-z]+|$)[^\\n]*", "Para %s mi polla en tu ojal...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+enta)[s]?(?:[^a-z]+|$)[^\\n]*", "%s?? pues come de aqui que alimenta!!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ino)$", "%s??? en tu culo mi pepino!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ano)$", "%s??? Me la agarras con la mano!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}ato)$", "%s??? En tu boca mi aparato");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}oca)[s]?(?:[^a-z]+|$)[^\\n]*", "%s?? mi polla en tu boca!!!!");
		when(tweet.getText()).thenReturn("Pos claro, es evidente");
		mpl.onStatus(tweet);
		when(tweet.getText()).thenReturn("Sabes que soy abogado? pues tenlo en mente");
		mpl.onStatus(tweet);
		when(tweet.getText()).thenReturn("Se van a pelear...");
		mpl.onStatus(tweet);
		when(tweet.getText()).thenReturn("Ven aqui un rato");
		mpl.onStatus(tweet);
		when(tweet.getText()).thenReturn("Ven aqui un rato no");
		mpl.onStatus(tweet);
		when(tweet.getText()).thenReturn("en tu boca?");
		mpl.onStatus(tweet);
		when(tweet.getText()).thenReturn("se equivoca?");
		mpl.onStatus(tweet);
		
		
	}
	
	@Test
	public void testListener() throws TwitterException{
		b = new Bot(tf);
		TextListener tl = new TextListener(b, "ado)[s]*", "Pues para %s el que tengo aqui colgado");
		tl.onStatus(tweet);
		when(tweet.getText()).thenReturn("un abogado?");
		tl.onStatus(tweet);
		when(tweet.getText()).thenReturn("Pos claro, es evidente");
		System.out.println("*********************************");
		tl = new TextListener(b, "ente)[s]?([^a-z]+|$)", "Sabes?, para %s mi polla en tu frente...");
		tl.onStatus(tweet);
		when(tweet.getText()).thenReturn("va a llegar");
		System.out.println("*********************************");
		tl = new TextListener(b, "ar)([^a-z]+|$)", "Sabes quien va a %s? mi polla en tu paladar...");
		tl.onStatus(tweet);
		System.out.println("*********************************");
		when(user.getId()).thenReturn(new Long(444444));
		when(tweet.getText()).thenReturn("todos terminados");
		tl.onStatus(tweet);
	}
	
	@Test
	public void testGIFListener() throws Exception{
		b = new Bot(tf);
		GIFFinderListener gifl = new GIFFinderListener(b);
		gifl.add("fail");
		gifl.add("wtf");
		gifl.add("l[o]+l");
		gifl.add("bf4");
		gifl.add("battlefield");
		//String html = gifl.tryGettingAImage("faifdsfdsfsdfsdfdsal", 0);
		//System.out.println("html: " +html);
		//html = gifl.tryGettingAImage("fail", 0);
		//assertTrue(html.length() > 0);
		when(tweet.getText()).thenReturn("vaya pedazo de fail");
		gifl.onStatus(tweet);
		when(tweet.getText()).thenReturn("wtf");
		gifl.onStatus(tweet);
		when(tweet.getText()).thenReturn("wtf again");
		gifl.onStatus(tweet);
		when(tweet.getText()).thenReturn("loooooooooooooooooooool");
		gifl.onStatus(tweet);
		when(tweet.getText()).thenReturn("mola el bf4 q no?");
		gifl.onStatus(tweet);
		when(tweet.getText()).thenReturn("loooooooooooooooooooool");
		gifl.onStatus(tweet);
		when(tweet.getText()).thenReturn("looofailooooooooooooool");
		gifl.onStatus(tweet);
		
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
