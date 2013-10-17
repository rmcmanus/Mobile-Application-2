/**
 * Description:  This class is used to list out the players that are on the team that was
 * selected in the MainActivity.  For our final submission, these names will be dynamically
 * selected from our underlying SQLite database.  For now the list contains dummy data
 * 
 * @author Ryan McManus, David Hunter
 */


package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SelectPlayerActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	public final static String EXTRA_PLAYER_NAME = "edu.mines.rmcmanus.dhunter.app2.PLAYERNAME";
	public final static String EXTRA_PLAYER_NUMBER = "edu.mines.rmcmanus.dhunter.app2.PLAYERNUMBER";
	public final static String EXTRA_PLAYER_POSITION = "edu.mines.rmcmanus.dhunter.app2.PLAYERPOSITION";
	public final static String EXTRA_PLAYER_TEAM = "edu.mines.rmcmanus.dhunter.app2.PLAYERTEAM";
	public final static String EXTRA_PLAYER_THROW = "edu.mines.rmcmanus.dhunter.app2.PLAYERTHROW";
	public final static String EXTRA_PLAYER_HIT = "edu.mines.rmcmanus.dhunter.app2.PLAYERHIT";
	
	public final static String EXTRA_ADD_PLAYER_TEAM = "edu.mines.rmcmanus.dhunter.app2.PLAYERADDTEAM";

	public final static String EXTRA_SELECT_PLAYER_PASSED = "edu.mines.rmcmanus.dhunter.app2.SELECTPLAYERPASSED";
	public String[] numberArray;
	public String[] playerArray;

	private DatabaseSQLiteHelper sqlHelper = null;
	private DatabaseCursorLoader dbLoader = null;
	private SimpleCursorAdapter cursorAdapter = null;

	private String teamName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_player);

		//receives the team name from the previous activity
		Intent intent = getIntent();
		teamName = intent.getStringExtra(MainActivity.EXTRA_TEAM_NAME);
		TextView teamNameView = (TextView) findViewById(R.id.team_name_label);
		teamNameView.setText(teamName);

		String[] from = new String[] {DatabaseSQLiteHelper.COLUMN_PLAYER_NAME, DatabaseSQLiteHelper.COLUMN_PLAYER_NUMBER, 
				DatabaseSQLiteHelper.COLUMN_PLAYER_POSITION, DatabaseSQLiteHelper.COLUMN_PLAYER_THROW, DatabaseSQLiteHelper.COLUMN_PLAYER_HIT};
		int[] to = new int[] {R.id.player_name_label, R.id.player_number_label, R.id.player_position_label, R.id.player_throws_label, R.id.player_hits_label};

		this.sqlHelper = new DatabaseSQLiteHelper(this);
		this.cursorAdapter = new SimpleCursorAdapter(this, R.layout.player_row, null, from, to, 0);		

		setListAdapter(this.cursorAdapter);
		registerForContextMenu(this.getListView());

		// Asynchronously load the data.
		loadData();

		/***************************************************************************************/
		/*****************************Dummy Data Below******************************************/
		/*
		//sets up an array list with dummy data
		ArrayList<String> players = new ArrayList<String>();
		for (int i = 0; i < 5; ++i) {
			players.add(getString(R.string.test_player) + Integer.toString(i));
		}

		String[] playerString = new String[players.size()];
		numberArray = new String[players.size()];
		playerArray = new String[players.size()];
		//populates an array for the string that will be displayed in the list view
		//as well as an array for dummy player names and numbers
		for (int i = 0; i < playerString.length; ++i) {
			numberArray[i] = Integer.toString(i + 5);
			playerString[i] = players.get(i) + "\t #" + numberArray[i];
			playerArray[i] = players.get(i);
		}
		//creates an array adapter from the array initialized above
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerString);
		//fills the list view
		ListView playerList = (ListView) findViewById(R.id.player_list);
		playerList.setAdapter(arrayAdapter);
		playerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//calls selected player with the view that was clicked and the position in the list
				//that was clicked
				selectedPlayer(v, position);
			}
		});
		 */

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.sqlHelper.close();
	}
	
	 @Override
	  public void onCreateContextMenu( ContextMenu menu, View view, ContextMenuInfo menuInfo )
	  {
	    Log.d( "ToDo: " + this.getClass().getName(), "onCreateContextMenu() ..." );
	    super.onCreateContextMenu( menu, view, menuInfo );
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate( R.menu.context_menu, menu );
	  }

	  /** Reaction to the context menu (long touch) selection */
	  @Override
	  public boolean onContextItemSelected( MenuItem item )
	  {
	    Log.d( "ToDo: " + this.getClass().getName(), "onContextItemSelected() ..." );
	    switch( item.getItemId() )
	    {
	      case R.id.deleteContext:
	        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	        this.dbLoader.delete( DatabaseSQLiteHelper.TABLE_PLAYERS, DatabaseSQLiteHelper.PLAYERS_COLUMN_ID + "=" + info.id, null );
	        return true;
	    }
	    return super.onContextItemSelected( item );
	  }

	@Override
	protected void onListItemClick( ListView listview, View view, int position, long id ) {
		Log.d( "ToDo: " + this.getClass().getName(), "onListItemClick() ..." );
		super.onListItemClick( listview, view, position, id );

		Intent playerIntent = new Intent(this, StatsActivity.class);
		SQLiteCursor sql = (SQLiteCursor) listview.getAdapter().getItem(position);

		String playerName = sql.getString(sql.getColumnIndex("name"));
		String playerNumber = sql.getString(sql.getColumnIndex("number"));
		String playerPosition = sql.getString(sql.getColumnIndex("position"));
		String playerThrow = sql.getString(sql.getColumnIndex("throw"));
		String playerHit = sql.getString(sql.getColumnIndex("hit"));

		playerIntent.putExtra(EXTRA_PLAYER_NAME, playerName);
		playerIntent.putExtra(EXTRA_PLAYER_NUMBER, playerNumber);
		playerIntent.putExtra(EXTRA_PLAYER_POSITION, playerPosition);
		playerIntent.putExtra(EXTRA_PLAYER_THROW, playerThrow);
		playerIntent.putExtra(EXTRA_PLAYER_HIT, playerHit);
		
		playerIntent.putExtra(EXTRA_SELECT_PLAYER_PASSED, true);

		startActivity(playerIntent);
	}
	
//	/**
//	 * This function takes the item in the player list that was selected and passes the
//	 * player name and their number to the next activity. 
//	 * 
//	 * @param v The view that was clicked on inside of the list view.
//	 * @param number The number of the player that was clicked on in the list view
//	 */
//	public void selectedPlayer(View v, int number) {
//		Intent playerInfoIntent = new Intent(this, StatsActivity.class);
//		playerInfoIntent.putExtra(EXTRA_PLAYER_NAME, playerArray[number]);
//		playerInfoIntent.putExtra(EXTRA_PLAYER_NUMBER, numberArray[number]);
//		playerInfoIntent.putExtra(EXTRA_SELECT_PLAYER_PASSED, true);
//		startActivity(playerInfoIntent);
//	}

	/**
	 * This function is called when the add button is pressed.  It starts the add
	 * player activity.
	 */
	public void addPlayer(View v) {
		Intent addPlayerIntent = new Intent(this, AddPlayerActivity.class);
		addPlayerIntent.putExtra(EXTRA_ADD_PLAYER_TEAM, teamName);
		startActivity(addPlayerIntent);
	}

	/**
	 * This is a throwaway function to let the user know that this application is not
	 * yet complete.
	 * @param v The parameter is the view (item) from the team list that is clicked on
	 */
	public void functionalityMissing(View v) {
		Toast.makeText(getApplicationContext(), "This functionality is not available yet!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		this.dbLoader = new DatabaseCursorLoader(this, this.sqlHelper, DatabaseSQLiteHelper.PLAYERS_TEAMS_QUERY + "'" + teamName + "'", null);
		return this.dbLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		this.cursorAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		this.cursorAdapter.swapCursor(null);
	}

	private void loadData() {
		Log.d("ToDo: " + this.getClass().getName(), "loadData() ... " + "Thread ID: " + Thread.currentThread().getId());
		getLoaderManager().initLoader( 0, null, this ); // Ensure a loader is initialized and active.
	}
}
