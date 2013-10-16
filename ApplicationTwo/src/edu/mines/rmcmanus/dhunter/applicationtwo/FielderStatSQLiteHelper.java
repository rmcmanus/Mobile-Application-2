package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FielderStatSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_FIELDERS = "fielders";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FIELDER_NAME = "name";
	public static final String COLUMN_FIELDER_AT_BAT = "at bats";
	public static final String COLUMN_FIELDER_RUNS = "runs";
	public static final String COLUMN_FIELDER_HITS = "hits";
	public static final String COLUMN_FIELDER_HOME_RUNS = "home runs";
	public static final String COLUMN_FIELDER_RBI = "rbi";
	public static final String COLUMN_FIELDER_AVG = "avg";
	
	private static final String DATABASE_NAME = "statkeeper.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TEAM_DATABASE_CREATE = 
			"create table " + TABLE_FIELDERS + "(" +
			COLUMN_ID + "integer primary key autoincrement, " +
			COLUMN_FIELDER_NAME + " text not null " +
			COLUMN_FIELDER_AT_BAT + " text not null " +
			COLUMN_FIELDER_RUNS + " text not null " +
			COLUMN_FIELDER_HITS + " text not null " +
			COLUMN_FIELDER_HOME_RUNS + " text not null " +
			COLUMN_FIELDER_RBI + " text not null " +
			COLUMN_FIELDER_AVG + " text not null);";

	public FielderStatSQLiteHelper(Context context) {
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDERS);
		onCreate(db);
	}

}
