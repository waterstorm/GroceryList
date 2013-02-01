package edu.ucsc.soe.lliu.grocerylist;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends Activity {
	private int itemColor;
	private View colorView;
	private static final int DEFAULT_COLOR = Color.BLUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		itemColor = DEFAULT_COLOR;
		colorView = (View) findViewById(R.id.view1);
		// Set color view default to blue
		GradientDrawable bgShape = (GradientDrawable) colorView.getBackground();
    	bgShape.setColor(itemColor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_second, menu);
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	public void addNewItem(View v){
		
		EditText et = (EditText) findViewById(R.id.editText1);
		String newItem = et.getText().toString();
		
		if (newItem.isEmpty()){
			// Show alert that item name can't be empty
			showAlert();
		}
		else {
			// Close second activity and pass data to main activity
			Intent intent = getIntent();		
			Bundle b = new Bundle();
			b.putString("item", newItem);
			b.putString("color", String.valueOf(itemColor));		
			intent.putExtras(b);
			setResult(RESULT_OK,intent);
			finish();
		}
	}
	
	public void goBack(View v){
		// Back to main activity
		Intent intent = getIntent();
		setResult(RESULT_CANCELED,intent);
		finish();
	}
	
	public void pickColor(View v){
		// Use a color picker AmbilWarna library 
		// http://code.google.com/p/android-color-picker/
		// Implemented a view to show selected color.
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, itemColor,
	            new OnAmbilWarnaListener() {
	        @Override
	        public void onOk(AmbilWarnaDialog dialog, int color) {
	            // color is the color selected by the user.
	        	itemColor = color;
	        	// Change preview color.
	        	GradientDrawable bgShape = (GradientDrawable) colorView.getBackground();
	        	bgShape.setColor(itemColor);
	        }
	                
	        @Override
	        public void onCancel(AmbilWarnaDialog dialog) {
	                // cancel was selected by the user
	        }
	    });

	    dialog.show();
		
	}
	
	private void showAlert(){
		// Display alert message
		new AlertDialog.Builder(this)
			.setTitle(R.string.app_name)
			.setMessage(R.string.message)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){}
			})
			.show();
	}
	

}
