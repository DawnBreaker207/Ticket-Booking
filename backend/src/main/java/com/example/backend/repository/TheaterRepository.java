package com.example.backend.repository;

import com.example.backend.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    //  Find theaters by location (city, area, etc)
    List<Theater> findByLocationContainingIgnoreCase(String location);
}
