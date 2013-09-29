package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		
		//grabs the player name and number from the previous activity
		Intent intent = getIntent();
		String playerName = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_NAME);
		String playerNumber = intent.getStringExtra(SelectPlayerActivity.EXTRA_PLAYER_NUMBER);
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
	}
	
	/**
	 * This function iterated through each of the edit text views and disables them so that
	 * the user cannot edit the values.
	 */
	public void makeDisabled() {
		for (EditText e : editArray) {
			e.setEnabled(false);
		}
	}
	
	/**
	 * This function iterated though each of the edit text views and enables them so that
	 * the user can edit the values.
	 */
	public void makeEnabled() {
		for (EditText e : editArray) {
			e.setEnabled(true);
		}
	}
	
	/**
	 * This function is called when the update/done button is pressed.  It determines if the
	 * edit text views should be editable or not, and changes the text of the button from
	 * update to done if the user is in edit mode.
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
		} 
		//If the user is in not in edit mode and the button is press, change the text of the
		//button to done and enable the edit text boxes for editing.
		else {
			makeEnabled();
			updateDoneButton.setText(getString(R.string.done_button));
			doneEditing = true;
		}
	}
}
