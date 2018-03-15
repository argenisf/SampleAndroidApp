package argenisferrer.example.com.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class StickerActivity extends AppCompatActivity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        Intent intent = getIntent();

        mImageView = (ImageView) findViewById(R.id.mainImageView);
        mImageView.setImageResource(intent.getIntExtra("stickerId",0));
    }
}
