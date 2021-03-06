/**
 * Description: This class shows the statistics of the selected player from the {@link SelectPlayerActivity}
 * or a new page generated by the {@link AddPlayerActivity} class.  This displays a page based on
 * what type of position the player plays.  Allows for stats update when the "Update" button
 * is pressed.
 * 
 * @author Ryan McManus, David Hunter
 */


package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StatsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	public EditText stat1;
	public EditText stat2;
	public EditText stat3;
	public EditText stat4;
	public EditText stat5;
	public EditText stat6;
	public EditText[] editArray;
	
	public Button updateDoneButton;
	public boolean doneEditing = false;
	public boolean pitcherSelection = false, fielderSelection = false;
	public String playerName = "", playerNumber = "", playerPosition = "", playerBats = "", playerThrows = "";
	
	private DatabaseSQLiteHelper sqlHelper = null;
	private DatabaseCursorLoader dbLoader = null;
	private SimpleCursorAdapter cursorAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		
		//grabs the player name and number from the previous activity
		Intent intent = getIntent();
		boolean selectPassed = intent.getBooleanExtra(SelectPlayerActivity.EXTRA_SELECT_PLAYER_PASSED, false);
		boolean addPassed = intent.getBooleanExtra(AddPlayerActivity.EXTRA_ADD_PLAYER_PASSED, false);
		//Sets information based on which activity you got to this page from
		if (selectPassed) {
			playerName = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_NAME);
			playerNumber = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_NUMBER);
			playerPosition = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_POSITION);
			playerThrows = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_THROW);
			playerBats = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_HIT);
		}
		else if (addPassed) {
			playerName = intent.getStringExtra(AddPlayerActivity.EXTRA_PLAYER_NAME);
			playerNumber = intent.getStringExtra(AddPlayerActivity.EXTRA_PLAYER_NUMBER);
			playerPosition = intent.getStringExtra(AddPlayerActivity.EXTRA_PLAYER_POSITION);
			playerThrows = intent.getStringExtra(AddPlayerActivity.EXTRA_PLAYER_THROWS);
			playerBats = intent.getStringExtra(AddPlayerActivity.EXTRA_PLAYER_BATS);
		}
		
		//Adds information about the player
		TextView playerPositionView = (TextView) findViewById(R.id.position_text_view);
		playerPositionView.setText(playerPosition);
		TextView playerBatsThrowsView = (TextView) findViewById(R.id.throws_bats_text_view);
		playerBatsThrowsView.setText(playerThrows.toCharArray()[0] + "/" + playerBats.toCharArray()[0]);
		
		TextView playerNameView = (TextView) findViewById(R.id.player_label);
		TextView playerNumberView = (TextView) findViewById(R.id.number_text_view);
		//Sets the player name and number
		playerNameView.setText(playerName);
		playerNumberView.setText("#" + playerNumber);
		
		//gets each edit text view for holding stats
		stat1 = (EditText) findViewById(R.id.stat_1_text_view);
		stat2 = (EditText) findViewById(R.id.stat_2_text_view);
		stat3 = (EditText) findViewById(R.id.stat_3_text_view);
		stat4 = (EditText) findViewById(R.id.stat_4_text_view);
		stat5 = (EditText) findViewById(R.id.stat_5_text_view);
		stat6 = (EditText) findViewById(R.id.stat_6_text_view);
		//puts each edit text into an array
		editArray = new EditText[6];
		editArray[0] = stat1;
		editArray[1] = stat2;
		editArray[2] = stat3;
		editArray[3] = stat4;
		editArray[4] = stat5;
		editArray[5] = stat6;
		//gets the update button
		updateDoneButton = (Button) findViewById(R.id.stats_update_button);
		//calls the function to disable all of the edit text views
		makeDisabled();
		
		checkPlayerPosition(playerPosition);
		
		String[] from = null;
		//Queries the respective database based on if the player is a pitcher or a fielder
		if (pitcherSelection) {
			from = new String[] {DatabaseSQLiteHelper.COLUMN_PITCHER_IP, DatabaseSQLiteHelper.COLUMN_PITCHER_WINS, 
					DatabaseSQLiteHelper.COLUMN_PITCHER_LOSES, DatabaseSQLiteHelper.COLUMN_PITCHER_ERA, 
					DatabaseSQLiteHelper.COLUMN_PITCHER_SO, DatabaseSQLiteHelper.COLUMN_PITCHER_WHIP};
		}
		else if (fielderSelection) {
			from = new String[] {DatabaseSQLiteHelper.COLUMN_FIELDER_AT_BAT, DatabaseSQLiteHelper.COLUMN_FIELDER_RUNS, 
					DatabaseSQLiteHelper.COLUMN_FIELDER_HITS, DatabaseSQLiteHelper.COLUMN_FIELDER_HOME_RUNS, 
					DatabaseSQLiteHelper.COLUMN_FIELDER_RBI, DatabaseSQLiteHelper.COLUMN_FIELDER_AVG};
		}
		int[] to = new int[] {R.id.stat_1_text_view, R.id.stat_2_text_view, R.id.stat_3_text_view, R.id.stat_4_text_view,
				R.id.stat_5_text_view, R.id.stat_6_text_view};
		
		this.sqlHelper = new DatabaseSQLiteHelper(this);
		this.cursorAdapter = new SimpleCursorAdapter(this, R.layout.player_row, null, from, to, 0);
		
		loadData();
	}
	
	/**
	 * Disallows the user from going back, otherwise could add information to the
	 * database twice
	 */
	@Override
	public void onBackPressed() {
	}
	
	/**
	 * This function iterates through each of the edit text views and disables them so that
	 * the user cannot edit the values.
	 */
	public void makeDisabled() {
		for (EditText e : editArray) {
			e.setEnabled(false);
		}
	}
	
	/**
	 * This function iterates though each of the edit text views and enables them so that
	 * the user can edit the values.
	 */
	public void makeEnabled() {
		for (EditText e : editArray) {
			e.setEnabled(true);
		}
	}
	
	/**
	 * This function is called when the update/save button is pressed.  It determines if the
	 * edit text views should be editable or not, and changes the text of the button from
	 * update to save if the user is in edit mode.  If the user presses save, then the data
	 * is saved to the database
	 * 
	 * @param v
	 */
	public void update(View v) {
		//If the user is done editing, then change the text of the button to update and
		//disable the edit texts
		if (doneEditing) {
			makeDisabled();
			updateDoneButton.setText(getString(R.string.update));
			doneEditing = false;
			saveToDb();
		} 
		//If the user is in not in edit mode and the button is press, change the text of the
		//button to save and enable the edit text boxes for editing.
		else {
			makeEnabled();
			updateDoneButton.setText(getString(R.string.save_button));
			doneEditing = true;
		}
	}
	
	
	/**
	 * This function is called if the user presses the save button.  The stats for the
	 * player will be saved to their respective database based on if they are a fielder
	 * or a pitcher.
	 * 
	 */
	public void saveToDb() {
		String stat1 = ((EditText) findViewById(R.id.stat_1_text_view)).getText().toString();
		String stat2 = ((EditText) findViewById(R.id.stat_2_text_view)).getText().toString();
		String stat3 = ((EditText) findViewById(R.id.stat_3_text_view)).getText().toString();
		String stat4 = ((EditText) findViewById(R.id.stat_4_text_view)).getText().toString();
		String stat5 = ((EditText) findViewById(R.id.stat_5_text_view)).getText().toString();
		String stat6 = ((EditText) findViewById(R.id.stat_6_text_view)).getText().toString();
		
		String[] test = new String[] {playerName};
		Log.d("Debug", playerName);
		
		ContentValues values = new ContentValues();
		if (pitcherSelection) {
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_NAME, playerName);
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_IP, stat1);
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_WINS, stat2);
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_LOSES, stat3);
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_ERA, stat4);
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_SO, stat5);
			values.put(DatabaseSQLiteHelper.COLUMN_PITCHER_WHIP, stat6);
			
			if (cursorAdapter.getCount() > 0)
				dbLoader.update(DatabaseSQLiteHelper.TABLE_PITCHERS, values, "name = ?", test);
			else {
				dbLoader.insert(DatabaseSQLiteHelper.TABLE_PITCHERS, DatabaseSQLiteHelper.COLUMN_PITCHER_NAME, values);
			}
		} else {
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_NAME, playerName);
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_AT_BAT, stat1);
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_RUNS, stat2);
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_HITS, stat3);
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_HOME_RUNS, stat4);
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_RBI, stat5);
			values.put(DatabaseSQLiteHelper.COLUMN_FIELDER_AVG, stat6);
			
			if (cursorAdapter.getCount() > 0)
				dbLoader.update(DatabaseSQLiteHelper.TABLE_FIELDERS, values, "name = ?", test);
			else {
				dbLoader.insert(DatabaseSQLiteHelper.TABLE_FIELDERS, DatabaseSQLiteHelper.COLUMN_FIELDER_NAME, values);
			}
		}
	}
	
	/**
	 * If the Done button is pressed, then the user is taken back to the MainActivty
	 * 
	 * @param v The view that was pressed
	 */
	public void finished(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	/**
	 * This function grabs every TextView that is on the stats page of a player, and changes the heading based on which
	 * position that player plays. If the player is a pitcher, then the view updates with pitcher-specific information
	 * populated by our database (final release). These include IP, W/L, SO, etc. Otherwise it changes the headings for
	 * fielder, which include HR, R, H, AVG, etc. These are also populated by our database (final release).
	 * 
	 * @param playerPosition	Current player position defined either by a new player being added or in selecting a player
	 */
	public void checkPlayerPosition(String playerPosition) {
		TextView col1 = (TextView) findViewById(R.id.col_1_text_view);
		TextView col2 = (TextView) findViewById(R.id.col_2_text_view);
		TextView col3 = (TextView) findViewById(R.id.col_3_text_view);
		TextView col4 = (TextView) findViewById(R.id.col_4_text_view);
		TextView col5 = (TextView) findViewById(R.id.col_5_text_view);
		TextView col6 = (TextView) findViewById(R.id.col_6_text_view);
		
		if (playerPosition.equals(getString(R.string.pitcher_test))) {
			this.setPitcherTags(col1, col2, col3, col4, col5, col6);
			pitcherSelection = true;
		}
		else {
			this.setFielderTags(col1, col2, col3, col4, col5, col6);
			fielderSelection = true;
		}
	}
	
	/**
	 * This function populates the texts views for the column headings
	 * based on if the player is a pitcher.
	 * 
	 * @param textViews The list of text views
	 */
	public void setPitcherTags(TextView...textViews) {
		textViews[0].setText(getString(R.string.pitcher_ip));
		textViews[1].setText(getString(R.string.pitcher_w));
		textViews[2].setText(getString(R.string.pitcher_l));
		textViews[3].setText(getString(R.string.pitcher_era));
		textViews[4].setText(getString(R.string.pitcher_so));
		textViews[5].setText(getString(R.string.pitcher_whip));
	}
	
	/**
	 * This function populates the texts views for the column headings
	 * based on if the player is a fielder.
	 * 
	 * @param textViews The list of text views
	 */
	public void setFielderTags(TextView...textViews) {
		textViews[0].setText(getString(R.string.fielder_ab));
		textViews[1].setText(getString(R.string.fielder_r));
		textViews[2].setText(getString(R.string.fielder_h));
		textViews[3].setText(getString(R.string.fielder_hr));
		textViews[4].setText(getString(R.string.fielder_rbi));
		textViews[5].setText(getString(R.string.fielder_avg));
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		if (pitcherSelection) {
			this.dbLoader = new DatabaseCursorLoader(this, this.sqlHelper, DatabaseSQLiteHelper.PITCHERS_QUERY + "'" + playerName + "'", null);
		}
		else if (fielderSelection)
			this.dbLoader = new DatabaseCursorLoader(this, this.sqlHelper, DatabaseSQLiteHelper.FIELDERS_QUERY + "'" + playerName + "'", null);
		return this.dbLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		this.cursorAdapter.swapCursor(data);
		setEditText();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		this.cursorAdapter.swapCursor(null);
	}
	
	private void loadData() {
		Log.d("ToDo: " + this.getClass().getName(), "loadData() ... " + "Thread ID: " + Thread.currentThread().getId());
		getLoaderManager().initLoader( 0, null, this ); // Ensure a loader is initialized and active.
	}
	
	/**
	 * This function sets each of the edit text views based on the stats that are in the
	 * database corresponding to the player.  The function is called after the data
	 * has been loaded asynchronously 
	 * 
	 */
	
	public void setEditText() {
		SQLiteCursor sql = (SQLiteCursor) cursorAdapter.getItem(0);
		String stat1, stat2, stat3, stat4, stat5, stat6;
		if (pitcherSelection) {
			stat1 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_PITCHER_IP));
		    stat2 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_PITCHER_WINS));
		    stat3 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_PITCHER_LOSES));
		    stat4 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_PITCHER_ERA));
		    stat5 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_PITCHER_SO));
		    stat6 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_PITCHER_WHIP));
		} else {
			stat1 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_FIELDER_AT_BAT));
		    stat2 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_FIELDER_RUNS));
		    stat3 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_FIELDER_HITS));
		    stat4 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_FIELDER_HOME_RUNS));
		    stat5 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_FIELDER_RBI));
		    stat6 = sql.getString(sql.getColumnIndex(DatabaseSQLiteHelper.COLUMN_FIELDER_AVG));
		}
	    
	    
	    EditText col1 = (EditText) findViewById(R.id.stat_1_text_view);
	    col1.setText(stat1);
	    EditText col2 = (EditText) findViewById(R.id.stat_2_text_view);
	    col2.setText(stat2);
	    EditText col3 = (EditText) findViewById(R.id.stat_3_text_view);
	    col3.setText(stat3);
	    EditText col4 = (EditText) findViewById(R.id.stat_4_text_view);
	    col4.setText(stat4);
	    EditText col5 = (EditText) findViewById(R.id.stat_5_text_view);
	    col5.setText(stat5);
	    EditText col6 = (EditText) findViewById(R.id.stat_6_text_view);
	    col6.setText(stat6);
	}
}
