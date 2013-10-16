package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_PLAYERS = "players";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLAYER_NAME = "name";
	public static final String COLUMN_PLAYER_NUMBER = "number";
	
	private static final String DATABASE_NAME = "statkeeper.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TEAM_DATABASE_CREATE = 
			"create table " + TABLE_PLAYERS + "(" +
			COLUMN_ID + "integer primary key autoincrement, " +
			COLUMN_PLAYER_NAME + " text not null " +
			COLUMN_PLAYER_NUMBER + " text not null);";

	public PlayerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TEAM_DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
		onCreate(db);
	}

}
