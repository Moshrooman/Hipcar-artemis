package com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses;

import android.content.Context;
import android.content.res.ColorStateList;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

//TODO: for vehicle fragment, ongoingreservation, we need to make it so that we can click everything but get status while location is loading
//1. load the viewpager instantly when clicking view, and load all of the textviews that don't require vehicle status
//2. as for the actual googlemap and the vehiclestatus textviews, we need to assign those to private variables
//then in the vehicle status string request in the on response we call a method that will assign all the textviews with values.
//3. Then we need to only disable the get status button when the shit is loading.
//4. Set the restart on the string requests to 20 seconds. Need to search for all ConnectionManager and add the line above it.
//      make sure to enable the buttons and dismiss the loading screens when the error comes, also to toast a message.

//TODO: for this only
//1. Need to make the recycler view shorter and add the "Add new vehicle" button.
//2. Need to fix the registration expire format date time its off by 12 hours.

public class VehicleFragment extends Fragment implements VehicleAdapter.VehicleStatusInterface{

    private final String vehicleLink = "https://artemis-api-dev.hipcar.com/vehicle?scope=includeModelAndMake,includeStation,includeVehicleRates";
    private final String vehicleActionLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/";
    private static final String activateVehicleLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/activate";
    private static final String deactivateVehicleLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/deactivate";
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
    private PopupWindow vehicleViewPopUpWindow;
    private ViewGroup vehicleViewPopUpContainer;
    private LayoutInflater layoutInflater;
    private WindowManager windowManager;
    private TextView exitTextView;
    //Viewpagers for the popupwindow.
    private ViewPager googleMapAndInfoViewPager;
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

    /*
    Start of activate/deactivate popup window variables
     */

    private PopupWindow activateDeactivatePopUpWindow;
    private View activateDeactivateMeasurePopUpWindow;
    private ViewGroup activateDeactivatePopUpContainer;
    private TextView activateDeactivateTitleTextView;
    private TextView activateDeactivateConfirmationTextView;
    private Button activateDeactivateCancelButton;
    private Button activateDeactivateOkButton;
    private GoogleMapInfoAdapter googleMapInfoAdapter;
    private Button getStatusButton;

    MapView hipCarMapView;
    TextView vehiclesInformationImmobilizerTextView;
    TextView vehiclesInformationIgnitionTextView;
    TextView vehiclesInformationCentralLockTextView;
    TextView vehiclesInformationMileageTextView;

    ColorStateList defaultTextViewColor;

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
        activateDeactivateMeasurePopUpWindow = rootView.findViewById(R.id.activateDeactivateMeasurePopUpWindow);

        activateDeactivatePopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.activatedeactivatepopup, null);
        activateDeactivateTitleTextView = (TextView) activateDeactivatePopUpContainer.findViewById(R.id.activateDeactivateTitleTextView);
        activateDeactivateConfirmationTextView = (TextView) activateDeactivatePopUpContainer.findViewById(R.id.activateDeactivateConfirmationTextView);
        activateDeactivateCancelButton = (Button) activateDeactivatePopUpContainer.findViewById(R.id.activateDeactivateCancelButton);
        activateDeactivateOkButton = (Button) activateDeactivatePopUpContainer.findViewById(R.id.activateDeactivateOkButton);

        activateDeactivateCancelButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    activateDeactivateCancelButton.setBackgroundResource(R.drawable.redactionviewbuttonpressed);

                } else {

                    activateDeactivateCancelButton.setBackgroundResource(R.drawable.redactionviewbutton);

                }

                return false;
            }
        });

        activateDeactivateOkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    activateDeactivateOkButton.setBackgroundResource(R.drawable.blueactionviewbuttonpressed);

                } else {

                    activateDeactivateOkButton.setBackgroundResource(R.drawable.blueactionviewbutton);

                }

                return false;
            }
        });

        initializePullToRefreshLayout();

        showLoadingScreen(false);

        //Below start of assigning variables for popup view.
        vehicleViewPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.vehicleviewpopup, null);
        googleMapAndInfoViewPager = (ViewPager) vehicleViewPopUpContainer.findViewById(R.id.googleMapAndInfoViewPager);
        popUpActionButtonArray = new Button[]{
                (Button) vehicleViewPopUpContainer.findViewById(R.id.unlockEngineButton),
                (Button) vehicleViewPopUpContainer.findViewById(R.id.lockEngineButton),
                (Button) vehicleViewPopUpContainer.findViewById(R.id.unlockDoorButton),
                (Button) vehicleViewPopUpContainer.findViewById(R.id.lockDoorButton),
                (Button) vehicleViewPopUpContainer.findViewById(R.id.resetModemButton),
                (Button) vehicleViewPopUpContainer.findViewById(R.id.getStatusButton),
        };
        getStatusButton = (Button) vehicleViewPopUpContainer.findViewById(R.id.getStatusButton);

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

        } else if(actionButtonText.toLowerCase().contains("lock")) {

            requestLink = vehicleActionLink
                    .replace(":id", String.valueOf(vehicle.getId()))
                    .concat(actionButton.getText().toString().toLowerCase().replace(" ", "-"));

        } else {

            requestLink = vehicleActionLink
                    .replace(":id", String.valueOf(vehicle.getId()))
                    .concat("modem-reset");

            //TODO: the reset modem comes back with error code but the modem actually still resets.

        }

        if (position == 1 || position == 3) {

            disabledPosition = position - 1;

        } else if (position == 0 || position == 2){

            disabledPosition = position + 1;

        } else {

            disabledPosition = position;

        }

        disableButton(actionButton);
        disableButton(popUpActionButtonArray[disabledPosition]);

        Log.e("Link: ", requestLink);

        final String finalRequestLink = requestLink;
        StringRequest buttonActionRequest = new StringRequest(Request.Method.PUT, requestLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                SuccessResponse responseString = gson.fromJson(response, SuccessResponse.class);

                enableButton(actionButton);
                enableButton(popUpActionButtonArray[disabledPosition]);

                SuperToast superToast = SuperToast.create(context, responseString.getMessage(), Style.DURATION_SHORT,
                        Style.green()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

                if (!finalRequestLink.contains("modem-reset")) {
                    Log.e("True", "True 2"); //TODO: only do the vehicles String request if its not a modem reset.
                    vehiclesStringRequest(thisFragment, true, actionButton);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                SuperToast superToast = SuperToast.create(context, "Error Making Request!", Style.DURATION_SHORT,
                        Style.red()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

                enableButton(actionButton);
                enableButton(popUpActionButtonArray[disabledPosition]);

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
                googleMapInfoAdapter.setVehicleStatusFields(vehicleStatus);
                dismissLoadingScreen(true);
                enableButton(actionButton);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                SuperToast superToast = SuperToast.create(context, "Error Making Request!", Style.DURATION_SHORT,
                        Style.red()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

                dismissLoadingScreen(true);
                enableButton(actionButton);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("token", userCredentials.getToken());

                return headerMap;
            }
        };

        //TODO: vehicleStatusRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
            setVehicleInformationLoadingTextViews();

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
                        vehicleStatuses, vehicleFragment, vehicleRecyclerView));

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

                SuperToast superToast = SuperToast.create(context, "Error Making Request!", Style.DURATION_SHORT,
                        Style.red()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

                if(!pulledToRefresh && !popUpRefresh) {

                    dismissLoadingScreen(false);

                }

                pullToRefreshLayout.refreshComplete();
                pulledToRefresh = false;
                firstTimeClickedTab = false;

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("token", userCredentials.getToken());

                return headerMap;
            }
        };

        //TODO: vehiclesRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        ConnectionManager.getInstance(context).add(vehiclesRequest);

    }

    @Override
    public void showVehicleStatusPopup(final Vehicle vehicle, int position) {

        this.vehicle = vehicle;
        this.recyclerViewPosition = position;

        setPopUpViewPagerAdapters();
        //We want to display the popup immediately while we let the information load in the background
        showPopUpAndSetExitClickListener();

        String modifiedVehicleStatusLink = vehicleActionLink.replace(":id", String.valueOf(vehicle.getId()))
                .concat("status");

        StringRequest vehicleStatusRequest = new StringRequest(Request.Method.GET, modifiedVehicleStatusLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                vehicleStatus = gson.fromJson(response, VehicleStatus.class);

                googleMapInfoAdapter.setVehicleStatusFields(vehicleStatus);
                dismissLoadingScreen(true);
                enableButton(getStatusButton);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                SuperToast superToast = SuperToast.create(context, "Error Making Request!", Style.DURATION_SHORT,
                        Style.red()).setAnimations(Style.ANIMATIONS_POP);
                superToast.show();

                dismissLoadingScreen(true);
                enableButton(getStatusButton);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("token", userCredentials.getToken());

                return headerMap;
            }
        };

        //TODO: vehicleStatusRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        ConnectionManager.getInstance(context).add(vehicleStatusRequest);

    }

    @Override
    public void activateDeactivateConfirmationPopup(final Vehicle vehicle, boolean activate, final Button activateActionButton, final Button deactivateActionButton) {

        final String vehicleLink;

        if (activate) {
            activateDeactivateTitleTextView.setText("Activate Vehicle");
            activateDeactivateConfirmationTextView.setText("Are you sure you want to activate this vehicle?");
            vehicleLink = activateVehicleLink.replace(":id", String.valueOf(vehicle.getId()));
        } else {
            activateDeactivateTitleTextView.setText("Deactivate Vehicle");
            activateDeactivateConfirmationTextView.setText("Are you sure you want to Deactivate this vehicle?");
            vehicleLink = deactivateVehicleLink.replace(":id", String.valueOf(vehicle.getId()));
        }

        activateDeactivateOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateDeactivatePopUpWindow.dismiss();

                activateActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
                activateActionButton.setEnabled(false);
                deactivateActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
                deactivateActionButton.setEnabled(false);

                StringRequest activateVehicleStringRequest = new StringRequest(Request.Method.PUT, vehicleLink, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SuccessResponse responseString = gson.fromJson(response, SuccessResponse.class);

                        SuperToast superToast = SuperToast.create(context, responseString.getMessage(), Style.DURATION_SHORT,
                                Style.green()).setAnimations(Style.ANIMATIONS_POP);
                        superToast.show();

                        activateActionButton.setBackgroundResource(R.drawable.greenactionviewbutton);
                        activateActionButton.setEnabled(true);
                        deactivateActionButton.setBackgroundResource(R.drawable.redactionviewbutton);
                        deactivateActionButton.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SuperToast superToast = SuperToast.create(context, "Error Making Request!", Style.DURATION_SHORT,
                                Style.red()).setAnimations(Style.ANIMATIONS_POP);
                        superToast.show();

                        activateActionButton.setBackgroundResource(R.drawable.greenactionviewbutton);
                        activateActionButton.setEnabled(true);
                        deactivateActionButton.setBackgroundResource(R.drawable.redactionviewbutton);
                        deactivateActionButton.setEnabled(true);

                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("token", userCredentials.getToken());

                        return headerMap;
                    }


                };

                ConnectionManager.getInstance(context).add(activateVehicleStringRequest);

            }
        });

        activateDeactivateCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateDeactivatePopUpWindow.dismiss();
            }
        });

        activateDeactivatePopUpWindow = new PopupWindow(activateDeactivatePopUpContainer, activateDeactivateMeasurePopUpWindow.getWidth(),
                activateDeactivateMeasurePopUpWindow.getHeight(), true);

        activateDeactivatePopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        activateDeactivatePopUpWindow.showAtLocation(vehicleFrameLayout, Gravity.CENTER, 0, 0);

        dimBackground(activateDeactivatePopUpContainer);



    }

    private void setPopUpViewPagerAdapters() {

        googleMapInfoAdapter = new GoogleMapInfoAdapter(layoutInflater, vehicle);

        googleMapAndInfoViewPager.setAdapter(googleMapInfoAdapter);
        googleMapAndInfoViewPager.setCurrentItem(googleMapAndInfoPosition, false);

    }


    private void showPopUpAndSetExitClickListener() {

        //Set the infoview pager to the first item every time opened. So first thing they see is map.
        googleMapAndInfoViewPager.setCurrentItem(0, false);

        vehicleViewPopUpWindow = new PopupWindow(vehicleViewPopUpContainer, popUpWindowMeasuredView.getWidth(),
                popUpWindowMeasuredView.getHeight(), true);

        vehicleViewPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        vehicleViewPopUpWindow.showAtLocation(vehicleFrameLayout, Gravity.CENTER, 0, 0);

        disableButton(getStatusButton);

        dimBackground(vehicleViewPopUpContainer);

        exitTextView = (TextView) vehicleViewPopUpContainer.findViewById(R.id.exitTextView);

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
                vehicleViewPopUpWindow.dismiss();
            }
        });

    }

    private void dimBackground(ViewGroup viewGroup) {

        //Different android versions have different view hierarchie's need to split the code for dimming background.
        if (android.os.Build.VERSION.SDK_INT > 22) {
            View popUpDimView = (View) viewGroup.getParent();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) popUpDimView.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(popUpDimView, layoutParams);
        } else {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) viewGroup.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(viewGroup, layoutParams);
        }

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

    private void disableButton(Button popUpActionButton) {

        popUpActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
        popUpActionButton.setEnabled(false);

    }

    private void enableButton(Button popUpActionButton) {

        popUpActionButton.setBackgroundResource(R.drawable.blueactionviewbutton);

        popUpActionButton.setEnabled(true);

    }

    public class GoogleMapInfoAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private Vehicle vehicle;

        public GoogleMapInfoAdapter(LayoutInflater inflater, Vehicle vehicle) {

            this.inflater = inflater;
            this.vehicle = vehicle;

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

                hipCarMapView = (MapView) layoutView.findViewById(R.id.hipCarMapView);
                popUpGreyScreenLoading = (RelativeLayout) layoutView.findViewById(R.id.popUpGreyScreenLoading);
                popUpLoadingLottieView = (LottieAnimationView) layoutView.findViewById(R.id.popUpLoadingLottieView);

                showLoadingScreen(true);

                hipCarMapView.onCreate(savedInstanceState);
                hipCarMapView.onResume();

            } else {

                layoutView = inflater.inflate(R.layout.fragment_vehicle_information, null);

                //Instantiate and assign all values in here so variables aren't created for both views.
                TextView vehiclesInformationPlateNumberTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationPlateNumberTextView);
                TextView vehiclesInformationVehicleModelTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationVehicleModelTextView);
                TextView vehiclesInformationStationTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationStationTextView);
                TextView vehiclesInformationCapacityTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationCapacityTextView);
                TextView vehiclesInformationColorTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationColorTextView);
                TextView vehiclesInformationYearTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationYearTextView);
                TextView vehiclesInformationExcessKmChargeTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationExcessKmChargeTextView);
                TextView vehiclesInformationRegistrationExpireTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationRegistrationExpireTextView);

                vehiclesInformationImmobilizerTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationImmobilizerTextView);
                vehiclesInformationIgnitionTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationIgnitionTextView);
                vehiclesInformationCentralLockTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationCentralLockTextView);
                vehiclesInformationMileageTextView = (TextView) layoutView.findViewById(R.id.vehiclesInformationMileageTextView);


                vehiclesInformationPlateNumberTextView.setText(vehicle.getPlate_number());
                vehiclesInformationVehicleModelTextView.setText(vehicle.getVehicle_model().getName());
                vehiclesInformationStationTextView.setText(vehicle.getStation().getName());
                vehiclesInformationCapacityTextView.setText(String.valueOf(vehicle.getCapacity()));
                vehiclesInformationColorTextView.setText(vehicle.getColor());
                vehiclesInformationYearTextView.setText(String.valueOf(vehicle.getYear()));
                vehiclesInformationExcessKmChargeTextView.setText(String.valueOf(vehicle.getExcess_km_charge()));
                vehiclesInformationRegistrationExpireTextView.setText(new DateTime(vehicle.getRegistration_expire()).withZoneRetainFields(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok"))).toString("dd-MMM-yyyy HH:mm"));

                setVehicleInformationLoadingTextViews();
            }


            container.addView(layoutView);

            return layoutView;

        }

        private void setVehicleStatusFields(final VehicleStatus vehicleStatus) {

            //Information Work
            vehiclesInformationImmobilizerTextView.setText(vehicleStatus.getImmobilizer());
            vehiclesInformationIgnitionTextView.setText(vehicleStatus.getIgnition());
            vehiclesInformationCentralLockTextView.setText(vehicleStatus.getCentral_lock());
            vehiclesInformationMileageTextView.setText(String.valueOf(vehicleStatus.getMileage()));

            setVehicleInformationLoadingTextViewsColorDefault();

            //Google map work
            hipCarMapView.onCreate(savedInstanceState);

            hipCarMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    dismissLoadingScreen(true);

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

    private void setVehicleInformationLoadingTextViews() {

        vehiclesInformationImmobilizerTextView.setText("Loading...");
        vehiclesInformationIgnitionTextView.setText("Loading...");
        vehiclesInformationCentralLockTextView.setText("Loading...");
        vehiclesInformationMileageTextView.setText("Loading...");

        defaultTextViewColor = vehiclesInformationImmobilizerTextView.getTextColors();

        vehiclesInformationImmobilizerTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        vehiclesInformationIgnitionTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        vehiclesInformationCentralLockTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        vehiclesInformationMileageTextView.setTextColor(ContextCompat.getColor(context, R.color.red));

    }

    private void setVehicleInformationLoadingTextViewsColorDefault() {

        vehiclesInformationImmobilizerTextView.setTextColor(defaultTextViewColor);
        vehiclesInformationIgnitionTextView.setTextColor(defaultTextViewColor);
        vehiclesInformationCentralLockTextView.setTextColor(defaultTextViewColor);
        vehiclesInformationMileageTextView.setTextColor(defaultTextViewColor);

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
