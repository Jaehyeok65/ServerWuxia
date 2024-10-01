package net.skhu.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.coobird.thumbnailator.Thumbnails;
import net.skhu.model.Image;

@RestController
public class ImageController {

    @PostMapping("/resize-image")
    public byte[] resizeImageFromUrl(@RequestBody Image imageUrl) throws IOException {
        // 이미지 URL로부터 BufferedImage를 읽어옵니다
    	System.out.println(imageUrl.getImageUrl());
        BufferedImage image = ImageIO.read(new URL(imageUrl.getImageUrl()));


        // BufferedImage를 ByteArrayInputStream으로 변환
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStream.toByteArray());

        // 이미지 리사이징
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(byteArrayInputStream)
                  .size(300, 300)
                  .outputFormat("WEBP")
                  .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
}
