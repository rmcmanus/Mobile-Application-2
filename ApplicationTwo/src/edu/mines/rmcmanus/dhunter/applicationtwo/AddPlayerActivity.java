package edu.mines.rmcmanus.dhunter.applicationtwo;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddPlayerActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_player);
		populatePositionSpinner();
		populateNumberSpinner();
	}

	public void populatePositionSpinner() {
		Spinner positionSpinner = (Spinner) findViewById(R.id.position_edit_text);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.position_array, android.R.layout.simple_spinner_dropdown_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		positionSpinner.setAdapter(arrayAdapter);
	}
	
	public void populateNumberSpinner() {
		Spinner numberSpinner = (Spinner) findViewById(R.id.number_edit_text);
		
		ArrayList<String> numberArray = new ArrayList<String>();
		numberArray = populateNumberArray(numberArray);
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numberArray);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		numberSpinner.setAdapter(arrayAdapter);
	}
	
	public ArrayList<String> populateNumberArray(ArrayList<String> numberArray) {
		for (int i = 0; i < 100; i++) {
			if (i < 10) {
				numberArray.add("0"+i);
			}
			else {
				numberArray.add(Integer.toString(i));
			}
		}
		return numberArray;	
	}

}
