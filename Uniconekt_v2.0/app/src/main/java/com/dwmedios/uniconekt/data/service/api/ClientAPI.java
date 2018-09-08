package com.dwmedios.uniconekt.data.service.api;


import com.dwmedios.uniconekt.data.service.response.AsesoresResponse;
import com.dwmedios.uniconekt.data.service.response.BannersResponse;
import com.dwmedios.uniconekt.data.service.response.CodigoPostalResponse;
import com.dwmedios.uniconekt.data.service.response.ConfiguracionesResponse;
import com.dwmedios.uniconekt.data.service.response.CuponCanjeadoResponse;
import com.dwmedios.uniconekt.data.service.response.CuponResponse;
import com.dwmedios.uniconekt.data.service.response.DispositivoResponse;
import com.dwmedios.uniconekt.data.service.response.EstadosResponse;
import com.dwmedios.uniconekt.data.service.response.FavoritosResponse;
import com.dwmedios.uniconekt.data.service.response.FinanciamientoResponse;
import com.dwmedios.uniconekt.data.service.response.LicenciaturasResponse;
import com.dwmedios.uniconekt.data.service.response.NivelAcademicoResponse;
import com.dwmedios.uniconekt.data.service.response.NotificacionesResponse;
import com.dwmedios.uniconekt.data.service.response.NotificacionesUniResponse;
import com.dwmedios.uniconekt.data.service.response.PaisesResponse;
import com.dwmedios.uniconekt.data.service.response.PaqueteAsesorResponse;
import com.dwmedios.uniconekt.data.service.response.PaquetesResponse;
import com.dwmedios.uniconekt.data.service.response.PersonaResponse;
import com.dwmedios.uniconekt.data.service.response.PostuladoFinanciamientoResponse;
import com.dwmedios.uniconekt.data.service.response.PostuladoGeneralResponse;
import com.dwmedios.uniconekt.data.service.response.PostuladosGeneralesResponse;
import com.dwmedios.uniconekt.data.service.response.PostuladosUniversidadesResponse;
import com.dwmedios.uniconekt.data.service.response.PostularseBecaResponse;
import com.dwmedios.uniconekt.data.service.response.SearchUniversidadesResponse;
import com.dwmedios.uniconekt.data.service.response.TipoCategoriaResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadDetalleResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadResponse;
import com.dwmedios.uniconekt.data.service.response.UsuarioResponse;
import com.dwmedios.uniconekt.data.service.response.VentaPaqueteAsesorResponse;
import com.dwmedios.uniconekt.data.service.response.VentasPaquetesResponse;
import com.dwmedios.uniconekt.data.service.response.VideoResponse;
import com.dwmedios.uniconekt.data.service.response.VisitasBannersResponse;
import com.dwmedios.uniconekt.data.service.response.becasResponse;
import com.dwmedios.uniconekt.model.VentasPaquetes;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public interface ClientAPI {
    @GET("getTiposCategorias")
    Call<TipoCategoriaResponse> getTiposCategorias();

    @GET("GetBannersVigentes")
    Call<BannersResponse> getBannersVigentes();


    @GET("GetPaises")
    Call<PaisesResponse> GetPaises();

    @GET("GetCuponesVigentes")
    Call<CuponResponse> GetCuponesVigentes();

    @GET("GetPaquetesDisponibles")
    Call<PaquetesResponse> GetPaquetesDisponibles();

    @FormUrlEncoded
    @POST("CrearCuentaAcceso")
    Call<UsuarioResponse> RegisterUser(@Field("json") String json);

    @FormUrlEncoded
    @POST("IngresarApp")
    Call<UsuarioResponse> Login(@Field("json") String json);

    @FormUrlEncoded
    @POST("RecuperarContrasenia")
    Call<UsuarioResponse> ResetPassword(@Field("json") String json);

    @FormUrlEncoded
    @POST("IngresarAppUniversitario")
    Call<DispositivoResponse> LoginUniversitario(@Field("json") String json);

    @FormUrlEncoded
    @POST("BusquedaUniversidades")
    Call<UniversidadResponse> SearchUniversity(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetDetallesUniversidad")
    Call<UniversidadDetalleResponse> GetDetailsUniversity(@Field("json") String json);

    @FormUrlEncoded
    @POST("RegistrarVisitaBanners")
    Call<VisitasBannersResponse> RegisterVisitedBanner(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetVideos")
    Call<VideoResponse> GetVideos(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetBecasVigentes")
    Call<becasResponse> GetBecas(@Field("json") String json);

    @FormUrlEncoded
    @POST("BuscarCodigoPostal")
    Call<CodigoPostalResponse> SearchCodigoPostal(@Field("json") String json);

    @FormUrlEncoded
    @POST("SolicitarFinanciamiento")
    Call<PostuladoFinanciamientoResponse> SolicitarFinanciamiento(@Field("json") String json);

    @FormUrlEncoded
    @POST("PostularseBeca")
    Call<PostularseBecaResponse> PostularseBeca(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetFinanciamientosVigentes")
    Call<FinanciamientoResponse> GetFinanciamientos(@Field("json") String json);

    @FormUrlEncoded
    @POST("SaveVentaPaquete")
    Call<VentasPaquetesResponse> RegistrarVentaPaquete(@Field("json") String json);

    @FormUrlEncoded
    @POST("SetFavorito")
    Call<FavoritosResponse> SetFavorito(@Field("json") String json);

    @FormUrlEncoded
    @POST("VerificarFavorito")
    Call<FavoritosResponse> VerificarFavorito(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetFavoritos")
    Call<UniversidadResponse> getFavoritos(@Field("json") String json, @Field("extranjero") boolean extranjero);

    @FormUrlEncoded
    @POST("RegistrarUniversidad")
    Call<UniversidadDetalleResponse> RegistrarUniversidad(@Field("json") String json);

    @FormUrlEncoded
    @POST("VerificarEstatusUniversidad")
    Call<UniversidadDetalleResponse> VerificarStatusUniversidad(@Field("json") String json);


    @FormUrlEncoded
    @POST("PostularseUniversidad")
    Call<PostuladosUniversidadesResponse> PostularUniversidad(@Field("json") String json);

    @FormUrlEncoded
    @POST("EditarPerfil")
    Call<PersonaResponse> EditarPerfil(@Field("json") String json);

    /*   @FormUrlEncoded
       @POST("ConsultarNotificaciones")
       Call<NotificacionesResponse> ConsultarNotificaciones(@Field("json") String json);
   */
    @FormUrlEncoded
    @POST("GetDetalleNotificacion")
    Call<PostuladoGeneralResponse> GetDetalleNotificacion(@Field("json") String json);

    @FormUrlEncoded
    @POST("CanjearCupon")
    Call<CuponCanjeadoResponse> CanjearCuppon(@Field("json") String json);

    @FormUrlEncoded
    @POST("ActualizarCuentaUniversitario")
    Call<PersonaResponse> actualizarCuenta(@Field("json") String json);

    @FormUrlEncoded
    @POST("verificarCuentaUniversitario")
    Call<PersonaResponse> verficarCuenta(@Field("json") String json);

    @FormUrlEncoded
    @POST("cerrarSesion")
    Call<DispositivoResponse> cerrarSesion(@Field("json") String json);

    /*---------------------------------------------------------------------------------- */

    @FormUrlEncoded
    @POST("CrearCuentaAccesoUniversitario")
    Call<UsuarioResponse> RegisterUniversitario(@Field("json") String json);

    @FormUrlEncoded
    @POST("RegistrarVisorBanners")
    Call<BannersResponse> RegistrarVisitaBanner(@Field("json") String json);

    @FormUrlEncoded
    @POST("ConsultarNotificaciones")
    Call<NotificacionesUniResponse> ConsultarNotUniversidad(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetEstados")
    Call<EstadosResponse> getEstados(@Field("json") String json);

    @GET("GetNivelesAcademicos")
    Call<NivelAcademicoResponse> getNivelesAcademicos();

    @FormUrlEncoded
    @POST("GetProgramasAcademicos")
    Call<LicenciaturasResponse> GetLicenciaturas(@Field("json") String json);

    @FormUrlEncoded
    @POST("GetPostulados")
    Call<PostuladosGeneralesResponse> GetPostulados(@Field("json") String json, @Field("tipo") int tipo);

    @GET("GetPaquetesAsesoresDisponibles")
    Call<PaqueteAsesorResponse> getPaquetesAsesores();

    @GET("GetAsesores")
    Call<AsesoresResponse> getAsesores();

    @FormUrlEncoded
    @POST("GetMisAsesores")
    Call<AsesoresResponse> getMisAsesores(@Field("json") String json);

    @FormUrlEncoded
    @POST("SaveVentaPaqueteAsesor")
    Call<VentaPaqueteAsesorResponse> saveVentaPaqueteAsesor(@Field("json") String json);

    @FormUrlEncoded
    @POST("ConsultarNotificacionesUniversitario")
    Call<NotificacionesResponse> getNotificacionesUniversitario(@Field("json") String json);

}
