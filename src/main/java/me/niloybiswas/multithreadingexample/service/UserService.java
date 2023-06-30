package me.niloybiswas.multithreadingexample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.niloybiswas.multithreadingexample.model.User;
import me.niloybiswas.multithreadingexample.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws IOException {
        final long start = System.currentTimeMillis();
        List<User> users = parseCSV(file);
        log.info("saving list of users of size {} => {} ", Thread.currentThread().getName(), users.size());
        users = userRepository.saveAll(users);
        final long end = System.currentTimeMillis();
        log.info("Execution Time => {}", (end-start));
        return CompletableFuture.completedFuture(users);
    }

    /*@Async
    public CompletableFuture<List<User>> findAllUsers() {
        log.info("{} => getting list of users...", Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }*/

    @Async
    public CompletableFuture<List<User>> findAllUsers() {
        log.info("{} => getting list of users...", Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    /**
     * Utility method for parsing CSV
     * @param file
     * @return
     * @throws IOException
     */
    private List<User> parseCSV(MultipartFile file) throws IOException {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = User.builder()
                            .name(data[0])
                            .email(data[1])
                            .gender(data[2])
                            .build();
                    users.add(user);
                }
                return users;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

}
