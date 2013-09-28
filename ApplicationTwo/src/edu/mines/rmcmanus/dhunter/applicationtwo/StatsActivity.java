package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatsActivity extends Activity {

	public EditText stat1;
	public EditText stat2;
	public EditText stat3;
	public EditText stat4;
	public EditText stat5;
	public EditText stat6;
	public EditText[] editArray;
	public Button updateDoneButton;
	public boolean doneEditing = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		
		Intent intent = getIntent();
		String playerName = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_NAME);
		String playerNumber = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_NUMBER);
		TextView playerNameView = (TextView) findViewById(R.id.player_label);
		TextView playerNumberView = (TextView) findViewById(R.id.number_text_view);
		playerNameView.setText(playerName);
		playerNumberView.setText("#" + playerNumber);
		
		stat1 = (EditText) findViewById(R.id.stat_1_text_view);
		stat2 = (EditText) findViewById(R.id.stat_2_text_view);
		stat3 = (EditText) findViewById(R.id.stat_3_text_view);
		stat4 = (EditText) findViewById(R.id.stat_4_text_view);
		stat5 = (EditText) findViewById(R.id.stat_5_text_view);
		stat6 = (EditText) findViewById(R.id.stat_6_text_view);
		editArray = new EditText[6];
		editArray[0] = stat1;
		editArray[1] = stat2;
		editArray[2] = stat3;
		editArray[3] = stat4;
		editArray[4] = stat5;
		editArray[5] = stat6;
		updateDoneButton = (Button) findViewById(R.id.stats_update_button);
		makeDisabled();
	}
	
	public void makeDisabled() {
		for (EditText e : editArray) {
			e.setEnabled(false);
		}
	}
	
	public void makeEnabled() {
		for (EditText e : editArray) {
			e.setEnabled(true);
		}
	}
	
	public void update(View v) {
		if (doneEditing) {
			makeDisabled();
			updateDoneButton.setText(getString(R.string.update));
			doneEditing = false;
		} else {
			makeEnabled();
			updateDoneButton.setText(getString(R.string.done_button));
			doneEditing = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}

}
