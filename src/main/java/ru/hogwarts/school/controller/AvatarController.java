package ru.hogwarts.school.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(
            value = "/{studentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public long uploadAvatar(
            @PathVariable long studentId,
            @RequestParam MultipartFile file
    )
            throws IOException {
        return avatarService.uploadAvatar(studentId, file);
    }
}
