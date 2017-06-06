package com.example.justinkwik.hipcar.Main.Reservation.CheckoutReservationClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Response.SuccessResponse;
import com.example.justinkwik.hipcar.R;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.google.android.gms.maps.MapsInitializer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class CheckedoutReservationFragment extends Fragment implements CheckedoutReservationAdapter.GenerateClickedInterface{


    private final String checkOutReservationLink = "https://artemis-api-dev.hipcar.com/reservation/checked-out";
    private final String generateVoucherLink = "https://artemis-api-dev.hipcar.com/reservation/:id/generate-voucher";

    private int red;
    private int black;
    private int navBarGrey;
    private View rootView;

    private UserCredentials userCredentials;
    private CheckedOutReservation[] checkedOutReservations;
    private Gson gson;
    private RecyclerView checkedOutReservationRecyclerView;

    /*
    Start of popupwindow variables
     */
    //Used only to measure the size of the pop-up window.
    private CheckedOutReservation checkedOutReservation; //TODO: need to use this for the getting id of the reservation to generate voucher.
    private FrameLayout checkedOutReservationFrameLayout;
    private LayoutInflater layoutInflater;
    private WindowManager windowManager;
    private CheckedoutReservationFragment thisFragment;

    //PTR, measuring, and utility variables.
    private RelativeLayout reservationGreyScreenLoading;
    private Animation loadingScreenFadeOut;
    private Context context;
    private LottieAnimationView reservationLoadingLottieView;
    private PtrFrameLayout pullToRefreshLayout;
    private boolean pulledToRefresh;
    private boolean firstTimeClickedTab;

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

    public CheckedoutReservationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is for the google maps.
        firstTimeClickedTab = true;
        thisFragment = this;

        this.context = getContext().getApplicationContext();
        userCredentials = LoginActivity.getUserCredentials();
        gson = new Gson();
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        MapsInitializer.initialize(context);
        loadingScreenFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_loading);
        pulledToRefresh = false;

        red = ContextCompat.getColor(getContext(), R.color.red);
        black = ContextCompat.getColor(getContext(), R.color.black);
        navBarGrey = ContextCompat.getColor(getContext(), R.color.navBarGrey);

        checkedOutReservationStringRequest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_checkout_reservation, container, false);

        checkedOutReservationRecyclerView = (RecyclerView) rootView.findViewById(R.id.checkedOutReservationRecyclerView);
        checkedOutReservationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        checkedOutReservationRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        checkedOutReservationFrameLayout = (FrameLayout) rootView.findViewById(R.id.checkedOutReservationFrameLayout);

        reservationGreyScreenLoading = (RelativeLayout) rootView.findViewById(R.id.reservationGreyScreenLoading);
        reservationLoadingLottieView = (LottieAnimationView) rootView.findViewById(R.id.reservationLoadingLottieView);

        pullToRefreshLayout = (PtrFrameLayout) rootView.findViewById(R.id.pullToRefreshLayout);

        initializePullToRefreshLayout();

        showLoadingScreen(false);

        //Voucher Pop up variables
        voucherPopUpContainer = (ViewGroup) layoutInflater.inflate(R.layout.voucherpopup, null);
        voucherCheckInOutPopUpWindowMeasuredView = rootView.findViewById(R.id.voucherCheckInOutPopUpWindowMeasuredView);
        voucherAmountEditText = (EditText) voucherPopUpContainer.findViewById(R.id.voucherAmountEditText);
        voucherPurposeSpinner = (Spinner) voucherPopUpContainer.findViewById(R.id.voucherPurposeSpinner);
        voucherOkButton = (Button) voucherPopUpContainer.findViewById(R.id.voucherOkButton);
        voucherCancelButton = (Button) voucherPopUpContainer.findViewById(R.id.voucherCancelButton);

        return rootView;
    }

    private void showVoucherPopupWindowAndSetClickListeners(final String actionUrl) {

        voucherPopUpWindow = new PopupWindow(voucherPopUpContainer, voucherCheckInOutPopUpWindowMeasuredView.getWidth(),
                voucherCheckInOutPopUpWindowMeasuredView.getHeight(), true);

        voucherPopUpWindow.setAnimationStyle(R.style.PopUpWindowAnimation);

        voucherPopUpWindow.showAtLocation(checkedOutReservationFrameLayout, Gravity.CENTER, 0, 0);

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
                checkedOutReservationStringRequest();

            }
        });

    }

    private void checkedOutReservationStringRequest() {

        StringRequest checkedOutReservationRequest = new StringRequest(Request.Method.GET, checkOutReservationLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                checkedOutReservations = gson.fromJson(response, CheckedOutReservation[].class);

                checkedOutReservationRecyclerView.setAdapter(new CheckedoutReservationAdapter(getActivity(),
                        checkedOutReservations, thisFragment));

                if(!pulledToRefresh) {

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

        ConnectionManager.getInstance(context).add(checkedOutReservationRequest);

    }


    private void dismissLoadingScreen(boolean popUp) {

        reservationGreyScreenLoading.setVisibility(View.GONE);
        reservationGreyScreenLoading.startAnimation(loadingScreenFadeOut);
        reservationLoadingLottieView.pauseAnimation();

    }

    private void showLoadingScreen(boolean popUp) {

        reservationGreyScreenLoading.setVisibility(View.VISIBLE);
        reservationGreyScreenLoading.bringToFront();
        reservationLoadingLottieView.playAnimation();

    }

    private int dPToPx(final Context context, final float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!firstTimeClickedTab) {
            //Everytime the tab is selected, load the information again.
            checkedOutReservationStringRequest();
            showLoadingScreen(false);
        }
    }

    @Override
    public void showVoucherPopUp(CheckedOutReservation checkedOutReservation) {

        showVoucherPopupWindowAndSetClickListeners(generateVoucherLink.replace(":id", String.valueOf(checkedOutReservation.getId())));

    }
}
