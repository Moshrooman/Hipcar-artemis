package com.example.justinkwik.hipcar.Main;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.justinkwik.hipcar.R;

public class PlaceHolderFragment extends Fragment {

    public PlaceHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View placeHolderFragment = inflater.inflate(R.layout.fragment_place_holder, container, false);

        return placeHolderFragment;
    }

}
