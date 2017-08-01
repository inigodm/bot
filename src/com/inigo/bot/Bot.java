package com.inigo.bot;

import java.io.File;
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
import twitter4j.UploadedMedia;

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
		System.out.println("in reply to " + inReplyTo.getText() + " " + text);
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
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}ar)(?:[^a-z]+|$)[^\\n]*", "Sabes quien va a %s? mi polla en tu paladar...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}ado)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s el que tengo aqui colgado");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}ada)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s la que tengo aqui colgada");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}al)$", "Para %s mi polla en tu ojal...");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+enta)[s]?(?:[^a-z]+|$)[^\\n]*", "%s?? pues come de aqui que alimenta!!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}ino)$", "%s??? en tu culo mi pepino!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ano)$", "%s??? Me la agarras con la mano!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+ato)$", "%s??? En tu boca mi aparato");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+atro)$", "%s??? Pa tu culo mi aparato");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+inco)$", "%s??? Por el culo te la hinco");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]+anta)[s]?(?:[^a-z]+|$)[^\\n]*", "%s?? mi polla en tu garganta!!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}oca)[s]?(?:[^a-z]+|$)[^\\n]*", "%s?? mi polla en tu boca!!!!");
		mpl.addPattern("(?:^|[\\w\\W]*[\\s]{1})([a-z]{2,}aje)[s]?(?:[^a-z]+|$)[^\\n]*", "Para %s mis cojones, que se van de viaje");
		GIFFinderListener gifl = new GIFFinderListener(this);
		gifl.add("fail[s]?");
		gifl.add("wtf");
		gifl.add("l[o]+l");
		gifl.add("bf4");
		gifl.add("omg");
		gifl.add("battlefield");
		gifl.add("battlegrounds");
		twitterStream.addListener(mpl);
		twitterStream.addListener(gifl);
		twitterStream.filter(tweetFilterCreator());
		System.out.println("Started.");
		stopExecution();
		
	}
	
	private void stopExecution(){
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private FilterQuery tweetFilterCreator() throws TwitterException{
		long[] friendsIDs = twitter.getFriendsIDs(-1).getIDs();
		System.out.println("Following: " + friendsIDs.length);
		FilterQuery tweetFilterQuery = new FilterQuery(friendsIDs); // See 
		return tweetFilterQuery;
	}

	public long userID() throws IllegalStateException, TwitterException {
		return twitter.getId();
	}

	public void replyWithMedia(Status inReplyTo, String path) throws TwitterException {
		replyWithMedia(inReplyTo, path, "");
	}
	
	public void replyWithMedia(Status inReplyTo, String path, String msg) throws TwitterException {
		System.out.println("in reply to " + inReplyTo.getText() + " path: " + path);
		StatusUpdate stat= new StatusUpdate("@" + inReplyTo.getUser().getScreenName() + " " + msg);
		UploadedMedia media = twitter.uploadMedia(new File(path));
	    stat.setInReplyToStatusId(inReplyTo.getId());
	    stat.setMediaIds(new long[]{media.getMediaId()});
	    twitter.updateStatus(stat);
	}
}
