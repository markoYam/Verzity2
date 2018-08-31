package com.dwmedios.uniconekt.data.controller;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.database.ConfiguracionesOrmlite;
import com.dwmedios.uniconekt.data.database.DireccionOrmlite;
import com.dwmedios.uniconekt.data.database.DispositivoOrmlite;
import com.dwmedios.uniconekt.data.database.PersonaOrmlite;
import com.dwmedios.uniconekt.data.database.UniversidadOrmlite;
import com.dwmedios.uniconekt.data.database.UsuarioOrmLite;
import com.dwmedios.uniconekt.data.database.VentasPaquetesOrmlite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Configuraciones;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.model.VentasPaquetes;

import java.util.List;

/**
 * Created by mYam on 18/04/2018.
 */

public class AllController {

    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private DireccionOrmlite mDireccionOrmlite;
    private DispositivoOrmlite mDispositivoOrmlite;
    private PersonaOrmlite mPersonaOrmlite;
    private UniversidadOrmlite mUniversidadOrmlite;
    private UsuarioOrmLite mUsuarioOrmLite;
    private VentasPaquetesOrmlite mVentasPaquetesController;
    private ConfiguracionesOrmlite mConfiguracionesOrmlite;

    public AllController(Context mContext) {
        this.mContext = mContext;
        mOrmLiteDatabaseHelper = new OrmLiteDatabaseHelper(this.mContext);
        mDireccionOrmlite = new DireccionOrmlite(mOrmLiteDatabaseHelper);
        mDispositivoOrmlite = new DispositivoOrmlite(mOrmLiteDatabaseHelper);
        mPersonaOrmlite = new PersonaOrmlite(mOrmLiteDatabaseHelper);
        mUniversidadOrmlite = new UniversidadOrmlite(mOrmLiteDatabaseHelper);
        mUsuarioOrmLite = new UsuarioOrmLite(mOrmLiteDatabaseHelper);
        mVentasPaquetesController = new VentasPaquetesOrmlite(mOrmLiteDatabaseHelper);
        mConfiguracionesOrmlite = new ConfiguracionesOrmlite(mOrmLiteDatabaseHelper);
    }

    // TODO: 18/04/2018 Direccion
    public List<Direccion> getDireccion() {
        return mDireccionOrmlite.selectAll();
    }

    public boolean addDireccion(Direccion mItem) {
        return mDireccionOrmlite.addElement(mItem) > 0;
    }

    public boolean updateDireccion(Direccion mItem) {
        if (mDireccionOrmlite.updateElement(mItem) > 0) {
            return true;
        } else {
            return (mDireccionOrmlite.addElement(mItem) > 0);
        }
    }

    // TODO: 18/04/2018 Dispositivo
    public List<Dispositivo> getDispositivo() {
        return mDispositivoOrmlite.selectAll();
    }

    public Dispositivo getDispositivoPersona() {
        return (mDispositivoOrmlite.selectAll() != null && mDispositivoOrmlite.selectAll().size() > 0 ? mDispositivoOrmlite.selectAll().get(0) : null);
    }

    public boolean addDispositivo(Dispositivo mItem) {
        return mDispositivoOrmlite.addElement(mItem) > 0;
    }

    // TODO: 18/04/2018 persona
    public List<Persona> getPersona() {
        return mPersonaOrmlite.selectAll();
    }

    public boolean updatePersona(Persona mPersona) {
        return mPersonaOrmlite.updateElement(mPersona) > 0;
    }

    public boolean addPersona(Persona mItem) {
        return mPersonaOrmlite.addElement(mItem) > 0;
    }

    // TODO: 18/04/2018 Universidad
    public List<Universidad> getUniversidad() {

        return mUniversidadOrmlite.selectAll();
    }

    public Universidad getUniversidadActual() {

        return (mUniversidadOrmlite.selectAll() != null && mUniversidadOrmlite.selectAll().size() > 0 ? mUniversidadOrmlite.selectAll().get(0) : null);
    }

    public boolean addUniversidad(Universidad mItem) {
        return mUniversidadOrmlite.addElement(mItem) > 0;
    }

    // TODO: 18/04/2018 Usuario
    public List<Usuario> getUsuario() {
        return mUsuarioOrmLite.selectAll();
    }

    public boolean addUsuario(Usuario mItem) {
        return mUsuarioOrmLite.addElement(mItem) > 0;
    }

    // TODO: 11/05/2018 actualizar cada vez que se guarde informacion del usuario en la base de datos
    public boolean clearAllTables() {
        if (mDireccionOrmlite.deleteElements(mDireccionOrmlite.selectAll()) > 0)
            Log.e("Borrar  Direccion", "Correcto");
        if (mDispositivoOrmlite.deleteElements(mDispositivoOrmlite.selectAll()) > 0)
            Log.e("Borrar  Dispositivo", "Correcto");
        if (mPersonaOrmlite.deleteElements(mPersonaOrmlite.selectAll()) > 0)
            Log.e("Borrar persona", "Correcto");
        if (mUsuarioOrmLite.deleteElements(mUsuarioOrmLite.selectAll()) > 0)
            Log.e("Borrar usuario", "Correcto");
        if (mUniversidadOrmlite.deleteElements(mUniversidadOrmlite.selectAll()) > 0)
            Log.e("Borrar Universidad", "Correcto");
        if (mVentasPaquetesController.deleteElements(mVentasPaquetesController.selectAll()) > 0)
            Log.e("Borrar Paquetes", "Correcto");
        return true;
    }

    public boolean validateInfoPersona() {
        List<Persona> personaList = getPersona();
        if (personaList.get(0).nombre.equals("temp")) {
            return false;
        } else {
            return true;
        }
    }

    public Persona getDatosPersona() {
        if (getPersona() != null && getPersona().size() > 0) {
            Persona temp = getPersona().get(0);
            if (temp != null) {
                temp.direccion = getDireccionPersona(temp.id_direccion);
                temp.dispositivosList = getDispositivo();
            }
            return temp;
        } else
            return null;
    }

    public Usuario getusuarioPersona() {
        return (mUsuarioOrmLite.selectAll() != null && mUsuarioOrmLite.selectAll().size() > 0 ? mUsuarioOrmLite.selectAll().get(0) : null);
    }

    public boolean VerificarCompraPaquete() {
        List<VentasPaquetes> ventasPaquetes = mVentasPaquetesController.selectAll();
        return (ventasPaquetes != null && ventasPaquetes.size() > 0);
    }

    public boolean SavePaquete(VentasPaquetes mVentasPaquetes) {
        return (mVentasPaquetesController.addElement(mVentasPaquetes) > 0);

    }

    public boolean updatePaquete(VentasPaquetes mVentasPaquetes) {
        if (mVentasPaquetesController.addElement(mVentasPaquetes) > 0) {
            return true;
        } else {
            mVentasPaquetesController.deleteElements(mVentasPaquetesController.selectAll());
            return (mVentasPaquetesController.addElement(mVentasPaquetes) > 0);
        }
    }

    public VentasPaquetes getVentaPaquete() {
        List<VentasPaquetes> mVentasPaquetes = mVentasPaquetesController.selectAll();
        return (mVentasPaquetes != null && mVentasPaquetes.size() > 0 ? mVentasPaquetes.get(0) : null);
    }

    public boolean deletePaquetes() {
        return (mVentasPaquetesController.deleteElements(mVentasPaquetesController.selectAll()) > 0);
    }

    public boolean addPaquetes(VentasPaquetes mVentasPaquetes) {
        return (mVentasPaquetesController.addElement(mVentasPaquetes) > 0);
    }

    public Persona getPersonaUsuario() {
        return (mPersonaOrmlite.selectAll() != null && mPersonaOrmlite.selectAll().size() > 0 ? mPersonaOrmlite.selectAll().get(0) : null);
    }

    public Universidad getuniversidadPersona() {
        return (mUniversidadOrmlite.selectAll() != null && mUniversidadOrmlite.selectAll().size() > 0 ? mUniversidadOrmlite.selectAll().get(0) : null);

    }

    public Direccion getDireccionUniversidad(int id) {
        return (mDireccionOrmlite.selectById(id));
    }

    public Direccion getDireccionPersona(int id) {
        return (mDireccionOrmlite.selectById(id));
    }

    public boolean updateDatosUniversidad(Universidad mUniversidad) {
        if (mUniversidadOrmlite.updateElement(mUniversidad) > 0) {
            Log.e("Datos Universidad", "Actualizado correctamente");
            if (mUniversidad.mDireccion != null) {
                if (mDireccionOrmlite.updateElement(mUniversidad.mDireccion) > 0) {
                    Log.e("Datos direccion", "Actualizado correctamente");
                } else {
                    if (mDireccionOrmlite.addElement(mUniversidad.mDireccion) > 0) {
                        Log.e("Datos direccion", "Agregado correctamente");
                    } else {
                        Log.e("Datos direccion", "Ocurrio un error al guardar");
                    }
                }
            } else {
                Log.e("Datos direccion", "No se encontraron datos");
            }
            if (mUniversidad.mPersona != null) {
                updateDatosPersona(mUniversidad.mPersona);
            }
        } else {
            mUniversidadOrmlite.deleteElements(mUniversidadOrmlite.selectAll());
            if (mUniversidadOrmlite.addElement(mUniversidad) > 0) {
                Log.e("Datos universidad", "Agregado nuevo");
                if (mUniversidad.mDireccion != null) {
                    if (mDireccionOrmlite.updateElement(mUniversidad.mDireccion) > 0) {
                        Log.e("Datos direccion", "Actualizado correctamente");
                    } else {
                        if (mDireccionOrmlite.addElement(mUniversidad.mDireccion) > 0) {
                            Log.e("Datos direccion", "Agregado correctamente");
                        } else {
                            Log.e("Datos direccion", "Ocurrio un error al guardar");
                        }
                    }
                } else {
                    Log.e("Datos direccion", "No se encontraron datos");
                }
            } else {
                Log.e("Datos Universidad", "Ocurrio un error al guardar");
            }
            if (mUniversidad.mPersona != null) {
                updateDatosPersona(mUniversidad.mPersona);
            }
        }
        return true;
    }

    public boolean ExisteSesionUniversiTario() {
        return (mPersonaOrmlite.selectAll() != null && mPersonaOrmlite.selectAll().size() > 0);
    }

    public boolean ExisteSesionUniversidad() {
        List<Usuario> tempuser = mUsuarioOrmLite.selectAll();
        List<Persona> tempPersona = mPersonaOrmlite.selectAll();
        return (tempuser != null && tempuser.size() > 0 && tempPersona != null && tempPersona.size() > 0 ? true : false);
    }

    public boolean saveAllInfoUser(Usuario mUsuario) {
        int count = 0;
        this.clearAllTables();
        if (mUsuarioOrmLite.addElement(mUsuario) > 0)
            Log.e("info Usuario", "Agregado Correctamente");
        else Log.e("info Usuario", "Ocurrio un error al agregar");

        if (mUsuario.persona != null) {
            if (mUsuario.persona.direccion != null) {
                if (mUsuario.persona.id_direccion == 0) {
                    mUsuario.persona.id_direccion = mUsuario.persona.direccion.id;
                }
            }
            if (mPersonaOrmlite.addElement(mUsuario.persona) > 0)
                Log.e("info Persona", "Agregado Correctamente");
            else Log.e("info Persona", "Ocurrio un error al agregar");

            if (mUsuario.persona.direccion != null) {
                if (mDireccionOrmlite.addElement(mUsuario.persona.direccion) > 0)
                    Log.e("info Dire Persona", "Agregado Correctamente");
                else Log.e("info Dire Persona", "Ocurrio un error al agregar");
            } else {
                Log.e("info Dire Persona", "No cuenta con direccion");
            }
        } else {
            Log.e("info Persona", "No cuenta con info Persona");
        }
        if (mUsuario.persona.dispositivosList != null && mUsuario.persona.dispositivosList.size() > 0) {
            if (mDispositivoOrmlite.addElement(mUsuario.persona.dispositivosList.get(0)) > 0)
                Log.e("info Dispositivo", "Agregado Correctamente");
            else Log.e("info Dispositivo", "Ocurrio un error al agregar");
        }
        if (mUsuario.persona.universidad != null && mUsuario.persona.universidad.size() > 0) {
            this.updateDatosUniversidad(mUsuario.persona.universidad.get(0));
        } else {
            Log.e("info Universidad", "No cuenta con Informacion de universidad");
        }
        return true;
    }

    public void updateDatosPersona(Persona mPersona) {
        if (mPersonaOrmlite.updateElement(mPersona) > 0) {
            Log.e("info Persona", "Actualizado Correctamente");
            if (mPersona.direccion != null) {
                if (mDireccionOrmlite.updateElement(mPersona.direccion) > 0) {
                    Log.e("Datos direccion P", "Actualizado correctamente");
                } else {
                    if (mDireccionOrmlite.addElement(mPersona.direccion) > 0) {
                        Log.e("Datos direccion P", "Agregado correctamente");
                    } else {
                        Log.e("Datos direccion P", "Ocurrio un error al guardar");
                    }
                }
            } else {
                Log.e("Datos direccion", "No se encontraron datos");
            }
        } else {
            mPersonaOrmlite.deleteElements(mPersonaOrmlite.selectAll());
            if (mPersonaOrmlite.addElement(mPersona) > 0) {
                Log.e("info Persona", "Agregado Correctamente");
                if (mPersona.direccion != null) {
                    if (mDireccionOrmlite.updateElement(mPersona.direccion) > 0) {
                        Log.e("Datos direccion P", "Actualizado correctamente");
                    } else {
                        if (mDireccionOrmlite.addElement(mPersona.direccion) > 0) {
                            Log.e("Datos direccion P", "Agregado correctamente");
                        } else {
                            Log.e("Datos direccion P", "Ocurrio un error al guardar");
                        }
                    }
                } else {
                    Log.e("Datos direccion", "No se encontraron datos");
                }
            } else {
                Log.e("info Persona", "Ocurrio un error al agregar");
            }
        }
    }

    public Configuraciones getConfiguraciones() {
        return (mConfiguracionesOrmlite.selectAll() != null && mConfiguracionesOrmlite.selectAll().size() > 0 ? mConfiguracionesOrmlite.selectAll().get(0) : null);
    }

    public boolean saveConfiguraciones(Configuraciones mConfiguraciones) {
        if (mConfiguracionesOrmlite.updateElement(mConfiguraciones) > 0) {
            Log.e("Config", "update");
        } else {
            mConfiguracionesOrmlite.deleteElements(mConfiguracionesOrmlite.selectAll());
            mConfiguracionesOrmlite.addElement(mConfiguraciones);
            Log.e("Config", "save");
        }
        return true;
    }
}
