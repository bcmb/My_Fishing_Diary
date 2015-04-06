package net.validcat.fishing;

import android.content.Intent;

public class Conteyner {

	// ������� ���������
	public final static String PLACE = "place";
	public final static String DATE = "date";
	// ������� ����
	 String mPlace = new String();
	 String mDate = new String();

	 // �����������
	public Conteyner(String place, String date) {
		// �������� ������ �� FishingList � �������������� ����
		this.mPlace = place;
		this.mDate = date;
	}

	// �����������
	public Conteyner(Intent intent) {
		// �������� ������ �� intent (������ �� MainActivity)
		mPlace = intent.getStringExtra(Conteyner.PLACE);
		mDate = intent.getStringExtra(Conteyner.DATE);
	}

	public static void packageIntent(Intent intent, String place, String date) {
		// ���������� ������ � intent
		intent.putExtra(Conteyner.PLACE, place);
		intent.putExtra(Conteyner.DATE, date);
	}

	public String getPlace() {
		return mPlace;
	}
	
	public  String getDate() {
		return mDate;
	}
	


}
