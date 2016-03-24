package com.example.vnguyen.salecalculator;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
// for Google Ad
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up Touch listerner for the UI
        setupTouchListerners(this.findViewById(R.id.parent));

        // set up Google Ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void calculate(View view) {
        TextView tv_result_total = (TextView) findViewById(R.id.lbl_result_total);
        TextView tv_result_save = (TextView) findViewById(R.id.lbl_result_save);

        // try to get the values from user input
        try {
            EditText et_reg_price = (EditText) findViewById(R.id.et_reg_price);
            EditText et_sale_percent = (EditText) findViewById(R.id.et_sale_percent);
            EditText et_coupon_percent = (EditText) findViewById(R.id.et_coupon_pecent);
            EditText et_tax_percent = (EditText) findViewById(R.id.et_tax_percent);

            double reg_price = Double.parseDouble(et_reg_price.getText().toString());
            double sale_percent = Double.parseDouble(et_sale_percent.getText().toString());
            double coupon_percent = Double.parseDouble(et_coupon_percent.getText().toString());
            double tax_percent = Double.parseDouble(et_tax_percent.getText().toString());

            // make sure the input is correct
            if (reg_price < 0 || sale_percent < 0 || coupon_percent < 0 || tax_percent < 0
                    || sale_percent > 100 || coupon_percent > 100) {
                displayErrorMessage();
            } else {
                double sale = sale_percent + coupon_percent > 100 ?
                        100: sale_percent + coupon_percent;
                double after_sale = reg_price * ((100 - sale)/100);
                double result_total = after_sale*(1 + tax_percent/100);
                double result_save = reg_price - after_sale;

                // display the result
                TextView tv_result_aftersale = (TextView) findViewById(R.id.lbl_result_aftersale);
                TextView tv_result_tax = (TextView) findViewById(R.id.lbl_result_tax);

                String s_result_total = String.format(
                        getResources().getString(R.string.result_total) + "%.2f", result_total);
                String s_result_save = String.format(
                        getResources().getString(R.string.result_save) + "%.2f", result_save);
                String s_result_tax = String.format(
                        getResources().getString(R.string.result_tax) +
                                "%.2f", after_sale*tax_percent/100);
                String s_result_aftersale = String.format(
                        getResources().getString(R.string.result_aftersale) + "%.2f", after_sale);
                tv_result_tax.setText(s_result_tax);
                tv_result_aftersale.setText(s_result_aftersale);
                tv_result_total.setText(s_result_total);
                tv_result_save.setText(s_result_save);
            }
        } catch (Exception e) {
            displayErrorMessage();
        }
    }

    public void setupTouchListerners(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupTouchListerners(innerView);
            }
        }
    }

    private void displayErrorMessage() {
        TextView tv_result_total = (TextView) findViewById(R.id.lbl_result_total);
        TextView tv_result_save = (TextView) findViewById(R.id.lbl_result_save);
        TextView tv_result_aftersale = (TextView) findViewById(R.id.lbl_result_aftersale);
        TextView tv_result_tax = (TextView) findViewById(R.id.lbl_result_tax);
        tv_result_aftersale.setText("");
        tv_result_save.setText("");
        tv_result_tax.setText("");
        tv_result_total.setText(R.string.error);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
