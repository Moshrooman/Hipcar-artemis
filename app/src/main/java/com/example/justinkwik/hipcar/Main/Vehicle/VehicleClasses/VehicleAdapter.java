package com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.justinkwik.hipcar.ConnectionManager;
import com.example.justinkwik.hipcar.ExpandAnimation.ExpandAnimation;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Response.SuccessResponse;
import com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses.ParseClassesVehicle.Vehicle;
import com.example.justinkwik.hipcar.R;
import com.example.justinkwik.hipcar.Splash.SplashActivity;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Justin Kwik on 03/07/2017.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    //TODO: need to create a confirmation pop-up screen for the activate and deactivate vehicle.
    //TODO: need to scroll to the bottom of the drawer view when expanding vehicle.

    private static final String activateVehicleLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/activate";
    private static final String deactivateVehicleLink = "https://artemis-api-dev.hipcar.com/vehicle/:id/deactivate";
    private Context context;
    private Vehicle[] vehicles;
    private DecimalFormat decimalFormat;
    private CalligraphyTypefaceSpan Exo2Bold;
    private CalligraphyTypefaceSpan Exo2Regular;
    private VehicleStatusInterface vehicleStatusInterface;
    private static boolean expandedOne = false;
    private UserCredentials userCredentials;
    private Gson gson;

    public VehicleAdapter(Context context, Vehicle[] vehicles, VehicleStatusInterface vehicleStatusInterface) {

        this.context = context;
        this.vehicles = vehicles;
        this.decimalFormat = new DecimalFormat();
        this.vehicleStatusInterface = vehicleStatusInterface;
        this.userCredentials = LoginActivity.getUserCredentials();
        this.gson = new Gson();

        Exo2Bold = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Bold.ttf"));
        Exo2Regular = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Regular.ttf"));

    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View vehiclesRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehiclesrow,
                parent, false);

        return new VehicleViewHolder(vehiclesRow);

    }

    @Override
    public void onBindViewHolder(final VehicleViewHolder holder, int position) {

        Vehicle vehicle = vehicles[position];

        ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) holder.expandableVehicles.getLayoutParams();

        Log.e(String.valueOf(position), String.valueOf(holder.lineToX.getVisibility() == View.VISIBLE));

        if (vehicle.getExpanded() == false && mp.bottomMargin == 0) {

            holder.lineToX.setVisibility(View.VISIBLE);
            holder.xToLine.setVisibility(View.INVISIBLE);

            ExpandAnimation collapseAnimation = new ExpandAnimation(holder.expandableVehicles, 0);
            collapseAnimation.setUpCollapseSubMenus(true);
            holder.expandableVehicles.startAnimation(collapseAnimation);

            mp.setMargins(0, 0, 0, -holder.expandableVehicles.getHeight());

        } else if (vehicle.getExpanded() == true && mp.bottomMargin != 0) {

            holder.lineToX.setVisibility(View.INVISIBLE);
            holder.xToLine.setVisibility(View.VISIBLE);

            ExpandAnimation collapseAnimation = new ExpandAnimation(holder.expandableVehicles, 0);
            collapseAnimation.setUpCollapseSubMenus(false);
            holder.expandableVehicles.startAnimation(collapseAnimation);

            mp.setMargins(0, 0, 0, 0);

        }

        holder.vehiclesIdTextView.setText(String.valueOf(vehicle.getId()));
        holder.vehiclesPlateNumberTextView.setText(vehicle.getPlate_number());
        holder.vehiclesStationTextView.setText(vehicle.getStation().getName());
        holder.vehiclesVehicleModelTextView.setText(vehicle.getVehicle_model().getName());

        setSplitTextViewFonts("Capacity", String.valueOf(vehicle.getCapacity()), holder.vehiclesCapacityTextView);
        setSplitTextViewFonts("Transmission", vehicle.getTransmission(), holder.vehiclesTransmissionTextView);
        setSplitTextViewFonts("Excess Km Charge", String.valueOf(vehicle.getExcess_km_charge()), holder.vehiclesExcessKmCharge);
        setSplitTextViewFonts("Year", String.valueOf(vehicle.getYear()), holder.vehiclesYearTextView);
        setSplitTextViewFonts("Color", vehicle.getColor(), holder.vehiclesColorTextView);
        setSplitTextViewFonts("Status", String.valueOf(vehicle.is_active()), holder.vehiclesStatusTextView);

        setExpandIndicatorClickListener(holder.lineToX, holder.xToLine, holder.vehiclesTableLayout,
                holder.expandableVehicles, vehicle);

        setViewActionButtonClickListener(holder.vehiclesViewActionButton, vehicle, position, holder.vehiclesActivateButton, holder.vehiclesDeactivateButton);

    }

    @Override
    public int getItemCount() {
        return vehicles.length;
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TableLayout vehiclesTableLayout;
        private LottieAnimationView lineToX;
        private LottieAnimationView xToLine;
        private TextView vehiclesIdTextView;
        private TextView vehiclesPlateNumberTextView;
        private TextView vehiclesStationTextView;
        private TextView vehiclesVehicleModelTextView;

        private LinearLayout expandableVehicles;

        private TextView vehiclesCapacityTextView;
        private TextView vehiclesTransmissionTextView;
        private TextView vehiclesExcessKmCharge;
        private TextView vehiclesYearTextView;

        private TextView vehiclesColorTextView;
        private TextView vehiclesStatusTextView;

        private Button vehiclesViewActionButton;
        private Button vehiclesActivateButton;
        private Button vehiclesDeactivateButton;

        public VehicleViewHolder(View itemView) {
            super(itemView);

            vehiclesTableLayout = (TableLayout) itemView.findViewById(R.id.vehiclesTableLayout);
            lineToX = (LottieAnimationView) itemView.findViewById(R.id.lineToX);
            xToLine = (LottieAnimationView) itemView.findViewById(R.id.xToLine);
            vehiclesIdTextView = (TextView) itemView.findViewById(R.id.vehiclesIdTextVIew);
            vehiclesPlateNumberTextView = (TextView) itemView.findViewById(R.id.vehiclesPlateNumberTextView);
            vehiclesStationTextView = (TextView) itemView.findViewById(R.id.vehiclesStationTextView);
            vehiclesVehicleModelTextView = (TextView) itemView.findViewById(R.id.vehiclesVehicleModelTextView);

            expandableVehicles = (LinearLayout) itemView.findViewById(R.id.expandableVehicles);

            vehiclesCapacityTextView = (TextView) itemView.findViewById(R.id.vehiclesCapacityTextView);
            vehiclesTransmissionTextView = (TextView) itemView.findViewById(R.id.vehiclesTransmissionTextView);
            vehiclesExcessKmCharge = (TextView) itemView.findViewById(R.id.vehiclesExcessKmCharge);
            vehiclesYearTextView = (TextView) itemView.findViewById(R.id.vehiclesYearTextView);

            vehiclesColorTextView = (TextView) itemView.findViewById(R.id.vehiclesColorTextView);
            vehiclesStatusTextView = (TextView) itemView.findViewById(R.id.vehiclesStatusTextView);

            vehiclesViewActionButton = (Button) itemView.findViewById(R.id.vehiclesViewActionButton);
            vehiclesActivateButton = (Button) itemView.findViewById(R.id.vehiclesActivateButton);
            vehiclesDeactivateButton = (Button) itemView.findViewById(R.id.vehiclesDeactivateButton);

        }
    }

    private void setExpandIndicatorClickListener(final LottieAnimationView lineToX, final LottieAnimationView xToLine,
                                                 TableLayout vehiclesTableLayout, final LinearLayout expandableVehicles,
                                                 final Vehicle vehicle) {

        vehiclesTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!vehicle.getExpanded()) {

                    lineToX.setVisibility(View.VISIBLE);

                    xToLine.setVisibility(View.INVISIBLE);

                    lineToX.playAnimation();

                    vehicle.setExpanded(true);

                } else {

                    xToLine.setVisibility(View.VISIBLE);
                    lineToX.setVisibility(View.INVISIBLE);
                    xToLine.playAnimation();

                    vehicle.setExpanded(false);
                }

                expandCollapseSubMenus(expandableVehicles);

                expandedOne = true;

            }
        });

    }

    private void setSplitTextViewFonts(String header, String value, TextView textView) {

        SpannableStringBuilder expandableOnGoingText = new SpannableStringBuilder();

        expandableOnGoingText.append(header + "\n").append(value);

        expandableOnGoingText.setSpan(Exo2Bold, 0, header.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        expandableOnGoingText.setSpan(Exo2Regular, header.length() + 1, header.length() + value.length() + 1
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(expandableOnGoingText, TextView.BufferType.SPANNABLE);

    }

    private void expandCollapseSubMenus(View view) {

        ExpandAnimation expandAnimation = new ExpandAnimation(view, 390);

        view.startAnimation(expandAnimation);

    }

    private void setViewActionButtonClickListener(Button viewActionButton, final Vehicle vehicle, final int position, final Button activateActionButton, final Button deactivateActionButton) {

        viewActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    v.setBackgroundResource(R.drawable.blueactionviewbuttonpressed);

                } else {

                    v.setBackgroundResource(R.drawable.blueactionviewbutton);

                }

                return false;

            }
        });

        viewActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vehicleStatusInterface.showVehicleStatusPopup(vehicle, position);

            }
        });

        activateActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    v.setBackgroundResource(R.drawable.greenactionviewbuttonpressed);

                } else {

                    v.setBackgroundResource(R.drawable.greenactionviewbutton);

                }

                return false;
            }
        });

        activateActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
                activateActionButton.setEnabled(false);
                deactivateActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
                deactivateActionButton.setEnabled(false);

                StringRequest activateVehicleStringRequest = new StringRequest(Request.Method.PUT, activateVehicleLink.replace(":id",
                        String.valueOf(vehicle.getId())), new Response.Listener<String>() {
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

        deactivateActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    v.setBackgroundResource(R.drawable.redactionviewbuttonpressed);

                } else {

                    v.setBackgroundResource(R.drawable.redactionviewbutton);

                }

                return false;
            }
        });

        deactivateActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
                activateActionButton.setEnabled(false);
                deactivateActionButton.setBackgroundResource(R.drawable.disabledactionviewbutton);
                deactivateActionButton.setEnabled(false);

                StringRequest deactivateVehicleStringRequest = new StringRequest(Request.Method.PUT, deactivateVehicleLink.replace(":id",
                        String.valueOf(vehicle.getId())), new Response.Listener<String>() {
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

                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("token", userCredentials.getToken());

                        return headerMap;
                    }


                };

                ConnectionManager.getInstance(context).add(deactivateVehicleStringRequest);
            }
        });

    }

    public interface VehicleStatusInterface {

        public void showVehicleStatusPopup(Vehicle vehicle, int position);

    }

}
