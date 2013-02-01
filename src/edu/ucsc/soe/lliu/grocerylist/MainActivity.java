package edu.ucsc.soe.lliu.grocerylist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Button button1, button2, button3;
	private ArrayList<GroceryItem> items, recentClickedItems;
	private MyAdapter adapter;
	private ListView lv;
	private static final String TAG = "Grocery List";
	

	private class GroceryItem{
		private String item;
		private String color;
	
		public GroceryItem(){	
		}
	
		public GroceryItem(String i, String c){
			setItem(i);
			setColor(c);
		}
	
		public String getItem(){
			return item;
		}
	
		public void setItem(String i){
			item = i;
		}
	
		public String getColor(){
			return color;
		}
	
		public void setColor(String c){
			color = c;
		}
	
		@Override
		public boolean equals (Object o) {
			GroceryItem x = (GroceryItem) o;
			if (item.equals(x.item)) 
				return true;
			return false;
		}
	}

	private class MyAdapter extends ArrayAdapter<GroceryItem>{

		int resource;
		public MyAdapter(Context _context, int _resource, ArrayList<GroceryItem> items) {
			super(_context, _resource, items);
			resource = _resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout newView;

			GroceryItem w = getItem(position);

			// Inflate a new view if necessary.
			if (convertView == null) {
				newView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
				vi.inflate(resource,  newView, true);
			} 
			else {
				newView = (LinearLayout) convertView;
			}

			// Fills in the view.
			TextView tv = (TextView) newView.findViewById(R.id.itemName);
			tv.setText(w.getItem());
			tv.setBackgroundColor(Integer.parseInt(w.getColor()));
		
			return newView;
		}		
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initiate three buttons
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		
		// Initialize a ArrayList to store the most recently clicked items
		recentClickedItems = new ArrayList<GroceryItem>();
		GroceryItem item1 = new GroceryItem("Milk",String.valueOf(Color.BLUE));
		GroceryItem item2 = new GroceryItem("Bread",String.valueOf(Color.YELLOW));
		GroceryItem item3 = new GroceryItem("Egg",String.valueOf(Color.GREEN));
		recentClickedItems.add(item1);
		recentClickedItems.add(item2);
		recentClickedItems.add(item3);
		showButtons();
		
		// Create ListView and adapter
		lv = (ListView) findViewById(R.id.listView1);
		items =new ArrayList<GroceryItem>();		
		adapter = new MyAdapter(this,R.layout.grocery_item,items);
		lv.setAdapter(adapter);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// This method is required to keep all the information when changing orientation
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void addNewItem(View v){
		// Start new activity
		Intent intent = new Intent(this, SecondActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public void addItemToList(View v){
		Button b = (Button) v;
		String itemName = b.getText().toString();
		String itemColor = b.getTag().toString();	
		GroceryItem item = new GroceryItem(itemName, itemColor);
		
		// Update Listview and button
		items.add(0,item);
		adapter.notifyDataSetChanged();
		updateButtons(item);
	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK){
			Bundle b = data.getExtras();
			String item = b.getString("item");
			String color = b.getString("color");
			GroceryItem newItem = new GroceryItem(item,color);
			items.add(0,newItem);
			adapter.notifyDataSetChanged();
			updateButtons(newItem);
		}
	}
	
	private void updateButtons(GroceryItem recItem){
		// Remove the least clicked item
		if (recentClickedItems.contains(recItem)){
			recentClickedItems.remove(recItem);
		}
		else{
			recentClickedItems.remove(2);
		}
		// Insert most recently input item at the top of the list
		// and update buttons
		recentClickedItems.add(0,recItem);
		showButtons();
	}
	
	private void showButtons(){		
		// Show text and color on button1	
		button1.setText(recentClickedItems.get(0).getItem());
		button1.setTag(recentClickedItems.get(0).getColor());
		doShowButton(button1, recentClickedItems.get(0));
	
		// Show text and color on button2
		button2.setText(recentClickedItems.get(1).getItem());
		button2.setTag(recentClickedItems.get(1).getColor());
		doShowButton(button2, recentClickedItems.get(1));
		
		// Show text and color on button3
		button3.setText(recentClickedItems.get(2).getItem());
		button3.setTag(recentClickedItems.get(2).getColor());
		doShowButton(button3, recentClickedItems.get(2));	
	}
	
	private void doShowButton(Button b, GroceryItem item){
		// Create a ColorDrawable object and pass it item color
		ColorDrawable cd = new ColorDrawable();
		cd.setColor(Integer.parseInt(item.getColor()));
		
		// Set StateListDrawable for button background
		StateListDrawable state = new StateListDrawable();
		state.addState(new int[] {android.R.attr.state_pressed},
				getResources().getDrawable(R.drawable.pressed_color1));
		state.addState(new int[] {android.R.attr.state_focused},
				getResources().getDrawable(R.drawable.focus_color1));
		state.addState(new int[] { }, cd);
		// Set the background of button according to ‘state’
		b.setBackgroundDrawable(state);
	}
	
	public void clearList(View v){
		// Clear list
		items.clear();
		adapter.notifyDataSetChanged();
	}


}

