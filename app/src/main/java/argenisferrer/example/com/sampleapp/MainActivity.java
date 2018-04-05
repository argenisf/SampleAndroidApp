package argenisferrer.example.com.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String MIXPANEL_TOKEN = "f8bd7cddaf94642530004c3d0509691f";
    private MixpanelAPI mixpanel;
    private Context mContext;
    private JSONObject mCurrentSuperProps;

    private Button mMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        // Initialize the SDK
        mixpanel = MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);
        mixpanel.getPeople().identify(mixpanel.getDistinctId());
        mCurrentSuperProps = mixpanel.getSuperProperties();
        mMainButton = (Button)findViewById(R.id.btnMain);

        //
        /*
         * Goal: increment the number of app launches and track app launched
         *
         * We will check the current super properties to see if the N launches one exists. If it does,
         * we will increment it, and if it doesn't we will set it to 1.
         * Also, we are logged in, we will increase the people counter.
         *
         * */

        try{
            int nLaunches = 0;
            if(mCurrentSuperProps.has("N launches")){
                nLaunches = mCurrentSuperProps.getInt("N launches");
            }
            nLaunches++;
            mCurrentSuperProps.put("N launches", nLaunches);
            Log.v("TestingApp",nLaunches+"");
            mixpanel.registerSuperProperties(mCurrentSuperProps);
            if(mCurrentSuperProps.has("loggedIn") && mCurrentSuperProps.getBoolean("loggedIn")){
                mixpanel.getPeople().increment("N launches", 1);
            }
        }catch (JSONException e){
            Log.v("TestingApp","error: "+ e.getMessage().toString());
        }

        mixpanel.track("App launched");
        mContext = this;
        mMainButton = (Button)findViewById(R.id.btnMain);


        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Goal: the main button was clicked. If the user has already logged in, we want to
                 * show him/her the sticker; otherwise, we want authentication to happen.
                 * We do this by checking the loggedIn super prop
                 * */

                Boolean loggedIn = false;
                mCurrentSuperProps = mixpanel.getSuperProperties();
                try {
                    if(mCurrentSuperProps.has("loggedIn") && mCurrentSuperProps.getBoolean("loggedIn")){
                        loggedIn = true;
                    }else{
                        mCurrentSuperProps.put("loggedIn", loggedIn);
                    }
                    mixpanel.registerSuperProperties(mCurrentSuperProps);
                } catch (JSONException e) {
                    Log.v("TestingApp","error: "+ e.getMessage().toString());
                }


                Intent intent;
                if(loggedIn){
                    intent = new Intent(mContext, StickerActivity.class);
                    Sticker mSticker = new Sticker(true);
                    intent.putExtra("stickerId", mSticker.getSticketId());
                }else{
                    mixpanel.track("Auth started");
                    intent = new Intent(mContext, Auth.class);
                }

                startActivity(intent);
            }
        });

    }
}
