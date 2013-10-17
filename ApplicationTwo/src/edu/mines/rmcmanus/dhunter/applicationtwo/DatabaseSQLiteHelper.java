package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSQLiteHelper extends SQLiteOpenHelper {
	
	// Create teams table
	public static final String TABLE_TEAMS = "teams";
	public static final String TEAMS_COLUMN_ID = "_id";
	public static final String COLUMN_TEAM_NAME = "name";
	
	// Create players table
	public static final String TABLE_PLAYERS = "players";
	public static final String PLAYERS_COLUMN_ID = "_id";
	public static final String COLUMN_PLAYER_NAME = "name";
	public static final String COLUMN_PLAYER_NUMBER = "number";
	public static final String COLUMN_PLAYER_POSITION = "position";
	public static final String COLUMN_PLAYER_TEAM = "team";
	public static final String COLUMN_PLAYER_THROW = "throw";
	public static final String COLUMN_PLAYER_HIT = "hit";
	
	// Create fielders table
	// Defined for any player not a pitcher
	public static final String TABLE_FIELDERS = "fielders";
	public static final String FIELDERS_COLUMN_ID = "_id";
	public static final String COLUMN_FIELDER_NAME = "name";
	public static final String COLUMN_FIELDER_AT_BAT = "atbats";
	public static final String COLUMN_FIELDER_RUNS = "runs";
	public static final String COLUMN_FIELDER_HITS = "hits";
	public static final String COLUMN_FIELDER_HOME_RUNS = "homeruns";
	public static final String COLUMN_FIELDER_RBI = "rbi";
	public static final String COLUMN_FIELDER_AVG = "avg";
	
	// Create pitchers table
	// Defined as any player that is a pitcher
	public static final String TABLE_PITCHERS = "pitchers";
	public static final String PITCHERS_COLUMN_ID = "_id";
	public static final String COLUMN_PITCHER_NAME = "name";
	public static final String COLUMN_PITCHER_IP = "ip";
	public static final String COLUMN_PITCHER_WINS = "wins";
	public static final String COLUMN_PITCHER_LOSES = "loses";
	public static final String COLUMN_PITCHER_ERA = "era";
	public static final String COLUMN_PITCHER_SO = "so";
	public static final String COLUMN_PITCHER_WHIP = "whip";
	
	private static final String TEAM_TABLE_CREATE = 
			"create table " + TABLE_TEAMS + "(" +
			TEAMS_COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_TEAM_NAME + " text not null);";
	
	private static final String PLAYER_TABLE_CREATE = 
			"create table " + TABLE_PLAYERS + "(" +
			PLAYERS_COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_PLAYER_NAME + " text not null, " +
			COLUMN_PLAYER_NUMBER + " text not null, " +
			COLUMN_PLAYER_POSITION + " text not null, " +
			COLUMN_PLAYER_TEAM + " text not null, " +
			COLUMN_PLAYER_THROW + " text not null, " +
			COLUMN_PLAYER_HIT + " text not null);";
	
	private static final String FIELDER_TABLE_CREATE = 
			"create table " + TABLE_FIELDERS + "(" +
			FIELDERS_COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_FIELDER_NAME + " text not null, " +
			COLUMN_FIELDER_AT_BAT + " text not null, " +
			COLUMN_FIELDER_RUNS + " text not null, " +
			COLUMN_FIELDER_HITS + " text not null, " +
			COLUMN_FIELDER_HOME_RUNS + " text not null, " +
			COLUMN_FIELDER_RBI + " text not null, " +
			COLUMN_FIELDER_AVG + " text not null);";
	
	private static final String PITCHER_TABLE_CREATE = 
			"create table " + TABLE_PITCHERS + "(" +
			PITCHERS_COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_PITCHER_NAME + " text not null, " +
			COLUMN_PITCHER_IP + " text not null, " +
			COLUMN_PITCHER_WINS + " text not null, " +
			COLUMN_PITCHER_LOSES + " text not null, " +
			COLUMN_PITCHER_ERA + " text not null, " +
			COLUMN_PITCHER_SO + " text not null, " +
			COLUMN_PITCHER_WHIP + " text not null);";
	
	protected static final String TEAMS_QUERY_SUMMARY = "select " + TEAMS_COLUMN_ID + ", " + COLUMN_TEAM_NAME + " from " + TABLE_TEAMS;
	protected static final String TEAMS_QUERY = "select * from " + TABLE_TEAMS + " where _id =";
	
	protected static final String PLAYERS_QUERY_SUMMARY = "select " + PLAYERS_COLUMN_ID + ", " +
			COLUMN_PLAYER_NAME + ", " +
			COLUMN_PLAYER_NUMBER + ", " +
			COLUMN_PLAYER_POSITION + ", " +
			COLUMN_PLAYER_TEAM + ", " +
			COLUMN_PLAYER_THROW + ", " +
			COLUMN_PLAYER_HIT + " from " + TABLE_PLAYERS;
	protected static final String PLAYERS_QUERY = "select * from" + TABLE_PLAYERS + " where _id =";
	protected static final String PLAYERS_TEAMS_QUERY = "select * from " + TABLE_PLAYERS + " where team = ";
	
	protected static final String FIELDERS_QUERY_SUMMARY = "select " + FIELDERS_COLUMN_ID + ", " +
			COLUMN_FIELDER_NAME + ", " +
			COLUMN_FIELDER_AT_BAT + ", " +
			COLUMN_FIELDER_RUNS + ", " +
			COLUMN_FIELDER_HITS + ", " +
			COLUMN_FIELDER_HOME_RUNS + ", " +
			COLUMN_FIELDER_RBI + ", " +
			COLUMN_FIELDER_AVG + " from " + TABLE_PLAYERS;
	protected static final String FIELDERS_QUERY = "select * from " + TABLE_FIELDERS + " where name =";
	
	protected static final String PITCHERS_QUERY_SUMMARY = "select " + PITCHERS_COLUMN_ID + ", " +
			COLUMN_PITCHER_NAME + ", " +
			COLUMN_PITCHER_IP + ", " +
			COLUMN_PITCHER_WINS + ", " +
			COLUMN_PITCHER_LOSES + ", " +
			COLUMN_PITCHER_ERA + ", " +
			COLUMN_PITCHER_SO + ", " +
			COLUMN_PITCHER_WHIP + " from " + TABLE_PLAYERS;
	protected static final String PITCHERS_QUERY = "select * from " + TABLE_PITCHERS + " where name =";
	
	private static final String DATABASE_NAME = "statkeeper.db";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TEAM_TABLE_CREATE);
		db.execSQL(PLAYER_TABLE_CREATE);
		db.execSQL(FIELDER_TABLE_CREATE);
		db.execSQL(PITCHER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PITCHERS);
		onCreate(db);
	}
}
