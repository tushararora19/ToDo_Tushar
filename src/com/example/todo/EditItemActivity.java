package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends Activity {

	EditText editItem;
	Button save;
	int position = -1;
	private static final String TAG = "EditItemActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate"); 
		setContentView(R.layout.activity_edit_item);
		// Show the Up button in the action bar.
		setupActionBar();
		setTitle("Edit Item");
		editItem = (EditText) findViewById(R.id.EditItemId);
		save = (Button) findViewById(R.id.SaveButtonId);

		// get text value from intent and set cursor to end of text
		editItem.setText(getIntent().getStringExtra("text").toString());
		editItem.setSelection(getIntent().getStringExtra("text").toString().length());

		position = Integer.parseInt(getIntent().getStringExtra("position").toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.d(TAG, "onCreateOptionsMenu"); 
		getMenuInflater().inflate(R.menu.edit_item, menu);
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

	// this method has to be public (since it is called from edit item activity xml on click)
	public void SaveItem(View v){
		Log.d(TAG, "onSaveClicked"); 
		if (!editItem.getText().toString().equals("")){
			Intent todoIntent = new Intent(this, ToDoActivity.class);
			todoIntent.putExtra("text", editItem.getText().toString());
			todoIntent.putExtra("position", Integer.toString(position));
			setResult(RESULT_OK, todoIntent);
			finish();
		}
		else {
			Toast.makeText(this, "Item name can not be blank", Toast.LENGTH_LONG).show();
		}
	}


	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
}
