package com.inigo.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyData;
import at.mukprojects.giphy4j.entity.giphy.GiphyOriginal;
import at.mukprojects.giphy4j.entity.search.SearchGiphy;
import at.mukprojects.giphy4j.exception.GiphyException;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;

public class GIFFinderListener implements StatusListener{

	Bot bot;
	Map<Pattern, Integer> words = new HashMap<>();
	
	public GIFFinderListener(Bot bot) {
		this.bot = bot;
	}
	
	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(Status status) {
		try {
			if (!bot.isFriend(status.getUser().getId()) || 
					status.getUser().getId() == bot.userID() ||
					status.isRetweet()){
				System.out.println(status.getUser().getId() + "No amigo ");
				return;
			}
			String tweet = status.getText().toLowerCase();
			Matcher m;
			for (Pattern word : words.keySet()){
				m = word.matcher(tweet);
				if (m.find()){
					String gifUrl = findAGifFor(tweet, word);
					if (!"".equals(gifUrl)){
						bot.reply(status, findAGifFor(tweet, word));
						return;
					}					
				}
			}
		} catch (IllegalStateException | TwitterException | GiphyException | IOException e) {
			e.printStackTrace();
		}
	}

	public String findAGifFor(String text, Pattern pattern) throws GiphyException, MalformedURLException, IOException{
		String res = tryGettingAImage(text, words.get(pattern));
		if (!"".equals(res)){
			res = tryGettingAImage(text, 0);
			words.put(pattern, 1);
		}
		return res;
	}
	
	public String tryGettingAImage(String text, Integer lastValid) throws GiphyException{
		Giphy giphy = new Giphy("be9a342ba4114c448ed626e237091243");
		List<GiphyData> r = giphy.search(text,1, lastValid).getDataList();
		String res = "";
		if (!r.isEmpty()){
			res = r.get(0).getBitlyGifUrl();
		}
		return res;
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

	public void add(String word) {
		words.put(Pattern.compile("(^|[^a-z])+("+word+")([^a-z]+|$)"), 0);
	}

	public String getARandomGIF(String html) {
		return html;
		
	}

}
