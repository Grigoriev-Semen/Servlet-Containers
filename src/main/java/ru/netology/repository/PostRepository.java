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
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        } else {
            return Optional.empty();
        }
    }

    public Post save(Post post) {
        long id = post.getId();

        if (id > counter.get() || id < 0) {
            return null;
        }

        if (id == 0) {
            id = counter.addAndGet(1);
            post.setId(id);
            repository.put(id, post);
        } else {
            repository.replace(id, post);
        }
        return repository.get(id);
    }

    public void removeById(long id) {
        repository.remove(id);
    }
}
