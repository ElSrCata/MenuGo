package com.oriol.menugo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Common.Config;
import com.oriol.menugo.Database.Database;
import com.oriol.menugo.Model.Order;
import com.oriol.menugo.Model.Request;
import com.oriol.menugo.ViewHolder.CartAdapter;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
/**
 * Classe Carrito
 * @author Oriol
 */
public class Cart extends AppCompatActivity {

    //Definición variables
    private static final int PAYPAL_REQUEST_CODE = 999;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    //PayPal payment
    //We use sandbox because it's only for testing
    static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    String address, comment;

    /**
     * OnCreate de la classe
     * @param savedInstanceState Si la actividad se reinicializa después de
     *      * previamente cerrado, entonces este paquete contiene los datos que más
     *      * suministrado recientemente en {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init Paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        //Firebase
        database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, R.string.emptyCart, Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    /**
     * Mensaje de Alerta modo Diálogo
     */
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle(R.string.dialogTitle);
        alertDialog.setMessage(R.string.dialogText);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View order_address_comment = layoutInflater.inflate(R.layout.order_address_comment, null);

        final MaterialEditText edtAddress = (MaterialEditText)order_address_comment.findViewById(R.id.edtAddress);
        final MaterialEditText edtComment = (MaterialEditText)order_address_comment.findViewById(R.id.edtComment);

        final RadioButton rdiOnDelivery = (RadioButton) order_address_comment.findViewById(R.id.onDelivery);
        final RadioButton rdiPayPal = (RadioButton) order_address_comment.findViewById(R.id.rdyPayPal);

        alertDialog.setView(order_address_comment);

        alertDialog.setIcon(R.drawable.cart);

        alertDialog.setPositiveButton(R.string.confirmOrder, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                address = edtAddress.getText().toString();
                comment = edtComment.getText().toString();

                //Check payment method
                if (!rdiOnDelivery.isChecked() && !rdiPayPal.isChecked())
                {
                    Toast.makeText(Cart.this, "Seleccione un método de pago", Toast.LENGTH_SHORT).show();

                }
                else if (rdiPayPal.isChecked())
                {
                    String formatAmount = txtTotalPrice.getText().toString().replace("$", "")
                            .replace(",", "");

                    Request request = new Request(Common.current_User.getPhone(), Common.current_User.getName(),
                            address, txtTotalPrice.getText().toString(),
                            "0", comment, "Pagado", "PayPal",cart
                    );

                    //Submit order to firebase
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    //Delete cart
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this, R.string.orderSubmitted, Toast.LENGTH_SHORT).show();
                    finish();

                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formatAmount), "USD",
                            "MenuGo", PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                }
                else if (rdiOnDelivery.isChecked())
                {
                    String formatAmount = txtTotalPrice.getText().toString().replace("$", "")
                            .replace(",", "");

                    Request request = new Request(Common.current_User.getPhone(), Common.current_User.getName(),
                            address, txtTotalPrice.getText().toString(),
                            "0", comment, "No Pagado", "onDelivery",cart
                    );

                    //Submit order to firebase
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    //Delete cart
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this, R.string.orderSubmitted, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        alertDialog.setNegativeButton(R.string.denegateOrder, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Resultado de la actividad
     * @param requestCode El código de solicitud entero proporcionado originalmente a
     *      * startActivityForResult(), que le permite identificar quién es este
     *      * resultado de donde vino.
     *
     * @param resultCode El código de resultado entero devuelto por la actividad secundaria
     *      * a través de su setResult().
     *
     * @param data Una intención, que puede devolver datos de resultados a la persona que llama
     *      * (se pueden adjuntar varios datos al Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {
                    Toast.makeText(this, "La confirmació no es nul", Toast.LENGTH_SHORT).show();
                    try {

                        String paymentDetail = confirmation.toJSONObject().toString(4);

                        JSONObject jsonObject = new JSONObject(paymentDetail);

                        /*
                        Request request = new Request(Common.current_User.getPhone(), Common.current_User.getName(),
                                address, txtTotalPrice.getText().toString(),
                                "0", comment, jsonObject.getJSONObject("response").getString("state"), cart
                        );

                        //Submit order to firebase
                        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                        //Delete cart
                        new Database(getBaseContext()).cleanCart();
                        Toast.makeText(Cart.this, R.string.orderSubmitted, Toast.LENGTH_SHORT).show();
                        finish();*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Pago cancelado", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Pago inválido", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Cargar lista de comida
     */
    private void loadListFood(){
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate total price
        double total = 0;

        for (Order order:cart){

            total += (Double.parseDouble(order.getPrice())) * (Double.parseDouble(order.getQuantity()));
        }

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    /**
     * Item Seleccionadp
     * @param item El contexto del menú del item seleccionado.
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
        {
            deleteCart(item.getOrder());
        }
        return true;
    }

    /**
     * Método para borrar el carrito.
     * @param position Posición del carrito
     */
    private void deleteCart(int position) {

        cart.remove(position);

        new Database(this).cleanCart();

        for (Order item:cart)
            new Database(this).addToCart(item);

        loadListFood();
    }
}