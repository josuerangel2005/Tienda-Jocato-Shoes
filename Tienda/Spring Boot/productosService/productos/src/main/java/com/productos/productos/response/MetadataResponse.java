package com.productos.productos.response;

import java.text.SimpleDateFormat;
import java.util.*;

public class MetadataResponse {

    private List<HashMap<String,String>> metadata = new ArrayList<>();

    public List<HashMap<String, String>> getMetadata() {
        return this.metadata;
    }

    public void setMetadata(String mensaje, Date date, String code) {
        HashMap<String,String> tempMetadata = new HashMap<>();

        tempMetadata.put("Mensaje: ",mensaje);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(date);


        tempMetadata.put("Date: " ,dateString);
        tempMetadata.put("Code: " , code);

        this.metadata.add(tempMetadata);
    }
}
