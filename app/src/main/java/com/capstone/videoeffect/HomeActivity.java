package com.capstone.videoeffect;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ImageView myprofile,ivphotoedit,ivvideoedit;
    LinearLayout llshare, llrate;


    FirebaseAuth firebaseAuth;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    //creating fragment object
    Fragment fragment = null;
    Intent intent = null;
    boolean doubleBackToExitPressedOnce = false;

    //On Back Pressed the navigation drawer closes
    @Override
    public void onBackPressed() {

        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        }
        else {
            if (checkNavigationMenuItem() != 0)
            {
                nav_view.setCheckedItem(R.id.nav_home);
                Intent intent = new Intent(HomeActivity.this , HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }
    }

    private int checkNavigationMenuItem() {
        Menu menu = nav_view.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myprofile = findViewById(R.id.myprofile);
        ivphotoedit = findViewById(R.id.ivphotoedit);
        ivvideoedit = findViewById(R.id.ivvideoedit);

        llshare = findViewById(R.id.llshare);
        llrate = findViewById(R.id.llrate);

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        //Navigation Drawer Menu
        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(this);
        //set data to Navigation Header

        View header = nav_view.getHeaderView(0);
        TextView tvname = (TextView) header.findViewById(R.id.tvname);
        TextView tvemail = (TextView) header.findViewById(R.id.tvemail);
        final ImageView ivavatar = (ImageView) header.findViewById(R.id.avatar);

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            tvname.setText(user.getDisplayName());
            tvemail.setText(user.getEmail());
            Glide.with(HomeActivity.this)
                    .load(user.getPhotoUrl())
                    .into(ivavatar);
        }

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
                if(!drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                    drawer_layout.openDrawer(Gravity.LEFT);
                }
                else {
                    drawer_layout.closeDrawer(Gravity.RIGHT);
                }
            }
        });


        ivphotoedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,PhotoHomeActivity.class);
                startActivity(intent);
            }
        });

        ivvideoedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,VideoHomeActivity.class);
                startActivity(intent);
            }
        });

        llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int applicationNameId = getApplication().getApplicationInfo().labelRes;
                final String appPackageName = getApplication().getPackageName();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
                String text = "Install this application To Create Amazing Photos and Videos: ";
                String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
                i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
                startActivity(Intent.createChooser(i, "Share via:"));
            }
        });

        llrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }

    //Navigate to selected menu item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                 intent = new Intent(HomeActivity.this , HomeActivity.class);
                break;
            case R.id.nav_photos:
                intent = new Intent(HomeActivity.this , PhotoHomeActivity.class);
                break;
            case R.id.nav_videos:
                intent = new Intent(HomeActivity.this , VideoHomeActivity.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(HomeActivity.this , MyProfileActivity.class);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                break;
            case R.id.nav_about:
                intent = new Intent(HomeActivity.this , AboutActivity.class);
                break;
        }

        //replacing the fragment
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}