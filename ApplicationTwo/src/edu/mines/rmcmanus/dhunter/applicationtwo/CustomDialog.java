package edu.mines.rmcmanus.dhunter.applicationtwo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CustomDialog extends Dialog implements
android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes, no;
	public String teamName;
	public boolean isSet = false;

	public CustomDialog(Activity a) {
		super(a);
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog);
		yes = (Button) findViewById(R.id.button1);
		yes.setOnClickListener(this);

	}

	public String getTeamName() {
		if (isSet) {
			return teamName;
		} else {
			return "";
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			EditText teamName;
			teamName = (EditText) findViewById(R.id.add_team_dialog_field);
			this.teamName = teamName.getText().toString();
			isSet = true;
			break;
		default:
			break;
		}
		dismiss();
	}
}
