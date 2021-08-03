package dev.webServer.controller;

import dev.webServer.message.ResponseMessage;
import dev.webServer.model.FileInfo;
import dev.webServer.model.Project;
import dev.webServer.service.ConvertService;
import dev.webServer.service.DataStorageService;
import dev.webServer.service.FilesStorageService;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front-end/test")
public class MessageController {
    @Autowired
    FilesStorageService storageService;
    @Autowired
    ConvertService convertService;
    @Autowired
    DataStorageService dataStorageService;

    @GetMapping("/hello")
    public String hello() {
        return "Full Stack Java with Spring Boot and VueJS!";
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", filename).build().toString();
            String ganttImage = url + "/gantt-chart-image";
            String json = url + "/json";

            return new FileInfo(filename, url, ganttImage, json);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<FileInfo> getFile(@PathVariable String filename) throws IOException {
        Resource file = storageService.load(filename);

        String url = MvcUriComponentsBuilder
                .fromMethodName(FilesController.class, "getFile", file.getFilename()).build().toString();
        String ganttImage = url + "/gantt-chart-image";
        String json = url + "/json";

        FileInfo fileInfo = new FileInfo(filename, url, ganttImage, json);
        return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
    }

    @DeleteMapping("files/delete/{filename:.+}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
        String message;
        if (storageService.delete(filename)) {
            message = filename + " deleted! ";
        } else {
            message = "Could not delete this file !";
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }


    @GetMapping("/files/json/{filename:.+}")
    public ResponseEntity<Project> getJsonFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        Project jsonProject = convertService.toJsonProject(file.getFilename());

        if (jsonProject != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jsonProject);
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new Project("Could not read file: " + file.getFilename()));

    }

    @GetMapping(
            value = "/files/gantt-chart-image/{filename:.+}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getGanttImage(@PathVariable String filename) throws IOException {
        Resource file = storageService.load(filename);
        Project jsonProject = convertService.toJsonProject(file.getFilename());

        if (jsonProject != null) {
            convertService.ganttToImage(jsonProject);

            Resource image = dataStorageService.load("gantt.png");
            InputStream in = image.getInputStream();
            return IOUtils.toByteArray(in);
        }

        return null;
    }

}
