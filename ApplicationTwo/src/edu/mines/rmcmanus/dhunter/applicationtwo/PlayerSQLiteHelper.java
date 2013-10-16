package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_PLAYERS = "players";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLAYER_NAME = "name";
	public static final String COLUMN_PLAYER_NUMBER = "number";
	public static final String COLUMN_PLAYER_TEAM = "team";
	public static final String COLUMN_PLAYER_THROW = "throw";
	public static final String COLUMN_PLAYER_HIT = "hit";
	
	private static final String DATABASE_NAME = "statkeeper.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String PLAYER_TABLE_CREATE = 
			"create table " + TABLE_PLAYERS + "(" +
			COLUMN_ID + "integer primary key autoincrement, " +
			COLUMN_PLAYER_NAME + " text not null, " +
			COLUMN_PLAYER_NUMBER + " text not null, " +
			COLUMN_PLAYER_TEAM + " text not null, " +
			COLUMN_PLAYER_THROW + " text not null, " +
			COLUMN_PLAYER_HIT + " text not null);";
	
	protected static final String DATABASE_QUERY_SUMMARY = "select " + COLUMN_ID + ", " +
			COLUMN_PLAYER_NAME + ", " +
			COLUMN_PLAYER_NUMBER + ", " +
			COLUMN_PLAYER_TEAM + ", " +
			COLUMN_PLAYER_THROW + ", " +
			COLUMN_PLAYER_HIT + " from " + TABLE_PLAYERS;
	protected static final String DATABASE_QUERY = "select * " + TABLE_PLAYERS + " where _id =";

	public PlayerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PLAYER_TABLE_CREATE);
		
		ContentValues cv = new ContentValues();
		
		cv.put( COLUMN_PLAYER_NAME, "Bob" );
	    db.insert( TABLE_PLAYERS, null, cv );

	    cv.put( COLUMN_PLAYER_NAME, "Norman" );
	    db.insert( TABLE_PLAYERS, null, cv );
	    
	    cv.put( COLUMN_PLAYER_NAME, "Phil" );
	    db.insert( TABLE_PLAYERS, null, cv );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
		onCreate(db);
	}

}
