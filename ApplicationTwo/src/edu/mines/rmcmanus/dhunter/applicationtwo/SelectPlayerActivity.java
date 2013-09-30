/**
 * Description:  This class is used to list out the players that are on the team that was
 * selected in the MainActivity.  For our final submission, these names will be dynamically
 * selected from our underlying SQLite database.  For now the list contains dummy data
 * 
 * @author Ryan McManus, David Hunter
 */


package edu.mines.rmcmanus.dhunter.applicationtwo;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPlayerActivity extends Activity {

	public final static String EXTRA_PLAYER_NAME = "edu.mines.rmcmanus.dhunter.app2.PLAYERNAME";
	public final static String EXTRA_PLAYER_NUMBER = "edu.mines.rmcmanus.dhunter.app2.PLAYERNUMBER";
	public final static String EXTRA_SELECT_PLAYER_PASSED = "edu.mines.rmcmanus.dhunter.app2.SELECTPLAYERPASSED";
	public String[] numberArray;
	public String[] playerArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_player);
		
		//receives the team name from the previous activity
		Intent intent = getIntent();
		String teamName = intent.getStringExtra(MainActivity.EXTRA_TEAM_NAME);
		TextView teamNameView = (TextView) findViewById(R.id.team_name_label);
		teamNameView.setText(teamName);
		
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
		
	}
	
	/**
	 * This function takes the item in the player list that was selected and passes the
	 * player name and their number to the next activity. 
	 * 
	 * @param v The view that was clicked on inside of the list view.
	 * @param number The number of the player that was clicked on in the list view
	 */
	public void selectedPlayer(View v, int number) {
		Intent playerInfoIntent = new Intent(this, StatsActivity.class);
		playerInfoIntent.putExtra(EXTRA_PLAYER_NAME, playerArray[number]);
		playerInfoIntent.putExtra(EXTRA_PLAYER_NUMBER, numberArray[number]);
		playerInfoIntent.putExtra(EXTRA_SELECT_PLAYER_PASSED, true);
		startActivity(playerInfoIntent);
	}
	
	/**
	 * This function is called when the add button is pressed.  It starts the add
	 * player activity.
	 */
	public void addPlayer(View v) {
		Intent addPlayerIntent = new Intent(this, AddPlayerActivity.class);
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
}
