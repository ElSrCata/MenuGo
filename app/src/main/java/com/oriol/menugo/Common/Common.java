package com.oriol.menugo.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.oriol.menugo.Model.User;
import com.oriol.menugo.R;

/**
 * @author oriol
 */

/**
 * Clase Common para guardar información en común entre todas las clases
 */
public class Common {

    //Definicion de variables
    public static User current_User;

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    /**
     * Método para comprobar si tiene conexión a internet
     * @param context variable para mirar si tiene conexión
     * @return devolvemos true o false según si tiene conexión o no
     */
    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
            {
                for(int i=0; i<info.length;i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }



}
