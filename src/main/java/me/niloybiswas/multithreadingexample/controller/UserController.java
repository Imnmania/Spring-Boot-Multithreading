package me.niloybiswas.multithreadingexample.controller;

import lombok.RequiredArgsConstructor;
import me.niloybiswas.multithreadingexample.model.User;
import me.niloybiswas.multithreadingexample.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {"application/json"})
    public ResponseEntity<?> saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Success"));
    }

    /*@GetMapping("/users")
    public CompletableFuture<?> findAllUsers() throws ExecutionException, InterruptedException {
        List<User> result =  userService.findAllUsers().get();
        return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }*/

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers() throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers().get());
    }

    @GetMapping("usersByMultiThread")
    public ResponseEntity<?> getUsersByMultiThread() throws ExecutionException, InterruptedException {
        CompletableFuture<List<User>> users1 = userService.findAllUsers();
        CompletableFuture<List<User>> users2 = userService.findAllUsers();
        CompletableFuture<List<User>> users3 = userService.findAllUsers();
//        CompletableFuture.allOf(users1, users2, users3);
        List<User> resultUsers1 = users1.get();
        List<User> resultUsers2 = users2.get();
        List<User> resultUsers3 = users3.get();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
