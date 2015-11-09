package com.wesleyreisz.mymusic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by wesleyreisz on 11/1/15.
 */
public class MyMusicApplication extends Application {
    public static final String ITUNES_URL = "https://itunes.apple.com/us/rss/topsongs/limit=%s/json";
    public static final String MAX_TO_GET = "25";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
