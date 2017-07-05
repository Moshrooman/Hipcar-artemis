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
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.example.justinkwik.hipcar.ExpandAnimation.ExpandAnimation;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses.ParseClassesVehicle.Vehicle;
import com.example.justinkwik.hipcar.R;
import com.example.justinkwik.hipcar.Splash.SplashActivity;
import com.google.gson.Gson;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Justin Kwik on 03/07/2017.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private Context context;
    private Vehicle[] vehicles;
    private CalligraphyTypefaceSpan Exo2Bold;
    private CalligraphyTypefaceSpan Exo2Regular;
    private VehicleStatusInterface vehicleStatusInterface;
    private UserCredentials userCredentials;
    private Gson gson;
    private LottieComposition lineToXComposition;
    private LottieComposition xToLineComposition;
    private RecyclerView recyclerView;

    public VehicleAdapter(Context context, Vehicle[] vehicles, VehicleStatusInterface vehicleStatusInterface, RecyclerView recyclerView) {

        this.context = context;
        this.vehicles = vehicles;
        this.vehicleStatusInterface = vehicleStatusInterface;
        this.userCredentials = LoginActivity.getUserCredentials();
        this.gson = new Gson();
        this.lineToXComposition = SplashActivity.getLineToXComposition();
        this.xToLineComposition = SplashActivity.getxToLineComposition();
        this.recyclerView = recyclerView;

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

        setExpandIndicatorClickListener(holder.expandIndicatorLottieView, holder.vehiclesTableLayout,
                holder.expandableVehicles, vehicle, position == vehicles.length - 1);

        setViewActionButtonClickListener(holder.vehiclesViewActionButton, vehicle, position, holder.vehiclesActivateButton, holder.vehiclesDeactivateButton);

        ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) holder.expandableVehicles.getLayoutParams();

        if (!vehicle.getExpanded() && mp.bottomMargin == 0) {

            ExpandAnimation collapseAnimation = new ExpandAnimation(holder.expandableVehicles, 0);
            collapseAnimation.setUpCollapseSubMenus(true);
            holder.expandableVehicles.startAnimation(collapseAnimation);

            mp.setMargins(0, 0, 0, -holder.expandableVehicles.getHeight());

            holder.expandIndicatorLottieView.setComposition(lineToXComposition);
            holder.expandIndicatorLottieView.setProgress(0);

        } else if (vehicle.getExpanded() && mp.bottomMargin != 0) {

            ExpandAnimation collapseAnimation = new ExpandAnimation(holder.expandableVehicles, 0);
            collapseAnimation.setUpCollapseSubMenus(false);
            holder.expandableVehicles.startAnimation(collapseAnimation);

            mp.setMargins(0, 0, 0, 0);

            holder.expandIndicatorLottieView.setComposition(xToLineComposition);
            holder.expandIndicatorLottieView.setProgress(0);

        }

        holder.expandIndicatorLottieView.setSpeed(0);
        holder.expandIndicatorLottieView.playAnimation();

    }

    @Override
    public int getItemCount() {
        return vehicles.length;
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TableLayout vehiclesTableLayout;
        private LottieAnimationView expandIndicatorLottieView;
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
            expandIndicatorLottieView = (LottieAnimationView) itemView.findViewById(R.id.expandIndicatorLottieView);
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

    private void setExpandIndicatorClickListener(final LottieAnimationView expandIndicatorLottieView,
                                                 final TableLayout vehiclesTableLayout, final LinearLayout expandableVehicles,
                                                 final Vehicle vehicle, final boolean lastItemScrollToBottom) {

        expandIndicatorLottieView.setComposition(lineToXComposition);

        vehiclesTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!vehicle.getExpanded()) {

                    expandIndicatorLottieView.setComposition(lineToXComposition);
                    expandIndicatorLottieView.setSpeed(1);
                    expandIndicatorLottieView.playAnimation();
                    vehicle.setExpanded(true);

                } else {

                    expandIndicatorLottieView.setComposition(xToLineComposition);
                    expandIndicatorLottieView.setSpeed(1);
                    expandIndicatorLottieView.playAnimation();
                    vehicle.setExpanded(false);

                }

                expandCollapseSubMenus(expandableVehicles, lastItemScrollToBottom);

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

    private void expandCollapseSubMenus(View view, boolean scrollToBottom) {

        ExpandAnimation expandAnimation = new ExpandAnimation(view, 390, recyclerView, scrollToBottom);

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

                vehicleStatusInterface.activateDeactivateConfirmationPopup(vehicle, true, activateActionButton, deactivateActionButton);

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

                vehicleStatusInterface.activateDeactivateConfirmationPopup(vehicle, false, activateActionButton, deactivateActionButton);

            }
        });

    }

    public interface VehicleStatusInterface {

        public void showVehicleStatusPopup(Vehicle vehicle, int position);
        public void activateDeactivateConfirmationPopup(Vehicle vehicle, boolean activate, Button activateButton, Button deactivateButton);

    }

}
