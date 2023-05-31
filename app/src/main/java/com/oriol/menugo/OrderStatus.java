package com.oriol.menugo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Model.Order;
import com.oriol.menugo.Model.Request;
import com.oriol.menugo.ViewHolder.OrderViewHolder;

/**
 * @author Oriol
 */
public class OrderStatus extends AppCompatActivity {

    //Definición variables
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    /**
     * Método onCreate de la actividad del estado del pedido
     * @param savedInstanceState Si la actividad se reinicializa después de
     *      * previamente cerrado, entonces este paquete contiene los datos que más
     *      * suministrado recientemente en {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.current_User.getPhone());
    }

    /**
     * Método para cargar los pedidos
     * @param phone teléfono del usuario
     */
    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request request, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                viewHolder.txtOrderAddress.setText(request.getAddress());
                viewHolder.txtOrderPhone.setText(request.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    /**
     * Método para cambiar el texto de estado del pedido
     * @param status estado del pedido
     * @return
     */
    private int convertCodeToStatus(String status) {
        if (status.equals("0"))
            return R.string.status0;
        else if (status.equals("1"))
            return R.string.status1;
        else
            return R.string.status2;
    }
}