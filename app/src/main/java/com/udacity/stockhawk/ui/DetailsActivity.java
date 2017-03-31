package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.details_quote_symbol) TextView mSymbolTextView;
    @BindView(R.id.details_quote_price) TextView mPriceTextView;
    @BindView(R.id.details_line_chart) LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String symbol = getIntent().getStringExtra("symbol");
        String history = getIntent().getStringExtra("history");
        String price = getIntent().getStringExtra("price");

        mPriceTextView.setText(price);
        mSymbolTextView.setText(symbol);

        setTitle(symbol);

        Calendar calendar = Calendar.getInstance();

        final ArrayList<String> labels = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        int commaPosition = 0;
        int numberOfLines = countLines(history) - 1;
        for (int i = 0; i < history.length(); i++) {
            if (history.charAt(i) == ',') {
                commaPosition = i;
                String dateInMillis = history.substring(i - 13, i);
                calendar.setTimeInMillis(Long.parseLong(dateInMillis));
                labels.add(calendar.get(Calendar.DAY_OF_MONTH) + "/"
                        + getMonthName(calendar.get(Calendar.MONTH)) + "/"
                        + calendar.get(Calendar.YEAR));
            }
            if (history.charAt(i) == '\n') {
                String quotePrice = history.substring(commaPosition + 1, i);
                entries.add(new Entry((float) numberOfLines, Float.parseFloat(quotePrice)));
                numberOfLines--;
            }
        }
        Collections.reverse(entries);
        Collections.reverse(labels);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.i("FORMATTER", "getFormattedValue: " + value);
                return labels.get((int) value);
            }
        };

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setDrawLabels(false); // no axis labels
        yAxis.setDrawAxisLine(false); // no axis line
        yAxis.setDrawGridLines(false); // no grid lines
        yAxis.setDrawZeroLine(true); // draw a zero line
        mLineChart.getAxisRight().setEnabled(false);

        LimitLine limitLine = new LimitLine(Float.valueOf(price), price);
        limitLine.setLineColor(ContextCompat.getColor(this, R.color.colorAccent));
        limitLine.setLineWidth(2f);
        limitLine.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        limitLine.setTextSize(10f);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLine.setTextStyle(Paint.Style.FILL);
        limitLine.enableDashedLine(10f, 10f, 0);
        yAxis.addLimitLine(limitLine);

        mLineChart.moveViewToX(labels.size());
        mLineChart.setScaleMinima(10f, 1f);
        mLineChart.setDescription(null);
        mLineChart.setScaleYEnabled(false);

        Legend legend = mLineChart.getLegend();
        legend.setEnabled(false);

        LineDataSet lineDataSet = new LineDataSet(entries, "Value at close in USD.");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.GRAY);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(8f);
        lineDataSet.setFillColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        lineDataSet.setFillAlpha(50);
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
        lineDataSet.setColor(Color.WHITE);
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);
    }

    public static String getMonthName(int month) {
        switch (month) {
            case (Calendar.JANUARY):
                return "JAN";
            case (Calendar.FEBRUARY):
                return "FEB";
            case (Calendar.MARCH):
                return "MAR";
            case (Calendar.APRIL):
                return "APR";
            case (Calendar.MAY):
                return "MAY";
            case (Calendar.JUNE):
                return "JUN";
            case (Calendar.JULY):
                return "JUL";
            case (Calendar.AUGUST):
                return "AUG";
            case (Calendar.SEPTEMBER):
                return "SEP";
            case (Calendar.OCTOBER):
                return "OCT";
            case (Calendar.NOVEMBER):
                return "NOV";
            case (Calendar.DECEMBER):
                return "DEC";
        }
        return null;
    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
