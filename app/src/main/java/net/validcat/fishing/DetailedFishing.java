package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedFishing extends Activity {
	public static final String LOG_TAG = "myLogs";
	// test develop commit
	TextView tvDetPlace; 
	TextView tvDetDate; 
	TextView tvDetWeather; 
	TextView tvDetDescription;
	TextView tvDetCatch;
	DB db;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_list);
		
		tvDetPlace = (TextView) findViewById(R.id.tvDetPlace);
		tvDetDate = (TextView) findViewById(R.id.tvDetDate);
		tvDetWeather = (TextView) findViewById(R.id.tvDetWeather);
		tvDetDescription = (TextView) findViewById(R.id.tvDetProcess);
		tvDetCatch = (TextView) findViewById(R.id.tvDetCatch);

		Intent intent = getIntent();
		long id = intent.getLongExtra("id", -1);
		if (id == -1) {
			Toast.makeText(this, "Wrong id", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
			
		db = new DB(this);
		db.open();
		FishingItem item = db.getFishingItemById(id);
		db.close();
		// cursor = mDB.rawQuery("SELECT * FROM mytab WHERE _id = 'ID'", null);
		// cursor = db.getAllData();

		tvDetPlace.setText("����� �������: " + item.getPlace());
		tvDetDate.setText("���� :" + item.getDate());
		tvDetWeather.setText("������: " + item.getWeather());
		tvDetDescription.setText("������ �������: " + item.getDescription());
		tvDetCatch.setText("����: " + item.getCatch());
		
//		tvDetDate.setText("���� :" + dataDate);
//		tvDetWeather.setText("������: " + dataWeather);
//		tvDetProcess.setText("������ �������: " + dataProcess);
//		tvDetCatch.setText("����: " + dataCatch);
	}
}