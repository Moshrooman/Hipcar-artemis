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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.Response.SuccessResponse;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.VehicleStatus;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class OnGoingReservationFragment extends Fragment implements OnGoingReservationAdapter.VehicleStatusInterface{

    private final String onGoingReservationLink = "https://artemis-api-dev.hipcar.com/reservation/on-going";
    private final String generateVoucherLink = "https://artemis-api-dev.hipcar.com/reservation/:id/generate-voucher";
    private final String vehicleActionLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/";
    private int red;
    private int black;
    private int navBarGrey;
    private View rootView;

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
    private RelativeLayout popUpRelativeLayout;
    //Viewpagers for the popupwindow.
    private ViewPager googleMapAndInfoViewPager;
    private ScrollView buttonScrollView;
    private int googleMapAndInfoPosition;
    private Bundle savedInstanceState;
    private BitmapDescriptor carIcon;
    private RelativeLayout reservationGreyScreenLoading;
    private Animation loadingScreenFadeOut;
    private Context context;
    private LottieAnimationView reservationLoadingLottieView;
    private PtrFrameLayout pullToRefreshLayout;
    private OnGoingReservationFragment thisFragment;
    private boolean pulledToRefresh;
    private boolean firstTimeClickedTab;
    private RelativeLayout popUpGreyScreenLoading;
    private LottieAnimationView popUpLoadingLottieView;
    private Button[] popUpActionButtonArray;
    private int recyclerViewPosition;
    private static int disabledPosition;

    /*
    Start of variables for voucher pop up
     */

    private PopupWindow voucherPopUpWindow;
    private ViewGroup voucherPopUpContainer;
    private View voucherCheckInOutPopUpWindowMeasuredView;
    private TextView voucherExitTextView;
    private EditText voucherAmountEditText;
    private Spinner voucherPurposeSpinner;
    private Button voucherOkButton;
    private Button voucherCancelButton;

    /*
    Start of variables for check in/check out pop up
     */

    private PopupWindow checkInOutPopUpWindow;
    private ViewGroup checkInOutPopUpContainer;
    private TextView checkInOutTitleTextView;
    private TextView checkInOutTextView;
    private TextView checkInOutExitTextView;
    private EditText checkInOutMileageEditText;
    private Button checkInOutOkButton;
    private Button checkInOutCancelButton;
    private TextView checkInOutDateTextView;

    public OnGoingReservationFragment() {

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

        onGoingReservationStringRequest(thisFragment, false, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_reservation, container, false);

        onGoingReservationRecyclerView = (RecyclerView) rootView.findViewById(R.id.onGoingReservationRecyclerView);
        onGoingReservationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        onGoingReservationRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        onGoingReservationFrameLayout = (FrameLayout) rootView.findViewById(R.id.onGoingReservationFrameLayout);
        popUpWindowMeasuredView = rootView.findViewById(R.id.popUpWindowMeasuredView);

        reservationGreyScreenLoading = (RelativeLayout) rootView.findViewById(R.id.reservationGreyScreenLoading);
        reservationLoadingLottieView = (LottieAnimationView) rootView.findViewById(R.id.reservationLoadingLottieView);

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

        //Voucher Pop up variables
        voucherPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.voucherpopup, null);
        voucherCheckInOutPopUpWindowMeasuredView = rootView.findViewById(R.id.voucherCheckInOutPopUpWindowMeasuredView);
        voucherAmountEditText = (EditText) voucherPopUpContainer.findViewById(R.id.voucherAmountEditText);
        voucherPurposeSpinner = (Spinner) voucherPopUpContainer.findViewById(R.id.voucherPurposeSpinner);
        voucherOkButton = (Button) voucherPopUpContainer.findViewById(R.id.voucherOkButton);
        voucherCancelButton = (Button) voucherPopUpContainer.findViewById(R.id.voucherCancelButton);

        //Check in/out pop up variables.
        checkInOutPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.checkinoutpopup, null);
        checkInOutExitTextView = (TextView) checkInOutPopUpContainer.findViewById(R.id.checkInOutExitTextView);
        checkInOutTitleTextView = (TextView) checkInOutPopUpContainer.findViewById(R.id.checkInOutTitleTextView);
        checkInOutTextView = (TextView) checkInOutPopUpContainer.findViewById(R.id.checkInOutTextView);
        checkInOutMileageEditText = (EditText) checkInOutPopUpContainer.findViewById(R.id.checkInOutMileageEditText);
        checkInOutOkButton = (Button) checkInOutPopUpContainer.findViewById(R.id.checkInOutOkButton);
        checkInOutCancelButton = (Button) checkInOutPopUpContainer.findViewById(R.id.checkInOutCancelButton);
        checkInOutDateTextView = (TextView) checkInOutPopUpContainer.findViewById(R.id.checkInOutDateTextView);

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

        if(actionButtonText.contains("Check")) {

            requestLink = vehicleActionLink
                    .replace(":id", String.valueOf(onGoingReservation.getId()))
                    .concat(actionButton.getText().toString().toLowerCase().replace(" ", "-"));

            showCheckInOutPopUpWindowAndSetClickListeners(requestLink, actionButtonText);

            //TODO: need to do popup asking for milage and date, the request is a put.
            return;

        } else if(actionButtonText.contains("Voucher")) {

            requestLink = generateVoucherLink
                    .replace(":id", String.valueOf(onGoingReservation.getId()));

            showVoucherPopupWindowAndSetClickListeners(requestLink);

            return;

        } else if(actionButtonText.contains("Status")) {

            //We are calling to refresh the recycler view information which in turn updates the current
            //this.onGoingReservation object, then we call refreshPopUpWindow in its on response because it uses
            //the new onGoingReservation object to display the information. The below method also handles
            //displaying the loading screens and stuff depending on the boolean passed.
            onGoingReservationStringRequest(thisFragment, true, actionButton);
            disableButton(actionButton);

            return;

        } else {

            requestLink = vehicleActionLink
                    .replace(":id", String.valueOf(onGoingReservation.getVehicle_id()))
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

                onGoingReservationStringRequest(thisFragment, true, actionButton);

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

    private void showVoucherPopupWindowAndSetClickListeners(final String actionUrl) {

        //TODO: need to make sure that the link for the generate voucher, that the id part changes when we select another
        //item from the recycler view.
        Log.e("Url: ", actionUrl);

        voucherPopUpWindow = new PopupWindow(voucherPopUpContainer, voucherCheckInOutPopUpWindowMeasuredView.getWidth(),
                voucherCheckInOutPopUpWindowMeasuredView.getHeight(), true);

        voucherPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        voucherPopUpWindow.showAtLocation(onGoingReservationFrameLayout, Gravity.CENTER, 0, 0);

        //Different android versions have different view hierarchie's need to split the code for dimming background.
        if (android.os.Build.VERSION.SDK_INT > 22) {
            View popUpDimView = (View) voucherPopUpContainer.getParent();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) popUpDimView.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(popUpDimView, layoutParams);
        } else {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) voucherPopUpContainer.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(voucherPopUpContainer, layoutParams);
        }

        voucherExitTextView = (TextView) voucherPopUpContainer.findViewById(R.id.voucherExitTextView);

        voucherExitTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    voucherExitTextView.setTextColor(black);

                } else {

                    voucherExitTextView.setTextColor(navBarGrey);

                }

                return false;
            }
        });

        voucherExitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voucherPopUpWindow.dismiss();
            }
        });

        ArrayAdapter<String> voucherAdapter = new ArrayAdapter<String>(rootView.getContext(),
                R.layout.customdropdown, getResources().getStringArray(R.array.voucherEntries));

        voucherPurposeSpinner.setAdapter(voucherAdapter);

        voucherOkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    voucherOkButton.setBackgroundResource(R.drawable.greenactionviewbuttonpressed);

                } else {

                    voucherOkButton.setBackgroundResource(R.drawable.greenactionviewbutton);

                }

                return false;
            }
        });

        voucherCancelButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    voucherCancelButton.setBackgroundResource(R.drawable.redactionviewbuttonpressed);

                } else {

                    voucherCancelButton.setBackgroundResource(R.drawable.redactionviewbutton);

                }

                return false;
            }
        });

        voucherOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String voucherAmountEditTextEntry = voucherAmountEditText.getText().toString();
                String voucherPurposeSpinnerEntry = voucherPurposeSpinner.getSelectedItem().toString();

                if (voucherAmountEditTextEntry.equals("") || voucherAmountEditTextEntry.equals("0")) {

                    SuperToast superToast = SuperToast.create(context, "Voucher Amount Must Be Greater Than 0!", Style.DURATION_SHORT,
                            Style.orange()).setAnimations(Style.ANIMATIONS_POP);
                    superToast.show();

                    return;

                }

                if (voucherPurposeSpinnerEntry.equals("")) {

                    SuperToast superToast = SuperToast.create(context, "Please Select A Voucher Purpose!", Style.DURATION_SHORT,
                            Style.orange()).setAnimations(Style.ANIMATIONS_POP);
                    superToast.show();

                    return;

                }

                final JSONObject voucherRequestBody = new JSONObject();

                try {

                    voucherRequestBody.put("amount", Integer.valueOf(voucherAmountEditTextEntry));
                    voucherRequestBody.put("type", voucherPurposeSpinnerEntry);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

                StringRequest voucherStringRequest = new StringRequest(Request.Method.POST, actionUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String responseString = gson.fromJson(response, SuccessResponse.class).getMessage();

                        SuperToast superToast = SuperToast.create(context, responseString, Style.DURATION_SHORT,
                                Style.green()).setAnimations(Style.ANIMATIONS_POP);
                        superToast.show();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return voucherRequestBody.toString().getBytes();
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("token", userCredentials.getToken());

                        return headerMap;
                    }
                };

                ConnectionManager.getInstance(context).add(voucherStringRequest);

            }
        });

        voucherCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voucherPopUpWindow.dismiss();
            }
        });

    }

    private void showCheckInOutPopUpWindowAndSetClickListeners (String actionUrl, String title) {

        //TODO: need to make a viewActionPopUp container that is inflated in the oncreate.
        //Need to make an empty view for the size that we want the voucher view to be. Make it invisible remember.
        checkInOutPopUpWindow = new PopupWindow(checkInOutPopUpContainer, voucherCheckInOutPopUpWindowMeasuredView.getWidth(),
                voucherCheckInOutPopUpWindowMeasuredView.getHeight(), true);

        checkInOutPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        //Show it in the center of the popupwindow, not the frame layout.
        checkInOutPopUpWindow.showAtLocation(onGoingReservationFrameLayout, Gravity.CENTER, 0, 0);

        //Different android versions have different view hierarchie's need to split the code for dimming background.
        if (android.os.Build.VERSION.SDK_INT > 22) {
            View popUpDimView = (View) checkInOutPopUpContainer.getParent();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) popUpDimView.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(popUpDimView, layoutParams);
        } else {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) checkInOutPopUpContainer.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.7f;
            windowManager.updateViewLayout(checkInOutPopUpContainer, layoutParams);
        }

        checkInOutTitleTextView.setText("Reservation " + title);
        checkInOutTextView.setText(title + " Date");

        checkInOutExitTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    checkInOutExitTextView.setTextColor(black);

                } else {

                    checkInOutExitTextView.setTextColor(navBarGrey);

                }

                return false;
            }
        });

        checkInOutExitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInOutPopUpWindow.dismiss();
            }
        });

        checkInOutOkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    checkInOutOkButton.setBackgroundResource(R.drawable.greenactionviewbuttonpressed);

                } else {

                    checkInOutOkButton.setBackgroundResource(R.drawable.greenactionviewbutton);

                }

                return false;
            }
        });

        checkInOutCancelButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    checkInOutCancelButton.setBackgroundResource(R.drawable.redactionviewbuttonpressed);

                } else {

                    checkInOutCancelButton.setBackgroundResource(R.drawable.redactionviewbutton);

                }

                return false;
            }
        });

        checkInOutOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mileageAmountEntry = checkInOutMileageEditText.getText().toString();

                if (mileageAmountEntry.equals("") || mileageAmountEntry.equals("0")) {

                    SuperToast superToast = SuperToast.create(context, "Mileage Value Must Be Greater Than 0!", Style.DURATION_SHORT,
                            Style.orange()).setAnimations(Style.ANIMATIONS_POP);
                    superToast.show();

                    return;

                }

            }
        });

        checkInOutDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: first set to the current time and date, then inflate a date and time picker as a dialoge
                //and set the checkinout date text view to the corresponding selected date and time.
                //in the form of dd/mm/yyyy hh:mm PM/AM
            }
        });

    }

    private void refreshPopUpWindowInfo(final Button actionButton) {

        String modifiedVehicleStatusLink = vehicleActionLink.replace(":id", String.valueOf(onGoingReservation.getVehicle_id()))
                .concat("status");

        StringRequest vehicleStatusRequest = new StringRequest(Request.Method.GET, modifiedVehicleStatusLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                vehicleStatus = gson.fromJson(response, VehicleStatus.class);

                //Problem is the this.onGoingreservation isn't updated.
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
                onGoingReservationStringRequest(thisFragment, false, null);

            }
        });

    }

    private void onGoingReservationStringRequest(final OnGoingReservationFragment onGoingReservationFragment, final boolean popUpRefresh,
                                                 final Button actionButton) {

        if (popUpRefresh) {

            showLoadingScreen(true);

        }

        StringRequest onGoingReservationRequest = new StringRequest(Request.Method.GET, onGoingReservationLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                onGoingReservations = gson.fromJson(response, OnGoingReservation[].class);

                if (popUpRefresh) {

                    onGoingReservation = onGoingReservations[recyclerViewPosition];
                    refreshPopUpWindowInfo(actionButton);

                }

                onGoingReservationRecyclerView.setAdapter(new OnGoingReservationAdapter(getActivity(),
                        onGoingReservations, onGoingReservationFragment));

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

        ConnectionManager.getInstance(context).add(onGoingReservationRequest);

    }

    @Override
    public void showVehicleStatusPopup(final OnGoingReservation onGoingReservation, int position) {

        //We want to display the popup immediately while we let the information load in the background
        showPopUpAndSetExitClickListener();

        this.onGoingReservation = onGoingReservation;
        this.recyclerViewPosition = position;

        String modifiedVehicleStatusLink = vehicleActionLink.replace(":id", String.valueOf(onGoingReservation.getVehicle_id()))
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

        googleMapAndInfoViewPager.setAdapter(new GoogleMapInfoAdapter(layoutInflater, onGoingReservation, vehicleStatus));
        googleMapAndInfoViewPager.setCurrentItem(googleMapAndInfoPosition, false);

    }


    private void showPopUpAndSetExitClickListener() {

        //Set the infoview pager to the first item every time opened. So first thing they see is map.
        googleMapAndInfoViewPager.setCurrentItem(0, false);

        viewActionPopUpWindow = new PopupWindow(viewActionPopUpContainer, popUpWindowMeasuredView.getWidth(),
                popUpWindowMeasuredView.getHeight(), true);

        viewActionPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        viewActionPopUpWindow.showAtLocation(onGoingReservationFrameLayout, Gravity.CENTER, 0, 0);

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
            reservationGreyScreenLoading.setVisibility(View.GONE);
            reservationGreyScreenLoading.startAnimation(loadingScreenFadeOut);
            reservationLoadingLottieView.pauseAnimation();
        } else {
            popUpGreyScreenLoading.setVisibility(View.GONE);
            popUpGreyScreenLoading.startAnimation(loadingScreenFadeOut);
            popUpLoadingLottieView.pauseAnimation();
        }

    }

    private void showLoadingScreen(boolean popUp) {

        if (!popUp) {
            reservationGreyScreenLoading.setVisibility(View.VISIBLE);
            reservationGreyScreenLoading.bringToFront();
            reservationLoadingLottieView.playAnimation();
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

    @Override
    public void onResume() {
        super.onResume();

        if (!firstTimeClickedTab) {
            //Everytime the tab is selected, load the information again.
            onGoingReservationStringRequest(thisFragment, false, null);
            showLoadingScreen(false);
        }
    }

}
