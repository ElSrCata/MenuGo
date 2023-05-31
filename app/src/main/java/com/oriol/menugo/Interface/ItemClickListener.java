package com.oriol.menugo.Interface;

import android.view.View;

/**
 * Interfície para guardar onClick
 * @author oriol
 */

public interface ItemClickListener {

    /**
     * Método onClick
     * @param view mostramos la vista donde se dirigirá
     * @param position guardamos la posición
     * @param isLongClick comprobamos si sigue siendo pulsado
     */
    void onClick(View view, int position, boolean isLongClick);

}


