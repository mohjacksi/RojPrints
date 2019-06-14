package com.mjacksi.rojprints.SimpleAlbum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mjacksi.rojprints.R;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> images;
    private LayoutInflater inflater;
    private int albumSize;


    private SimpleAlbumImagesListActivity simpleAlbumImagesListActivity;

    public ImageAdapter(Context context, int albumSize, SimpleAlbumImagesListActivity SimpleAlbumImagesListActivity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        images = new ArrayList<>();
        this.albumSize = albumSize;
        this.simpleAlbumImagesListActivity = SimpleAlbumImagesListActivity;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(inflater.inflate(R.layout.album_list_item_square, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        final Image image = images.get(position);

        Glide.with(context)
                .load(image.getPath())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleAlbumImagesListActivity.editImageAt(position);
            }
        });

        if(position == 0)
            holder.sequence.setText("The cover of your album");
        else
            holder.sequence.setText(String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<Image> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }


    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView sequence;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            sequence = itemView.findViewById(R.id.sequence);
        }
    }
}
