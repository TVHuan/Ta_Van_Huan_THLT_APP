package com.example.bai2thapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    private Context context;
    private List<Photo> photoList;

    public PhotoAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_photo, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_view_item);
            holder.favIcon = convertView.findViewById(R.id.icon_favorite);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Photo photo = photoList.get(position);

        if (photo.isFromUri()) {
            holder.imageView.setImageURI(photo.getUri());
        } else {
            holder.imageView.setImageResource(photo.getResourceId());
        }

        holder.favIcon.setVisibility(photo.isFavorite() ? View.VISIBLE : View.GONE);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageView favIcon;
    }
}
