package com.oriol.menugo.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oriol.menugo.R;

/**
 * @author Oriol
 */
public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Definición variables
    public TextView food_name;
    public ImageView food_image, fav_image, quick_cart;
    private ItemClickListener itemClickListener;


    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    /**
     * Añadimos un recogedor de eventos
     * @param itemClickListener recogemos el item donde se hizo click
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * ViewHolder de la comida
     * @param itemView recogemos la vista del item
     */
    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name = (TextView)itemView.findViewById(R.id.food_name);
        food_image = (ImageView)itemView.findViewById(R.id.food_image);
        fav_image = (ImageView)itemView.findViewById(R.id.fav);
        quick_cart = (ImageView)itemView.findViewById(R.id.btn_cart_quick);

        itemView.setOnClickListener(this);
    }

    /**
     * Método onClick para recuperar la vista pulsada.
     * @param view La vista que fue pulsada.
     */
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
