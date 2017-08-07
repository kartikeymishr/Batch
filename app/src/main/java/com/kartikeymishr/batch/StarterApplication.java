/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.kartikeymishr.batch;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("9c3fb88b04e7f7658de62957632b98f0b679a1bb")
                .clientKey("fb60c20b147b258a336ce41e4c6ce5d67dd5803b")
                .server("http://ec2-52-14-244-16.us-east-2.compute.amazonaws.com:80/parse/")
                .build()
        );

        // Prerequisites for using a Parse Server
        /*  Open build.gradle (Module: app) and add these lines in the dependencies block:
         *  compile 'com.parse.bolts:bolts-android:1.+'
         *  compile 'com.parse:parse-android:1.+'
         *  ****************************************************************************************
         *  Open AndroidManifest.xml and add this line in the <application> tag:
         *  android:name=".StarterApplication"
         */

        // ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
