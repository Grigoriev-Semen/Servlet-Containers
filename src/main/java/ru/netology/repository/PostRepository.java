package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private static final AtomicLong counter = new AtomicLong(0);
    private static final Map<Long, Post> repository = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(repository.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(repository.getOrDefault(id, null));
    }

    public Optional<Post> save(Post post) {
        return Optional.ofNullable(post)
                .filter((v) -> v.getId() == 0)
                .map((v) -> {
                    post.setId(counter.addAndGet(1));
                    repository.put(counter.get(), post);
                    return v;
                }).or(() -> Optional.ofNullable(repository.computeIfPresent(post.getId(), (k, o) -> post)));
    }

    public boolean removeById(long id) {
        return repository.remove(id) != null;
    }
}
