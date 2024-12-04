package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood_log")
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private long createdAt;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Mood getMood() {
        return mood;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoodLog moodLog = (MoodLog) o;
        return createdAt == moodLog.createdAt && Objects.equals(id, moodLog.id) && Objects.equals(user, moodLog.user) && Objects.equals(mood, moodLog.mood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, mood, createdAt);
    }
}