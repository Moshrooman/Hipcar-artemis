package com.example.justinkwik.hipcar.Main.Reservation.CheckoutReservationClasses;

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
import com.airbnb.lottie.LottieComposition;
import com.example.justinkwik.hipcar.ExpandAnimation.ExpandAnimation;
import com.example.justinkwik.hipcar.R;
import com.example.justinkwik.hipcar.Splash.SplashActivity;

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
 * Created by Justin Kwik on 06/06/2017.
 */
public class CheckedoutReservationAdapter extends RecyclerView.Adapter<CheckedoutReservationAdapter.CheckoutReservationViewHolder>{

    //For StringRequests, new Intents, etc.
    private Context context;
    private CheckedOutReservation[] checkedOutReservations;
    private DecimalFormat decimalFormat;
    private CalligraphyTypefaceSpan Exo2Bold;
    private CalligraphyTypefaceSpan Exo2Regular;
    private GenerateClickedInterface generateClickedInterface;
    private LottieComposition xToLineComposition;
    private LottieComposition lineToXComposition;

    public CheckedoutReservationAdapter(Context context, CheckedOutReservation[] checkedOutReservations, GenerateClickedInterface generateClickedInterface) {
        this.context = context;
        this.checkedOutReservations = checkedOutReservations;
        this.decimalFormat = new DecimalFormat();
        this.generateClickedInterface = generateClickedInterface;
        this.xToLineComposition = SplashActivity.getxToLineComposition();
        this.lineToXComposition = SplashActivity.getLineToXComposition();

        Exo2Bold = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Bold.ttf"));
        Exo2Regular = new CalligraphyTypefaceSpan(TypefaceUtils.load(this.context.getAssets(), "fonts/Exo2-Regular.ttf"));

    }

    @Override
    public CheckoutReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View checkedOutReservationRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkedoutreservationrow,
                parent, false);

        return new CheckoutReservationViewHolder(checkedOutReservationRow);

    }

    @Override
    public void onBindViewHolder(final CheckoutReservationViewHolder holder, int position) {

        CheckedOutReservation checkedOutReservation = checkedOutReservations[position];

        holder.fullNameTextView.setText(checkedOutReservation.getFull_name());
        holder.contactNumberTextView.setText(checkedOutReservation.getContact_number());
        holder.totalAmountTextView.setText("Rp. " +
                String.valueOf(decimalFormat.format(checkedOutReservation.getTotal_amount())));
        holder.balanceTextView.setText("Rp. " +
                String.valueOf(decimalFormat.format(checkedOutReservation.getUser().getBalance())));

        setSplitTextViewFonts("ID", String.valueOf(checkedOutReservation.getId()), holder.idTextView);
        setSplitTextViewFonts("Email", checkedOutReservation.getEmail(), holder.emailTextView);
        setSplitTextViewFonts("Duration", formatDateString(checkedOutReservation.getActual_return_date(), true, checkedOutReservation.getReturn_date()), holder.durationTextView);
        setSplitTextViewFonts("Plate Number", checkedOutReservation.getVehicle().getPlate_number(), holder.plateNumberTextView);

        setSplitTextViewFonts("Vehicle Model", checkedOutReservation.getVehicle().getVehicle_model().getName(),
                holder.vehicleModelTextView);
        setSplitTextViewFonts("Pick Up Date", formatDateString(checkedOutReservation.getPickup_date(), false, null),
                holder.pickUpDateTextView);
        setSplitTextViewFonts("Grace Period Expire", formatDateString(checkedOutReservation.getReturn_date(), false, null),
                holder.gracePeriodExpireTextView);
        setSplitTextViewFonts("Check-In Date", formatDateString(checkedOutReservation.getActual_pickup_date(), false, null),
                holder.checkInDateTextView);


        setSplitTextViewFonts("Check-Out Date", formatDateString(checkedOutReservation.getActual_return_date(), false, null),
                holder.checkOutDateTextView);
        setSplitTextViewFonts("Pick-Up Station", checkedOutReservation.getPickup_station().getName(), holder.pickUpStationTextView);
        setSplitTextViewFonts("Return Station", checkedOutReservation.getReturn_station().getName(), holder.returnStationTextView);

        setExpandIndicatorClickListener(holder.expandIndicatorLottieView, holder.checkedOutReservationTableLayout,
                holder.expandableCheckedOutReservation, checkedOutReservation);

        setViewActionButtonClickListener(holder.generateButton, checkedOutReservation);

        ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) holder.expandableCheckedOutReservation.getLayoutParams();

        if (!checkedOutReservation.getExpanded() && mp.bottomMargin == 0) {

            ExpandAnimation collapseAnimation = new ExpandAnimation(holder.expandableCheckedOutReservation, 0);
            collapseAnimation.setUpCollapseSubMenus(true);
            holder.expandableCheckedOutReservation.startAnimation(collapseAnimation);

            mp.setMargins(0, 0, 0, -holder.expandableCheckedOutReservation.getHeight());

            holder.expandIndicatorLottieView.setComposition(lineToXComposition);
            holder.expandIndicatorLottieView.setProgress(0);

        } else if (checkedOutReservation.getExpanded() && mp.bottomMargin != 0) {

            ExpandAnimation collapseAnimation = new ExpandAnimation(holder.expandableCheckedOutReservation, 0);
            collapseAnimation.setUpCollapseSubMenus(false);
            holder.expandableCheckedOutReservation.startAnimation(collapseAnimation);

            mp.setMargins(0, 0, 0, 0);

            holder.expandIndicatorLottieView.setComposition(xToLineComposition);
            holder.expandIndicatorLottieView.setProgress(0);

        }

    }

    @Override
    public int getItemCount() {
        return checkedOutReservations.length;
    }

    public class CheckoutReservationViewHolder extends RecyclerView.ViewHolder {

        private TableLayout checkedOutReservationTableLayout;
        private LottieAnimationView expandIndicatorLottieView;

        private TextView fullNameTextView;
        private TextView contactNumberTextView;
        private TextView totalAmountTextView;
        private TextView balanceTextView;

        private LinearLayout expandableCheckedOutReservation;

        private TextView idTextView;
        private TextView emailTextView;
        private TextView durationTextView;
        private TextView plateNumberTextView;

        private TextView vehicleModelTextView;
        private TextView pickUpDateTextView;
        private TextView gracePeriodExpireTextView;
        private TextView checkInDateTextView;

        private TextView checkOutDateTextView;
        private TextView pickUpStationTextView;
        private TextView returnStationTextView;

        private Button generateButton;

        public CheckoutReservationViewHolder(View itemView) {
            super(itemView);

            checkedOutReservationTableLayout = (TableLayout) itemView.findViewById(R.id.checkedOutReservationTableLayout);

            totalAmountTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);
            fullNameTextView = (TextView) itemView.findViewById(R.id.fullNameTextView);
            balanceTextView = (TextView) itemView.findViewById(R.id.balanceTextView);

            plateNumberTextView = (TextView) itemView.findViewById(R.id.plateNumberTextView);
            expandIndicatorLottieView = (LottieAnimationView) itemView.findViewById(R.id.expandIndicatorLottieView);

            expandableCheckedOutReservation = (LinearLayout) itemView.findViewById(R.id.expandableCheckedOutReservation);
            idTextView = (TextView) itemView.findViewById(R.id.idTextView);
            emailTextView = (TextView) itemView.findViewById(R.id.emailTextView);
            contactNumberTextView = (TextView) itemView.findViewById(R.id.contactNumberTextView);
            vehicleModelTextView = (TextView) itemView.findViewById(R.id.vehicleModelTextView);
            pickUpDateTextView = (TextView) itemView.findViewById(R.id.pickUpDateTextView);
            gracePeriodExpireTextView = (TextView) itemView.findViewById(R.id.gracePeriodExpireTextView);
            checkInDateTextView = (TextView) itemView.findViewById(R.id.checkInDateTextView);
            checkOutDateTextView = (TextView) itemView.findViewById(R.id.checkOutDateTextView);
            pickUpStationTextView = (TextView) itemView.findViewById(R.id.pickUpStationTextView);
            returnStationTextView = (TextView) itemView.findViewById(R.id.returnStationTextView);
            durationTextView = (TextView) itemView.findViewById(R.id.durationTextView);
            generateButton = (Button) itemView.findViewById(R.id.generateButton);

        }
    }

    private void setExpandIndicatorClickListener(final LottieAnimationView expandIndicatorLottieView,
                                                 TableLayout checkedOutReservationTableLayout, final LinearLayout expandableCheckedOutReservation,
                                                 final CheckedOutReservation checkedOutReservation) {

        expandIndicatorLottieView.setComposition(lineToXComposition);

        checkedOutReservationTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkedOutReservation.getExpanded()) {

                    expandIndicatorLottieView.setComposition(lineToXComposition);
                    expandIndicatorLottieView.playAnimation();
                    checkedOutReservation.setExpanded(true);

                } else {

                    expandIndicatorLottieView.setComposition(xToLineComposition);
                    expandIndicatorLottieView.playAnimation();
                    checkedOutReservation.setExpanded(false);

                }

                expandCollapseSubMenus(expandableCheckedOutReservation);

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

    public String formatDateString(String date, boolean duration, String date2) {

        if(date.equals("-")) {

            return date;

        }

        DateTime actualReturnDate = new DateTime(date, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));
        String formattedString = "";

        if(duration) {

            if(date.equals("-") || date2.equals("-")) {

                return "-";

            }

            DateTime pickUpDate = new DateTime(date2, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));

            int daysBetween = Days.daysBetween(pickUpDate, actualReturnDate).getDays();
            formattedString += formattedString + "" + daysBetween + " days ";
            pickUpDate = pickUpDate.plusDays(daysBetween);

            int hoursBetween = Hours.hoursBetween(pickUpDate, actualReturnDate).getHours();
            formattedString += hoursBetween + " hours ";
            pickUpDate = pickUpDate.plusHours(hoursBetween);

            int minutesBetween = Minutes.minutesBetween(pickUpDate, actualReturnDate).getMinutes();
            formattedString += minutesBetween + " minutes";

        } else {

            formattedString = actualReturnDate.toString("dd MMM yyyy HH:mm");

        }

        return formattedString;

    }

    private void setViewActionButtonClickListener(Button viewActionButton, final CheckedOutReservation checkedOutReservation) {

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

                generateClickedInterface.showVoucherPopUp(checkedOutReservation);

            }
        });

    }

    public interface GenerateClickedInterface {

        public void showVoucherPopUp(CheckedOutReservation checkedOutReservation);

    }

}
