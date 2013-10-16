package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PitcherStatSQLiteHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_PITCHERS = "pitchers";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PITCHER_NAME = "name";
	public static final String COLUMN_PITCHER_IP = "ip";
	public static final String COLUMN_PITCHER_WINS = "wins";
	public static final String COLUMN_PITCHER_LOSES = "loses";
	public static final String COLUMN_PITCHER_ERA = "era";
	public static final String COLUMN_PITCHER_SO = "so";
	public static final String COLUMN_PITCHER_WHIP = "whip";
	
	private static final String DATABASE_NAME = "statkeeper.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TEAM_DATABASE_CREATE = 
			"create table " + TABLE_PITCHERS + "(" +
			COLUMN_ID + "integer primary key autoincrement, " +
			COLUMN_PITCHER_NAME + " text not null " +
			COLUMN_PITCHER_IP + " text not null " +
			COLUMN_PITCHER_WINS + " text not null " +
			COLUMN_PITCHER_LOSES + " text not null " +
			COLUMN_PITCHER_ERA + " text not null " +
			COLUMN_PITCHER_SO + " text not null " +
			COLUMN_PITCHER_WHIP + " text not null);";

	public PitcherStatSQLiteHelper(Context context) {
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PITCHERS);
		onCreate(db);
	}

}
