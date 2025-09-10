package com.productos.imagenes.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageService {

    private final Cloudinary cloudinary;

    public ImageService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
    Map<String, Object> options = ObjectUtils.asMap(
        "resource_type", "auto"  
    );

    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
    return uploadResult.get("secure_url").toString();
}

}
