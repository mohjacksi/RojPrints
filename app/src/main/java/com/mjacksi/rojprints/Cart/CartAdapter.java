package com.mjacksi.rojprints.Cart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Project> projects;
    private CartActivity cartActivity;

    public CartAdapter(Context context, List<Project> projects, CartActivity cartActivity) {
        this.context = context;
        this.projects = projects;
        this.cartActivity = cartActivity;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {

        final Project project = projects.get(position);

        Glide.with(context)
                .load(project.getImages().get(0).getpath())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(holder.image);

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartActivity.minusAt(position);
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartActivity.plusAt(position);
            }
        });
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("", "onClick: " + position);


                cartActivity.cancelProjectAt(position);
            }
        });

        holder.size.setText(project.getSize());
        holder.title.setText(project.getType());
        holder.totalPrice.setText(project.getTotalPrice() + " IQD");
        holder.counter.setText(project.getCountAtCart() + "");
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void setData(List<Project> projets) {

        if (projets != null) {
            this.projects = projets;
        }
        notifyDataSetChanged();
    }

    public void updateAt(int position) {
        notifyItemChanged(position);
    }


    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView size;
        TextView title;
        ImageView image;
        TextView totalPrice;

        ImageButton minusButton;
        TextView counter;
        ImageButton plusButton;
        ImageButton cancelButton;
        public CartViewHolder(View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.cart_size);
            title = itemView.findViewById(R.id.cart_title);
            image = itemView.findViewById(R.id.cart_image);
            totalPrice = itemView.findViewById(R.id.cart_price);
            minusButton = itemView.findViewById(R.id.cart_minus);
            counter = itemView.findViewById(R.id.cart_count);
            plusButton = itemView.findViewById(R.id.cart_plus);
            cancelButton = itemView.findViewById(R.id.cart_cancel);
        }
    }
}
