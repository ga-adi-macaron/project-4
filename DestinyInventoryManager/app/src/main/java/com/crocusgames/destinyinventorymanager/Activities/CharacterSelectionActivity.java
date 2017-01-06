package com.crocusgames.destinyinventorymanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crocusgames.destinyinventorymanager.AccountInfoObjects.AccountMainObject;
import com.crocusgames.destinyinventorymanager.AccountInfoObjects.Character;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoObject;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.AUTHORIZATION_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.BUNGIE_NET_START_URL;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_VALUE;

public class CharacterSelectionActivity extends AppCompatActivity {
    private ImageView mBannerChar1, mBannerChar2, mBannerChar3, mEmblem1, mEmblem2, mEmblem3;
    private TextView mClassName1, mClassName2, mClassName3;
    private FrameLayout mChar1, mChar2, mChar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        mBannerChar1 = (ImageView) findViewById(R.id.banner_char1);
        mBannerChar2 = (ImageView) findViewById(R.id.banner_char2);
        mBannerChar3 = (ImageView) findViewById(R.id.banner_char3);
        mEmblem1 = (ImageView) findViewById(R.id.icon_char1);
        mEmblem2 = (ImageView) findViewById(R.id.icon_char2);
        mEmblem3 = (ImageView) findViewById(R.id.icon_char3);
        mClassName1 = (TextView) findViewById(R.id.name_class1);
        mClassName2 = (TextView) findViewById(R.id.name_class2);
        mClassName3 = (TextView) findViewById(R.id.name_class3);
        mChar1 = (FrameLayout) findViewById(R.id.layout_char1);
        mChar2 = (FrameLayout) findViewById(R.id.layout_char2);
        mChar3 = (FrameLayout) findViewById(R.id.layout_char3);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        String accessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME,"");

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        getAccountDetails(accessToken);

        mChar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterInfo.setSelectedCharacter(0);
                Intent intent = new Intent(CharacterSelectionActivity.this, CharInvActivity.class);
                startActivity(intent);
            }
        });


        mChar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterInfo.setSelectedCharacter(1);
                Intent intent = new Intent(CharacterSelectionActivity.this, CharInvActivity.class);
                startActivity(intent);
            }
        });

        mChar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterInfo.setSelectedCharacter(2);
                Intent intent = new Intent(CharacterSelectionActivity.this, CharInvActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getAccountDetails(final String accessToken) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = AppConstants.GET_CHARACTERS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                AccountMainObject mainObject = gson.fromJson(response, AccountMainObject.class);

                CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
                String membershipTypeName = characterInfo.getMembershipType().toString();

                String membershipId = mainObject.getResponse().getDestinyAccounts().get(0).
                        getUserInfo().getMembershipId();
                Long membershipType = mainObject.getResponse().getDestinyAccounts().get(0).
                        getUserInfo().getMembershipType();

                characterInfo.setMembershipId(membershipId);
                characterInfo.setMembershipType(membershipType);

//                if (membershipTypeName.equals("-5")) {
//                    //Either the API is under maintenance; or the user has no characters.
//                    //Need to display some kind of warning here
//                    Intent intent = new Intent(CharacterSelectionActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }

                List<Character> characters = mainObject.getResponse().getDestinyAccounts().get(0).getCharacters();
                Log.d(AppConstants.TAG, "Character Quantity " + characters.size());

                //BİRŞEYLER PATLARSA AŞAĞIDAKİNİ UNCOMMENTLE
                characterInfo.clearAllCharacterList();

                for (int i = 0; i < characters.size(); i++) {
                    String className = characters.get(i).getCharacterClass().getClassName();
                    String backgroundUrl = characters.get(i).getBackgroundPath();
                    String emblemUrl = characters.get(i).getEmblemPath();
                    String characterId = characters.get(i).getCharacterId();
                    String genderName = characters.get(i).getGender().getGenderName();
                    String raceName = characters.get(i).getRace().getRaceName();
                    String lightLevel = characters.get(i).getPowerLevel().toString();
                    String normalLevel = characters.get(i).getLevel().toString();

                    characterInfo.addToCharacterList(new CharacterInfoObject(backgroundUrl, characterId,
                            className, emblemUrl, genderName, lightLevel, normalLevel, raceName));

                }

                bindViews(mChar1, mChar2, mChar3);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: Failed");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void bindViews(FrameLayout char1, FrameLayout char2, FrameLayout char3) {
        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        switch (characterInfo.getCharacterList().size()) {
            case 0:
                Toast.makeText(this, "You've no characters.", Toast.LENGTH_SHORT).show();
                char1.setVisibility(View.GONE);
                char2.setVisibility(View.GONE);
                char3.setVisibility(View.GONE);
                break;
            case 1:
                createChar1(characterInfo);
                char2.setVisibility(View.GONE);
                char3.setVisibility(View.GONE);
                break;
            case 2:
                createChar1(characterInfo);
                createChar2(characterInfo);
                char3.setVisibility(View.GONE);
                break;
            case 3:
                createChar1(characterInfo);
                createChar2(characterInfo);
                createChar3(characterInfo);
                break;
        }
    }

    public void createChar1(CharacterInfoSingleton characterInfo) {
        Picasso.with(CharacterSelectionActivity.this).load(BUNGIE_NET_START_URL + characterInfo.
                getCharacterList().get(0).getBackgroundUrl()).into(mBannerChar1);
        Picasso.with(CharacterSelectionActivity.this).load(BUNGIE_NET_START_URL + characterInfo.
                getCharacterList().get(0).getEmblemUrl()).into(mEmblem1);
        mClassName1.setText(characterInfo.getCharacterList().get(0).getClassName());
    }

    public void createChar2(CharacterInfoSingleton characterInfo) {
        Picasso.with(CharacterSelectionActivity.this).load(BUNGIE_NET_START_URL + characterInfo.
                getCharacterList().get(1).getBackgroundUrl()).into(mBannerChar2);
        Picasso.with(CharacterSelectionActivity.this).load(BUNGIE_NET_START_URL + characterInfo.
                getCharacterList().get(1).getEmblemUrl()).into(mEmblem2);
        mClassName2.setText(characterInfo.getCharacterList().get(1).getClassName());
    }

    public void createChar3(CharacterInfoSingleton characterInfo) {
        Picasso.with(CharacterSelectionActivity.this).load(BUNGIE_NET_START_URL + characterInfo.
                getCharacterList().get(2).getBackgroundUrl()).into(mBannerChar3);
        Picasso.with(CharacterSelectionActivity.this).load(BUNGIE_NET_START_URL + characterInfo.
                getCharacterList().get(2).getEmblemUrl()).into(mEmblem3);
        mClassName3.setText(characterInfo.getCharacterList().get(2).getClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
