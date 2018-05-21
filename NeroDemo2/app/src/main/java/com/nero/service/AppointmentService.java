package com.nero.service;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nero.model.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonnaay on 1/10/18.
 */

public class AppointmentService {
    private DatabaseReference mFirebaseDatabase;

    public String insertAppointmentInfo(Appointment appointment) {
        String key = "";
        try {
            initializeFireBase();
            fetchAppointmentInfo(appointment);
            key = mFirebaseDatabase.child("appointments").push().getKey();
            appointment.setId(key);
            mFirebaseDatabase.child("appointments").child(key).setValue(appointment);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public String fetchAppointmentInfo(Appointment appointment) {
        String key = "";
        try {
            initializeFireBase();
            //mFirebaseDatabase.child("appointments").child("").
            //appointment.setId(key);
           //key = mFirebaseDatabase.child("appointments").child(appointment.getAppointmentTime());
            DatabaseReference node = mFirebaseDatabase.child("appointments");

            Query q = node.orderByChild("appointmentTime").equalTo(appointment.getAppointmentTime());
            mFirebaseDatabase.child("appointments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List notes = new ArrayList<>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        // Note note = noteDataSnapshot.getValue(Note.class);
                        //notes.add(note);
                        System.out.println("---------"+noteDataSnapshot);
                    }
                    //adapter.updateList(notes);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    private void initializeFireBase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
