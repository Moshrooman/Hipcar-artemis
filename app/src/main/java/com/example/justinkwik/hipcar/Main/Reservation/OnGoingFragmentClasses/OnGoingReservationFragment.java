package com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.VehicleStatus;
import com.example.justinkwik.hipcar.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
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
    private ViewPager googleMapAndInfoViewPager;
    private ViewPager buttonViewPager;
    private int googleMapAndInfoPosition;

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

        //Below start of assigning variables for popup view.
        viewActionPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.viewactionpopup, null);
        buttonViewPager = (ViewPager) viewActionPopUpContainer.findViewById(R.id.buttonViewPager);
        googleMapAndInfoViewPager = (ViewPager) viewActionPopUpContainer.findViewById(R.id.googleMapAndInfoViewPager);

        //Page change listener so while stuff is loading and information is succesfully loaded, it stays on the same page.
        googleMapAndInfoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                googleMapAndInfoPosition = position;
                Log.e("Slider Position: ", String.valueOf(googleMapAndInfoPosition));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

        //TODO: add a loading progress bar while the informatin loads and always set to the first item, but while loading
        //allow to scroll, so maybe try and display the old information so atleast they can read that. (override save state?)

        //We want to display the popup immediately while we let the information load in the background
        showPopUpAndSetExitClickListener(viewActionPopUpContainer);

        this.onGoingReservation = onGoingReservation;

        String modifiedVehicleStatusLink = vehicleStatusLink.replace(":id", String.valueOf(onGoingReservation.getVehicle_id()));

        StringRequest vehicleStatusRequest = new StringRequest(Request.Method.GET, modifiedVehicleStatusLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("String Request DONE: ", "True"); //TODO: take away this Log.

                vehicleStatus = gson.fromJson(response, VehicleStatus.class);

                setPopUpViewPagerAdapters();

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

    private void setPopUpViewPagerAdapters() {

        googleMapAndInfoViewPager.setAdapter(new GoogleMapInfoAdapter(layoutInflater, onGoingReservation, vehicleStatus));
        googleMapAndInfoViewPager.setCurrentItem(googleMapAndInfoPosition, false);

    }



    private void showPopUpAndSetExitClickListener(ViewGroup viewActionPopUpContainer) {

        //Set the infoview pager to the first item every time opened. So first thing they see is map.
        googleMapAndInfoViewPager.setCurrentItem(0, false);

        viewActionPopUpWindow = new PopupWindow(viewActionPopUpContainer, popUpWindowMeasuredView.getWidth(),
                popUpWindowMeasuredView.getHeight(), true);

        viewActionPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        viewActionPopUpWindow.showAtLocation(onGoingReservationFrameLayout, Gravity.CENTER, 0, 0);

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) viewActionPopUpContainer.getLayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.3f;
        windowManager.updateViewLayout(viewActionPopUpContainer, layoutParams);

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

    public class GoogleMapInfoAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private OnGoingReservation onGoingReservation;
        private VehicleStatus vehicleStatus;

        public GoogleMapInfoAdapter(LayoutInflater inflater, OnGoingReservation onGoingReservation, VehicleStatus vehicleStatus) {

            this.inflater = inflater;
            this.onGoingReservation = onGoingReservation;
            this.vehicleStatus = vehicleStatus;

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View layoutView;

            if(position == 0) {

                layoutView = inflater.inflate(R.layout.fragment_place_holder, null);

            } else {

                layoutView = inflater.inflate(R.layout.fragment_information, null);

                //Instantiate and assign all values in here so variables aren't created for both views.
                DecimalFormat decimalFormat = new DecimalFormat();
                TextView informationNameTextView = (TextView) layoutView.findViewById(R.id.informationNameTextView);
                TextView informationContactNumberTextView = (TextView) layoutView.findViewById(R.id.informationContactNumberTextView);
                TextView informationEmailTextView = (TextView) layoutView.findViewById(R.id.informationEmailTextView);
                TextView informationDurationTextView = (TextView) layoutView.findViewById(R.id.informationDurationTextView);
                TextView informationBalanceTextView = (TextView) layoutView.findViewById(R.id.informationBalanceTextView);
                TextView informationPriceEstimateTextView = (TextView) layoutView.findViewById(R.id.informationPriceEstimateTextView);
                TextView informationPlateNumberTextView = (TextView) layoutView.findViewById(R.id.informationPlateNumberTextView);
                TextView informationImmobilizerTextView = (TextView) layoutView.findViewById(R.id.informationImmobilizerTextView);
                TextView informationIgnitionTextView = (TextView) layoutView.findViewById(R.id.informationIgnitionTextView);
                TextView informationCentralLockTextView = (TextView) layoutView.findViewById(R.id.informationCentralLockTextView);
                TextView informationMileageTextView = (TextView) layoutView.findViewById(R.id.informationMileageTextView);
                TextView informationSpeedTextView = (TextView) layoutView.findViewById(R.id.informationSpeedTextView);

                informationNameTextView.setText(onGoingReservation.getFull_name());
                informationContactNumberTextView.setText(onGoingReservation.getContact_number());
                informationEmailTextView.setText(onGoingReservation.getEmail());
                informationDurationTextView.setText(new OnGoingReservationAdapter().formatDateString(
                        onGoingReservation.getReturn_date(), true));
                informationBalanceTextView.setText("Rp. " +
                        String.valueOf(decimalFormat.format(onGoingReservation.getUser().getBalance())));
                informationPriceEstimateTextView.setText("Rp. " +
                        String.valueOf(decimalFormat.format(onGoingReservation.getTotal_price())));
                informationPlateNumberTextView.setText(onGoingReservation.getVehicle().getPlate_number());
                informationImmobilizerTextView.setText(vehicleStatus.getImmobilizer());
                informationIgnitionTextView.setText(vehicleStatus.getIgnition());
                informationCentralLockTextView.setText(vehicleStatus.getCentral_lock());
                informationMileageTextView.setText(String.valueOf(vehicleStatus.getMileage()));
                informationSpeedTextView.setText(String.valueOf(vehicleStatus.getPosition().getSpeed_over_ground()));

            }

            container.addView(layoutView);

            return layoutView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
