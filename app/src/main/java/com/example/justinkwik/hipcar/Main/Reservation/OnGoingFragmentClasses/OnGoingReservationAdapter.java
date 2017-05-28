package com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.R;

import java.text.DecimalFormat;

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
    private boolean expandList;
    private CalligraphyTypefaceSpan Exo2Medium;
    private CalligraphyTypefaceSpan Exo2Regular;


    public OnGoingReservationAdapter(Context context, OnGoingReservation[] onGoingReservations) {

        this.context = context;
        this.onGoingReservations = onGoingReservations;
        this.decimalFormat = new DecimalFormat();
        this.expandList = true;

        Exo2Medium = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Medium.ttf"));
        Exo2Regular = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Regular.ttf"));

    }

    @Override
    public OnGoingReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View onGoingReservationRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoingreservationrow,
                parent, false);

        return new OnGoingReservationViewHolder(onGoingReservationRow);

    }

    @Override
    public void onBindViewHolder(OnGoingReservationViewHolder holder, int position) {

        OnGoingReservation onGoingReservation = onGoingReservations[position];

        //Need to add a boolean in each onGoingReservation object if it is expanded, and expand it if the boolean is true.

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
        setSplitTextViewFonts("Vehicle Model", onGoingReservation.getVehicle().getVehicle_model().getName(), holder.vehicleModelTextView);
        setSplitTextViewFonts("Pick Up Date", onGoingReservation.getPickup_date(), holder.pickUpDateTextView);
        //setSplitTextViewFonts("Grace Period Expire", onGoingReservation., holder.gracePeriodExpireTextView); //TODO: ask how to calculate
        setSplitTextViewFonts("Check-In Date", onGoingReservation.getActual_pickup_date(), holder.checkInDateTextView);
        setSplitTextViewFonts("Check-Out Date", onGoingReservation.getActual_return_date(), holder.checkOutDateTextView);
        setSplitTextViewFonts("Pick-Up Station", onGoingReservation.getPickup_station().getName(), holder.pickUpStationTextView);
        setSplitTextViewFonts("Pick-Up Station", onGoingReservation.getPickup_station().getName(), holder.pickUpStationTextView);
        setSplitTextViewFonts("Return Station", onGoingReservation.getReturn_station().getName(), holder.returnStationTextView);
        setSplitTextViewFonts("Return Station", onGoingReservation.getReturn_station().getName(), holder.returnStationTextView);
        //setSplitTextViewFonts("Duration", onGoingReservation.getD, holder.durationTextView); //TODO: end date - start date

//        private TextView durationTextView;

        setExpandIndicatorClickListener(holder.lineToX, holder.xToLine, holder.onGoingReservationTableLayout);
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

        }
    }

    private void setExpandIndicatorClickListener(final LottieAnimationView lineToX, final LottieAnimationView xToLine,
                                                 TableLayout onGoingReservationTableLayout) {

        onGoingReservationTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(expandList) {

                    lineToX.setVisibility(View.VISIBLE);

                    xToLine.setVisibility(View.INVISIBLE);

                    lineToX.playAnimation();

                    expandList = false;

                } else {

                    xToLine.setVisibility(View.VISIBLE);
                    lineToX.setVisibility(View.INVISIBLE);
                    xToLine.playAnimation();

                    expandList = true;
                }

            }
        });

    }

    private void setSplitTextViewFonts(String header, String value, TextView textView) {

        SpannableStringBuilder expandableOnGoingText = new SpannableStringBuilder();
        expandableOnGoingText.append(header + "\n").append(value);
        expandableOnGoingText.setSpan(Exo2Medium, 0, header.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        expandableOnGoingText.setSpan(Exo2Regular, header.length() + 1, header.length() + value.length() + 1
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(expandableOnGoingText, TextView.BufferType.SPANNABLE);

    }

}
