package com.oriol.menugo.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.oriol.menugo.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para guardar las funciones relacionadas con SQL
 * @author oriol
 */
public class Database extends SQLiteAssetHelper {
    //Definición variables
    private static final String DB_NAME="MenuGoDB.db";
    private static final int DB_VER=1;

    /**
     * Constructor de la clase
     * @param context
     */
    public Database(Context context) {

        super(context, DB_NAME,null,DB_VER);
    }

    //Variable Lista para guardar los carritos de compra
    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName", "ProductId", "Quantity", "Price", "Discount", "Image"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        //Creation of orders list
        final List<Order> result = new ArrayList<>();

        if(c.moveToFirst())
        {
            do {
                result.add(new Order(c.getString(c.getColumnIndexOrThrow("ProductId")),
                        c.getString(c.getColumnIndexOrThrow("ProductName")),
                        c.getString(c.getColumnIndexOrThrow("Quantity")),
                        c.getString(c.getColumnIndexOrThrow("Price")),
                        c.getString(c.getColumnIndexOrThrow("Discount")),
                        c.getString(c.getColumnIndexOrThrow("Image"))
                ));

            } while (c.moveToNext());
        }
        return result;
    }

    /**
     * Método para añadir items al carrito
     * @param order item pedido
     */
    public void addToCart(Order order){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId, ProductName, Quantity, Price, Discount, Image) VALUES ('%s','%s','%s','%s','%s', '%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        db.execSQL(query);
    }

    /**
     * Método para limpiar el carrito de la compra
     */
    public void cleanCart(){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    /**
     * Método para añadir a favoritos
     * @param foodId ID de la comida
     */
    public void addToFavorites(String foodId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(FoodId) VALUES('%s');", foodId);
        db.execSQL(query);
    }

    /**
     * Método para borrar de favoritos
     * @param foodId ID de la comida
     */
    public void removefromFavorites(String foodId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s';", foodId);
        db.execSQL(query);
    }

    /**
     * Método para comprobar si una comida es favorita
     * @param foodId ID de la comida
     * @return devolvemos true o false según si es favorita o no
     */
    public boolean isFavorite(String foodId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE foodId='%s';", foodId);
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * Método para contar cuantos items tenemos en el carrito
     * @return devolvemos total items del carrito
     */
    public int getCountCart() {

        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                count = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return count;
    }
}
