package net.validcat.fishing;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import net.validcat.fishing.db.DB;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddNewFishing extends AppCompatActivity implements OnClickListener {
    public static final String LOG_TAG = AddNewFishing.class.getSimpleName();

    @Bind(R.id.et_place)
    EditText etPlace;
    @Bind(R.id.etDate)
    EditText etDate;
    @Bind(R.id.et_weather)
    EditText etWeather;
    @Bind(R.id.et_process)
    EditText etProcess;
    @Bind(R.id.et_catch)
    EditText etCatch;
    @Bind(R.id.btn_create)
    Button btnCreate;
    @Bind(R.id.btn_change)
    Button btnChange;
    @Bind(R.id.btn_add_photo)
    Button btnAddFoto;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    private Bitmap bitmap;
    private DB db;
    private static final int DIALOG_DATE = 1;
    private int day;
    private int month;
    private int year;
    private CameraManager cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fishing_list);
        ButterKnife.bind(this);

        // listener for the button
        btnCreate.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnAddFoto.setOnClickListener(this);

        Intent MyIntent = getIntent();
        String date = MyIntent.getStringExtra("keyDate");
        // Log.d(LOG_TAG, " --- MyDate --- " + date);
        etDate.setText(date);

        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                // save in data base
                String myPlace = etPlace.getText().toString();
                String myDate = etDate.getText().toString();
                String myWeather = etWeather.getText().toString();
                String myDescription = etProcess.getText().toString();
                String myCatch = etCatch.getText().toString();

                FishingItem item = new FishingItem();
                item.setPlace(myPlace);
                item.setDate(myDate);
                item.setWeather(myWeather);
                item.setDescription(myDescription);
                item.setCatches(myCatch);

                // open a connection to the database
                db = new DB(AddNewFishing.this);
                db.open();
                long id = db.saveFishingItem(item);
                db.close();

                Intent data = new Intent();
                FishingItem.packageIntent(data, myPlace, myDate, id);
                // send container
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btn_change:
                showDialog(DIALOG_DATE);
                break;
            case R.id.btn_add_photo:
                cm = new CameraManager();
                cm.startCameraForResult(this);
//                myCameraManager.startIntent();
//                bitmap = myCameraManager.getFoto();

        }

    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    AddNewFishing.this.year = year;
                    AddNewFishing.this.month = monthOfYear;
                    AddNewFishing.this.day = dayOfMonth;
                    etDate.setText(AddNewFishing.this.day + "." + AddNewFishing.this.month + "." + AddNewFishing.this.year);
                }
            }, year, month, day);

            return dpd;
        }

        return super.onCreateDialog(id);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap b = cm.extractPhotoBitmapFromResult(requestCode, resultCode, data);
        if (b != null) {
            ivPhoto.setImageBitmap(b);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_fishing_action_bar, menu);

        return true;
    }
}
