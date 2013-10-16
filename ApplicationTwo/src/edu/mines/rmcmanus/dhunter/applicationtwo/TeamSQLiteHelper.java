package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_TEAMS = "teams";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEAM_NAME = "name";
	
	private static final String DATABASE_NAME = "statkeeper.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TEAM_DATABASE_CREATE = 
			"create table " + TABLE_TEAMS + "(" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_TEAM_NAME + " text not null);";

	public TeamSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	protected static final String DATABASE_QUERY_SUMMARY = "select " + COLUMN_ID + ", " + COLUMN_TEAM_NAME + " from " + TABLE_TEAMS;
	protected static final String DATABASE_QUERY = "select * " + TABLE_TEAMS + " where _id =";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TEAM_DATABASE_CREATE);
		
		ContentValues cv = new ContentValues();

	    // Insert a bit of test data ... yes, hard-coded strings ... but it's just test data!
	    cv.put( COLUMN_TEAM_NAME, "Normal" );
	    db.insert( TABLE_TEAMS, null, cv );

	    cv.put( COLUMN_TEAM_NAME, "Unnormal" );
	    db.insert( TABLE_TEAMS, null, cv );
	    
	    cv.put( COLUMN_TEAM_NAME, "So not normal" );
	    db.insert( TABLE_TEAMS, null, cv );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
		onCreate(db);
	}

}
