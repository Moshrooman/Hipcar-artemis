package com.example.justinkwik.hipcar.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.justinkwik.hipcar.CustomViewPager.NonSwipeableViewPager;
import com.example.justinkwik.hipcar.ExpandAnimation.ExpandAnimation;
import com.example.justinkwik.hipcar.HipCarApplication;
import com.example.justinkwik.hipcar.Login.LoginActivity;
import com.example.justinkwik.hipcar.Login.UserCredentials;
import com.example.justinkwik.hipcar.Main.Reservation.ReservationFragment;
import com.example.justinkwik.hipcar.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private UserCredentials userCredentials;
    private final SharedPreferences sharedPreferences = HipCarApplication.getSharedPreferences();
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private boolean openDrawer;
    private String navBarEntries[];
    private LottieAnimationView navBarButtonHtA;
    private LottieAnimationView navBarButtonAtH;
    private View navBarClickView;
    private TextView mainActivityTitle;
    private NonSwipeableViewPager viewPager;
    private LinearLayout changePassLogOutLayout;
    private TextView logoutTextView;
    private TextView changePasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userCredentials = LoginActivity.getUserCredentials();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        openDrawer = true;
        navBarEntries = getResources().getStringArray(R.array.navBarEntries);
        navBarButtonHtA = (LottieAnimationView) findViewById(R.id.navBarButtonHtA);
        navBarButtonAtH = (LottieAnimationView) findViewById(R.id.navBarButtonAtH);
        navBarClickView = findViewById(R.id.navBarClickView);
        mainActivityTitle = (TextView) findViewById(R.id.mainActivityTitle);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.mainActivityViewPager);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawerListView.setAdapter(new MyNavBarAdapter());
        drawerListView.setOnItemClickListener(this);

        drawerListView.setItemChecked(0, true);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                if (!openDrawer) {
                    navBarButtonAtH.setVisibility(View.VISIBLE);
                    navBarButtonHtA.setVisibility(View.INVISIBLE);
                    navBarButtonAtH.playAnimation();
                    drawerLayout.setVisibility(View.GONE);

                    openDrawer = true;
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navBarClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (openDrawer) {

                    navBarButtonHtA.setVisibility(View.VISIBLE);

                    navBarButtonAtH.setVisibility(View.INVISIBLE);

                    navBarButtonHtA.playAnimation();

                    drawerLayout.setVisibility(View.VISIBLE);
                    drawerLayout.openDrawer(Gravity.LEFT);
                    drawerLayout.bringToFront();

                    openDrawer = false;

                } else {

                    navBarButtonAtH.setVisibility(View.VISIBLE);

                    navBarButtonHtA.setVisibility(View.INVISIBLE);

                    navBarButtonAtH.playAnimation();

                    drawerLayout.closeDrawer(Gravity.LEFT);

                    openDrawer = true;

                }

            }
        });

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        //TODO: for logging out delete the credentials and set loggedin boolean to false (both in sharedPreference)
        //TODO: give an option to skip splash screen in beginning
        //TODO: put the app in background if they click back
        //TODO: the log out should be in the special welcome nav bar entry as well as change password
        //TODO: add a profile picture to the welcome nav bar from slack? pull from slack using slack api
        //TODO: damage, add api in the backend
        //TODO: when clicking another page while logout view is expanded, call the animation on the relative layout to collapse
            //also when the drawer closes, but we first have to check if it is expanded.
        //TODO: put a border on the log out and change password textviews.
        //TODO: implement reservation

    }

    public class MyNavBarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return navBarEntries.length;
        }

        @Override
        public Object getItem(int position) {
            return navBarEntries[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if(position == 0) {

                    convertView = layoutInflater.inflate(R.layout.welcomenavbar, null);

                    changePassLogOutLayout = (LinearLayout) convertView.findViewById(R.id.changePassLogOutLayout);
                    logoutTextView = (TextView) convertView.findViewById(R.id.logoutTextView);
                    changePasswordTextView = (TextView) convertView.findViewById(R.id.changePasswordTextView);

                    logoutTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //TODO: implement log out
                            Log.e("Clicked: ", "Logout");

                        }
                    });

                    changePasswordTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //TODO: implement change password
                            Log.e("Clicked: ", "Change Password");

                        }
                    });

                } else {

                    convertView = layoutInflater.inflate(R.layout.rownavbar, null);

                }

            }

            if(position == 0) {

                TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
                nameTextView.setText(userCredentials.getName());

            } else {

                TextView navBarEntryTextView = (TextView) convertView.findViewById(R.id.navBarEntryTextView);
                navBarEntryTextView.setText(navBarEntries[position - 1]);

            }

            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0) {

            ExpandAnimation expandAnimation = new ExpandAnimation(changePassLogOutLayout, 390);
            changePassLogOutLayout.startAnimation(expandAnimation);

        } else {

            viewPager.setCurrentItem(position - 1, false); //TODO: see if want to use smooth scrolling

            mainActivityTitle.setText(navBarEntries[position - 1]);

            navBarButtonAtH.setVisibility(View.VISIBLE);
            navBarButtonHtA.setVisibility(View.INVISIBLE);
            navBarButtonAtH.playAnimation();

            openDrawer = true;

            drawerLayout.closeDrawer(Gravity.LEFT);

        }

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            if(position == 2) {

                return new ReservationFragment();

            } else {

                return new PlaceHolderFragment();

            }

        }

        @Override
        public int getCount() {
            return navBarEntries.length; //TODO: need to increase to add log out and change password under clicking the welcome
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
