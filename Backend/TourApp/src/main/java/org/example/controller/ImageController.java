package org.example.controller;

import org.example.entity.Image;
import org.example.model.ParkModel;
import org.example.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/{parkName}")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file, @PathVariable("parkName") String parkName) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        imageService.saveImage(image, file.getBytes(), parkName);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping(value = "/{parkName}")
    public ResponseEntity<ParkModel> getImage(@PathVariable("parkName") String parkName) throws IOException {
        ParkModel image = imageService.getImage(parkName);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping(value = "/sample/{imageName}")
    public ResponseEntity<File> getImage1(@PathVariable("imageName") String name) throws IOException {
        File image = imageService.getImage1(name);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
