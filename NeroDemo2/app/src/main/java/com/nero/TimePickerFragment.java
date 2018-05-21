package com.nero;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.M)
public class TimePickerFragment extends DialogFragment {
    private RadioGroup radioTimeGroup;
    private RadioButton radioTimeButton;
    Spinner spinner;
    ArrayList<String> timeSlots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_frame, container);
    }

    private void populateList(final View view, String date) {
        timeSlots = new ArrayList<>();
        spinner = (Spinner) view.findViewById(R.id.time_spinner);
        timeSlots.add("సమయం");
        for (int i = 0; i < 20; i++) {
            timeSlots.add(i + "-10:00AM");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, timeSlots);

        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String date = bundle.getString("date");
        populateList(view, date);
        super.onViewCreated(view, savedInstanceState);
        Button timeButton = (Button) view.findViewById(R.id.selected_time_btn);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner timeSpinner = (Spinner) view.findViewById(R.id.time_spinner);
                String spinnerValue = timeSpinner.getSelectedItem().toString();
                if (spinnerValue.equals("సమయం")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder
                            .setMessage("దయచేసి ఒక స్లాట్ను ఎంచుకోండి.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    }
                            );
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    TextView tvDate = (TextView) getActivity().findViewById(R.id.p_time);
                    String date = tvDate.getText().toString();
                    tvDate.setText(date + " " + spinnerValue);
                    TextView timePickTxt = (TextView) getActivity().findViewById(R.id.textView7);
                    timePickTxt.setVisibility(View.VISIBLE);
                    getDialog().dismiss();
                }
            }

        });
    }
}