package com.oriol.menugo.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.oriol.menugo.Cart;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Interface.ItemClickListener;
import com.oriol.menugo.Model.Order;
import com.oriol.menugo.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adaptador para la clase Carrito
 * @author Oriol
 */
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    //Definición de variables
    public TextView txt_cart_name, txt_price;
    public ImageView img_cart_count, cart_image;

    private ItemClickListener itemClickListener;


    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    /**
     * ViewHolder del carrito
     * @param itemView item del carrito
     */
    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        img_cart_count = (ImageView) itemView.findViewById(R.id.cart_item_count);
        cart_image = (ImageView)itemView.findViewById(R.id.cart_image);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Método para crear un menú en el carrito
     * @param contextMenu Contexto en el cual se usa el menú
     * @param view Vista donde se construye el menú
     * @param contextMenuInfo Información extra del menú
     */
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle(R.string.selectAction);
        contextMenu.add(0,0, getAdapterPosition(), Common.DELETE);

    }
}

/**
 * Classe para el ViewHolder
 */
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    //Definición variables
    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    /**
     * Adaptador del carrito
     * @param listData lista de las órdenes
     * @param cart item carrito
     */
    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    /**
     * Creación del View Holder
     * @param parent ViewGroup donde se añadirá la nueva vista
     * @param viewType Tipo de vista de la nueva View
     *
     * @return
     */
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);

    }

    /**
     * Enlaçe del ViewHolder
     * @param holder El ViewHolder que debe actualizarse para representar el contenido del item
     * @param position La posición del item con el adaptador.
     */
    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        Picasso.with(cart.getBaseContext()).load(listData.get(position).getImage()).resize(70, 70)
                .centerCrop().into(holder.cart_image);

        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        double price = (Double.parseDouble(listData.get(position).getPrice())) * (Double.parseDouble(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    /**
     * Método para contar cuantos items hay
     * @return devolvemos la cantidad de items que hay en el carrito
     */
    @Override
    public int getItemCount() {
        return listData.size();
    }
}
