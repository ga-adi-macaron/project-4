package com.ezequielc.successplanner.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezequielc.successplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String URL = "https://apimk.com/motivationalquotes?get_quote=yes";
    public static final String DATE_FORMATTED = "dateFormatted";
    public static final String DAY_OF_WEEK = "dayOfWeek";

    TextView mQuote;
    CardView mCardView;
    CalendarView mCalendarView;
    SharedPreferences mSharedPreferences;
    boolean mGotQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuote = (TextView) findViewById(R.id.quote_text);
        mCardView = (CardView) findViewById(R.id.card_view);
        mCalendarView = (CalendarView) findViewById(R.id.calendar_view);

        mSharedPreferences = getSharedPreferences(SettingsActivity.PREFERENCES, Context.MODE_PRIVATE);

        if (mSharedPreferences.getBoolean(SettingsActivity.GET_QUOTE_SWITCH, true)) {
            if (isConnected()) {
                getQuote();
                mGotQuote = true;
            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                mQuote.setText("Unable to Receive Quotes...");
                mGotQuote = false;
            }
        } else {
            mCardView.setVisibility(View.GONE);
            mGotQuote = false;
        }

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                intentData(year, month, dayOfMonth);
            }
        });
    }

    public void intentData(int year, int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        String dayOfWeek = "";
        String monthString = "";

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            default:
                break;
        }

        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                monthString = "January";
                break;
            case Calendar.FEBRUARY:
                monthString = "February";
                break;
            case Calendar.MARCH:
                monthString = "March";
                break;
            case Calendar.APRIL:
                monthString = "April";
                break;
            case Calendar.MAY:
                monthString = "May";
                break;
            case Calendar.JUNE:
                monthString = "June";
                break;
            case Calendar.JULY:
                monthString = "July";
                break;
            case Calendar.AUGUST:
                monthString = "August";
                break;
            case Calendar.SEPTEMBER:
                monthString = "September";
                break;
            case Calendar.OCTOBER:
                monthString = "October";
                break;
            case Calendar.NOVEMBER:
                monthString = "November";
                break;
            case Calendar.DECEMBER:
                monthString = "December";
                break;
            default:
                break;
        }

        Intent intent = new Intent(MainActivity.this, DailyActivity.class);
        intent.putExtra(DAY_OF_WEEK, dayOfWeek + ": " + monthString + " " + dayOfMonth + ", " + year);
        intent.putExtra(DATE_FORMATTED, month + 1 + "/" + dayOfMonth + "/" + year);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSharedPreferences.getBoolean(SettingsActivity.GET_QUOTE_SWITCH, true)) {
            mCardView.setVisibility(View.VISIBLE);
            if (!mGotQuote) {
                getQuote();
                mGotQuote = true;
            }
            if (!isConnected()) {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                mQuote.setText("Unable to Receive Quotes...");
                mGotQuote = false;
            }
        } else {
            mCardView.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_background:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_vision_board:
                // Vision Board
                Intent visionBoardIntent = new Intent(MainActivity.this, VisionBoardActivity.class);
                startActivity(visionBoardIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void getQuote(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code: " + response);
                }
                try {
                    final StringBuilder qod = new StringBuilder();
                    JSONArray array = new JSONArray(response.body().string());
                    JSONObject object = array.getJSONObject(0);
                    String quote = object.getString("quote");
                    String author = object.getString("author_name");
                    qod.append("\"" + quote + "\"" + "\n- " + author);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mQuote.setText(qod.toString());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
