package com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses;

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
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.ExpandAnimation.ExpandAnimation;
import com.example.justinkwik.hipcar.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimePrinter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Justin Kwik on 24/05/2017.
 */
public class OnGoingReservationAdapter extends RecyclerView.Adapter<OnGoingReservationAdapter.OnGoingReservationViewHolder> {

    //For StringRequests, new Intents, etc.
    private Context context;
    private OnGoingReservation[] onGoingReservations;
    private DecimalFormat decimalFormat;
    private CalligraphyTypefaceSpan Exo2Bold;
    private CalligraphyTypefaceSpan Exo2Regular;
    private VehicleStatusInterface vehicleStatusInterface;

    //Empty construtor to allow access to the formatDateString method.
    public OnGoingReservationAdapter() {

    }

    public OnGoingReservationAdapter(Context context, OnGoingReservation[] onGoingReservations, VehicleStatusInterface vehicleStatusInterface) {

        this.context = context;
        this.onGoingReservations = onGoingReservations;
        this.decimalFormat = new DecimalFormat();
        this.vehicleStatusInterface = vehicleStatusInterface;

        Exo2Bold = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Bold.ttf"));
        Exo2Regular = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Regular.ttf"));

    }

    @Override
    public OnGoingReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View onGoingReservationRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoingreservationrow,
                parent, false);

        return new OnGoingReservationViewHolder(onGoingReservationRow);

    }

    @Override
    public void onBindViewHolder(final OnGoingReservationViewHolder holder, int position) {

        OnGoingReservation onGoingReservation = onGoingReservations[position];

        holder.fullNameTextView.setText(onGoingReservation.getFull_name());
        holder.balanceTextView.setText("Rp. " +
                String.valueOf(decimalFormat.format(onGoingReservation.getUser().getBalance())));
        holder.priceEstimatesTextView.setText("Rp. " +
                String.valueOf(decimalFormat.format(onGoingReservation.getTotal_price())));
        holder.plateNumberTextView.setText(onGoingReservation.getVehicle().getPlate_number());

        setSplitTextViewFonts("ID", String.valueOf(onGoingReservation.getId()), holder.idTextView);
        setSplitTextViewFonts("Email", onGoingReservation.getEmail(), holder.emailTextView);
        setSplitTextViewFonts("Contact #", onGoingReservation.getContact_number(), holder.contactNumberTextView);
        setSplitTextViewFonts("Pickup Km", String.valueOf(onGoingReservation.getPickup_km()), holder.pickUpKmTextView);
        setSplitTextViewFonts("Vehicle Model", onGoingReservation.getVehicle().getVehicle_model().getName(),
                holder.vehicleModelTextView);
        setSplitTextViewFonts("Pick Up Date", formatDateString(onGoingReservation.getPickup_date(), false),
                holder.pickUpDateTextView);
        setSplitTextViewFonts("Grace Period Expire", formatDateString(onGoingReservation.getReturn_date(), false),
                holder.gracePeriodExpireTextView);

        setSplitTextViewFonts("Check-In Date", formatDateString(onGoingReservation.getActual_pickup_date(), false),
                holder.checkInDateTextView);
        setSplitTextViewFonts("Check-Out Date", formatDateString(onGoingReservation.getActual_return_date(), false),
                holder.checkOutDateTextView);
        setSplitTextViewFonts("Pick-Up Station", onGoingReservation.getPickup_station().getName(), holder.pickUpStationTextView);
        setSplitTextViewFonts("Return Station", onGoingReservation.getReturn_station().getName(), holder.returnStationTextView);
        setSplitTextViewFonts("Duration", formatDateString(onGoingReservation.getReturn_date(), true), holder.durationTextView);
        //TODO: need to fix this duration, answer not consistent with the dev website.

        setExpandIndicatorClickListener(holder.lineToX, holder.xToLine, holder.onGoingReservationTableLayout,
                holder.expandableOnGoingReservation, onGoingReservation);

        setViewActionButtonClickListener(holder.viewActionButton, onGoingReservation, position);

    }

    @Override
    public int getItemCount() {
        return onGoingReservations.length;
    }

    public class OnGoingReservationViewHolder extends RecyclerView.ViewHolder {

        private TextView fullNameTextView;
        private TextView balanceTextView;
        private TextView priceEstimatesTextView;
        private TextView plateNumberTextView;
        private TableLayout onGoingReservationTableLayout;
        private LottieAnimationView lineToX;
        private LottieAnimationView xToLine;

        private LinearLayout expandableOnGoingReservation;
        private TextView idTextView;
        private TextView emailTextView;
        private TextView contactNumberTextView;
        private TextView pickUpKmTextView;
        private TextView vehicleModelTextView;
        private TextView pickUpDateTextView;
        private TextView gracePeriodExpireTextView;
        private TextView checkInDateTextView;
        private TextView checkOutDateTextView;
        private TextView pickUpStationTextView;
        private TextView returnStationTextView;
        private TextView durationTextView;
        private Button viewActionButton;

        public OnGoingReservationViewHolder(View itemView) {
            super(itemView);

            fullNameTextView = (TextView) itemView.findViewById(R.id.fullNameTextView);
            balanceTextView = (TextView) itemView.findViewById(R.id.balanceTextView);
            priceEstimatesTextView = (TextView) itemView.findViewById(R.id.priceEstimatesTextView);
            plateNumberTextView = (TextView) itemView.findViewById(R.id.plateNumberTextView);
            onGoingReservationTableLayout = (TableLayout) itemView.findViewById(R.id.onGoingReservationTableLayout);
            lineToX = (LottieAnimationView) itemView.findViewById(R.id.lineToX);
            xToLine = (LottieAnimationView) itemView.findViewById(R.id.xToLine);

            expandableOnGoingReservation = (LinearLayout) itemView.findViewById(R.id.expandableOnGoingReservation);
            idTextView = (TextView) itemView.findViewById(R.id.idTextView);
            emailTextView = (TextView) itemView.findViewById(R.id.emailTextView);
            contactNumberTextView = (TextView) itemView.findViewById(R.id.contactNumberTextView);
            pickUpKmTextView = (TextView) itemView.findViewById(R.id.pickUpKmTextView);
            vehicleModelTextView = (TextView) itemView.findViewById(R.id.vehicleModelTextView);
            pickUpDateTextView = (TextView) itemView.findViewById(R.id.pickUpDateTextView);
            gracePeriodExpireTextView = (TextView) itemView.findViewById(R.id.gracePeriodExpireTextView);
            checkInDateTextView = (TextView) itemView.findViewById(R.id.checkInDateTextView);
            checkOutDateTextView = (TextView) itemView.findViewById(R.id.checkOutDateTextView);
            pickUpStationTextView = (TextView) itemView.findViewById(R.id.pickUpStationTextView);
            returnStationTextView = (TextView) itemView.findViewById(R.id.returnStationTextView);
            durationTextView = (TextView) itemView.findViewById(R.id.durationTextView);
            viewActionButton = (Button) itemView.findViewById(R.id.viewActionButton);

        }
    }

    private void setExpandIndicatorClickListener(final LottieAnimationView lineToX, final LottieAnimationView xToLine,
                                                 TableLayout onGoingReservationTableLayout, final LinearLayout expandableOnGoingReservation,
                                                 final OnGoingReservation onGoingReservation) {

        onGoingReservationTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!onGoingReservation.getExpanded()) {

                    lineToX.setVisibility(View.VISIBLE);

                    xToLine.setVisibility(View.INVISIBLE);

                    lineToX.playAnimation();

                    onGoingReservation.setExpanded(true);

                } else {

                    xToLine.setVisibility(View.VISIBLE);
                    lineToX.setVisibility(View.INVISIBLE);
                    xToLine.playAnimation();

                    onGoingReservation.setExpanded(false);
                }

                expandCollapseSubMenus(expandableOnGoingReservation);

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

        DateTime formatDateTime = new DateTime(date, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));
        String formattedString = "";

        if(duration) {

            //TODO: need to change this to LocalDateTime because depends on where they rented from.
            DateTime localDateTime = new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));

//            Period differencePeriod = new Period(formatDateTime, localDateTime, PeriodType.days());

//            formattedString = "" + differencePeriod.getDays() + "days " + differencePeriod.getHours() + "hours " +
//                    differencePeriod.getMinutes() + "minutes";

            //Returning time but not adding the days hours and minutes.

            int daysBetween = Days.daysBetween(formatDateTime, localDateTime).getDays();
            formattedString += formattedString + "" + daysBetween + " days ";
            formatDateTime = formatDateTime.plusDays(daysBetween);

            int hoursBetween = Hours.hoursBetween(formatDateTime, localDateTime).getHours();
            formattedString += hoursBetween + " hours ";
            formatDateTime = formatDateTime.plusHours(hoursBetween);

            int minutesBetween = Minutes.minutesBetween(formatDateTime, localDateTime).getMinutes();
            formattedString += minutesBetween + " minutes";


        } else {

            formattedString = formatDateTime.toString("dd MMM yyyy HH:mm");

        }

        return formattedString;

    }

    private void setViewActionButtonClickListener(Button viewActionButton, final OnGoingReservation onGoingReservation, final int position) {

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

                vehicleStatusInterface.showVehicleStatusPopup(onGoingReservation, position);

            }
        });

    }

    public interface VehicleStatusInterface {

        public void showVehicleStatusPopup(OnGoingReservation onGoingReservation, int position);

    }

}
