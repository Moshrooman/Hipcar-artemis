package com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.ExpandAnimation.ExpandAnimation;
import com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses.ParseClassesVehicle.Vehicle;
import com.example.justinkwik.hipcar.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.DecimalFormat;
import java.util.TimeZone;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Justin Kwik on 03/07/2017.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    //TODO: still need to handle click for the activate and deactivate.

    //For StringRequests, new Intents, etc.
    private Context context;
    private Vehicle[] vehicles;
    private DecimalFormat decimalFormat;
    private CalligraphyTypefaceSpan Exo2Bold;
    private CalligraphyTypefaceSpan Exo2Regular;
    private VehicleStatusInterface vehicleStatusInterface;

    public VehicleAdapter(Context context, Vehicle[] vehicles, VehicleStatusInterface vehicleStatusInterface) {

        this.context = context;
        this.vehicles = vehicles;
        this.decimalFormat = new DecimalFormat();
        this.vehicleStatusInterface = vehicleStatusInterface;

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

        com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses.ParseClassesVehicle.Vehicle vehicle = vehicles[position];

        holder.vehiclesIdTextView.setText(vehicle.getId());
        holder.vehiclesPlateNumberTextView.setText(vehicle.getPlate_number());
        holder.vehiclesStationTextView.setText(vehicle.getStation());
        holder.vehiclesVehicleModelTextView.setText(vehicle.getVehicle_model().getName());

        setSplitTextViewFonts("Capacity", String.valueOf(vehicle.getCapacity()), holder.vehiclesCapacityTextView);
        setSplitTextViewFonts("Transmission", vehicle.getTransmission(), holder.vehiclesTransmissionTextView);
        setSplitTextViewFonts("Excess Km Charge", String.valueOf(vehicle.getExcess_km_charge()), holder.vehiclesExcessKmCharge);
        setSplitTextViewFonts("Year", String.valueOf(vehicle.getYear()), holder.vehiclesYearTextView);
        setSplitTextViewFonts("Color", vehicle.getColor(), holder.vehiclesColorTextView);
        setSplitTextViewFonts("Status", String.valueOf(vehicle.is_active()), holder.vehiclesStatusTextView);

        setExpandIndicatorClickListener(holder.lineToX, holder.xToLine, holder.vehiclesTableLayout,
                holder.expandableVehicles, vehicle);

        setViewActionButtonClickListener(holder.vehiclesViewActionButton, vehicle, position);

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

    public String formatDateString(String date, boolean duration) {

        if(date.equals("-")) {

            return date;

        }

        DateTime actualReturnDate = new DateTime(date, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));
        String formattedString = "";

        if(duration) {

            //TODO: need to change this to LocalDateTime because depends on where they rented from.
            DateTime localDateTime = new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));

            int daysBetween = Days.daysBetween(actualReturnDate, localDateTime).getDays();
            formattedString += formattedString + "" + daysBetween + " days ";
            actualReturnDate = actualReturnDate.plusDays(daysBetween);

            int hoursBetween = Hours.hoursBetween(actualReturnDate, localDateTime).getHours();
            formattedString += hoursBetween + " hours ";
            actualReturnDate = actualReturnDate.plusHours(hoursBetween);

            int minutesBetween = Minutes.minutesBetween(actualReturnDate, localDateTime).getMinutes();
            formattedString += minutesBetween + " minutes";


        } else {

            formattedString = actualReturnDate.toString("dd MMM yyyy HH:mm");

        }

        return formattedString;

    }

    private void setViewActionButtonClickListener(Button viewActionButton, final Vehicle vehicle, final int position) {

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

    }

    public interface VehicleStatusInterface {

        public void showVehicleStatusPopup(Vehicle vehicle, int position);

    }

}
