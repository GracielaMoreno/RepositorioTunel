package Service;

import Modelo.Imagen;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {

    //public static final String URL="http://192.168.100.109:3000/";
    public static final String URL ="http://200.25.207.130:3000/";
    public static final String URLPlaceToPay="https://test.placetopay.ec/redirection/";

    @Multipart
    @POST("/imagenSoliTag/upload-image")
    Call<Imagen> agregarImagenSolicitud(@Part MultipartBody.Part image);

    @Multipart
    @POST("/imagenSolicitudCambio/upload-image")
    Call<Imagen> agregarImagenCambio(@Part MultipartBody.Part image);



}
