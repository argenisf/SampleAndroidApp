package argenisferrer.example.com.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Auth extends AppCompatActivity {

    private MixpanelAPI mixpanel;
    private Button mButtonEnter;
    private Context mContext;
    private EditText mEmailInput;
    private EditText mNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mContext = this;
        mixpanel = MixpanelAPI.getInstance(this, MainActivity.MIXPANEL_TOKEN);

        mButtonEnter = (Button) findViewById(R.id.btnEnter);
        mEmailInput = (EditText) findViewById(R.id.emailInput);
        mNameInput = (EditText) findViewById(R.id.nameInput);

        mButtonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEmailInput.getText().toString().trim().length() > 0 && mNameInput.getText().toString().trim().length() > 0){
                    if(isValidEmail(mEmailInput.getText().toString().trim())){

                        String theEmail = mEmailInput.getText().toString().trim();

                        // all checked, let's log in and proceed
                        JSONObject props = new JSONObject();
                        try {
                            TimeZone tz = TimeZone.getTimeZone("UTC");
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                            df.setTimeZone(tz);
                            String nowAsISO = df.format(new Date());

                            props.put("$created", nowAsISO);
                            props.put("$email",theEmail);
                            props.put("$name",mNameInput.getText().toString().trim());
                            props.put("loggedIn",true);
                        } catch (JSONException e) {}
                        // register super properties
                        mixpanel.registerSuperProperties(props);
                        //create the alias
                        mixpanel.alias(theEmail,mixpanel.getDistinctId());

                        Sticker sticker = new Sticker(true);
                        Intent intent = new Intent(mContext, StickerActivity.class);
                        intent.putExtra("stickerId",sticker.getSticketId());
                        intent.putExtra("justSignedUp",true);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(mContext,"Please enter a valid email address", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(mContext,"You need to input your email and name", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public final static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
