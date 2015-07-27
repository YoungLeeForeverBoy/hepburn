package hepburn.love.crazysheep.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.dao.ImageResultDto;
import hepburn.love.crazysheep.ui.activity.PhotoViewActivity;

/**
 * adapter for recyclerview
 *
 * Created by crazysheep on 15/7/23.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder> {

    public static final String TAG = ImageRecyclerAdapter.class.getSimpleName();

    private Context mContext;
    private List<ImageResultDto.ImageItemDto> mImageUrls;
    private LayoutInflater mInflater;

    public ImageRecyclerAdapter(Context context, List<ImageResultDto.ImageItemDto> imgUrls) {
        mContext = context;
        mImageUrls = imgUrls;

        mInflater = LayoutInflater.from(mContext);
    }

    public void addData(List<ImageResultDto.ImageItemDto> imgUrls) {
        if(mImageUrls == null)
            mImageUrls = new ArrayList<>();
        mImageUrls.addAll(imgUrls);

        notifyDataSetChanged();
    }

    public void clearData() {
        mImageUrls = null;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int i) {
        holder.mImageIv.setImageResource(0);
        Glide.clear(holder.mImageIv);

        // measure current imageview height by its width
        // refer to {#https://github.com/drakeet/Meizhi/blob/master/app/src/main/java/me/drakeet/meizhi/MeizhiListAdapter.java}
        holder.mImageIv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (mImageUrls != null && i < mImageUrls.size() && mImageUrls.get(i) != null) {
                    int thumbWidth = mImageUrls.get(i).image_width;
                    int thumbHeight = mImageUrls.get(i).image_height;
                    if (thumbWidth > 0 && thumbHeight > 0) {
                        int width = holder.mImageIv.getMeasuredWidth();
                        int height = Math.round(width * (float) thumbHeight / thumbWidth);
                        holder.mImageIv.getLayoutParams().height = height;
                        holder.mImageIv.setMinimumHeight(height);

                        // so fucking pretty api for Glide
                        Glide.with(mContext)
                                .load(mImageUrls.get(i).image_url)
                                .override(width, height)
                                .into(holder.mImageIv);
                    }
                }

                // api <= 15
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                    holder.mImageIv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    holder.mImageIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        holder.mImageIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click item pos: " + i, Toast.LENGTH_SHORT).show();

                // check the original image
                if(mImageUrls != null && i < mImageUrls.size() && mImageUrls.get(i) != null)
                    PhotoViewActivity.start(mContext, mImageUrls.get(i).image_url);
            }
        });
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemRootView = mInflater.inflate(R.layout.layout_image_item, null);

        return new ImageViewHolder(itemRootView);
    }

    @Override
    public int getItemCount() {
        return mImageUrls == null ? 0 : mImageUrls.size();
    }

    //////////////////////////////////////////////////////////////////////////
    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageIv;

        public ImageViewHolder(View parent) {
            super(parent);

            mImageIv = (ImageView) parent.findViewById(R.id.image_iv);
        }
    }
}
