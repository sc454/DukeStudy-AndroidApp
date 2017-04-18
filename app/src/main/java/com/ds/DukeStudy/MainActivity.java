package com.ds.DukeStudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.fragments.AddCourseFragment;
import com.ds.DukeStudy.fragments.CourseFragment;
import com.ds.DukeStudy.fragments.EditProfileFragment;


import com.ds.DukeStudy.fragments.GroupFragment;

import com.ds.DukeStudy.fragments.ProfileFragment;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

// Main Activity which defaults to Profile page of the user
// This activity handles navigation drawer clicks

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ProfileFragment.OnFragmentInteractionListener,GroupFragment.OnFragmentInteractionListener,EditProfileFragment.OnFragmentInteractionListener,CourseFragment.OnFragmentInteractionListener {

    // Fields

    public Student student;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DataSnapshot courses, groups;
    private DataSnapshot data;

    private static final String TAG = "MainActivity";
    private ValueEventListener userListener, coursesListener, groupsListener;
    private DatabaseReference userRef, coursesRef, groupsRef;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ValueEventListener dbListener;
    private DatabaseReference dbReference;
    private FirebaseAuth.AuthStateListener authListener;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://dukestudy-a11a3.appspot.com/");

    private NavigationView navView;
    private TextView userName, userEmail;

    private HashMap<Integer,String> courseMenuIds = new HashMap<Integer,String>();
    private HashMap<Integer,String> groupMenuIds = new HashMap<Integer,String>();
    private ArrayList<Course> studentCourses = new ArrayList<>();
    private ArrayList<Group> studentGroups = new ArrayList<>();

    private static final int ADD_CLASS = 101; //TODO: random number

    // Getters

    public FirebaseUser getUser() {return user;}
    public Student getStudent() {return student;}
    public DataSnapshot getGroupData() {return groups;}
    public DataSnapshot getCourseData() {return courses;}


    // Android methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get view items
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.inflateHeaderView(R.layout.main_nav_header);
        userName = (TextView) headerView.findViewById(R.id.navUserName);
        userEmail = (TextView) headerView.findViewById(R.id.navUserEmail);

        // Set tool bar and drawer layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        // Set references
        user = Database.getUser();
        auth = FirebaseAuth.getInstance();
        coursesRef = Database.ref.child("courses");
        groupsRef = Database.ref.child("groups");
        userRef = Database.ref.child("students").child(user.getUid());

        // Load information
        createListeners();

        // Send to profile
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ProfileFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void updateMenuList() {
        Log.i(TAG, "Updating menu list");

        // Clear navigation drawer options
        Menu menu = navView.getMenu();
        SubMenu classSubMenu = menu.getItem(1).getSubMenu();
        SubMenu groupSubMenu = menu.getItem(2).getSubMenu();
        classSubMenu.clear(); groupSubMenu.clear();
        courseMenuIds.clear(); groupMenuIds.clear();

        Log.i(TAG, "Found " + studentCourses.size() + " courses");
        Log.i(TAG, "Found " + studentGroups.size() + " groups");
        Integer itemId = 0;

        // Fill courses
        for (Course course : studentCourses) {
            classSubMenu.add(0, itemId, Menu.NONE, course.getTitle()).setIcon(R.drawable.ic_menu_class);
            courseMenuIds.put(itemId, course.getKey());
            Log.i(TAG, "Mapping item " + itemId + " to " + course.getKey() + " course");
            itemId++;
        }

        // Add course
        classSubMenu.add(0, ADD_CLASS, 0, "Add Class").setIcon(R.drawable.ic_menu_addclass);

        //Fill groups
        for (Group group : studentGroups) {
            groupSubMenu.add(0, itemId, Menu.NONE, group.getName()).setIcon(R.drawable.ic_menu_groups);
            groupMenuIds.put(itemId, group.getKey());
            Log.i(TAG, "Mapping item " + itemId + " to " + group.getKey() + " group");
            itemId++;
        }
//        studentGroups.clear(); studentCourses.clear();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO
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
        Log.i(TAG, "Item " + item.getTitle() + " selected");

        // Handle navigation clicks
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_profile:
                fragment = new ProfileFragment(); break;
            case ADD_CLASS:
                fragment = new AddCourseFragment(); break;
            default:
                if (courseMenuIds.containsKey(item.getItemId())) {
                    fragment = new CourseFragment().newInstance(courseMenuIds.get(item.getItemId()));
                } else if (groupMenuIds.containsKey(item.getItemId())){
                    fragment = new GroupFragment().newInstance(groupMenuIds.get(item.getItemId()));
                } else {
                    Log.i(TAG, "Menu id not found");
                }
        }

        // Start fragment and close drawer
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(false);
        return true;
    }

    //TODO
    @Override
    public void onFragmentInteraction(int tag, int view) {
        //On clicking edit profile, start up edit profile fragment
        if (tag == 0) {
            if (view == R.id.editProfileButton) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new EditProfileFragment()).commit();
            }
        }
    }

    public void onFragmentInteraction(int tag, String view) {}

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
            userRef.removeEventListener(userListener);
        }
        if (dbListener != null) {
            dbReference.removeEventListener(dbListener);
        }
        //FirebaseAuth.getInstance().signOut();
    }

    void createListeners() {

        // User listener
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                if (student == null) {
                    student = new Student(Database.getUser().getEmail());
                    student.put();
                }
                studentGroups.clear(); studentCourses.clear();
                loadStudent(student);
                updateMenuList();
                Log.i(TAG, "loadUser:onDataChange");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };

//         Course listener
//        dbListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                data = dataSnapshot;
//                updateMenuList();
//                Log.i(TAG, "loadDb:onDataChange");
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "loadDb:onCancelled", databaseError.toException());
//            }
//        };
//        dbReference = Database.ref;
//        dbReference.addValueEventListener(dbListener);

        // Authorization listener
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "onCreate:onAuthStateChanged");
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    userRef = Database.ref.child("students").child(user.getUid());
                    userRef.addValueEventListener(userListener);
                }
            }
        };
    }

    private void loadStudent(Student student) {
        // Load student info
        if (student != null) {
            // Set user name and email
            userName.setText(student.getName());
            userEmail.setText(student.getEmail());
            // Load courses and groups
            Log.i(TAG, "Loading student info...");
            loadCourses(student.getCourseKeys());
            loadGroups(student.getGroupKeys());
        } else {
            Log.w(TAG, "Student is unexpectedly null");
        }
    }

    private void loadCourses(ArrayList<String> courseKeys) {
        studentCourses.clear();
        for (final String courseKey : courseKeys) {
            Log.i(TAG, "Loading course " + courseKey + "...");
            Database.ref.child("courses").child(courseKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course == null) {
                        Log.e(TAG, "Course " + courseKey + " is unexpectedly null");
                        Toast.makeText(MainActivity.this, "Error: Could not fetch student.", Toast.LENGTH_SHORT).show();
                    } else {
                        addCourse(course);
                        updateMenuList();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                }
            });
        }
    }

    private void loadGroups(ArrayList<String> groupKeys) {
        studentGroups.clear();
        for (final String groupKey : groupKeys) {
            Log.i(TAG, "Loading group " + groupKey + "...");
            Database.ref.child("groups").child(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Group group = dataSnapshot.getValue(Group.class);
                    if (group == null) {
                        Log.e(TAG, "Group " + groupKey + " is unexpectedly null");
                        Toast.makeText(MainActivity.this, "Error: Could not fetch group.", Toast.LENGTH_SHORT).show();
                    } else {
                        addGroup(group);
                        updateMenuList();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                }
            });
        }
    }

    private void addCourse(Course course) {
        Log.e(TAG, "Adding course " + course.getKey());
        studentCourses.add(course);
        Log.i(TAG, "Course size " + studentCourses.size());
    }

    private void addGroup(Group group) {
        Log.e(TAG, "Adding group " + group.getKey());
        studentGroups.add(group);
        Log.i(TAG, "Group size " + studentGroups.size());
    }
}
