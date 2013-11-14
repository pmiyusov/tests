package com.example.autocompletetest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.example.autocompletetest.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity implements   TextWatcher {
	List<String> dictionary=null;
	AutoCompleteTextView actv = null;
	static String previousInput = null;
	static String lastInput = null;
	ArrayAdapter<String> adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dictionary=new ArrayList<String>();
		dictionary.add(new String(" "));
		setContentView(R.layout.activity_main);
		adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,dictionary);
		actv= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		actv.setThreshold(3);
		actv.setAdapter(adapter);
		actv.setTextColor(Color.BLUE);
		actv.setEnabled(true);
		actv.addTextChangedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void reaction() {
		previousInput = lastInput;
		lastInput = actv.getText().toString();
		if (actv.enoughToFilter() == false)
			return;
		if(lastInput.length() > previousInput.length()){

			List<String> newDictionary=new ArrayList<String>();
			newDictionary.add(new String(" "));		
			for(String item :dictionary){
				if(item.startsWith(lastInput)) {
					newDictionary.add(item);
				}
			}
			dictionary = newDictionary;
			newDictionary = null;
			return;
		}
		actv.setTextColor(Color.RED);
		actv.setEnabled(false);
		update();
		return;
	}
	void  update(){
		dictionary = new ArrayList<String>();
		Uri resUri = Uri.parse("android.resource://"
				+ getBaseContext().getPackageName() + "/" + R.raw.data);
		InputStream is = getResources().openRawResource(R.raw.data);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;
		Toast t = Toast.makeText(getApplicationContext(), "Updating ...  ", Toast.LENGTH_LONG);
		t.show();

		try {
			while ((line = br.readLine()) != null) {
				if (lastInput == null || line.startsWith(lastInput)) {
					dictionary.add(line);
				}
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			actv.setEnabled(true);
			actv.setTextColor(Color.BLUE);
			return;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,dictionary);
		actv= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		actv.setThreshold(3);
		actv.setAdapter(adapter);
		actv.setTextColor(Color.BLUE);
		actv.setEnabled(true);

	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		reaction();
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}
}
