package com.example.kelly79126.todo_app;

import com.raizlabs.android.dbflow.annotation.Database;
/**
 * Created by kelly79126 on 2017/1/11.
 */

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "MyDataBase";

    public static final int VERSION = 1;
}