package worldline.ssm.rd.ux.wltwitter.adapters;

import java.util.List;

import worldline.ssm.rd.ux.wltwitter.R;
import worldline.ssm.rd.ux.wltwitter.WLTwitterApplication;
import worldline.ssm.rd.ux.wltwitter.async.DownloadImageAsyncTask;
import worldline.ssm.rd.ux.wltwitter.components.ImageMemoryCache;
import worldline.ssm.rd.ux.wltwitter.interfaces.TweetListener;
import worldline.ssm.rd.ux.wltwitter.pojo.Tweet;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsAdapter extends BaseAdapter implements OnClickListener {

	// The list of items to display
	private final List<Tweet> mTweets;

	// The Layout inflater
	private final LayoutInflater mInflater;

	// The listener for events
	private TweetListener mListener;
	
	// The cache for images
	private final ImageMemoryCache mImageMemoryCache;
	
	public TweetsAdapter (List<Tweet> tweets){
		mTweets = tweets;
		mInflater = LayoutInflater.from(WLTwitterApplication.getContext());
		
		// Instantiate our cache
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 16;
		mImageMemoryCache = new ImageMemoryCache(cacheSize);
	}

	public void setTweetListener(TweetListener listener){
		mListener = listener;
	}
	
	@Override
	public int getCount() {
		return null != mTweets ? mTweets.size() : 0; 
	}

	@Override
	public Object getItem(int position) {
		return null != mTweets ? mTweets.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getViewBestWay(position, convertView, parent);
	}

	/**
	 * Ugly way, because we create a new view each time, resulting in memory leaks, poor performance
	 * and will at some point crash  because of OutOfMemoryError
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@SuppressWarnings("unused")
	private View getViewUglyWay(int position, View convertView, ViewGroup parent) {
		// Inflate our View
		final View view = mInflater.inflate(R.layout.tweet_listitem, null);

		// Get the current item
		final Tweet tweet = (Tweet) getItem(position);

		// Set the user name, to do so, retrieve the corresponding TextView
		final TextView userName = (TextView) view.findViewById(R.id.tweetListItemNameTextView);
		userName.setText(tweet.user.name);

		// Set the user alias, to do so, retrieve the corresponding TextView
		final TextView userAlias = (TextView) view.findViewById(R.id.tweetListItemAliasTextView);
		userAlias.setText("@" + tweet.user.screenName);

		// Set the text of the tweet, to do so, retrieve the corresponding TextView
		final TextView text = (TextView) view.findViewById(R.id.tweetListItemTweetTextView);
		text.setText(tweet.text);

		return view;
	}

	/**
	 * Better way because we rely on convertView to reuse some existing views, and be more memory efficient,
	 * but we still iterate over the view hierarchy each time the method is called to retrieve the component
	 * and set new values
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@SuppressWarnings("unused")
	private View getViewBetterWay(int position, View convertView, ViewGroup parent) {
		// If we don't have any convertView to reuse, inflate one
		if (null == convertView){
			convertView = mInflater.inflate(R.layout.tweet_listitem, null);
		}

		// Get the current item
		final Tweet tweet = (Tweet) getItem(position);

		// Set the user name, to do so, retrieve the corresponding TextView
		final TextView userName = (TextView) convertView.findViewById(R.id.tweetListItemNameTextView);
		userName.setText(tweet.user.name);

		// Set the user alias, to do so, retrieve the corresponding TextView
		final TextView userAlias = (TextView) convertView.findViewById(R.id.tweetListItemAliasTextView);
		userAlias.setText("@" + tweet.user.screenName);

		// Set the text of the tweet, to do so, retrieve the corresponding TextView
		final TextView text = (TextView) convertView.findViewById(R.id.tweetListItemTweetTextView);
		text.setText(tweet.text);

		return convertView;
	}

	/**
	 * Best way, because we reuse the convertView, and we keep a reference to the component, so we don't
	 * iterate each time over the view hierarchy to find component, it's only done once the view is inflated
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View getViewBestWay(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// If we don't have any convertView to reuse, inflate one
		if (null == convertView){
			convertView = mInflater.inflate(R.layout.tweet_listitem, null);

			// Instantiate the ViewHolder
			holder = new ViewHolder(convertView);
			// Set as tag to the convertView to retrieve it easily
			convertView.setTag(holder);
		} else {
			// Just retrieve the ViewHolder instance in the tag of the view
			holder = (ViewHolder) convertView.getTag();
		}

		// Get the current item
		final Tweet tweet = (Tweet) getItem(position);

		// Set the user name
		holder.name.setText(tweet.user.name);
		
		// Set the alias
		holder.alias.setText(tweet.user.screenName);
		
		// Set the text
		holder.text.setText(tweet.text);
		
		// Register a listener to handle the click on the button
		// And keep track of the position in the tag of the button
		holder.button.setTag(position);
		holder.button.setOnClickListener(this);
		
		// Display the images
		final Bitmap image = mImageMemoryCache.getBitmapFromMemCache(tweet.user.profileImageUrl);
		if (null == image){
			new DownloadImageAsyncTask(holder.image, mImageMemoryCache).execute(tweet.user.profileImageUrl);	
		} else {
			holder.image.setImageBitmap(image);
		}
		
		return convertView;
	}

	private class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView alias;
		public TextView text;
		public Button button;

		public ViewHolder(View view){
			image = (ImageView) view.findViewById(R.id.tweetListItemImageView);
			name = (TextView) view.findViewById(R.id.tweetListItemNameTextView);
			alias = (TextView) view.findViewById(R.id.tweetListItemAliasTextView);
			text = (TextView) view.findViewById(R.id.tweetListItemTweetTextView);
			button = (Button) view.findViewById(R.id.tweetListItemButton);
		}
	}

	@Override
	public void onClick(View view) {
		int position = (Integer) view.getTag();

        // If we have a listener set, call the retweet method
		if (null != mListener){
			final Tweet tweet = (Tweet) getItem(position);
			mListener.onRetweet(tweet);
		}
	}

}
