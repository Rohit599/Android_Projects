package com.example.android.mynotes;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

import static android.media.CamcorderProfile.get;

/**
 * Created by ROHITCS on 7/17/2017.
 */

public class Note implements Serializable{
    private long nDateTime;
    private String nTitle;
    private String nContent;

    public Note( long DateTime, String Title, String Content) {
        nTitle = Title;
        nDateTime=DateTime;
        nContent = Content;
    }

    public void setTitle(String Title) {
        nTitle = Title;
    }

    public void setDateTime(Long DateTime) {
        nDateTime = DateTime;
    }

    public void setContent(String Content) {
        nContent = Content;
    }

    public String getTitle() {
        return nTitle;
    }

    public Long getDateTime() {
        return nDateTime;
    }

    public String getContent() {
        return nContent;
    }


    public String getDateTimeFormatted(Context context){

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        return currentDateTimeString;
    }

}
