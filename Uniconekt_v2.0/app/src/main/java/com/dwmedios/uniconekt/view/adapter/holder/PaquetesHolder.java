package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Item_aplica;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.adapter.PaquetesAdapter;
import com.dwmedios.uniconekt.view.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaquetesHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewNombrePaquete)
    TextView mTextViewNombre;
    @BindView(R.id.textViewCostoPaquete)
    TextView mTextViewCosto;
    @BindView(R.id.textViewDescripcionPaquete)
    TextView mTextViewDescripcion;
    @BindView(R.id.VigenciaPaquete)
    TextView mTextViewVigencia;
    @BindView(R.id.buttonVerMas)
    Button mButton;
    @BindView(R.id.buttonVerMas2)
    Button mButton2;
    @BindView(R.id.recycleViewItemsAplica)
    RecyclerView recyclerView;
    CustomAdapter mCustomAdapter;

    public PaquetesHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        // this.setIsRecyclable(false);
    }

    public void Configure(final Paquetes mPaquetes, final PaquetesAdapter.onclick mOnclick) {
        if (mPaquetes.nombre != null) mTextViewNombre.setText(mPaquetes.nombre);
        java.util.Currency moneda = java.util.Currency.getInstance("MXN");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance();


        if (mPaquetes.costo != null) {
            format.setCurrency(moneda);
            String costo = format.format(mPaquetes.costo).toString();
            costo = costo.replace("MXN", "");
            costo = costo.replace("MX", "");
            costo = costo.replace("$", "");
            mTextViewCosto.setText("$" + costo + " MXN");

            //  mTextViewCosto.setText(mPaquetes.costo+"");
            System.out.println(format.format(23));
        }
        if (mPaquetes.descripcion != null) mTextViewDescripcion.setText(mPaquetes.descripcion);
        if (mPaquetes.dias_vigencia != 0)
            mTextViewVigencia.setText(mPaquetes.dias_vigencia + " días de vigencia");
        List<Item_aplica> mItem_aplicas = new ArrayList<>();

        /** POSTULACION**/
        Item_aplica postulacion = new Item_aplica();
        postulacion.textoAplica = "Aplica postulación";
        postulacion.aplica = mPaquetes.aplica_Postulacion;
        mItem_aplicas.add(postulacion);

        /** BECAS**/
        Item_aplica becas = new Item_aplica();
        becas.textoAplica = "Aplica becas";
        becas.aplica = mPaquetes.aplica_Beca;
        mItem_aplicas.add(becas);

        /** FINANCIAMIENTO**/
        Item_aplica financiamiento = new Item_aplica();
        financiamiento.aplica = mPaquetes.aplica_Financiamiento;
        financiamiento.textoAplica = "Aplica financiamiento";
        mItem_aplicas.add(financiamiento);


        /** LOGO**/
        Item_aplica logo = new Item_aplica();
        logo.textoAplica = "Aplica logo";
        logo.aplica = mPaquetes.aplica_logo;
        mItem_aplicas.add(logo);
/**Imagenes **/
        Item_aplica img = new Item_aplica();
        img.textoAplica = "Aplica imagenes";
        img.aplica = mPaquetes.aplica_imagenes;
        mItem_aplicas.add(img);

        /** DIRECCION**/
        Item_aplica Dirección = new Item_aplica();
        Dirección.textoAplica = "Aplica dirección";
        Dirección.aplica = mPaquetes.aplica_direccion;
        mItem_aplicas.add(Dirección);

        /** favoritos**/
        Item_aplica favoritos = new Item_aplica();
        favoritos.textoAplica = "Aplica favoritos";
        favoritos.aplica = mPaquetes.aplica_favoritos;
        mItem_aplicas.add(favoritos);
        /** ubicacion**/
        Item_aplica ubicacion = new Item_aplica();
        ubicacion.textoAplica = "Aplica ubicación";
        ubicacion.aplica = mPaquetes.aplica_geolocalizacion;
        mItem_aplicas.add(ubicacion);

        /** redes**/
        Item_aplica redes = new Item_aplica();
        redes.textoAplica = "Aplica redes sociales";
        redes.aplica = mPaquetes.aplica_redes;
        mItem_aplicas.add(redes);

        Item_aplica Contacto = new Item_aplica();
        Contacto.textoAplica = "Aplica contacto";
        Contacto.aplica = mPaquetes.aplica_contacto;
        mItem_aplicas.add(Contacto);

        /** ProspectusVideo**/
        Item_aplica ProspectusVideo = new Item_aplica();
        ProspectusVideo.textoAplica = "Aplica prospectus con folletos y un video";
        ProspectusVideo.aplica = mPaquetes.aplica_video_1;
        mItem_aplicas.add(ProspectusVideo);

        /** ProspectusVideo2**/
        Item_aplica ProspectusVideo2 = new Item_aplica();
        ProspectusVideo2.textoAplica = "Aplica prospectus con folletos y dos videos";
        ProspectusVideo2.aplica = mPaquetes.aplica_video_2;
        mItem_aplicas.add(ProspectusVideo2);


        Item_aplica Descripcion = new Item_aplica();
        Descripcion.textoAplica = "Aplica descripción";
        Descripcion.aplica = mPaquetes.aplica_descripcion;
        mItem_aplicas.add(Descripcion);

        /** PROSPECTUS**/
        Item_aplica prospectus2 = new Item_aplica();
        prospectus2.textoAplica = "Aplica prospectus con folletos";
        prospectus2.aplica = mPaquetes.aplica_Prospectus;
        mItem_aplicas.add(prospectus2);


        mCustomAdapter = new CustomAdapter(mItem_aplicas, R.layout.row_item_aplica_paquete, mConfigureHolder);
        LinearLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mCustomAdapter);
        Utils.setAnimRecyclerView(itemView.getContext(), R.anim.layout_animation, recyclerView);

        if (mPaquetes.actual) {
            mButton.setVisibility(View.GONE);
            mButton2.setVisibility(View.VISIBLE);
            //mButton.setBackgroundColor(Color.parseColor("#F12D24"));
        } else {
            mButton.setVisibility(View.VISIBLE);
            mButton2.setVisibility(View.GONE);
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickButton(mPaquetes);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickButton(mPaquetes);
            }
        });

    }

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        @Override
        public void Configure(View itemView, Object mObject) {
            Item_aplica mItem_aplica = (Item_aplica) mObject;
            CheckBox mCheckBox = itemView.findViewById(R.id.checkboxAplica);
            mCheckBox.setChecked(mItem_aplica.aplica);
            TextView mTextView = itemView.findViewById(R.id.textViewNombreAplica);
            mTextView.setText(mItem_aplica.textoAplica);
        }

        @Override
        public void Onclick(Object mObject) {

        }
    };
}
