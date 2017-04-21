# DukeStudy
DukeStudy is a mobile Android application designed to help connect Duke University classmates in order to produce a more productive learning environment.

## Installation
DukeStudy should be imported into Android Studio as a project.

Note that our development was performed using Android Studio 2.3. Our testing was performed with the Nexus 5 Emulator, API 25:5554.

## Workflow
Once launched, the user will be prompted to sign in. If the user is not currently signed up, registration can be completed with an *@duke.edu* email address. Existing users can also choose to reset their password, if needed.

Once logged in, the user can add classes for which they would like to find study groups. The first step is to press the Add Class button on the navigation bar. This presents a list of available classes. The user can select a class for more information, or toggle their enrollment via the button. Note that we assume an exhaustive list of classes will be provided and stored in the database. Thus, the user does not have the option to a create class.

Added classes can be found in the navigation bar. By navigating to the added class, the user can find posts, groups, and current members. Posts can be selected to add a comment. Groups can be created, toggled (which add or remove the group to the user's navigation), or selected to see more information. Members can be selected to view the respective profile.

Within a group, the user can again post and view members, in a similar manner. Additionally, study events can be created and toggled.

## Support Details
### Code Structure
Within our code, we have several authentication activities, which handle logins and registrations, and a main activity, which calls fragments to deal with the different user interface components such as those for course pages, group pages, and course listings. Separate activities are called for creating new groups and events.

The main fragments and the fragments they utilize are outlined below.

| Fragment        | Sub-Fragments                                          |
|-----------------|--------------------------------------------------------|
| CourseFragment  | PostsFragment, GroupListFragment, and MembersFragment  |
| GroupsFragment  | PostsFragment, EventsListFragment, and MembersFragment |
| ProfileFragment | EditProfileFragment                                    |

Each of these fragments is used to load, display, edit, and save information to and from the database. The main objects used are outlined below.

| Object   | Description                                                                            |
|----------|----------------------------------------------------------------------------------------|
| Student  | Contains all user information, such as profile information and enrolled courses/groups |
| Course   | Contains all course details, including title and instructor                            |
| Group    | Contains all group details, including group name and student members                   |
| Event    | Contains all event details, including meeting time and location                        |
| Post     | Contains post and author information                                                   |
| Comment  | Contains comment and author information                                                |
| Util     | Useful utility functions                                                                       |
| Database | References to database and storage                                                     |

It should be noted that the **fragments** and **objects** are in separate packages in the java directory for organizational simplicity. Also, there is some auxiliary code used for data display and entry in the **misc** package. However, they are not core activities and were implemented mostly for stylistic reasons.

### Database Structure
We used Google's Firebase database to store all of our information. Profile pictures are also stored in Firebase's storage. Database directories were derived from the Java objects that it contained. Thus, our database contains the following directories:
* students
* courses
* groups
* events
* posts
* post-comments

### APIs
The main API used in this application is the Firebase API. This is because it can automatically convert custom objects to and from Firebase's JSON-structured database, as long as the object has public getters and setters. Also, there are built-in listeners that can be triggered on changes to user-specified database subtrees. These functionalities greatly simplified development.

### Test Scripts
Due to a many-to-many database problem and time constraints, we decided against automated testing. Instead, we performed manual unit, component, and system testing.

### Additional Files
Our documentation file, *FinalDocumentation.pdf*, can be found in the root directory.

### Sources
We modified code from tutorials to complete our [posts](https://github.com/firebase/quickstart-android/tree/master/database) and [authentication](http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/) sections. Originally, our posts section did not utilize this codebase, but we were not satisfied with the functionality and design of our implementation so we adapted a tutorial to fit our needs.

:copyright: JBeibS 2017
