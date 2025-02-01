package com.example.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.data.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
