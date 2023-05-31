package com.oriol.menugo.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.oriol.menugo.R;

/**
 * @author Oriol
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Definición variables
    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    /**
     * Definimos onClick en el menú
     */
    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    /**
     * View holder del menú
     * @param itemView vista a la que se hace click
     */
    public MenuViewHolder(View itemView) {
        super(itemView);

        txtMenuName = (TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    /**
     * Método para iniciar recopilador de eventos click
     * @param itemClickListener recopilador eventos donde se hace click
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * Método para recuperar el onClick en la vista.
     * @param view Vista que fue pulsada.
     */
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
