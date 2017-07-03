package com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses.OnGoingReservation;
import com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses.OnGoingReservationAdapter;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Response.SuccessResponse;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.VehicleStatus.VehicleStatus;
import com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses.ParseClassesVehicle.Vehicle;
import com.example.justinkwik.hipcar.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

//TODO: start copying everything from the ongoing classes becaue the view also brings up a popup of the map and stuff.\
//TODO: for the api link its https://artemis-api-dev.hipcar.com/vehicle header as token, and params "scope" and enter includeModelAndMake,includeStation,includeVehicleRates

public class VehicleFragment extends Fragment implements VehicleAdapter.VehicleStatusInterface{

    private final String vehicleLink = "https://artemis-api-dev.hipcar.com/vehicle";
    private final String vehicleActionLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/";
    private int red;
    private int black;
    private int navBarGrey;
    private View rootView;

    private UserCredentials userCredentials;
    private Vehicle[] vehicleStatuses;
    private Gson gson;
    private RecyclerView vehicleRecyclerView;
    private VehicleStatus vehicleStatus;

    /*
    Start of popupwindow variables
     */
    //Used only to measure the size of the pop-up window.
    private Vehicle vehicle;
    private View popUpWindowMeasuredView;
    private FrameLayout vehicleFrameLayout;
    private PopupWindow viewActionPopUpWindow;
    private ViewGroup viewActionPopUpContainer;
    private LayoutInflater layoutInflater;
    private WindowManager windowManager;
    private TextView exitTextView;
    private RelativeLayout popUpRelativeLayout;
    //Viewpagers for the popupwindow.
    private ViewPager googleMapAndInfoViewPager;
    private ScrollView buttonScrollView;
    private int googleMapAndInfoPosition;
    private Bundle savedInstanceState;
    private BitmapDescriptor carIcon;
    private RelativeLayout vehicleGreyScreenLoading;
    private Animation loadingScreenFadeOut;
    private Context context;
    private LottieAnimationView vehicleLoadingLottieView;
    private PtrFrameLayout pullToRefreshLayout;
    private VehicleFragment thisFragment;
    private boolean pulledToRefresh;
    private boolean firstTimeClickedTab;
    private RelativeLayout popUpGreyScreenLoading;
    private LottieAnimationView popUpLoadingLottieView;
    private Button[] popUpActionButtonArray;
    private int recyclerViewPosition;
    private static int disabledPosition;

    public VehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is for the google maps.
        this.savedInstanceState = savedInstanceState;
        this.thisFragment = this;
        firstTimeClickedTab = true;

        this.context = getContext().getApplicationContext();
        userCredentials = LoginActivity.getUserCredentials();
        gson = new Gson();
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        MapsInitializer.initialize(context);
        carIcon = BitmapDescriptorFactory.fromResource(R.drawable.caricon);
        loadingScreenFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_loading);
        pulledToRefresh = false;
        disabledPosition = 0;

        red = ContextCompat.getColor(getContext(), R.color.red);
        black = ContextCompat.getColor(getContext(), R.color.black);
        navBarGrey = ContextCompat.getColor(getContext(), R.color.navBarGrey);

        vehiclesStringRequest(thisFragment, false, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vehicle, container, false);

        vehicleRecyclerView = (RecyclerView) rootView.findViewById(R.id.vehicleRecyclerView);
        vehicleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        vehicleFrameLayout = (FrameLayout) rootView.findViewById(R.id.vehicleFrameLayout);
        popUpWindowMeasuredView = rootView.findViewById(R.id.popUpWindowMeasuredView);

        vehicleGreyScreenLoading = (RelativeLayout) rootView.findViewById(R.id.vehicleGreyScreenLoading);
        vehicleLoadingLottieView = (LottieAnimationView) rootView.findViewById(R.id.vehicleLoadingLottieView);

        pullToRefreshLayout = (PtrFrameLayout) rootView.findViewById(R.id.pullToRefreshLayout);

        initializePullToRefreshLayout();

        showLoadingScreen(false);

        //Below start of assigning variables for popup view.
        viewActionPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.viewactionpopup, null);
        popUpRelativeLayout = (RelativeLayout) viewActionPopUpContainer.findViewById(R.id.popUpRelativeLayout);
        buttonScrollView = (ScrollView) viewActionPopUpContainer.findViewById(R.id.buttonScrollView);
        googleMapAndInfoViewPager = (ViewPager) viewActionPopUpContainer.findViewById(R.id.googleMapAndInfoViewPager);
        popUpGreyScreenLoading = (RelativeLayout) viewActionPopUpContainer.findViewById(R.id.popUpGreyScreenLoading);
        popUpLoadingLottieView = (LottieAnimationView) viewActionPopUpContainer.findViewById(R.id.popUpLoadingLottieView);
        popUpActionButtonArray = new Button[]{
                (Button) viewActionPopUpContainer.findViewById(R.id.unlockEngineButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.lockEngineButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.unlockDoorButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.lockDoorButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.getStatusButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.generateVoucherButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.checkInButton),
                (Button) viewActionPopUpContainer.findViewById(R.id.checkOutButton),
        };

        setpopUpActionButtonClickListeners();

        //So only if they stay within the tab the google maps will stay there.
        googleMapAndInfoViewPager.setOffscreenPageLimit(1);

        //Page change listener so while stuff is loading and information is succesfully loaded, it stays on the same page.
        googleMapAndInfoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                googleMapAndInfoPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;

    }

    private void setpopUpActionButtonClickListeners() {

        for (int i = 0; i < popUpActionButtonArray.length; i++) {

            final int finalI = i;

            if(popUpActionButtonArray[i].getText().toString().contains("Check")) {

                popUpActionButtonArray[i].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            popUpActionButtonArray[finalI].setBackgroundResource(R.drawable.redactionviewbuttonpressed);

                        } else {

                            popUpActionButtonArray[finalI].setBackgroundResource(R.drawable.redactionviewbutton);

                        }

                        return false;
                    }
                });

            } else {

                popUpActionButtonArray[i].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            popUpActionButtonArray[finalI].setBackgroundResource(R.drawable.blueactionviewbuttonpressed);

                        } else {

                            popUpActionButtonArray[finalI].setBackgroundResource(R.drawable.blueactionviewbutton);

                        }

                        return false;
                    }
                });

            }

            popUpActionButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    actionButtonStringRequest(popUpActionButtonArray[finalI], finalI);

                }
            });

        }

    }

    private void actionButtonStringRequest(final Button actionButton, int position) {

        String actionButtonText = actionButton.getText().toString();
        String requestLink = "";

        if(actionButtonText.contains("Status")) {

            //We are calling to refresh the recycler view information which in turn updates the current
            //this.vehicle object, then we call refreshPopUpWindow in its on response because it uses
            //the new vehicle object to display the information. The below method also handles
            //displaying the loading screens and stuff depending on the boolean passed.
            vehiclesStringRequest(thisFragment, true, actionButton);
            disableButton(actionButton);

            return;

        } else {

            requestLink = vehicleActionLink
                    .replace(":id", String.valueOf(vehicle.getId()))
                    .concat(actionButton.getText().toString().toLowerCase().replace(" ", "-"));

        }

        if (position == 1 || position == 3) {

            disabledPosition = position - 1;

        } else {

            disabledPosition = position + 1;

        }

        disableButton(actionButton);
        disableButton(popUpActionButtonArray[disabledPosition]);

        StringRequest buttonActionRequest = new StringRequest(Request.Method.PUT, requestLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                SuccessResponse responseString = gson.fromJson(response, SuccessResponse.class);

                enableButton(actionButton);
                enableButton(popUpActionButtonArray[disabledPosition]);

                SuperToast superToast = SuperToast.create(context, responseString.getMessage(), Style.DURATION_SHORT,
                        Style.green()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

                vehiclesStringRequest(thisFragment, true, actionButton);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                SuperToast superToast = SuperToast.create(context, "Error Making Request!", Style.DURATION_SHORT,
                        Style.red()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("token", userCredentials.getToken());


                return headerMap;
            }

        };

        ConnectionManager.getInstance(context).add(buttonActionRequest);

    }

    private void refreshPopUpWindowInfo(final Button actionButton) {

        String modifiedVehicleStatusLink = vehicleActionLink.replace(":id", String.valueOf(vehicle.getId()))
                .concat("status");

        StringRequest vehicleStatusRequest = new StringRequest(Request.Method.GET, modifiedVehicleStatusLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                vehicleStatus = gson.fromJson(response, VehicleStatus.class);

                setPopUpViewPagerAdapters();
                dismissLoadingScreen(true);
                enableButton(actionButton);

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

        ConnectionManager.getInstance(context).add(vehicleStatusRequest);

    }

    private void initializePullToRefreshLayout() {

        int dPHeight = dPToPx(context, 10);

        StoreHouseHeader storeHouseHeader = new StoreHouseHeader(context);
        storeHouseHeader.setPadding(0, dPHeight, 0, dPHeight);
        storeHouseHeader.setTextColor(red);
        storeHouseHeader.initWithString("HIPCAR");

        pullToRefreshLayout.setHeaderView(storeHouseHeader);
        pullToRefreshLayout.addPtrUIHandler(storeHouseHeader);

        pullToRefreshLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(pullToRefreshLayout, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                pulledToRefresh = true;
                vehiclesStringRequest(thisFragment, false, null);

            }
        });

    }

    private void vehiclesStringRequest(final VehicleFragment vehicleFragment, final boolean popUpRefresh,
                                       final Button actionButton) {

        if (popUpRefresh) {

            showLoadingScreen(true);

        }

        StringRequest vehiclesRequest = new StringRequest(Request.Method.GET, vehicleLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                vehicleStatuses = gson.fromJson(response, Vehicle[].class);

                if (popUpRefresh) {

                    vehicle = vehicleStatuses[recyclerViewPosition];
                    refreshPopUpWindowInfo(actionButton);

                }

                vehicleRecyclerView.setAdapter(new VehicleAdapter(getActivity(),
                        vehicleStatuses, vehicleFragment));

                if(!pulledToRefresh && !popUpRefresh) {

                    dismissLoadingScreen(false);

                }

                pullToRefreshLayout.refreshComplete();
                pulledToRefresh = false;
                firstTimeClickedTab = false;

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

        ConnectionManager.getInstance(context).add(vehiclesRequest);

    }

    @Override
    public void showVehicleStatusPopup(final Vehicle vehicle, int position) {

        //We want to display the popup immediately while we let the information load in the background
        showPopUpAndSetExitClickListener();

        this.vehicle = vehicle;
        this.recyclerViewPosition = position;

        String modifiedVehicleStatusLink = vehicleActionLink.replace(":id", String.valueOf(vehicle.getId()))
                .concat("status");

        StringRequest vehicleStatusRequest = new StringRequest(Request.Method.GET, modifiedVehicleStatusLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                vehicleStatus = gson.fromJson(response, VehicleStatus.class);

                setPopUpViewPagerAdapters();
                dismissLoadingScreen(true);
                enablePopUpButtons();

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

        ConnectionManager.getInstance(context).add(vehicleStatusRequest);

    }

    private void setPopUpViewPagerAdapters() {

        googleMapAndInfoViewPager.setAdapter(new GoogleMapInfoAdapter(layoutInflater, vehicle, vehicleStatus));
        googleMapAndInfoViewPager.setCurrentItem(googleMapAndInfoPosition, false);

    }


    private void showPopUpAndSetExitClickListener() {

        //Set the infoview pager to the first item every time opened. So first thing they see is map.
        googleMapAndInfoViewPager.setCurrentItem(0, false);

        viewActionPopUpWindow = new PopupWindow(viewActionPopUpContainer, popUpWindowMeasuredView.getWidth(),
                popUpWindowMeasuredView.getHeight(), true);

        viewActionPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        viewActionPopUpWindow.showAtLocation(vehicleFrameLayout, Gravity.CENTER, 0, 0);

        showLoadingScreen(true);
        disablePopUpButtons();

        //Different android versions have different view hierarchie's need to split the code for dimming background.
        if (android.os.Build.VERSION.SDK_INT > 22) {
            View popUpDimView = (View) viewActionPopUpContainer.getParent();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) popUpDimView.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(popUpDimView, layoutParams);
        } else {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) viewActionPopUpContainer.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(viewActionPopUpContainer, layoutParams);
        }

        exitTextView = (TextView) viewActionPopUpContainer.findViewById(R.id.exitTextView);

        exitTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    exitTextView.setTextColor(black);

                } else {

                    exitTextView.setTextColor(navBarGrey);

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

    private void dismissLoadingScreen(boolean popUp) {

        //If it is not the popup, we handle the reservation loading screen.
        if(!popUp) {
            vehicleGreyScreenLoading.setVisibility(View.GONE);
            vehicleGreyScreenLoading.startAnimation(loadingScreenFadeOut);
            vehicleLoadingLottieView.pauseAnimation();
        } else {
            popUpGreyScreenLoading.setVisibility(View.GONE);
            popUpGreyScreenLoading.startAnimation(loadingScreenFadeOut);
            popUpLoadingLottieView.pauseAnimation();
        }

    }

    private void showLoadingScreen(boolean popUp) {

        if (!popUp) {
            vehicleGreyScreenLoading.setVisibility(View.VISIBLE);
            vehicleGreyScreenLoading.bringToFront();
            vehicleLoadingLottieView.playAnimation();
        } else {
            popUpGreyScreenLoading.setVisibility(View.VISIBLE);
            popUpGreyScreenLoading.bringToFront();
            popUpLoadingLottieView.playAnimation();
        }



    }

    private int dPToPx(final Context context, final float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    private void disablePopUpButtons() {

        for (int i = 0; i < popUpActionButtonArray.length; i++) {

            popUpActionButtonArray[i].setBackgroundResource(R.drawable.disabledactionviewbutton);
            popUpActionButtonArray[i].setEnabled(false);

        }

    }

    private void enablePopUpButtons() {

        for (int i = 0; i < popUpActionButtonArray.length; i++) {

            Button popUpActionButton = popUpActionButtonArray[i];

            if (popUpActionButton.getText().toString().contains("Check")) {

                popUpActionButtonArray[i].setBackgroundResource(R.drawable.redactionviewbutton);

            } else {

                popUpActionButtonArray[i].setBackgroundResource(R.drawable.blueactionviewbutton);

            }


            popUpActionButtonArray[i].setEnabled(true);

        }

    }

    private void disableButton(Button popUpActionButton) {

        popUpActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
        popUpActionButton.setEnabled(false);

    }

    private void enableButton(Button popUpActionButton) {

        if (popUpActionButton.getText().toString().contains("Check")) {

            popUpActionButton.setBackgroundResource(R.drawable.redactionviewbutton);

        } else {

            popUpActionButton.setBackgroundResource(R.drawable.blueactionviewbutton);

        }

        popUpActionButton.setEnabled(true);

    }

    public class GoogleMapInfoAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private Vehicle vehicle;
        private VehicleStatus vehicleStatus;

        public GoogleMapInfoAdapter(LayoutInflater inflater, Vehicle vehicle, VehicleStatus vehicleStatus) {

            this.inflater = inflater;
            this.vehicle = vehicle;
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

                layoutView = inflater.inflate(R.layout.fragment_map, null);

                final MapView hipCarMapView = (MapView) layoutView.findViewById(R.id.hipCarMapView);

                hipCarMapView.onCreate(savedInstanceState);

                hipCarMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        LatLng carCoordinates = new LatLng(vehicleStatus.getPosition().getLat(), vehicleStatus.getPosition().getLon());

                        googleMap.getUiSettings().setMapToolbarEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        googleMap.addMarker(new MarkerOptions()
                                .position(carCoordinates)
                                .title("Car Location")
                                .icon(carIcon));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(carCoordinates, 18));

                        hipCarMapView.onResume();

                    }
                });


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

                informationNameTextView.setText(vehicle.getFull_name());
                informationContactNumberTextView.setText(vehicle.getContact_number());
                informationEmailTextView.setText(vehicle.getEmail());
                informationDurationTextView.setText(new OnGoingReservationAdapter().formatDateString(
                        vehicle.getReturn_date(), true));
                informationBalanceTextView.setText("Rp. " +
                        String.valueOf(decimalFormat.format(vehicle.getUser().getBalance())));
                informationPriceEstimateTextView.setText("Rp. " +
                        String.valueOf(decimalFormat.format(vehicle.getTotal_price())));
                informationPlateNumberTextView.setText(vehicle.getVehicle().getPlate_number());
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

    @Override
    public void onResume() {
        super.onResume();

        if (!firstTimeClickedTab) {
            //Everytime the tab is selected, load the information again.
            vehiclesStringRequest(thisFragment, false, null);
            showLoadingScreen(false);
        }
    }

}