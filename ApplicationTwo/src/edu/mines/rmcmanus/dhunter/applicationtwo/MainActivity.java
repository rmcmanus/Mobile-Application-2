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

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public final static String EXTRA_TEAM_NAME = "edu.mines.rmcmanus.dhunter.app2.TEAMNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
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
}
