package com.ds.DukeStudy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ds.DukeStudy.fragments.CourseListFragment;
import com.ds.DukeStudy.fragments.CoursesFragment;
import com.ds.DukeStudy.fragments.EditProfileFragment;
import com.ds.DukeStudy.fragments.FirebaseExFragment;
import com.ds.DukeStudy.fragments.GroupsFragment;
import com.ds.DukeStudy.fragments.ProfileFragment;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Main Activity which defaults to Profile page of the user
// This activity handles navigation drawer clicks

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ProfileFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,FirebaseExFragment.OnFragmentInteractionListener,EditProfileFragment.OnFragmentInteractionListener,CoursesFragment.OnFragmentInteractionListener {

    // Fields

    public Student student;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DataSnapshot data;


    private String TAG = "MAIN";
    private ValueEventListener dbListener;
    private ValueEventListener userListener;
    private DatabaseReference userReference;
    private DatabaseReference dbReference;
    private FirebaseAuth.AuthStateListener authListener;
    private HashMap<Integer,String> courseMenuIds = new HashMap<Integer,String>();
    private HashMap<Integer,String> groupMenuIds = new HashMap<Integer,String>();
    private static final int ADD_CLASS = 101; //random number

//    private boolean isCourse;
//    private String identificationKey;

    // Getters

    public FirebaseUser getUser() {return user;}
//    public String getIDkey() {return identificationKey;}
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference,below URL is the Firebase storage URL.
    StorageReference storageRef = storage.getReferenceFromUrl("gs://dukestudy-a11a3.appspot.com/");
    // Android methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create database listener

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                Log.i(TAG, "loadUser:onDataChange");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };

        dbListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot;
                updateMenuList();
                Log.i(TAG, "loadDb:onDataChange");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadDb:onCancelled", databaseError.toException());
            }
        };

        dbReference = Database.ref;
        dbReference.addValueEventListener(dbListener);

        // Initialize user

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "onCreate:onAuthStateChanged");
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    userReference = Database.ref.child("students").child(user.getUid());
                    userReference.addValueEventListener(userListener);
                }
            }
        };

        // Add fragments to navigate between items in the navigation bar
        // Set profile page as default

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = ProfileFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
       // final ImageView user_iv = (ImageView) headerView.findViewById(R.id.navProfileIcon);
        final TextView fullName_tv = (TextView) headerView.findViewById(R.id.navUserName);
        final TextView userEmail_tv = (TextView) headerView.findViewById(R.id.navUserEmail);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            user = FirebaseAuth.getInstance().getCurrentUser();

            rootRef.child("students").addValueEventListener(new ValueEventListener() {
                // Update the profile view with new data each time something changes
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid())){
                        //  Toast.makeText(getActivity(), "UID exists!",
                        //          Toast.LENGTH_SHORT).show();
                        fullName_tv.setText(dataSnapshot.child(user.getUid()).child("name").getValue().toString());
                        userEmail_tv.setText(dataSnapshot.child(user.getUid()).child("email").getValue().toString());
                    }
                    else{
                        //    Toast.makeText(getActivity(), "UID doesn't exist!",
                        //          Toast.LENGTH_SHORT).show();
                        fullName_tv.setText("Name");
                        userEmail_tv.setText("Email");

                        //pictureView.setImageResource(R.drawable.ic_menu_profile);;
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void updateMenuList() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Log.i(TAG, "Updating menu list");

        // Fill navigation drawer
        Menu menu = navigationView.getMenu();
        MenuItem classItems = menu.getItem(1);
        MenuItem groupItems = menu.getItem(2);

        SubMenu classSubMenu = classItems.getSubMenu();
        SubMenu groupSubMenu = groupItems.getSubMenu();
        classSubMenu.clear();
        groupSubMenu.clear();

        // Find keys
        ArrayList<String> courseKeys = new ArrayList<>();
        ArrayList<String> groupKeys = new ArrayList<>();

        if (student != null) {
            courseKeys = student.getCourseKeys();
            groupKeys = student.getGroupKeys();
        }

        // Fill courses
        Log.i(TAG, "Found " + courseKeys.size() + " courses");
        Integer itemId = 0;

        for (String courseKey : courseKeys) {
            // Add course
            Course c = data.child("courses").child(courseKey).getValue(Course.class);
            classSubMenu.add(0, itemId, 0, c.getTitle()).setIcon(R.drawable.ic_menu_class);
            // Map menu id to course key
            courseMenuIds.put(itemId, c.getKey());
            Log.i(TAG, "Submenu size " + classSubMenu.size());
            Log.i(TAG, "Mapping item " + itemId + " to " + c.getKey() + " course");
            itemId++;
        }

        //Add course
        classSubMenu.add(0, ADD_CLASS, 0, "Add Class").setIcon(R.drawable.ic_menu_addclass);

        //Fill groups
        for (String groupKey : groupKeys) {
            Group g = data.child("groups").child(groupKey).getValue(Group.class);
            groupSubMenu.add(0, itemId, 0, g.getName()).setIcon(R.drawable.ic_menu_groups);
            groupMenuIds.put(itemId, g.getKey());
            itemId++;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_logout:
                if (authListener != null) {
                    auth.removeAuthStateListener(authListener);
                    auth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        getSupportActionBar().setTitle(item.getTitle());
        Log.i(TAG, "Item " + item.getItemId() + " selected");
        // Handle navigation clicks
        Fragment fragment = null;
        Class fragmentClass = null;
        String curID="";
        Boolean isCourse=Boolean.FALSE;
        switch (item.getItemId()) {
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class; break;
            case R.id.firebase_ex:
                fragmentClass = FirebaseExFragment.class; break;
//            case R.id.nav_addClass:
            case ADD_CLASS:
                fragmentClass = CourseListFragment.class; break;
            default:
                if (courseMenuIds.containsKey(item.getItemId())) {
                    fragmentClass = CoursesFragment.class;
                    curID = courseMenuIds.get(item.getItemId());
                    isCourse=Boolean.TRUE;
                } else if (groupMenuIds.containsKey(item.getItemId())){
                    fragmentClass = GroupsFragment.class;
                    curID = groupMenuIds.get(item.getItemId());
                } else {
                    Log.i(TAG, "Menu id not found");
                }
        }

        // Begin fragment

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle mybundle=new Bundle();
        mybundle.putString("myid",curID);
        mybundle.putBoolean("isCourse", isCourse);
        fragment.setArguments(mybundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Close drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int tag, int view) {
        //On clicking edit profile, start up edit profile fragment
        Fragment fragment = null;
        Class fragmentClass = null;
        if (tag == 0) {
            if (view == R.id.editProfileButton) {
                //   Log.d("MyApp","I am here");
                fragmentClass = EditProfileFragment.class;
            }
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(int tag, String userName) {
        //Allow interaction between fragments.
        // Change tags for different fragments
//        Fragment fragment = null;
//        Class fragmentClass = null;
//        if (tag == 1) {
////            Log.d("MyApp","I am here");
//            fragmentClass = ProfileFragment.class;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString("UserName",userName);
        // bundle.putString("Email",profileEmail);
//            try {
//                fragment = (Fragment) fragmentClass.newInstance();
//            }
//
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//            fragment.setArguments(bundle);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        if (userListener != null) {
            userReference.removeEventListener(userListener);
        }
        if (dbListener != null) {
            dbReference.removeEventListener(dbListener);
        }
        //FirebaseAuth.getInstance().signOut();
    }
}
