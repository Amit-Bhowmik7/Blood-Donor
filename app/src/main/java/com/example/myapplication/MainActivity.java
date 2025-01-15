package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private androidx.appcompat.widget.Toolbar toolbar;
    private DrawerLayout drawerLayout, dropdownmenu;
    private TextView marquee;
    private ImageButton dropdownimage, notification, notificationIcon;
    private LinearLayout search_option, request_option, organization_option, feed_option, ambulance_option, thalassemia_option, facts_option, helpline_option, volunteer_option, feedback_option, contactus_option, donation_option, ratingUs;
    private TextView totalUserTextView, totalDonorTextView;
    private DatabaseReference countsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_option = findViewById(R.id.search_option);
        request_option = findViewById(R.id.request_option);
        feed_option = findViewById(R.id.feed_option);
        organization_option = findViewById(R.id.organization_option);
        ambulance_option = findViewById(R.id.ambulance_option);
        thalassemia_option = findViewById(R.id.thalassemia_option);
        facts_option = findViewById(R.id.facts_option);
        helpline_option = findViewById(R.id.helpline_option);
        volunteer_option = findViewById(R.id.volunteer_option);
        feedback_option = findViewById(R.id.feedback_option);
        contactus_option = findViewById(R.id.contatus_option);
        donation_option = findViewById(R.id.donation_option);
        marquee = findViewById(R.id.marqueeText);
        totalUserTextView = findViewById(R.id.total_user);
        totalDonorTextView = findViewById(R.id.total_donor);
        dropdownimage = findViewById(R.id.dropdownimage);
        ratingUs = findViewById(R.id.ratingUs);
        notification = findViewById(R.id.notification);
        notificationIcon = findViewById(R.id.notification);
        notificationIcon.setVisibility(View.GONE);


        search_option.setOnClickListener(this);
        request_option.setOnClickListener(this);
        feed_option.setOnClickListener(this);
        organization_option.setOnClickListener(this);
        ambulance_option.setOnClickListener(this);
        thalassemia_option.setOnClickListener(this);
        facts_option.setOnClickListener(this);
        helpline_option.setOnClickListener(this);
        volunteer_option.setOnClickListener(this);
        feedback_option.setOnClickListener(this);
        contactus_option.setOnClickListener(this);
        donation_option.setOnClickListener(this);
        ratingUs.setOnClickListener(this);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification_Page.class);
                startActivity(intent);
            }
        });


        // Toolbar setup
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Drawer setup
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        dropdownimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView); // Close the drawer
                } else {
                    drawerLayout.openDrawer(navigationView); // Open the drawer
                }
            }
        });

        // Handle left navigation menu item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.admin) {
                Intent intent = new Intent(getApplicationContext(), Admin_SignIn.class);
                startActivity(intent);

            } else if (id == R.id.profile) {
                Intent intent = new Intent(getApplicationContext(), Profile_Page.class);
                startActivity(intent);
            } else if (id == R.id.share) {
                // Handle Share click
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareMessage = "Check out this amazing app!";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } else if (id == R.id.rating) {
                // Handle Rate Us click
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(rateIntent);
                } catch (Exception e) {
                    // If Play Store app is not installed, open in browser
                    Uri browserUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);
                }
            } else if (id == R.id.follow_facebook) {
                // Replace with your Facebook page link
                String facebookPageUrl = "https://www.facebook.com/amitbhowmikct";

                // Create an intent to open the URL in a browser or the Facebook app
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebookPageUrl));
                startActivity(intent);
            } else if (id == R.id.contact) {
                Intent intent = new Intent(getApplicationContext(), Contact_Form.class);
                startActivity(intent);
            } else if (id == R.id.about) {
                // Handle Privacy Policy click
            } else if (id == R.id.privecyPolicy) {
                Intent intent = new Intent(getApplicationContext(), PrivacyPolicy_page.class);
                startActivity(intent);
            } else if (id == R.id.signout) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.exit) {
                finishAffinity();
            }

            drawerLayout.closeDrawers(); // Close the drawer after selection
            return true;
        });

        //UserCounts database path
        countsRef = FirebaseDatabase.getInstance().getReference("UserCounts");
        //show the users and donors in mainActiviy function call
        loadCounts();

        marquee.setSelected(true);

    }

    //show the users and donors in mainActiviy function
    private void loadCounts() {
        countsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalUsers = snapshot.child("totalUsers").getValue(Integer.class) != null
                        ? snapshot.child("totalUsers").getValue(Integer.class)
                        : 0;
                int totalDonors = snapshot.child("totalDonors").getValue(Integer.class) != null
                        ? snapshot.child("totalDonors").getValue(Integer.class)
                        : 0;

                totalUserTextView.setText(String.valueOf(totalUsers));
                totalDonorTextView.setText(String.valueOf(totalDonors));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                totalUserTextView.setText("Error");
                totalDonorTextView.setText("Error");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_option:
                Intent intent = new Intent(MainActivity.this, Blood_Search.class);
                startActivity(intent);
                break;

            case R.id.request_option:
                Intent intent1 = new Intent(MainActivity.this, Blood_Request_Send.class);
                startActivity(intent1);
                break;

            case R.id.feed_option:
                Intent intent2 = new Intent(MainActivity.this, Blood_Request_List.class);
                startActivity(intent2);
                break;

            case R.id.organization_option:
                Intent intent3 = new Intent(MainActivity.this, Organization_List.class);
                startActivity(intent3);
                break;

            case R.id.ambulance_option:
                Intent intent4 = new Intent(MainActivity.this, Ambulance_List.class);
                startActivity(intent4);
                break;

            case R.id.thalassemia_option:
                Intent intent5 = new Intent(MainActivity.this, Thalassemia_Patient_List.class);
                startActivity(intent5);
                break;

            case R.id.helpline_option:
                Intent intent6 = new Intent(MainActivity.this, Help_Line_List.class);
                startActivity(intent6);
                break;

            case R.id.volunteer_option:
                Intent intent7 = new Intent(MainActivity.this, Volunteer_list.class);
                startActivity(intent7);
                break;

            case R.id.feedback_option:
                Intent intent8 = new Intent(MainActivity.this, FeedBack_Form.class);
                startActivity(intent8);
                break;

            case R.id.contatus_option:
                Intent intent9 = new Intent(MainActivity.this, Contact_Form.class);
                startActivity(intent9);
                break;

            case R.id.donation_option:
                Intent intent10 = new Intent(MainActivity.this, Donation_Person_List.class);
                startActivity(intent10);
                break;

            case R.id.facts_option:
                Intent intent11 = new Intent(MainActivity.this, Facts_List.class);
                startActivity(intent11);
                break;

            case R.id.ratingUs:
                // Handle Rate Us click
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(rateIntent);
                } catch (Exception e) {
                    // If Play Store app is not installed, open in browser
                    Uri browserUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);
                    break;
                }
        }
    }
}