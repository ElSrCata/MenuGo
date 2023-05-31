package com.oriol.menugo.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oriol.menugo.Interface.ItemClickListener;
import com.oriol.menugo.R;

/**
 * @author Oriol
 */
public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Definición variables
    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;

    private ItemClickListener itemClickListener;

    /**
     * ViewHolder de la orden
     * @param itemView Vista del item
     */
    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress = (TextView) itemView.findViewById(R.id.order_address);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * OnClick de la vista
     * @param v La vista que fue pulsada
     */
    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
