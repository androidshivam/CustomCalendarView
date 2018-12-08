package com.humonicsglobal.calendarlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.humonicsglobal.calendarlibrary.listeners.DialogCompleteListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shivam on 08/12/2018.
 */
public class CustomCalendarDialog extends DialogFragment implements DialogCompleteListener {


    private CustomCalendarData customCalendarData = new CustomCalendarData();
    private CustomCalendarView calendarView;
    private Callback callback = null;

    public CustomCalendarDialog setStartDate(@Nullable Date startDate) {
        customCalendarData.setSelectedStartDate(startDate);
        return this;
    }

    public CustomCalendarDialog setEndDate(@Nullable Date endDate) {
        customCalendarData.setSelectedEndDate(endDate);
        return this;
    }

    public CustomCalendarDialog setSingle(boolean single) {
        customCalendarData.setSingle(single);
        return this;
    }

    public CustomCalendarDialog setFirstMonday(boolean firsMonday) {
        customCalendarData.setFirstMonday(firsMonday);
        return this;
    }

    public CustomCalendarDialog setCallback(@Nullable Callback callback) {
        this.callback = callback;
        return this;
    }

    public CustomCalendarDialog setTimeTheme(@Nullable Integer themeResource) {
        customCalendarData.setTimeTheme(themeResource);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SlyCalendarDialogStyle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarView = (CustomCalendarView)getActivity().getLayoutInflater().inflate(R.layout.slycalendar_main, container);
        calendarView.setSlyCalendarData(customCalendarData);
        calendarView.setCallback(callback);
        calendarView.setCompleteListener(this);
        return calendarView;
    }

    @Override
    public void complete() {
        this.dismiss();
    }


    public interface Callback {
        void onCancelled();

        void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes);
    }



    public CustomCalendarDialog setBackgroundColor(Integer backgroundColor) {
        customCalendarData.setBackgroundColor(backgroundColor);
        return this;
    }

    public CustomCalendarDialog setHeaderColor(Integer headerColor) {
        customCalendarData.setHeaderColor(headerColor);
        return this;
    }

    public CustomCalendarDialog setHeaderTextColor(Integer headerTextColor) {
        customCalendarData.setHeaderTextColor(headerTextColor);
        return this;
    }

    public CustomCalendarDialog setTextColor(Integer textColor) {
        customCalendarData.setTextColor(textColor);
        return this;
    }

    public CustomCalendarDialog setSelectedColor(Integer selectedColor) {
        customCalendarData.setSelectedColor(selectedColor);
        return this;
    }

    public CustomCalendarDialog setSelectedTextColor(Integer selectedTextColor) {
        customCalendarData.setSelectedTextColor(selectedTextColor);
        return this;
    }


}
