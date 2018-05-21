package com.nero;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jonnaay on 1/8/18.
 */

public class AppointmentConfirmation extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointment_confirmation, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        String confirmation = bundle.getString("confirmation");
        String name = bundle.getString("name");
        String age = bundle.getString("age");
        String phoneNumber = bundle.getString("phone");
        String email = bundle.getString("email");
        String time = bundle.getString("time");
        TextView confirmationText = (TextView) getActivity().findViewById(R.id.apt_confirmation_val);
        confirmationText.setText(confirmation);
        TextView nameText = (TextView) getActivity().findViewById(R.id.apt_confirmation_name_val);
        nameText.setText(name);
        TextView ageText = (TextView) getActivity().findViewById(R.id.apt_confirmation_age_val);
        ageText.setText(age);
        TextView phoneText = (TextView) getActivity().findViewById(R.id.apt_confirmation_phone_val);
        phoneText.setText(phoneNumber);
        TextView emailText = (TextView) getActivity().findViewById(R.id.apt_confirmation_email_val);
        emailText.setText(email);
        TextView timeText = (TextView) getActivity().findViewById(R.id.apt_confirmation_time_val);
        timeText.setText(time);

    }
}