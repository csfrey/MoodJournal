package com.csfrey.moodjournal.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    Mood findById(String id);

    List<Mood> findAllByUserId(String id);
}