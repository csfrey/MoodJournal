package com.csfrey.moodjournal.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.csfrey.moodjournal.model.Mood;
import com.csfrey.moodjournal.model.MoodRepository;
import com.csfrey.moodjournal.model.User;
import com.csfrey.moodjournal.model.UserRepository;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
class MoodController {

    private final Logger log = LoggerFactory.getLogger(MoodController.class);
    private MoodRepository moodRepository;
    private UserRepository userRepository;

    public MoodController(MoodRepository moodRepository, UserRepository userRepository) {
        this.moodRepository = moodRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/moods")
    Collection<Mood> moods(Principal principal) {
        return moodRepository.findAllByUserId(principal.getName());
    }

    @GetMapping("/mood/{id}")
    ResponseEntity<?> getMood(@PathVariable Long id) {
        Optional<Mood> mood = moodRepository.findById(id);
        return mood.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/mood")
    ResponseEntity<Mood> createMood(@Valid @RequestBody Mood mood,
                                      @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException {
        log.info("Request to create mood: {}", mood);
        Map<String, Object> details = principal.getAttributes();
        String userId = details.get("sub").toString();
 
        // check to see if user already exists
        Optional<User> user = userRepository.findById(userId);
        mood.setUser(user.orElse(new User(userId,
                        details.get("name").toString(), details.get("email").toString())));

        Mood result = moodRepository.save(mood);
        return ResponseEntity.created(new URI("/api/mood/" + result.getId()))
                .body(result);
    }

    @PutMapping("/mood/{id}")
    ResponseEntity<Mood> updateMood(@Valid @RequestBody Mood mood) {
        log.info("Request to update mood: {}", mood);
        Mood result = moodRepository.save(mood);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/mood/{id}")
    public ResponseEntity<?> deleteMood(@PathVariable Long id) {
        log.info("Request to delete mood: {}", id);
        moodRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}