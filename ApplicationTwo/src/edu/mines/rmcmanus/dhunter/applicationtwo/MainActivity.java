// This application was tested on a Nexus 4
// This application was tested on API 17
/**
* Description: This class is used to load a list of teams into a ListView.  This is the
* initial view when the application is loaded.  For the final submission the teams will
* be loaded dynamically from a SQLite database stored locally on the device.  Right now
* the list is populated with dummy data.
*
* Point Distribution: We agree on a 50/50 point distribution
*
* Documentation Statement: We here by solemnly swear that we did not cheat.  We used StackOverflow
* for small examples of how code segments should be implemented (ie. ArrayAdapters for arrays in xml) 
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
import android.widget.TextView;
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
		
		//ListView lv = (ListView) findViewById(android.R.id.list);
		setListAdapter(this.cursorAdapter);
		//lv.setAdapter(cursorAdapter);
	    //lv.setDividerHeight(2);

		registerForContextMenu(this.getListView());
		
	    // Asynchronously load the data.
	    loadData();
		
		
/***************************************************************************************/
/*****************************Dummy Data Below******************************************/
	    /*
	     
		//adds some dummy data to an array list to test the ListView
		ArrayList<String> teams = new ArrayList<String>();
		for (int i = 0; i < 5; ++i) {
			teams.add(getString(R.string.test_team) + Integer.toString(i));
		}
		String[] teamArray = new String[teams.size()];
		//Converts the array list into an array for use with the ArrayAdapter
		for (int i = 0; i < teamArray.length; ++i) {
			teamArray[i] = teams.get(i);
		}
		//creates an array adapter with a simple list view, and the array of dummy teams
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teamArray);
		//finds the list view and sets the array adapter to the adapter initialized above
		ListView teamList = (ListView) findViewById(R.id.team_list);
		teamList.setAdapter(arrayAdapter);
		teamList.setOnItemClickListener(new OnItemClickListener() {
			//listens for a item in the list to be clicked on
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//calls selected team with the view that was pressed
				selectedTeam(v);
			}
		});
		
		*/
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.sqlHelper.close();
	}
	
	/**
	 * This function takes the team that was clicked on in the team list and calls the select
	 * player activity with the name of the team that was selected
	 * 
	 * @param v The parameter is the view (item) from the team list that is clicked on
	 */
	public void selectedTeam(View v) {
		Intent playerIntent = new Intent(this, SelectPlayerActivity.class);
		TextView tv = (TextView) v;
		playerIntent.putExtra(EXTRA_TEAM_NAME, (String) tv.getText());
		startActivity(playerIntent);
	}
	
	/**
	 * This is a throwaway function to let the user know that this application is not
	 * yet complete.
	 * @param v The parameter is the view (item) from the team list that is clicked on
	 */
	public void functionalityMissing(View v) {
		Toast.makeText(getApplicationContext(), "This functionality is not available yet!", Toast.LENGTH_SHORT).show();
	}
	
	public void addTeam(View v) {
		final CustomDialog cd = new CustomDialog(this);
		cd.show();
		cd.setCanceledOnTouchOutside(true);
//		cd.setOnCancelListener(new OnCancelListener() {
//			
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				Toast.makeText(getApplicationContext(), "No Team Added", Toast.LENGTH_SHORT).show();
//			}
//		});
		cd.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				String teamNameInsert = cd.getTeamName();
				if (teamNameInsert.equals("")) {
					Toast.makeText(getApplicationContext(), "No Team Added", Toast.LENGTH_SHORT).show();
				} else {
					ContentValues values = new ContentValues();
				    values.put(DatabaseSQLiteHelper.COLUMN_TEAM_NAME, teamNameInsert);
					dbLoader.insert(DatabaseSQLiteHelper.TABLE_TEAMS, DatabaseSQLiteHelper.COLUMN_TEAM_NAME, values);
				}
			}
		});
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
	        this.dbLoader.delete( DatabaseSQLiteHelper.TABLE_TEAMS, DatabaseSQLiteHelper.TEAMS_COLUMN_ID + "=" + info.id, null );
	        return true;
	    }
	    return super.onContextItemSelected( item );
	  }
	
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
}
