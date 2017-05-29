package com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.CustomViewPager.FragmentViewPager;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.Main.PlaceHolderFragment;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.VehicleStatus;
import com.example.justinkwik.hipcar.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class OnGoingReservationFragment extends Fragment implements OnGoingReservationAdapter.VehicleStatusInterface{

    private final String onGoingReservationLink = "https://artemis-api-dev.hipcar.com/reservation/on-going";
    private final String vehicleStatusLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/status";
    private UserCredentials userCredentials;
    private OnGoingReservation[] onGoingReservations;
    private Gson gson;
    private RecyclerView onGoingReservationRecyclerView;
    private VehicleStatus vehicleStatus;

    /*
    Start of popupwindow variables
     */
    //Used only to measure the size of the pop-up window.
    private OnGoingReservation onGoingReservation;
    private View popUpWindowMeasuredView;
    private FrameLayout onGoingReservationFrameLayout;
    private PopupWindow viewActionPopUpWindow;
    private ViewGroup viewActionPopUpContainer;
    private LayoutInflater layoutInflater;
    private WindowManager windowManager;
    private TextView exitTextView;
    //Viewpagers for the popupwindow.
    private FragmentViewPager googleMapAndInfoViewPager;
    private ViewPager buttonViewPager;

    public OnGoingReservationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userCredentials = LoginActivity.getUserCredentials();
        gson = new Gson();
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        onGoingReservationStringRequest(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        onGoingReservationRecyclerView = (RecyclerView) view.findViewById(R.id.onGoingReservationRecyclerView);
        onGoingReservationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        onGoingReservationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        onGoingReservationFrameLayout = (FrameLayout) view.findViewById(R.id.onGoingReservationFrameLayout);
        popUpWindowMeasuredView = view.findViewById(R.id.popUpWindowMeasuredView);

        //TODO: fix this set adapter thing, stack overflow:
        //https://stackoverflow.com/questions/19888539/illegalargumentexception-no-view-found-for-id-for-fragment-viewpager-in-vie
        //1. Made FragmentViewPager
        //2. Changed ViewPager to FragmentViewPager in XML
        //3. In this class changed Viewpager to FragmentViewPager
        //4. called storeAdapter instead of setAdapter.
        //TODO: work on making the view for the information fragment, and ask go for the google map api key
        //Also work on the buttonviewpager, figure out if there is a way to not hae a fragment for each button.

        viewActionPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.viewactionpopup, null);
        googleMapAndInfoViewPager = (FragmentViewPager) viewActionPopUpContainer.findViewById(R.id.googleMapAndInfoViewPager);
        buttonViewPager = (ViewPager) viewActionPopUpContainer.findViewById(R.id.buttonViewPager);
//        googleMapAndInfoViewPager.storeAdapter(new GoogleMapInfoAdapter(getActivity().getSupportFragmentManager()));


        return view;
    }

    private void onGoingReservationStringRequest(final OnGoingReservationFragment onGoingReservationFragment) {

        StringRequest onGoingReservationRequest = new StringRequest(Request.Method.GET, onGoingReservationLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                onGoingReservations = gson.fromJson(response, OnGoingReservation[].class);

                onGoingReservationRecyclerView.setAdapter(new OnGoingReservationAdapter(getActivity(),
                        onGoingReservations, onGoingReservationFragment));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("token", userCredentials.getToken());

                return headerMap;
            }
        };

        ConnectionManager.getInstance(getActivity().getApplicationContext()).add(onGoingReservationRequest);

    }

    @Override
    public void showVehicleStatusPopup(final OnGoingReservation onGoingReservation) {

        this.onGoingReservation = onGoingReservation;

        String modifiedVehicleStatusLink = vehicleStatusLink.replace(":id", String.valueOf(onGoingReservation.getVehicle_id()));

        StringRequest vehicleStatusRequest = new StringRequest(Request.Method.GET, modifiedVehicleStatusLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("String Request DONE: ", "True"); //TODO: take away this Log.

                vehicleStatus = gson.fromJson(response, VehicleStatus.class);

                displayPopUpWindow();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("token", userCredentials.getToken());

                return headerMap;
            }
        };

        ConnectionManager.getInstance(getActivity().getApplicationContext()).add(vehicleStatusRequest);

    }

    private void displayPopUpWindow() {

        viewActionPopUpWindow = new PopupWindow(viewActionPopUpContainer, popUpWindowMeasuredView.getWidth(),
                popUpWindowMeasuredView.getHeight(), true);

        viewActionPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        setPopUpClickListeners(viewActionPopUpContainer, viewActionPopUpWindow);

        viewActionPopUpWindow.showAtLocation(onGoingReservationFrameLayout, Gravity.CENTER, 0, 0);

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) viewActionPopUpContainer.getLayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.3f;
        windowManager.updateViewLayout(viewActionPopUpContainer, layoutParams);

    }

    private void setPopUpClickListeners(ViewGroup viewActionPopUpContainer, final PopupWindow viewActionPopUpWindow) {

        exitTextView = (TextView) viewActionPopUpContainer.findViewById(R.id.exitTextView);

        exitTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    exitTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                } else {

                    exitTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.navBarGrey));

                }

                return false;
            }
        });

        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewActionPopUpWindow.dismiss();
            }
        });

    }

    public class GoogleMapInfoAdapter extends FragmentPagerAdapter {

        public GoogleMapInfoAdapter(FragmentManager fm) {

            super(fm);

        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return new PlaceHolderFragment();
                case 1:
                    return new InformationFragment(onGoingReservation, vehicleStatus);

            }

            return new PlaceHolderFragment();

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
