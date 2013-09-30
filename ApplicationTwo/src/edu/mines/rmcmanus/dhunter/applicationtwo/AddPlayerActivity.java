/**
 * Description:  This class defines the implementation of the add player view.  This code
 * populates a number of spinners with both string array data in strings.xml and dynamically
 * created ints that are cast as Strings
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPlayerActivity extends Activity {
	
	private String playerPosition;
	private String playerNumber;
	private String playerThrows;
	private String playerBats;
	public final static String EXTRA_PLAYER_NAME = "edu.mines.rmcmanus.dhunter.app2.PLAYERNAME";
	public final static String EXTRA_PLAYER_NUMBER = "edu.mines.rmcmanus.dhunter.app2.PLAYERNUMBER";
	public final static String EXTRA_PLAYER_POSITION = "edu.mines.rmcmanus.dhunter.app2.PLAYERPOSITION";
	public final static String EXTRA_PLAYER_THROWS = "edu.mines.rmcmanus.dhunter.app2.PLAYERTHROWS";
	public final static String EXTRA_PLAYER_BATS = "edu.mines.rmcmanus.dhunter.app2.PLAYERBATS";
	public final static String EXTRA_ADD_PLAYER_PASSED = "edu.mines.rmcmanus.dhunter.app2.ADDPLAYERPASSED";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_player);
		populatePositionSpinner();
		populateNumberSpinner();
		populateThrowHitSpinners();
	}

	/**
	 * Creates and populates a spinner that allows the user
	 * to choose the position of their new player created.
	 * This avoids error checking as the only choices that can
	 * be made are those that are populated in the spinner.
	 * 
	 * Since there are a limited number of positions on the baseball diamond,
	 * the names of each position are populated in a string-array in
	 * the string.xml.
	 */
	public void populatePositionSpinner() {
		Spinner positionSpinner = (Spinner) findViewById(R.id.position_edit_text);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.position_array, android.R.layout.simple_spinner_dropdown_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		positionSpinner.setAdapter(arrayAdapter);
		positionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				playerPosition = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				playerPosition = arg0.getItemAtPosition(0).toString();
			}
			
		});
	}
	
	/**
	 * Creates and populates a spinner that allows the user
	 * to choose the number of their new player created.
	 * This avoids error checking as the only choices that can
	 * be made are those that are populated in the spinner.
	 * 
	 * There are a limited number of team numbers that can be
	 * assigned to a player. They range from 00-99. However,
	 * instead of writing every string to an array, {@link #populateNumberSpinner()}
	 * generates the strings by calling {@link #populateNumberArray(ArrayList)},
	 * and then places the spinner.
	 */
	public void populateNumberSpinner() {
		Spinner numberSpinner = (Spinner) findViewById(R.id.number_edit_text);
		
		final ArrayList<String> numberArray = new ArrayList<String>();
		populateNumberArray(numberArray);
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numberArray);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		numberSpinner.setAdapter(arrayAdapter);
		numberSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				playerNumber = numberArray.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				playerNumber = numberArray.get(0);
			}
			
		});
	}
	
	/**
	 * Creates and populates a spinner that allows the user
	 * to choose the hand with which the newly created player
	 * throws and hits. This avoids error checking as the only
	 * choices that can be made are populated in the spinner.
	 * 
	 * This function only allows 2 options: Right and Left.
	 * Due to the similarity of both spinners, both were
	 * populated simultaneously.
	 */
	public void populateThrowHitSpinners() {
		Spinner throwSpinner = (Spinner) findViewById(R.id.throwing_edit_text);
		Spinner hitSpinner = (Spinner) findViewById(R.id.hitting_edit_text);
		
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.direction_array, android.R.layout.simple_spinner_dropdown_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		throwSpinner.setAdapter(arrayAdapter);
		throwSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				playerThrows = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				playerThrows = arg0.getItemAtPosition(0).toString();
			}
			
		});
		hitSpinner.setAdapter(arrayAdapter);
		hitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				playerBats = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				playerBats = arg0.getItemAtPosition(0).toString();
			}
			
		});
	}
	
	/**
	 * Generates the numbers 00-99 as strings, and then populates the
	 * passed ArrayList object.
	 * 
	 * @param numberArray 	The parameter passed to store numbers generated
	 */
	public void populateNumberArray(ArrayList<String> numberArray) {
		for (int i = 0; i < 100; i++) {
			if (i < 10) {
				numberArray.add("0"+i);
			}
			else {
				numberArray.add(Integer.toString(i));
			}
		}
	}
	
	/**
	 * This function checks to make sure that the user entered a name for the player, 
	 * and if they did then the StatsActivity is called with all the information that the
	 * user put in.
	 * 
	 * @param v
	 */
	public void commitPlayer(View v) {
		EditText name = (EditText) findViewById(R.id.name_edit_text);
		if (name.getText().toString().matches("")) {
			Toast.makeText(getApplicationContext(), "You must enter a player name", Toast.LENGTH_SHORT).show(); 
		}
		else {
			Intent intent = new Intent(this, StatsActivity.class);
			intent.putExtra(EXTRA_PLAYER_NAME, name.getText().toString());
			intent.putExtra(EXTRA_PLAYER_NUMBER, playerNumber);
			intent.putExtra(EXTRA_PLAYER_POSITION, playerPosition.toString());
			intent.putExtra(EXTRA_PLAYER_THROWS, playerThrows.toString());
			intent.putExtra(EXTRA_PLAYER_BATS, playerBats.toString());
			intent.putExtra(EXTRA_ADD_PLAYER_PASSED, true);
			startActivity(intent);
		}
	}

}
