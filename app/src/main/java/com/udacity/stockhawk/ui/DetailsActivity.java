package com.udacity.stockhawk.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

        String symbol = getIntent().getStringExtra(getString(R.string.symbol_key));
        String history = getIntent().getStringExtra(getString(R.string.history_key));
        String price = getIntent().getStringExtra(getString(R.string.price_key));

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
                        + getMonthName(this, calendar.get(Calendar.MONTH)) + "/"
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
        limitLine.setTextSize(12f);
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

        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.line_dataset_description));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.GRAY);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setFillColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        lineDataSet.setFillAlpha(50);
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
        lineDataSet.setColor(Color.WHITE);
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);
    }

    private static String getMonthName(Context context, int month) {
        switch (month) {
            case (Calendar.JANUARY):
                return context.getString(R.string.january_abbreviation);
            case (Calendar.FEBRUARY):
                return context.getString(R.string.february_abbreviation);
            case (Calendar.MARCH):
                return context.getString(R.string.march_abbreviation);
            case (Calendar.APRIL):
                return context.getString(R.string.april_abbreviation);
            case (Calendar.MAY):
                return context.getString(R.string.may_abbreviation);
            case (Calendar.JUNE):
                return context.getString(R.string.june_abbreviation);
            case (Calendar.JULY):
                return context.getString(R.string.july_abbreviation);
            case (Calendar.AUGUST):
                return context.getString(R.string.august_abbreviation);
            case (Calendar.SEPTEMBER):
                return context.getString(R.string.september_abbreviation);
            case (Calendar.OCTOBER):
                return context.getString(R.string.october_abbreviation);
            case (Calendar.NOVEMBER):
                return context.getString(R.string.november_abbreviation);
            case (Calendar.DECEMBER):
                return context.getString(R.string.december_abbreviation);
        }
        return null;
    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
