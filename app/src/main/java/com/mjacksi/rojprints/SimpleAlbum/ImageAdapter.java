package com.mjacksi.rojprints.SimpleAlbum;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mjacksi.rojprints.R;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> images;
    private LayoutInflater inflater;
    private int albumSize;
    private float[] ratio;
    private ConstraintSet set = new ConstraintSet();

    private SimpleAlbumImagesListActivity simpleAlbumImagesListActivity;

    public ImageAdapter(Context context, int albumSize, SimpleAlbumImagesListActivity SimpleAlbumImagesListActivity, float[] ratio) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        images = new ArrayList<>();
        this.albumSize = albumSize;
        this.simpleAlbumImagesListActivity = SimpleAlbumImagesListActivity;
        this.ratio = ratio;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(inflater.inflate(R.layout.album_list_item_square, parent, false));
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        final Image image = images.get(position);
        final FrameLayout frameLayout= holder.frameLayout;
        final ConstraintLayout constraintLayout = holder.constraintLayout;


        set.clone(constraintLayout);
        set.setDimensionRatio(frameLayout.getId(),String.format( "H,%f:%f",ratio[0],ratio[1]));
        set.applyTo(constraintLayout);


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
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(position);
                simpleAlbumImagesListActivity.deleteImageAt(position);
            }
        });

        if(position == 0)
            holder.sequence.setText(context.getString(R.string.cover_image_description));
        else
            holder.sequence.setText(String.valueOf(position));

        // To tack snapshot to view
//        View v = holder.imageView;
//        v.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
//        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                "/rojprint";
//        File dir = new File(file_path);
//        if(!dir.exists())
//            dir.mkdirs();
//        File file = new File(dir, "rojprint" + image.getName() + ".png");
//        FileOutputStream fOut = null;
//        try {
//            fOut = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
        FrameLayout frameLayout;
        ConstraintLayout constraintLayout;

        ImageButton deleteButton;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            sequence = itemView.findViewById(R.id.sequence);
            frameLayout = itemView.findViewById(R.id.frame_layout);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
