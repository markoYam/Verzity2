package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Licenciaturas;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LicenciaturasResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Licenciaturas> mLicenciaturasList;
}
