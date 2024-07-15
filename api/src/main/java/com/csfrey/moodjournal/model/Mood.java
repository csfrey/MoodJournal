package com.csfrey.moodjournal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "moods")
public class Mood {
  @Id
  @GeneratedValue
  private String id;

  private int rating;

  @NonNull
  private String remarks;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private User user;
}
