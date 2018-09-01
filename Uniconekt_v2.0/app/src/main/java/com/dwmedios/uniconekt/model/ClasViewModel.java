package com.dwmedios.uniconekt.model;

import android.graphics.drawable.Drawable;

public class ClasViewModel {

    public static class menu {
        public tipoMenu tipo;
        public String nombre;
        public int drawableImage;
        public String color;
    }

    public static enum tipoMenu {
        Universidad,
        Becas,
        Financiamiento,
        Cupones,
        Viaja,
        nombre,
        academicos,
        cercaMi,
        Euu,
        favoritos,
        postulados,
        paquetes,
        examen,
        extranjero,
        buscar_pais,
        videos,
        folletos
    }
}
