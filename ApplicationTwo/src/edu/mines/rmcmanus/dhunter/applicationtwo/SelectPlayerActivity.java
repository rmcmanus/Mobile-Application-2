/**
 * Description:  This class is used to list out the players that are on the team that was
 * selected in the MainActivity.  The list of these players are the results of a query
 * to the database.
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

	}
	
	/**
	 * This function is called when a user clicks on an item in the list view.  The
	 * function then passes all the information about the selected player (name, number
	 * position, throwing hand and hitting hand) to the StatsActivity.
	 * 
	 */
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

	/**
	 * This function is called when the add button is pressed.  It starts the add
	 * player activity.
	 */
	public void addPlayer(View v) {
		Intent addPlayerIntent = new Intent(this, AddPlayerActivity.class);
		addPlayerIntent.putExtra(EXTRA_ADD_PLAYER_TEAM, teamName);
		startActivity(addPlayerIntent);
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
}
