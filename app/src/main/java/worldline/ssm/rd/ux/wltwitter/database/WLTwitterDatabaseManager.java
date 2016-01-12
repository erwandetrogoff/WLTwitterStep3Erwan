package worldline.ssm.rd.ux.wltwitter.database;

import java.util.List;

import worldline.ssm.rd.ux.wltwitter.WLTwitterApplication;
import worldline.ssm.rd.ux.wltwitter.pojo.Tweet;
import worldline.ssm.rd.ux.wltwitter.pojo.TwitterUser;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class WLTwitterDatabaseManager {

	public static Tweet tweetFromCursor(Cursor c) {
		if (null != c) {
			final Tweet tweet = new Tweet();
			tweet.user = new TwitterUser();

			// Retrieve the date created
			if (c.getColumnIndex(WLTwitterDatabaseContract.DATE_CREATED) >= 0) {
				tweet.dateCreated = c.getString(c.getColumnIndex(WLTwitterDatabaseContract.DATE_CREATED));
			}

			// Retrieve the user name
			if (c.getColumnIndex(WLTwitterDatabaseContract.USER_NAME) >= 0) {
				tweet.user.name = c.getString(c.getColumnIndex(WLTwitterDatabaseContract.USER_NAME));
			}

			// Retrieve the user alias
			if (c.getColumnIndex(WLTwitterDatabaseContract.USER_ALIAS) >= 0) {
				tweet.user.screenName = c.getString(c.getColumnIndex(WLTwitterDatabaseContract.USER_ALIAS));
			}

			// Retrieve the user image url
			if (c.getColumnIndex(WLTwitterDatabaseContract.USER_IMAGE_URL) >= 0) {
				tweet.user.profileImageUrl = c.getString(c.getColumnIndex(WLTwitterDatabaseContract.USER_IMAGE_URL));
			}

			// Retrieve the text of the tweet
			if (c.getColumnIndex(WLTwitterDatabaseContract.TEXT) >= 0) {
				tweet.text = c.getString(c.getColumnIndex(WLTwitterDatabaseContract.TEXT));
			}

			return tweet;
		}
		return null;
	}

	public static ContentValues tweetToContentValues(Tweet tweet) {
		final ContentValues values = new ContentValues();

		// Set the date created
		if (!TextUtils.isEmpty(tweet.dateCreated)) {
			values.put(WLTwitterDatabaseContract.DATE_CREATED, tweet.dateCreated);
		}

		// Set the date created as timestamp
		values.put(WLTwitterDatabaseContract.DATE_CREATED_TIMESTAMP, tweet.getDateCreatedTimestamp());

		// Set the user name
		if (!TextUtils.isEmpty(tweet.user.name)) {
			values.put(WLTwitterDatabaseContract.USER_NAME, tweet.user.name);
		}

		// Set the user alias
		if (!TextUtils.isEmpty(tweet.user.screenName)) {
			values.put(WLTwitterDatabaseContract.USER_ALIAS, tweet.user.screenName);
		}

		// Set the user image url
		if (!TextUtils.isEmpty(tweet.user.profileImageUrl)) {
			values.put(WLTwitterDatabaseContract.USER_IMAGE_URL, tweet.user.profileImageUrl);
		}

		// Set the text of the tweet
		if (!TextUtils.isEmpty(tweet.text)) {
			values.put(WLTwitterDatabaseContract.TEXT, tweet.text);
		}

		return values;
	}


	public static void testDatabase(List<Tweet> tweets) {
		//Retrieve a writable database from your database helper
		final SQLiteOpenHelper sqLiteOpenHelper = new WLTwitterDatabaseHelper(WLTwitterApplication.getContext());
		final SQLiteDatabase tweetsDatabase = sqLiteOpenHelper.getWritableDatabase();

		// then iterate over the list of tweets and insert all tweets in database

		for (Tweet tweet : tweets) {
			final ContentValues contentValues = new ContentValues();
			contentValues.put(WLTwitterDatabaseContract.USER_NAME, tweet.user.name);
			contentValues.put(WLTwitterDatabaseContract.USER_ALIAS, tweet.user.screenName);
			contentValues.put(WLTwitterDatabaseContract.DATE_CREATED, tweet.dateCreated);
			contentValues.put(WLTwitterDatabaseContract.DATE_CREATED_TIMESTAMP, tweet.dateCreated);
			//...

			contentValues.put(WLTwitterDatabaseContract.USER_IMAGE_URL, tweet.user.profileImageUrl);
			tweetsDatabase.insert(WLTwitterDatabaseContract.TABLE_TWEETS, "", contentValues);
		}


		//Finally, after inserting all tweets in database, query the database to retrieve all entries as cursor and log
		final Cursor cursor = tweetsDatabase.query(WLTwitterDatabaseContract.TABLE_TWEETS, WLTwitterDatabaseContract.PROJECTION_FULL, null, null, null, null, null);

		//iterate over the retrieved values
		while (cursor.moveToNext()){
			final String tweetUserName = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract.USER_NAME));
			final String tweetUserAlias = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract.USER_ALIAS));
			final String tweetText = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract.TEXT));
			final String tweetUserImageUrl = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract.USER_IMAGE_URL));
			final String tweetDateCreated = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract.DATE_CREATED));
			final String tweetDateCreatedTimestamp = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract.DATE_CREATED_TIMESTAMP));
			final String tweetId = cursor.getString(cursor.getColumnIndex(WLTwitterDatabaseContract._ID));
//TODO pas sur du tout

		}

		//close the cursor
		if (!cursor.isClosed()){
			cursor.close();
		}
}
	public static void testContentProvider(List<Tweet> tweets){
		// TODO Test your ContentProvider here
	}

}
