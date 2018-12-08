package com.humonicsglobal.calendarlibrary;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.humonicsglobal.calendarlibrary.listeners.DateSelectListener;
import com.humonicsglobal.calendarlibrary.listeners.DialogCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shivam on 08/12/2018.
 */
public class CustomCalendarView extends FrameLayout implements DateSelectListener {

    private CustomCalendarData customCalendarData = new CustomCalendarData();

    private CustomCalendarDialog.Callback callback = null;

    private DialogCompleteListener completeListener = null;


    public CustomCalendarView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void setCallback(@Nullable CustomCalendarDialog.Callback callback) {
        this.callback = callback;
    }

    public void setCompleteListener(@Nullable DialogCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public void setSlyCalendarData(CustomCalendarData slyCalendarData) {
        if (slyCalendarData.getBackgroundColor()==null) {slyCalendarData.setBackgroundColor(this.customCalendarData.getBackgroundColor());}
        if (slyCalendarData.getHeaderColor()==null) {slyCalendarData.setHeaderColor(this.customCalendarData.getHeaderColor());}
        if (slyCalendarData.getHeaderTextColor()==null) {slyCalendarData.setHeaderTextColor(this.customCalendarData.getHeaderTextColor());}
        if (slyCalendarData.getTextColor()==null) {slyCalendarData.setTextColor(this.customCalendarData.getTextColor());}
        if (slyCalendarData.getSelectedColor()==null) {slyCalendarData.setSelectedColor(this.customCalendarData.getSelectedColor());}
        if (slyCalendarData.getSelectedTextColor()==null) {slyCalendarData.setSelectedTextColor(this.customCalendarData.getSelectedTextColor());}
        this.customCalendarData = slyCalendarData;
        showCalendar();
    }

    private void init(@Nullable AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.slycalendar_frame, this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCalendarView, defStyle, 0);

        customCalendarData.setBackgroundColor(typedArray.getColor(R.styleable.CustomCalendarView_backgroundColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defBackgroundColor)));
        customCalendarData.setHeaderColor(typedArray.getColor(R.styleable.CustomCalendarView_headerColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defHeaderColor)));
        customCalendarData.setHeaderTextColor(typedArray.getColor(R.styleable.CustomCalendarView_headerTextColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defHeaderTextColor)));
        customCalendarData.setTextColor(typedArray.getColor(R.styleable.CustomCalendarView_textColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defTextColor)));
        customCalendarData.setSelectedColor(typedArray.getColor(R.styleable.CustomCalendarView_selectedColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defSelectedColor)));
        customCalendarData.setSelectedTextColor(typedArray.getColor(R.styleable.CustomCalendarView_selectedTextColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defSelectedTextColor)));

        typedArray.recycle();

        ((ViewGroup) findViewById(R.id.content)).removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.slycalendar_calendar, (ViewGroup) findViewById(R.id.content), true);

        showCalendar();
    }

    private void showCalendar() {
        paintCalendar();
        showTime();

        findViewById(R.id.txtCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onCancelled();
                }
                if (completeListener!=null) {
                    completeListener.complete();
                }
            }
        });

        findViewById(R.id.txtSave).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback!=null) {
                    Calendar start = null;
                    Calendar end = null;
                    if (customCalendarData.getSelectedStartDate()!=null) {
                        start = Calendar.getInstance();
                        start.setTime(customCalendarData.getSelectedStartDate());
                    }
                    if (customCalendarData.getSelectedEndDate()!=null) {
                        end = Calendar.getInstance();
                        end.setTime(customCalendarData.getSelectedEndDate());
                    }
                    callback.onDataSelected(start, end, customCalendarData.getSelectedHour(), customCalendarData.getSelectedMinutes());
                }
                if (completeListener!=null) {
                    completeListener.complete();
                }
            }
        });


        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = null;
        if (customCalendarData.getSelectedStartDate() != null) {
            calendarStart.setTime(customCalendarData.getSelectedStartDate());
        } else {
            calendarStart.setTime(customCalendarData.getShowDate());
        }

        if (customCalendarData.getSelectedEndDate() != null) {
            calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(customCalendarData.getSelectedEndDate());
        }

        ((TextView) findViewById(R.id.txtYear)).setText(String.valueOf(calendarStart.get(Calendar.YEAR)));


        if (calendarEnd == null) {
            ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                    new SimpleDateFormat("EE, dd MMMM", Locale.getDefault()).format(calendarStart.getTime())
            );
        } else {
            if (calendarStart.get(Calendar.MONTH) == calendarEnd.get(Calendar.MONTH)) {
                ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                        getContext().getString(R.string.slycalendar_dates_period, new SimpleDateFormat("EE, dd", Locale.getDefault()).format(calendarStart.getTime()), new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.getTime()))
                );
            } else {
                ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                        getContext().getString(R.string.slycalendar_dates_period, new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarStart.getTime()), new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.getTime()))
                );
            }
        }

        ((TextView) findViewById(R.id.txtSelectedMonth)).setText(new SimpleDateFormat("LLLL yyyy", Locale.getDefault()).format(customCalendarData.getShowDate()));

        findViewById(R.id.btnMonthPrev).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(customCalendarData.getShowDate());
                currentCalendar.add(Calendar.MONTH, -1);
                customCalendarData.setShowDate(currentCalendar.getTime());
                showCalendar();
            }
        });

        findViewById(R.id.btnMonthNext).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(customCalendarData.getShowDate());
                currentCalendar.add(Calendar.MONTH, 1);
                customCalendarData.setShowDate(currentCalendar.getTime());
                showCalendar();
            }
        });

        findViewById(R.id.txtTime).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int style = R.style.SlyCalendarTimeDialogTheme;
                if (customCalendarData.getTimeTheme()!=null) {
                    style = customCalendarData.getTimeTheme();
                }

                TimePickerDialog tpd = new TimePickerDialog(getContext(), style,  new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        customCalendarData.setSelectedHour(hourOfDay);
                        customCalendarData.setSelectedMinutes(minute);
                        showTime();
                    }
                }, customCalendarData.getSelectedHour(), customCalendarData.getSelectedMinutes(), true);
                tpd.show();
            }
        });

        ((TextView) findViewById(R.id.day1)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_mon) : getContext().getString(R.string.slycalendar_sun));
        ((TextView) findViewById(R.id.day2)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_tue) : getContext().getString(R.string.slycalendar_mon));
        ((TextView) findViewById(R.id.day3)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_wed) : getContext().getString(R.string.slycalendar_tue));
        ((TextView) findViewById(R.id.day4)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_thu) : getContext().getString(R.string.slycalendar_wed));
        ((TextView) findViewById(R.id.day5)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_fri) : getContext().getString(R.string.slycalendar_thu));
        ((TextView) findViewById(R.id.day6)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_sat) : getContext().getString(R.string.slycalendar_fri));
        ((TextView) findViewById(R.id.day7)).setText(customCalendarData.isFirstMonday() ? getContext().getString(R.string.slycalendar_sun) : getContext().getString(R.string.slycalendar_sat));

        GridAdapter adapter = new GridAdapter(getContext(), customCalendarData, this);
        ((GridView) findViewById(R.id.calendarGrid)).setAdapter(adapter);
    }

    @Override
    public void dateSelect(Date selectedDate) {
        if (customCalendarData.getSelectedStartDate()==null || customCalendarData.isSingle()) {
            customCalendarData.setSelectedStartDate(selectedDate);
            showCalendar();
            return;
        }
        if (customCalendarData.getSelectedEndDate() == null) {
            if (selectedDate.getTime() < customCalendarData.getSelectedStartDate().getTime()) {
                customCalendarData.setSelectedEndDate(customCalendarData.getSelectedStartDate());
                customCalendarData.setSelectedStartDate(selectedDate);
                showCalendar();
                return;
            } else if (selectedDate.getTime() == customCalendarData.getSelectedStartDate().getTime()) {
                customCalendarData.setSelectedEndDate(null);
                customCalendarData.setSelectedStartDate(selectedDate);
                showCalendar();
                return;
            } else if (selectedDate.getTime() > customCalendarData.getSelectedStartDate().getTime()) {
                customCalendarData.setSelectedEndDate(selectedDate);
                showCalendar();
                return;
            }
        }
        if (customCalendarData.getSelectedEndDate() != null) {
            customCalendarData.setSelectedEndDate(null);
            customCalendarData.setSelectedStartDate(selectedDate);
            showCalendar();
        }
    }

    @Override
    public void dateLongSelect(Date selectedDate) {
        customCalendarData.setSelectedEndDate(null);
        customCalendarData.setSelectedStartDate(selectedDate);
        showCalendar();
    }

    private void paintCalendar() {
        findViewById(R.id.mainFrame).setBackgroundColor(customCalendarData.getBackgroundColor());
        findViewById(R.id.headerView).setBackgroundColor(customCalendarData.getHeaderColor());
        ((TextView)findViewById(R.id.txtYear)).setTextColor(customCalendarData.getHeaderTextColor());
        ((TextView)findViewById(R.id.txtSelectedPeriod)).setTextColor(customCalendarData.getHeaderTextColor());
        ((TextView)findViewById(R.id.txtTime)).setTextColor(customCalendarData.getHeaderColor());

    }



    private void showTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, customCalendarData.getSelectedHour());
        calendar.set(Calendar.MINUTE, customCalendarData.getSelectedMinutes());
        ((TextView) findViewById(R.id.txtTime)).setText(
                new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime())
        );

    }

}
