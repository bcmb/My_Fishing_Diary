package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherDialogFragment extends DialogFragment {
    private int temperature;
    @Bind(R.id.weather_group)
    RadioGroup weatherGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FishingDialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weather, null);
        ButterKnife.bind(this, v);

        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        final TextView tvTemp = (TextView) v.findViewById(R.id.temperatureValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private void progressChanged() {
                temperature = currentValue(seekBar.getProgress());
                tvTemp.setText(temperature + "\u00B0");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressChanged();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressChanged();
            }
        });

        return new AlertDialog.Builder(getActivity())
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
    }

    private int currentValue(int progress){
       return progress - 50;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_TEMPERATURE, temperature);
        i.putExtra(Constants.EXTRA_IMAGE_KEY, getSelectedWeather());
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public int getSelectedWeather() {
        return weatherGroup.indexOfChild(weatherGroup.findViewById(weatherGroup.getCheckedRadioButtonId()));
    }
}
