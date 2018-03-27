package com.ziniuyimeixiang.imanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by j_mei on 2018-03-27.
 */

public class GridLayoutAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Bitmap> bitmaps;
    private int imageWidth;

    public GridLayoutAdapter (Context context, ArrayList<Bitmap> bitmapArrayList, int width) {
        this.mContext = context;
        this.bitmaps = bitmapArrayList;
        this.imageWidth = width;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int i) {
        return bitmaps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null)
        {
            imageView = new ImageView(mContext);
        }
        else
        {
            imageView = (ImageView) view;
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        imageView.setImageBitmap(bitmaps.get(i));

        return imageView;
    }
}
