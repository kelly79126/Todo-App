package com.example.kelly79126.todo_app;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by kelly79126 on 2017/1/11.
 */

@Table(database = MyDatabase.class)
public class MyTable extends BaseModel {
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String name;

    @Column
    String note;

    @Column
    int year;

    @Column
    int month;

    @Column
    int day;

    @Column
    String priority;

    @Column
    String status;


}