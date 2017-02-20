package com.codepath.taskit.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by darewreck_PC on 2/18/2017.
 */

@Database(name = TodoDatabase.NAME, version = TodoDatabase.VERSION)
public class TodoDatabase {
    public static final String NAME = "TodoDabase";
    public static final int VERSION = 1;
}
