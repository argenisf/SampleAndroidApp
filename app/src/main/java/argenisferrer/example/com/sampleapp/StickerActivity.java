package argenisferrer.example.com.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class StickerActivity extends AppCompatActivity {

    ImageView mImageView;
    MixpanelAPI mixpanel;
    JSONObject mCurrentSuperProps;
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        mixpanel = MixpanelAPI.getInstance(this, MainActivity.MIXPANEL_TOKEN);
        mCurrentSuperProps = mixpanel.getSuperProperties();

        Intent intent = getIntent();
        JSONObject props = new JSONObject();
        try {
            // check if the user was just created to queue up the people updates
            if (intent.getBooleanExtra("justSignedUp", false) && mCurrentSuperProps.has("$created")
                    && mCurrentSuperProps.has("$email") && mCurrentSuperProps.has("$name")) {
                String theEmail = mCurrentSuperProps.getString("$email");
                int nLaunches = mCurrentSuperProps.getInt("N launches");
                props.put("$created", mCurrentSuperProps.getString("$created"));
                props.put("$email", theEmail);
                props.put("$name", mCurrentSuperProps.getString("$name"));
                props.put("N launches", nLaunches);
                mixpanel.identify(theEmail);
                mixpanel.getPeople().identify(theEmail);
                // track success
                mixpanel.track("Signed up");
                mixpanel.getPeople().setOnce(props);
            }
        }catch (JSONException e){ }

        Sticker sticker = new Sticker();
        int stickerId = intent.getIntExtra("stickerId",0);
        final String stickerName = sticker.getStickerName(stickerId);
        mImageView = (ImageView) findViewById(R.id.mainImageView);
        mImageView.setImageResource(stickerId);
        props = new JSONObject();
        try {
            props.put("Sticker name", stickerName);
            mixpanel.track("Viewed Sticker", props);
        }catch (JSONException e){  }

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject props = new JSONObject();
                try {
                    props.put("Sticker name", stickerName);
                }catch (JSONException e){}
                mixpanel.track("Shared sticker", props);
            }
        });
    }
}
