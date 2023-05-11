package com.oriol.menugo.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    public ImageView imageView;

    public MenuViewHolder(View itemView) {
        super(itemView);

        //txtMenuName = (TextView) itemView.findViewById(R.id)
    }

    @Override
    public void onClick(View view) {

    }
}
