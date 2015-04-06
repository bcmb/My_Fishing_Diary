package net.validcat.fishing;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {
	
	private final List<Conteyner> mItems = new ArrayList<Conteyner>();
	private final Context mContext;

	public MyListAdapter(Context context) {
		mContext = context;
	}
// ����������� ���������
	@Override
	public int getCount() {
		return mItems.size();
	}
// ������� �� �������
	@Override
	public Object getItem(int pos) {
		return mItems.get(pos);
	}
// id �� �������
	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	// �������� � ���������� ������
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// ������� ���������������� ��������� conteyner)
		// �������� ������ �� ������� (id)
		final Conteyner conteyner =  (Conteyner) getItem (position);
		
		// ������� View ������ ���������� �� main.xml
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout itemLayout = (FrameLayout) mInflater.inflate(R.layout.main,  parent, false);
		//������� �������
		final TextView place = (TextView)itemLayout.findViewById(R.id.place);
		final TextView date = (TextView)itemLayout.findViewById(R.id.date);
		place.setText(conteyner.getPlace());
		date.setText(conteyner.getDate());
		
		return itemLayout;
	}

	public void add(Conteyner item) {
		// �������� ������ � ������ (������)
		mItems.add(item);
		// �������� ������� ������������� ��� ��������������� ������
		notifyDataSetChanged();
	}
	
	
}
