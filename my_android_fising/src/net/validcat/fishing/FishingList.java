package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FishingList extends Activity {
	private EditText etPlace, etDate;
	private Button btnCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fishing_list);

		// ������� ��������
		etPlace = (EditText) findViewById(R.id.etPlace);
		etDate = (EditText) findViewById(R.id.etDate);
		btnCreate = (Button) findViewById(R.id.btnCreate);

		// ����������� ���������� ��� ������
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// c������� ���������� ��� ��������
				// ���������� ����������
				Intent data = new Intent();
				ToDoItem.packageIntent(data, getPlaceText(),  getDateText());

				// ���������� ���������
				setResult(RESULT_OK, data);
				finish();
			}
		});

	}

	// ��������� ��������� ������������� �����
	private String getPlaceText() {
		return etPlace.getText().toString();
	}

	// ��������� ��������� ������������� �����
	private String getDateText() {
		return etDate.getText().toString();
	}

}
