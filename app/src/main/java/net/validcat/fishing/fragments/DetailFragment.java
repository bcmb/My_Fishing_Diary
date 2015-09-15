package net.validcat.fishing.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.validcat.fishing.FishingItem;
import net.validcat.fishing.R;
import net.validcat.fishing.db.DB;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    public static final String LOG_TAG = DetailFragment.class.getSimpleName();
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.tv_description)
    TextView tvDescription;
    @Bind(R.id.tv_catch)
    TextView tvCatch;
    private DB db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailFragmentView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, detailFragmentView);

        Intent intent = getActivity().getIntent();
        long id = intent.getLongExtra("id", -1);
        if (id == -1) {
            Toast.makeText(getActivity(), "Wrong id", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
        }
        db = new DB(getActivity());
        updateUiByItemId(id);

        return detailFragmentView;
    }

    public void updateUiByItemId(long id) {
        db.open();
        FishingItem item = db.getFishingItemById(id);
        db.close();
        tvPlace.setText("Place: " + item.getPlace());
        tvDate.setText("Date: " + item.getDate());
        tvWeather.setText("Weather: " + item.getWeather());
        tvDescription.setText("Description: " + item.getDescription());
        tvCatch.setText("Price: " + item.getPrice());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getGroupId();
//        return super.onOptionsItemSelected(item);
//    }
}