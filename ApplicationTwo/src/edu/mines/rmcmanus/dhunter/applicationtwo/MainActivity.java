package edu.mines.rmcmanus.dhunter.applicationtwo;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final static String EXTRA_TEAM_NAME = "edu.mines.rmcmanus.dhunter.app2.TEAMNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		ArrayList<String> teams = new ArrayList<String>();
		for (int i = 0; i < 5; ++i) {
			teams.add(getString(R.string.test_team) + Integer.toString(i));
		}
		String[] teamArray = new String[teams.size()];
		for (int i = 0; i < teamArray.length; ++i) {
			teamArray[i] = teams.get(i);
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teamArray);
		ListView teamList = (ListView) findViewById(R.id.team_list);
		teamList.setAdapter(arrayAdapter);
		teamList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				selectedTeam(v);
			}
		});
	}
	
	public void selectedTeam(View v) {
		Intent playerIntent = new Intent(this, SelectPlayerActivity.class);
		TextView tv = (TextView) v;
		playerIntent.putExtra(EXTRA_TEAM_NAME, (String) tv.getText());
		startActivity(playerIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
