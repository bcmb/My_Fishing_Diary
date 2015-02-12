package net.validcat.fishing;

import android.content.Intent;

public class ToDoItem {
	// �� ����� ��� ����?
	public static final String ITEM_SEP = System.getProperty("line.separator");
	// ������� ���������
	public final static String PLACE = "place";
	public final static String DATE = "date";
	// ������� ����
	 String mPlace = new String();
	 String mDate = new String();

	public ToDoItem(String place, String date) {
		// �������� ������ �� FishingList � �������������� ����
		this.mPlace = place;
		this.mDate = date;
	}

	public ToDoItem(Intent intent) {
		// �������� ������ �� intent (������ �� MainActivity)
		mPlace = intent.getStringExtra(ToDoItem.PLACE);
		mDate = intent.getStringExtra(ToDoItem.DATE);
	}

	public static void packageIntent(Intent intent, String place, String date) {
		// ���������� ������ � intent
		intent.putExtra(ToDoItem.PLACE, place);
		intent.putExtra(ToDoItem.DATE, date);
	}

	public String getPlace() {
		return mPlace;
	}
	
	public  String getDate() {
		return mDate;
	}
	
	// �� ����� ��� ����?
	public String toString(){
		return mPlace + ITEM_SEP + mDate + ITEM_SEP;
	}

}
