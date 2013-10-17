// This application was tested on a Nexus 4
// This application was tested on API 18
/**
 * Description: This class is used to load a list of teams into a ListView.  This is the
 * initial view when the application is loaded.  The teams are loaded from a local
 * SQLite Database.
 *
 * Point Distribution: We agree on a 50/50 point distribution
 *
 * Documentation Statement: We here by solemnly swear that we did not cheat.  We used StackOverflow
 * for small examples of how code segments should be implemented (ie. ArrayAdapters for arrays in xml) 
 * and the code from class on how to implement the database, adapted for our needs. (Author Randy Bower).
 *
 * @author Ryan McManus, David Hunter
 */

package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	public final static String EXTRA_TEAM_NAME = "edu.mines.rmcmanus.dhunter.app2.TEAMNAME";

	private SQLiteOpenHelper sqlHelper = null;
	private DatabaseCursorLoader dbLoader = null;
	private SimpleCursorAdapter cursorAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		this.sqlHelper = new DatabaseSQLiteHelper(this);
		this.cursorAdapter = new SimpleCursorAdapter(this, R.layout.team_row, null, new String[] {DatabaseSQLiteHelper.COLUMN_TEAM_NAME}, new int[] {R.id.team_label}, 0);

		setListAdapter(this.cursorAdapter);

		registerForContextMenu(this.getListView());

		// Asynchronously load the data.
		loadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.sqlHelper.close();
	}

	/**
	 * This function is called when the add button is pressed on the Main Activity.
	 * This button displays a dialog box where a user can enter a team name.  If the
	 * user enters in some text, then that team will be entered into the database.
	 * Otherwise a toast message will appear that says no team was added.
	 * 
	 * @param v The view that was clicked on.
	 */
	public void addTeam(View v) {
		final CustomDialog cd = new CustomDialog(this);
		cd.show();
		cd.setCanceledOnTouchOutside(true);
		cd.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				String teamNameInsert = cd.getTeamName();
				if (teamNameInsert.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.no_team_toast), Toast.LENGTH_SHORT).show();
				} else {
					ContentValues values = new ContentValues();
					values.put(DatabaseSQLiteHelper.COLUMN_TEAM_NAME, teamNameInsert);
					dbLoader.insert(DatabaseSQLiteHelper.TABLE_TEAMS, DatabaseSQLiteHelper.COLUMN_TEAM_NAME, values);
				}
			}
		});
	}
	
	/**
	 * This function gets called when an item is clicked in the ListView.  The select
	 * player intent is then called with the team name of the item in the list that
	 * was clicked.  The activity is then started.
	 * 
	 */
	@Override
	protected void onListItemClick( ListView listview, View view, int position, long id ) {
		Log.d( "ToDo: " + this.getClass().getName(), "onListItemClick() ..." );
		super.onListItemClick( listview, view, position, id );

		Intent playerIntent = new Intent(this, SelectPlayerActivity.class);
		SQLiteCursor sql = (SQLiteCursor) listview.getAdapter().getItem(position);
		String teamName = sql.getString(sql.getColumnIndex("name"));
		playerIntent.putExtra(EXTRA_TEAM_NAME, teamName);
		startActivity(playerIntent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		this.dbLoader = new DatabaseCursorLoader(this, this.sqlHelper, DatabaseSQLiteHelper.TEAMS_QUERY_SUMMARY, null);
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

	private void loadData()
	{
		Log.d("ToDo: " + this.getClass().getName(), "loadData() ... " + "Thread ID: " + Thread.currentThread().getId());
		getLoaderManager().initLoader( 0, null, this ); // Ensure a loader is initialized and active.
	}

	@Override
	public void onCreateContextMenu( ContextMenu menu, View view, ContextMenuInfo menuInfo ) {
		Log.d( "ToDo: " + this.getClass().getName(), "onCreateContextMenu() ..." );
		super.onCreateContextMenu( menu, view, menuInfo );
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.context_menu, menu );
	}

	/** Reaction to the context menu (long touch) selection */
	@Override
	public boolean onContextItemSelected( MenuItem item ) {
		Log.d( "ToDo: " + this.getClass().getName(), "onContextItemSelected() ..." );
		switch( item.getItemId() )
		{
		case R.id.deleteContext:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			this.dbLoader.delete( DatabaseSQLiteHelper.TABLE_TEAMS, DatabaseSQLiteHelper.TEAMS_COLUMN_ID + "=" + info.id, null );
			return true;
		}
		return super.onContextItemSelected( item );
	}
}
