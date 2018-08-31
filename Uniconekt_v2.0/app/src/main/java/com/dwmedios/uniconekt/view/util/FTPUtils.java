package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Created by Luis Cabanas on 10/10/2016.
 */

public class FTPUtils {
    public static final String pathServer = "/";
    public static final String pathServerMultimedia = "/Upload/Tecnicos";
    private String ip = "192.168.100.38";
    private String usuario = "mYam";
    private String contrasena = "45620";

    FTPClient ftpClient;
    BufferedInputStream buffer;
    File rutaSd;
    File rutaCompleta;
    Context context;

    public FTPUtils(Context context) {
        this.context = context;
    }
    // Configuracion mConfiguracionServidor;
    //Configuracion mConfiguracionUsuario;
    //  Configuracion mConfiguracionPassword;

    // private ConfiguracionController mConfiguracionController;
/*
     @Inject
      public FTPUtils(Context context,ConfiguracionController mConfiguracionController) {
          ftpClient = null;
          buffer = null;
          rutaSd = null;
          rutaCompleta = null;
          this.context = context;
        //  this.mConfiguracionController = mConfiguracionController;
      }
*/
   /* public boolean loginFTP() throws IOException {
     /*   mConfiguracionServidor = mConfiguracionController.getConfiguracion(Configuracion.TYPE_FTP_SERVIDOR);
        mConfiguracionUsuario = mConfiguracionController.getConfiguracion(Configuracion.TYPE_FTP_USUARIO);
        mConfiguracionPassword = mConfiguracionController.getConfiguracion(Configuracion.TYPE_FTP_PASSWORD);
        //Establece conexion con el servidor
        //Toast.makeText(context, "Conectando . . .", Toast.LENGTH_SHORT).show();
        try {

         //   ftpClient = new FTPClient();
            //direccion del servidor donde se aloja el servicio ftp...............
            ftpClient.connect(ip);
        } catch (Exception e) {

            //Informa al usuario
            //  Toast.makeText(context, "Imposible conectar . . .", Toast.LENGTH_SHORT).show();
            return false;    //En caso de que no sea posible la conexion
        }


        //Hace login en el servidor
        if (ftpClient.login(usuario, contrasena)) {
            //Informa al usuario
            //Toast.makeText(context, "Login correcto . . .", Toast.LENGTH_SHORT).show();
            return true;    //En caso de login correcto
        } else {

            //Informa al usuario
            //Toast.makeText(context, "Login incorrecto . . .", Toast.LENGTH_SHORT).show();
            return false;    //En caso de login incorrecto
        }

    }


    public boolean uploadImage(String name, String path, String ubicacion) throws IOException {
        /*  mConfiguracionServidor = mConfiguracionController.getConfiguracion(Configuracion.TYPE_FTP_SERVIDOR);
        mConfiguracionUsuario = mConfiguracionController.getConfiguracion(Configuracion.TYPE_FTP_USUARIO);
        mConfiguracionPassword = mConfiguracionController.getConfiguracion(Configuracion.TYPE_FTP_PASSWORD);
        //ftpClient.enterLocalActiveMode();
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //Cambia la carpeta Ftp

        if (ftpClient.changeWorkingDirectory(ubicacion)) {
            rutaCompleta = new File(path);
            //Crea un buffer hacia el servidor de subida
            buffer = new BufferedInputStream(new FileInputStream(rutaCompleta));

            if (ftpClient.storeFile(name, buffer)) {
                buffer.close();        //Cierra el bufer
                return true;        //Se ha subido con exito
            } else {
                buffer.close();        //Cierra el bufer
                return false;        //No se ha subido
            }
        } else {
            return false;        //Imposible cambiar de directo en servidor ftp
        }
    }

    public boolean deleteImage(String name, String ubicacion) throws IOException {
        ftpClient.enterLocalActiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        if (ftpClient.changeWorkingDirectory(ubicacion)) {

            if (ftpClient.deleteFile(name)) {

                //Se ha subido con exito
                Log.e("FTP-Delete_Image", "exito");
                return true;
            } else {
                Log.e("FTP-Delete_Image", "falla");
                return false;        //No se ha subido
            }
        } else {
            Log.e("FTP-Delete_Image", "Imposible cambiar de directorio en servidor ftp");
            return false;        //Imposible cambiar de directo en servidor ftp
        }*/
    }
