package com.itheima.topline.activity;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.itheima.calendarview.CalendarView;
import com.itheima.calendarview.DateBean;
import com.itheima.calendarview.listener.OnMonthItemClickListener;
import com.itheima.calendarview.listener.OnPagerChangeListener;
import com.itheima.topline.R;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.init();
        DateBean d = calendarView.getDateInit();
        tv_title.setText(d.getSolar()[0] + "年" + d.getSolar()[1] + "月" +
                d.getSolar()[2] + "日");
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                tv_title.setText(date[0] + "年" + date[1] + "月" + date[2] + "日");
            }
        });
        calendarView.setOnItemClickListener(new OnMonthItemClickListener() {
            @Override
            public void onMonthItemClick(View view, DateBean date) {
                tv_title.setText(date.getSolar()[0] + "年" + date.getSolar()[1] + "月" +
                        date.getSolar()[2] + "日");
            }
        });
    }
    public void lastMonth(View view) {
        calendarView.lastMonth();
    }
    public void nextMonth(View view) {
        calendarView.nextMonth();
    }
}
