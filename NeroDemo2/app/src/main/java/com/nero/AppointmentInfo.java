package com.nero;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.nero.model.Appointment;
import com.nero.service.AppointmentService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jonnaay on 1/8/18.
 */

public class AppointmentInfo extends Fragment implements DatePickerDialog.OnDateSetListener {
    private DatePickerDialog dpd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointment, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView time = (TextView) getActivity().findViewById(R.id.p_time);
        time.setEnabled(false);
        Button button = (Button) view.findViewById(R.id.p_time_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            AppointmentInfo.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            AppointmentInfo.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }

                Calendar sunday;
                List<Calendar> weekends = new ArrayList<>();
                int weeks = 15214;
                for (int i = 0; i < (weeks * 7); i = i + 7) {
                    sunday = Calendar.getInstance();
                    if (i == 0) {
                        int day = sunday.get(Calendar.DAY_OF_WEEK);
                        if (day == 1) {
                            Calendar sundayInit = Calendar.getInstance();
                            sundayInit.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + i));
                            weekends.add(sundayInit);
                        }
                    }
                    sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    weekends.add(sunday);
                }
                Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
                dpd.setDisabledDays(disabledDays);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);
                dpd.setMinDate(calendar);
                dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        Button appointmentButton = (Button) view.findViewById(R.id.appointment_submit);
        appointmentButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                TextView pName = (TextView) getActivity().findViewById(R.id.p_name);
                TextView pMobile = (TextView) getActivity().findViewById(R.id.p_mobile);
                TextView pEmail = (TextView) getActivity().findViewById(R.id.p_email);
                TextView pAGE = (TextView) getActivity().findViewById(R.id.p_age);
                TextView pAppointmentTime = (TextView) getActivity().findViewById(R.id.p_time);
                String date = "";
                String time = "";
                String splitStrings[] = pAppointmentTime.getText().toString().split(" ");
                if (splitStrings.length == 1) {
                    date = pAppointmentTime.getText().toString().split(" ")[0];
                } else if (splitStrings.length > 1) {
                    date = pAppointmentTime.getText().toString().split(" ")[0];
                    time = pAppointmentTime.getText().toString().split(" ")[1];
                }
                if (pName.getText().toString().trim().length() == 0) {
                    showAlert(view, "దయచేసి పేరు నమోదు చేయండి.");
                } else if (pMobile.getText().toString().trim().length() != 10) {
                    showAlert(view, "దయచేసి ఫోను నంబరు నమోదు చేయండి.");
                } else if (date.trim().length() == 0) {
                    showAlert(view, "దయచేసి అపాయింట్మెంట్ తేదీ నమోదు చేయండి.");
                } else if (time.trim().length() == 0) {
                    showAlert(view, "దయచేసి అపాయింట్మెంట్ సమయం నమోదు చేయండి.");
                } else if (pAGE.getText().length() == 0) {
                    showAlert(view, "దయచేసి వయస్సు నమోదు చేయండి.");
                } else {
                    Appointment appointment = new Appointment();
                    appointment.setName(pName.getText().toString());
                    appointment.setPhoneNumber(pMobile.getText().toString());
                    appointment.setEmail(pEmail.getText().toString());
                    appointment.setAge(Integer.parseInt(pAGE.getText().toString()));
                    appointment.setAppointmentDate(date);
                    appointment.setAppointmentTime(pAppointmentTime.getText().toString());

                    AppointmentService appointmentService = new AppointmentService();
                    String confirmation = appointmentService.insertAppointmentInfo(appointment);
                    Bundle bundle = new Bundle();
                    bundle.putString("confirmation", confirmation);
                    bundle.putString("name", appointment.getName());
                    bundle.putString("age", String.valueOf(appointment.getAge()));
                    bundle.putString("phone", appointment.getPhoneNumber());
                    bundle.putString("email", appointment.getEmail());
                    bundle.putString("time", appointment.getAppointmentDate() + " " + appointment.getAppointmentTime());
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new AppointmentConfirmation();
                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.screen_area, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void showAlert(final View view, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }
                );
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String formattedDate = +dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        Bundle bundle = new Bundle();
        bundle.putString("date", formattedDate);
        android.app.DialogFragment picker = new TimePickerFragment();
        picker.setArguments(bundle);
        picker.show(getFragmentManager(), "timePicker");
        TextView tvDate = (TextView) getActivity().findViewById(R.id.p_time);
        tvDate.setText(formattedDate);
    }
}