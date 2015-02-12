package net.validcat.fishing;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	MyListAdapter adapter;
	private static final int ITEM_REQUEST = 0;

	@SuppressLint("InflateParams") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ������� �������
		adapter = new MyListAdapter(getApplicationContext());
		
		// ������������ ����������� ����� ������� � �������
		getListView().setFooterDividersEnabled(true);
		
		// ������ ����� � xml
//		LayoutInflater inflater = (LayoutInflater)getApplicationContext().
		//getSystemService (LAYOUT_INFLATER_SERVICE);
		TextView footerView = (TextView)getLayoutInflater().inflate(R.layout.footer_view, null);
		
		// ������� ����� � ListView
		getListView().addFooterView(footerView);
		
		if (null == footerView) {
			return;
		}
		
		//��������� ������� �� �����
		footerView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				Intent startNewActivity = new Intent (MainActivity.this, FishingList.class);
				startActivityForResult (startNewActivity,ITEM_REQUEST);
			}
			
		});
		// ����������� ������� � ListView
		setListAdapter (adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ��������� ���������, ��������� ������ ������������ � �������
		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST ){
			ToDoItem toDo = new ToDoItem(data);
			adapter.add(toDo);
		}
	}
}

