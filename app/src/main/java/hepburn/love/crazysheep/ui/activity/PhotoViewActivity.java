package hepburn.love.crazysheep.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import hepburn.love.crazysheep.R;
import uk.co.senab.photoview.PhotoView;

/**
 * check the original image
 *
 * Created by crazysheep on 15/7/27.
 */
public class PhotoViewActivity extends BaseActivity {

    //////////////////////common api for start this activity////////////////

    public static void start(Context context, String imageUrl) {
        Intent i = new Intent(context, PhotoViewActivity.class);
        i.putExtra("image_url", imageUrl);
        context.startActivity(i);
    }

    ////////////////////////////////////////////////////////////////////////

    private PhotoView mImagePv;

    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_view);

        parseIntent();
        initUI();
    }

    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra("image_url");
    }

    private void initUI() {
        mImagePv = (PhotoView) findViewById(R.id.image_pv);

        Glide.with(this)
                .load(mImageUrl)
                .into(mImagePv);
    }

}
