package com.scottlindley.suyouthinkyoucandoku;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ArmoryActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String ARMORY_SHARED_PREFS = "armory prefs";
    public static final String COIN_COUNT_KEY = "coin count";
    public static final String BOMB_COUNT_KEY = "bomb count";
    public static final String SPY_COUNT_KEY = "spy count";
    public static final String INTERFERENCE_COUNT_KEY = "interference count";
    private CardView mBuyBombBtn, mBuySpyBtn, mBuyInterferenceBtn;
    private TextView mBombInventoryCount, mSpyInventoryCount, mInterferenceInventoryCount;
    private TextView mCoinCountText;
    private int mCoinCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armory);

        getSupportActionBar().hide();

        setUpViews();
    }

    private void setUpViews(){
        SharedPreferences prefs = getSharedPreferences(ARMORY_SHARED_PREFS, MODE_PRIVATE);
        mCoinCount = prefs.getInt(COIN_COUNT_KEY, 20);
        mCoinCountText = (TextView)findViewById(R.id.coins_text);
        mCoinCountText.setText("Coins: " + mCoinCount);

        mBuyBombBtn = (CardView)findViewById(R.id.erase_buy_button);
        mBuySpyBtn = (CardView)findViewById(R.id.spy_buy_button);
        mBuyInterferenceBtn = (CardView)findViewById(R.id.interference_buy_button);

        mBombInventoryCount = (TextView) findViewById(R.id.bomb_inventory_count);
        mSpyInventoryCount = (TextView) findViewById(R.id.spy_inventory_count);
        mInterferenceInventoryCount = (TextView) findViewById(R.id.interference_inventory_count);

        mBuyBombBtn.setOnClickListener(this);
        mBuySpyBtn.setOnClickListener(this);
        mBuyInterferenceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor prefsEdit = getSharedPreferences(ARMORY_SHARED_PREFS, MODE_PRIVATE).edit();

        if (mCoinCount < 3){
            Toast.makeText(this, "Not enough coins! Win races to collect more.", Toast.LENGTH_SHORT).show();
        } else {
            switch (view.getId()){
                case R.id.erase_buy_button:
                    int bombCount = Integer.parseInt(mBombInventoryCount.getText().toString());
                    bombCount++;
                    prefsEdit.putInt(BOMB_COUNT_KEY, bombCount);
                    mBombInventoryCount.setText(String.valueOf(bombCount));
                    break;
                case R.id.spy_buy_button:
                    int spyCount = Integer.parseInt(mSpyInventoryCount.getText().toString());
                    spyCount++;
                    prefsEdit.putInt(SPY_COUNT_KEY, spyCount);
                    mSpyInventoryCount.setText(String.valueOf(spyCount));
                    break;
                case R.id.interference_buy_button:
                    int interfCount = Integer.parseInt(mInterferenceInventoryCount.getText().toString());
                    interfCount++;
                    prefsEdit.putInt(INTERFERENCE_COUNT_KEY, interfCount);
                    mInterferenceInventoryCount.setText(String.valueOf(interfCount));
                    break;
                default:
                    return;
            }
            mCoinCount = mCoinCount - 3;
            prefsEdit.putInt(COIN_COUNT_KEY, mCoinCount);
            prefsEdit.commit();
            mCoinCountText.setText("Coins: " + mCoinCount);
        }
    }
}
