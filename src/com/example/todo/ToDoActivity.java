package com.example.todo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ToDoActivity extends Activity{

	private static final String TAG = "ToDoActivity";
	Button addItem;
	EditText newItem;
	ListView lvitmes;
	ArrayList<String> items; 
	ArrayAdapter<String> itemsAdapter;
	static final int REQUEST_CODE = 1;
	String filename = "todo_Tushar.txt";

	public boolean ButtonClicked(View v) {
		Log.d(TAG, "onAddClicked");
		newItem = (EditText) findViewById(R.id.NewItemID);
		String newItemText = newItem.getText().toString().trim();
		if (!newItemText.equals("")){
			// check if item already exists in the list
			for (int i=0;i<items.size();i++){
				if (newItemText.equals(items.get(i))){
					String duplicateItem = "Item '" + newItemText + "' already exists in list. \n Please enter different name or click existing to edit"; 
					Toast.makeText(this, duplicateItem, Toast.LENGTH_LONG).show();
					return false;
				}
			}
			items.add(newItemText);
			//itemsAdapter.add(newItem.getText().toString());
			itemsAdapter.notifyDataSetInvalidated();
			newItem.setText("");
			saveItems();
			return true;
		} else {
			Toast.makeText(this, "Please enter New Item Name", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Log.d(TAG, "onReceivingEditResult");
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
			String value = intent.getExtras().getString("text").toString().trim();
			int pos = Integer.parseInt(intent.getExtras().getString("position").toString());
			boolean flag = true;
			for (int i=0;i<items.size();i++){
				if (value.equals(items.get(i))){
					String editedItem = "Edited Item name '" + value + "' already exists in list.\nItem not edited."; 
					Toast.makeText(this, editedItem, Toast.LENGTH_LONG).show();
					flag = false;
				}
			}
			if (flag){
				items.set(pos, value);
				itemsAdapter.notifyDataSetInvalidated();
				saveItems();
				Toast.makeText(this, "Item edited successfully.", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate"); 
		setContentView(R.layout.activity_to_do);

		lvitmes = (ListView) findViewById(R.id.ToDoListID);
		items = new ArrayList<String>();
		readItems();

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		// simple_list_item_1 is predefined in android package and text1 is its textView id
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
		// to use your own list (customized):
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_items, R.id.textViewID, items);

		lvitmes.setAdapter(itemsAdapter);

		lvitmes.setTextFilterEnabled(true);

		//		items.add("Item1");
		//		items.add("Item2");

		addItem = (Button) findViewById(R.id.AddID);

		// setting up view listener for removing items and editing items
		setupRemoveListener();
		setupEditItemListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.d(TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	private void readItems(){
		// opening a file and reading a newline delimited list of itmes
		File filesDir = this.getFilesDir();

		File toDoFile = new File(filesDir, filename);
		if (toDoFile.exists()){
			Log.d(TAG, "fileExists");
		}
		try{
			@SuppressWarnings("resource")
			BufferedReader buf = new BufferedReader(new FileReader(toDoFile));
			String str = buf.readLine();
			while(str != null){
				items.add(str);
				str = buf.readLine();
			}
		} catch (Exception e){
			items = new ArrayList<String>();
			e.printStackTrace();
		}
	}

	private void saveItems(){
		File filesDir = this.getFilesDir();
		File toDoFile = new File(filesDir, filename);
		try{
			@SuppressWarnings("resource")
			PrintWriter pw = new PrintWriter(toDoFile);
			for (int i=0;i<items.size();i++){
				pw.write(items.get(i));
				pw.write("\n");
			}
			pw.close();
		} catch (Exception e){
			items = new ArrayList<String>();
			e.printStackTrace();
		}
	}

	private void setupEditItemListener() {

		lvitmes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				Intent n = new Intent(getApplicationContext(), EditItemActivity.class);
				n.putExtra("text", items.get(position));
				n.putExtra("position", Integer.toString(position));
				startActivityForResult(n, REQUEST_CODE);
			}
		});
	}

	private void setupRemoveListener() {

		lvitmes.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item, int pos, long id) {
				items.remove(pos);
				itemsAdapter.notifyDataSetInvalidated();
				saveItems();
				return true;
			}
		});
	}
}
