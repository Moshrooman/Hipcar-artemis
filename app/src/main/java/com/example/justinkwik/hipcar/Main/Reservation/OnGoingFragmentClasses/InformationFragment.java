package com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.VehicleStatus;
import com.example.justinkwik.hipcar.R;

public class InformationFragment extends Fragment {

    private OnGoingReservation onGoingReservation;
    private VehicleStatus vehicleStatus;

    public InformationFragment(OnGoingReservation onGoingReservation, VehicleStatus vehicleStatus) {

        this.onGoingReservation = onGoingReservation;
        this.vehicleStatus = vehicleStatus;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

}
