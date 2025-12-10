package com.dawn.backend.repository;

import com.dawn.backend.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Page<Theater> findAll(Pageable pageable);
    //  Find theaters by location (city, area, etc)
    Page<Theater> findByLocationContainingIgnoreCase(String location, Pageable pageable);

}
