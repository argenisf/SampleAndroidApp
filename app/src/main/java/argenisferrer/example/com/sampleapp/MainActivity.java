package argenisferrer.example.com.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String MIXPANEL_TOKEN = "f8bd7cddaf94642530004c3d0509691f";
    private MixpanelAPI mixpanel;
    private Context mContext;
    private JSONObject mCurrentProps;

    private Button mMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mixpanel = MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);
        mixpanel.unregisterSuperProperty("loggedIn");
        mixpanel.track("App launched");

        mMainButton = (Button)findViewById(R.id.btnMain);


        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean loggedIn = false;
                mCurrentProps = mixpanel.getSuperProperties();
                try {
                    if(mCurrentProps.has("loggedIn") && mCurrentProps.getBoolean("loggedIn")){
                        loggedIn = true;
                    }else{
                        mCurrentProps.put("loggedIn", loggedIn);
                    }
                } catch (JSONException e) {}
                Intent intent;
                if(loggedIn){
                    intent = new Intent(mContext, StickerActivity.class);
                    Sticker mSticker = new Sticker(true);
                    intent.putExtra("stickerId", mSticker.getSticketId());
                }else{
                    intent = new Intent(mContext, Auth.class);
                }

                startActivity(intent);
            }
        });

    }
}
