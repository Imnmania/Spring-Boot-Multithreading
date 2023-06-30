package me.niloybiswas.multithreadingexample.repository;

import me.niloybiswas.multithreadingexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
