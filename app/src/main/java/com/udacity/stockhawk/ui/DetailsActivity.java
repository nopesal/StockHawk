package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.details_quote_symbol) TextView mSymbolTextView;
    @BindView(R.id.details_quote_price) TextView mPriceTextView;

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
    }

}
