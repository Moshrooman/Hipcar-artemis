package com.example.justinkwik.hipcar.Main.Reservation.OnGoingFragmentClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.R;

import java.text.DecimalFormat;

/**
 * Created by Justin Kwik on 24/05/2017.
 */
public class OnGoingReservationAdapter extends RecyclerView.Adapter<OnGoingReservationAdapter.OnGoingReservationViewHolder> {

    //For StringRequests, new Intents, etc.
    private Context context;
    private OnGoingReservation[] onGoingReservations;
    private DecimalFormat decimalFormat;
    private boolean expandList;

    public OnGoingReservationAdapter(Context context, OnGoingReservation[] onGoingReservations) {

        this.context = context;
        this.onGoingReservations = onGoingReservations;
        this.decimalFormat = new DecimalFormat();
        this.expandList = true;

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

        public OnGoingReservationViewHolder(View itemView) {
            super(itemView);

            fullNameTextView = (TextView) itemView.findViewById(R.id.fullNameTextView);
            balanceTextView = (TextView) itemView.findViewById(R.id.balanceTextView);
            priceEstimatesTextView = (TextView) itemView.findViewById(R.id.priceEstimatesTextView);
            plateNumberTextView = (TextView) itemView.findViewById(R.id.plateNumberTextView);
            onGoingReservationTableLayout = (TableLayout) itemView.findViewById(R.id.onGoingReservationTableLayout);
            lineToX = (LottieAnimationView) itemView.findViewById(R.id.lineToX);
            xToLine = (LottieAnimationView) itemView.findViewById(R.id.xToLine);

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

}
