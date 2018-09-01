package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.NivelAcademico;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NivelAcademicoResponse extends BaseResponse {
    @SerializedName("Data")
    public List<NivelAcademico> mNivelAcademicosList;
}
