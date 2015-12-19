package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;

/**
 * Created by Denis on 10.12.2015.
 */
public class WeatherDialogFragment extends DialogFragment {
    private int weatherKey;
    private String temperature;
    ImageView sunny;
    ImageView cloudy;
    ImageView partlyCloudy;
    ImageView rain;
    ImageView snow;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weather, null);
        SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        final TextView temperatureValue = (TextView) v.findViewById(R.id.temperatureValue);

        sunny = (ImageView)v.findViewById(R.id.ic_sunny);
        cloudy = (ImageView)v.findViewById(R.id.ic_cloudy);
        partlyCloudy = (ImageView)v.findViewById(R.id.ic_partly_cloudy);
        rain = (ImageView)v.findViewById(R.id.ic_rain);
        snow = (ImageView)v.findViewById(R.id.ic_snow);

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWeather(v);
            }
        };
        sunny.setOnClickListener(oclBtn);
        cloudy.setOnClickListener(oclBtn);
        partlyCloudy.setOnClickListener(oclBtn);
        rain.setOnClickListener(oclBtn);
        snow.setOnClickListener(oclBtn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temperature = currentValue(seekBar.getProgress());
                temperatureValue.setText(temperature);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                temperature = currentValue(seekBar.getProgress());
                temperatureValue.setText(temperature);
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.title_weather_dialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();

//        ButterKnife.bind(this, v); //

        return dialog;
    }

    public void changeWeather(View v) {
        final int defaultBgColor = getResources().getColor(R.color.color_default_background);

        switch (v.getId()) {
            case R.id.ic_sunny:
                setBackgroundSelection(getResources().getColor(R.color.color_background),
                        defaultBgColor, defaultBgColor, defaultBgColor, defaultBgColor);
                weatherKey = 0;
                break;
            case R.id.ic_cloudy:
                setBackgroundSelection(defaultBgColor, getResources().getColor(R.color.color_background),
                        defaultBgColor, defaultBgColor, defaultBgColor);
                weatherKey = 1;
                break;
            case R.id.ic_partly_cloudy:
                setBackgroundSelection(defaultBgColor, defaultBgColor, getResources().getColor(R.color.color_background),
                        defaultBgColor, defaultBgColor);
                weatherKey = 2;
                break;
            case R.id.ic_rain:
                setBackgroundSelection(defaultBgColor, defaultBgColor, defaultBgColor,
                        getResources().getColor(R.color.color_background), defaultBgColor);
                weatherKey = 3;
                break;
            case R.id.ic_snow:
                setBackgroundSelection(defaultBgColor, defaultBgColor, defaultBgColor, defaultBgColor,
                        getResources().getColor(R.color.color_background));
                weatherKey = 4;
                break;
        }
    }

    private void setBackgroundSelection(int sunny, int cloudy, int partly, int rain, int snow) {
        this.sunny.setBackgroundColor(sunny);
        this.cloudy.setBackgroundColor(cloudy);
        this.partlyCloudy.setBackgroundColor(partly);
        this.rain.setBackgroundColor(rain);
        this.snow.setBackgroundColor(snow);
    }

    private String currentValue (int progress){
       return progress > 50 ? "+" + String.valueOf(progress - 50)+ "\u00B0" + "C"
               : "-" + String.valueOf(50 - progress)+ "\u00B0" + "C";
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_TEMPERATURE, temperature);
        i.putExtra(Constants.EXTRA_IMAGE_KEY, weatherKey);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

}
