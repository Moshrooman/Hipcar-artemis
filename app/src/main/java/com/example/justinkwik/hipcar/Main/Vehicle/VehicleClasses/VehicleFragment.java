package com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justinkwik.hipcar.R;

//TODO: start copying everything from the ongoing classes becaue the view also brings up a popup of the map and stuff.\
//TODO: for the api link its https://artemis-api-dev.hipcar.com/vehicle header as token, and params "scope" and enter includeModelAndMake,includeStation,includeVehicleRates

public class VehicleFragment extends Fragment {


    public VehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle, container, false);
    }

}
