package edu.mines.rmcmanus.dhunter.applicationtwo;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SelectPlayerActivity extends Activity {

	public final static String EXTRA_PLAYER_NAME = "edu.mines.rmcmanus.dhunter.app2.PLAYERNAME";
	public final static String EXTRA_PLAYER_NUMBER = "edu.mines.rmcmanus.dhunter.app2.PLAYERNUMBER";
	public String[] numberArray;
	public String[] playerArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_player);
		
		Intent intent = getIntent();
		String teamName = intent.getStringExtra(MainActivity.EXTRA_TEAM_NAME);
		TextView teamNameView = (TextView) findViewById(R.id.team_name_label);
		teamNameView.setText(teamName);
		
		ArrayList<String> players = new ArrayList<String>();
		for (int i = 0; i < 5; ++i) {
			players.add(getString(R.string.test_player) + Integer.toString(i));
		}
		String[] playerString = new String[players.size()];
		numberArray = new String[players.size()];
		playerArray = new String[players.size()];
		for (int i = 0; i < playerString.length; ++i) {
			numberArray[i] = Integer.toString(i + 5);
			playerString[i] = players.get(i) + "\t #" + numberArray[i];
			playerArray[i] = players.get(i);
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerString);
		ListView playerList = (ListView) findViewById(R.id.player_list);
		playerList.setAdapter(arrayAdapter);
		playerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				selectedPlayer(v, position);
			}
		});
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	public void selectedPlayer(View v, int number) {
		Intent playerInfoIntent = new Intent(this, StatsActivity.class);
//		TextView tv = (TextView) v;
//		playerInfoIntent.putExtra(EXTRA_PLAYER_NAME, (String) tv.getText());
		playerInfoIntent.putExtra(EXTRA_PLAYER_NAME, playerArray[number]);
		playerInfoIntent.putExtra(EXTRA_PLAYER_NUMBER, numberArray[number]);
		startActivity(playerInfoIntent);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_player, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
