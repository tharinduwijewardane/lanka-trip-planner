package com.lanka.tripplanner.util;

import java.util.ArrayList;
import com.lanka.tripplanner.core.Place;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {

	private static final String DATABASE_NAME = "lankatripplannerDB";
	private static final String DATABASE_TABLE_PLACE = "place";
	private static final String DATABASE_TABLE_TIME = "time";
	private static final int DATABASE_VERSION = 1;

	private DbHelper mDbHelper;
	private final Context mContext;
	private SQLiteDatabase mSqLiteDatabase;

	private static Context cnt;

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_RATING = "rating";
	public static final String KEY_VISITING_TIME = "visiting_time";
	public static final String KEY_PROVINCE = "province";

	public static final String KEY_FROM = "source";
	public static final String KEY_TO = "destination";
	public static final String KEY_TIME_TO_TRAVEL = "time_to_travel";

	private static class DbHelper extends SQLiteOpenHelper {

		private static final String TABLE_PLACE_CREATE = "create table "
				+ DATABASE_TABLE_PLACE + " (" + KEY_ROWID
				+ " integer primary key autoincrement, " + KEY_NAME
				+ " varchar(50), " + KEY_LATITUDE + " real, " + KEY_LONGITUDE
				+ " real, " + KEY_CATEGORY + " text, " + KEY_RATING
				+ " integer, " + KEY_VISITING_TIME + " integer, "
				+ KEY_PROVINCE + " text);";

		private static final String TABLE_TIME_CREATE = "create table "
				+ DATABASE_TABLE_TIME + " (" + KEY_ROWID
				+ " integer primary key autoincrement, " + KEY_FROM
				+ " varchar(50), " + KEY_TO + " varchar(50), "
				+ KEY_TIME_TO_TRAVEL + " text);";

		DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			cnt = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_PLACE_CREATE);
			db.execSQL(TABLE_TIME_CREATE);

			// TODO use here to initialize the database with initial data
			InitData initBatch = new InitData();

			Place[] initPlaces = initBatch.getInitPlaces(cnt);
			int[][] initDurations = initBatch.getInitDurations();

			Place tempPlace;
			for (int i = 0; i < initPlaces.length; i++) {
				tempPlace = initPlaces[i];
				db.execSQL("INSERT INTO " + DATABASE_TABLE_PLACE + " ("
						+ KEY_NAME + "," + KEY_LATITUDE + "," + KEY_LONGITUDE
						+ "," + KEY_CATEGORY + "," + KEY_RATING + ","
						+ KEY_VISITING_TIME + "," + KEY_PROVINCE
						+ ") VALUES ('" + tempPlace.getName() + "',"
						+ tempPlace.getLati() + "," + tempPlace.getLongi()
						+ ",'" + tempPlace.getCategory() + "',"
						+ tempPlace.getRating() + ","
						+ tempPlace.getTimeToVisit() + ",'"
						+ tempPlace.getProvince() + "');");
			}

			for (int j = 0; j < initPlaces.length; j++) {
				for (int k = 0; k < initPlaces.length; k++) {
					db.execSQL("INSERT INTO " + DATABASE_TABLE_TIME + " ("
							+ KEY_FROM + "," + KEY_TO + ","
							+ KEY_TIME_TO_TRAVEL + ") VALUES ('"
							+ initPlaces[j].getName() + "','"
							+ initPlaces[k].getName() + "',"
							+ initDurations[j][k] + ");");
				}
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PLACE + ";");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TIME + ";");
			onCreate(db);
		}

	}

	/**
	 * @param context
	 */
	public Database(Context context) {
		mContext = context;
	}

	/**
	 * @return a writable database instance
	 */
	public Database openForWrite() {
		mDbHelper = new DbHelper(mContext);
		mSqLiteDatabase = mDbHelper.getWritableDatabase();
		return this;
	}

	public Database open() throws SQLException {
		if (mDbHelper == null) {
			mDbHelper = new DbHelper(mContext);
			mSqLiteDatabase = mDbHelper.getReadableDatabase();
		}
		return this;
	}

	public void close() {
		if (mSqLiteDatabase != null) {
			mSqLiteDatabase.close();
		}
		mDbHelper.close();
	}

	/**
	 * insert a Place entry.
	 * 
	 * use as follows Database mDatabase = new Database(getActivity());
	 * mDatabase.openForWrite(); mDatabase.insertEntry(place);
	 * mDatabase.close();
	 * 
	 * @param place
	 * @return row id of the inserted row. -1 if error occurred
	 */
	public long insertPlace(Place place) {

		ContentValues cv = new ContentValues();

		cv.put(KEY_NAME, place.getName());
		cv.put(KEY_LATITUDE, place.getLati());
		cv.put(KEY_LONGITUDE, place.getLongi());
		cv.put(KEY_CATEGORY, place.getCategory());
		cv.put(KEY_RATING, place.getRating());
		cv.put(KEY_VISITING_TIME, place.getTimeToVisit());
		cv.put(KEY_PROVINCE, place.getProvince());

		return mSqLiteDatabase.insert(DATABASE_TABLE_PLACE, null, cv);
	}

	/**
	 * insert time to travel between two places.
	 * 
	 * @param from
	 * @param to
	 * @param timeToTravel
	 * @return row id of the inserted row. -1 if error occurred
	 */
	public long insertTimeToTravel(String from, String to, int timeToTravel) {

		ContentValues cv = new ContentValues();

		cv.put(KEY_FROM, from);
		cv.put(KEY_TO, to);
		cv.put(KEY_TIME_TO_TRAVEL, timeToTravel);

		return mSqLiteDatabase.insert(DATABASE_TABLE_TIME, null, cv);
	}

	/**
	 * @param from
	 * @param to
	 * @return time to travel from "from" to "to"
	 */
	public int getTimeToTravel(String from, String to) {

		int timeToTravel;

		Cursor c = mSqLiteDatabase.query(DATABASE_TABLE_TIME,
				new String[] { KEY_TIME_TO_TRAVEL }, KEY_FROM + " = '" + from
						+ "' AND " + KEY_TO + " = '" + to + "';", null, null,
				null, null, null);

		if (c != null) {
			c.moveToFirst();
			timeToTravel = c.getInt(c.getColumnIndex(KEY_TIME_TO_TRAVEL));
		} else {
			timeToTravel = 0;
		}
		c.close();

		return timeToTravel;
	}

	/**
	 * @return a list of all stored Places
	 */
	public ArrayList<Place> readData() {

		ArrayList<Place> places = new ArrayList<Place>();

		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_LATITUDE,
				KEY_LONGITUDE, KEY_CATEGORY, KEY_RATING, KEY_VISITING_TIME,
				KEY_PROVINCE };
		Cursor c = mSqLiteDatabase.query(DATABASE_TABLE_PLACE, columns, null,
				null, null, null, null);

		int iRowId = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_NAME);
		int iLat = c.getColumnIndex(KEY_LATITUDE);
		int iLon = c.getColumnIndex(KEY_LONGITUDE);
		int iCategory = c.getColumnIndex(KEY_CATEGORY);
		int iRating = c.getColumnIndex(KEY_RATING);
		int iVisitingTime = c.getColumnIndex(KEY_VISITING_TIME);
		int iProvince = c.getColumnIndex(KEY_PROVINCE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			String name = c.getString(iName);
			double latitude = c.getDouble(iLat);
			double longitude = c.getDouble(iLon);
			String category = c.getString(iCategory);
			int rating = c.getInt(iRating);
			int visitingTime = c.getInt(iVisitingTime);
			String province = c.getString(iProvince);

			Place place = new Place(rating, visitingTime, name, latitude,
					longitude, category, province);
			places.add(place);
		}

		c.close();
		return places;
	}
}