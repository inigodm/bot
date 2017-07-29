package com.inigo.bot;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyContainer;
import at.mukprojects.giphy4j.entity.giphy.GiphyData;
import at.mukprojects.giphy4j.exception.GiphyException;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;

public class GIFFinderListener implements StatusListener {

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
			// If isnt friend, is own bot's tweet or is a retweet, don't do nothing
			if (!bot.isFriend(status.getUser().getId()) || status.getUser().getId() == bot.userID()
					|| status.isRetweet()) {
				System.out.println(status.getUser().getId() + "No amigo, bot o retweet ");
				return;
			}
			//else check the patterns
			for (Pattern word : words.keySet()) {
				if (replyIfPattern(word, status)){
					// if replied we don't want to reply more to the same tweet
					break;
				}
			}
		} catch (IllegalStateException | TwitterException | GiphyException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean replyIfPattern(Pattern word, Status status) throws MalformedURLException, GiphyException, IOException, TwitterException{
		String tweet = status.getText().toLowerCase();
		Matcher m = word.matcher(tweet);
		if (m.matches()) {
			if (m.groupCount() > 0){
				tweet = m.group(1);
			}
			return doTheReply(status, tweet, word);
		}
		return false;
	}
	
	private boolean doTheReply(Status status, String textToReply, Pattern patternUsed) throws MalformedURLException, GiphyException, IOException, TwitterException{
		String gifUrl = findAGifFor(textToReply, patternUsed);
		if (!"".equals(gifUrl)) {
			bot.replyWithMedia(status, gifUrl);
			removeFile(gifUrl);
			return true;
		}
		return false;
	}
	
	private void removeFile(String filename){
		File f = new File(filename);
		f.delete();
	}

	public String findAGifFor(String text, Pattern pattern) throws GiphyException, MalformedURLException, IOException {
		String res = tryGettingAImage(text, words.get(pattern));
		if ("".equals(res)) {
			res = tryGettingAImage(text, 0);
			words.put(pattern, 1);
		}
		return res;
	}

	public String tryGettingAImage(String text, Integer lastValid) throws GiphyException, IOException {
		Giphy giphy = new Giphy("be9a342ba4114c448ed626e237091243");
		System.out.println("buscando algo para '"+  text + "'");
		List<GiphyData> r = giphy.search(text, 1, lastValid).getDataList();
		String res = "";
		if (!r.isEmpty()) {
			res = downloadFromGiphy(r.get(0));
		}
		return res;
	}

	/** Downloads given image from giphy and returns the local name of the downloadded file
	 * @param giphyData
	 * @return
	 * @throws IOException
	 */
	public String downloadFromGiphy(GiphyData giphyData) throws IOException {
		String search = getMinimumSizedImage(giphyData.getImages());
		String path = "./gifs/" + giphyData.getId() + search.substring(search.lastIndexOf("."));
		System.out.println("Downloading: " + "wget " + search + " -O " + path);
		Process p = Runtime.getRuntime().exec("wget " + search + " -O " + path);
        System.out.println("Waiting for batch file ...");
	    try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return path;
	}

	private String getMinimumSizedImage(GiphyContainer giphyContainer) {
		String res = giphyContainer.getDownsized().getUrl();
		int size = Integer.parseInt(giphyContainer.getDownsized().getSize());
		if (Integer.parseInt(giphyContainer.getFixedHeight().getSize()) < size){
			res = giphyContainer.getFixedHeight().getUrl();
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
		words.put(Pattern.compile("(?:^|[\\w\\W]*[\\s]{1})(" + word +")[s]?(?:[^a-z]+|$)[^\\n]*"), 0);
	}

	public String getARandomGIF(String html) {
		return html;

	}

}
