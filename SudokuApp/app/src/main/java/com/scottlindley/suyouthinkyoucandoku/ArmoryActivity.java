package com.scottlindley.suyouthinkyoucandoku;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ArcMotion;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ArmoryActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ArmoryActivity";
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
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_armory);

        getSupportActionBar().hide();

        setUpViews();
    }

    @Override
    protected void onResume() {
        findViewById(R.id.coins_card).setVisibility(View.VISIBLE);
        findViewById(R.id.inventory_card).setVisibility(View.VISIBLE);
        super.onResume();
    }

    private void setUpViews(){
        SharedPreferences prefs = getSharedPreferences(ARMORY_SHARED_PREFS, MODE_PRIVATE);
        mCoinCount = prefs.getInt(COIN_COUNT_KEY, 3);
        mCoinCountText = (TextView)findViewById(R.id.coins_text);
        mCoinCountText.setText("Coins: " + mCoinCount);

        mBuyBombBtn = (CardView)findViewById(R.id.erase_buy_button);
        mBuySpyBtn = (CardView)findViewById(R.id.spy_buy_button);
        mBuyInterferenceBtn = (CardView)findViewById(R.id.interference_buy_button);

        mBombInventoryCount = (TextView) findViewById(R.id.bomb_inventory_count);
        mSpyInventoryCount = (TextView) findViewById(R.id.spy_inventory_count);
        mInterferenceInventoryCount = (TextView) findViewById(R.id.interference_inventory_count);

        int bombCount = prefs.getInt(BOMB_COUNT_KEY, 0);
        int spyCount = prefs.getInt(SPY_COUNT_KEY, 0);
        int interfCount = prefs.getInt(INTERFERENCE_COUNT_KEY, 0);

        mBombInventoryCount.setText(String.valueOf(bombCount));
        mSpyInventoryCount.setText(String.valueOf(spyCount));
        mInterferenceInventoryCount.setText(String.valueOf(interfCount));

        mBuyBombBtn.setOnClickListener(this);
        mBuySpyBtn.setOnClickListener(this);
        mBuyInterferenceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final SharedPreferences.Editor prefsEdit = getSharedPreferences(ARMORY_SHARED_PREFS, MODE_PRIVATE).edit();

        if (mCoinCount < 3){
            Toast.makeText(this, "Not enough coins! Win races to collect more.", Toast.LENGTH_SHORT).show();
        } else {

            final ImageView purchaseImage = new ImageView(ArmoryActivity.this);
            purchaseImage.setElevation(12);
            purchaseImage.setColorFilter(ContextCompat.getColor(ArmoryActivity.this, R.color.colorAccent));
            int[] startXY = new int[2];
            int[] endXY = new int[2];

            switch (view.getId()){
                case R.id.erase_buy_button:
                    int bombCount = Integer.parseInt(mBombInventoryCount.getText().toString());
                    bombCount++;
                    prefsEdit.putInt(BOMB_COUNT_KEY, bombCount);
                    mBombInventoryCount.setText(String.valueOf(bombCount));

                    purchaseImage.setImageResource(R.drawable.explosion);
                    findViewById(R.id.bomb_inventory_icon).getLocationOnScreen(endXY);
                    break;
                case R.id.spy_buy_button:
                    int spyCount = Integer.parseInt(mSpyInventoryCount.getText().toString());
                    spyCount++;
                    prefsEdit.putInt(SPY_COUNT_KEY, spyCount);
                    mSpyInventoryCount.setText(String.valueOf(spyCount));

                    purchaseImage.setImageResource(R.drawable.eye);
                    findViewById(R.id.spy_inventory_icon).getLocationOnScreen(endXY);
                    break;
                case R.id.interference_buy_button:
                    int interfCount = Integer.parseInt(mInterferenceInventoryCount.getText().toString());
                    interfCount++;
                    prefsEdit.putInt(INTERFERENCE_COUNT_KEY, interfCount);
                    mInterferenceInventoryCount.setText(String.valueOf(interfCount));

                    purchaseImage.setImageResource(R.drawable.lightning);
                    findViewById(R.id.interference_inventory_icon).getLocationOnScreen(endXY);
                    break;
                default:
                    return;
            }

            view.getLocationOnScreen(startXY);
            purchaseImage.setX(startXY[0]);
            purchaseImage.setY(startXY[1]);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80, 80);
            purchaseImage.setLayoutParams(params);
            ((RelativeLayout)findViewById(R.id.activity_armory)).addView(purchaseImage);

            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumVerticalAngle(70f);
            Path path = arcMotion.getPath(startXY[0], startXY[1]-70, endXY[0], endXY[1]-70);
            Animator positionAnimator =
                    ObjectAnimator.ofFloat(purchaseImage, View.TRANSLATION_X, View.TRANSLATION_Y, path).setDuration(750);
            positionAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    ((RelativeLayout)findViewById(R.id.activity_armory)).removeView(purchaseImage);
                    mCoinCount = mCoinCount - 3;
                    prefsEdit.putInt(COIN_COUNT_KEY, mCoinCount);
                    prefsEdit.commit();
                    mCoinCountText.setText("Coins: " + mCoinCount);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            AnimatorSet animSet = new AnimatorSet();

            animSet.setInterpolator(AnimationUtils.loadInterpolator(ArmoryActivity.this,android.R.interpolator.fast_out_slow_in));
            animSet.play(positionAnimator);
            animSet.start();
        }
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.coins_card).setVisibility(View.INVISIBLE);
        findViewById(R.id.inventory_card).setVisibility(View.INVISIBLE);
        finishAfterTransition();
        super.onBackPressed();
    }
}
