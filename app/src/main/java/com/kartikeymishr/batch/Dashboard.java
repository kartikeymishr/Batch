package com.kartikeymishr.batch;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAuth firebaseAuth;

    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getCurrentUser().reload();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (!firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "Email ID doesn't exist", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
        }

        final int[] ICONS = new int[]{
                R.drawable.ic_attendance,
                R.drawable.ic_timetable,
                R.drawable.ic_mail,
                R.drawable.ic_chat,
        };

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);

        tabs.getTabAt(0).setIcon(ICONS[0]); // Attendance
        tabs.getTabAt(1).setIcon(ICONS[1]); // Time Table
        tabs.getTabAt(2).setIcon(ICONS[2]); // Mail
        tabs.getTabAt(3).setIcon(ICONS[3]); // Chat
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    AttendanceTab attendanceTab = new AttendanceTab();
                    return attendanceTab;
                case 1:
                    TimeTableTab timeTableTab = new TimeTableTab();
                    return timeTableTab;
                case 2:
                    MailTab mailTab = new MailTab();
                    return mailTab;
                case 3:
                    ChatTab chatTab = new ChatTab();
                    return chatTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Update the count to correspond to the number of tabs.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
            }
            return null;
        }
    }
}
