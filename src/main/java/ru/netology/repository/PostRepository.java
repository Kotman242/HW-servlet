package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private static PostRepository instance;
    private final AtomicLong counter;
    private final ConcurrentHashMap<Long, Post> storage;

    private PostRepository() {
        storage = new ConcurrentHashMap<>();
        counter = new AtomicLong(0);
    }

    public static PostRepository getInstance() {
        if (instance == null) {
            synchronized (PostRepository.class) {
                if (instance == null) {
                    instance = new PostRepository();
                }
            }
        }
        return instance;
    }

    public List<Post> all() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Post> getById(long id) {
        return storage.entrySet().stream()
                .filter(entry -> entry.getKey() == id)
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == 0) return saveNewPost(post);
        return changePost(post);
    }

    public void removeById(long id) {
        storage.remove(id);
    }

    public boolean contain(long id) {
        return storage.containsKey(id);
    }

    private Post saveNewPost(Post post) {
        final long id = counter.incrementAndGet();
        Post newPost = new Post(id, post.getContent());
        storage.put(id, newPost);
        return newPost;
    }

    private Post changePost(Post post) {
        if (!storage.containsKey(post.getId())) throw new NotFoundException();
        Post newPost = new Post(post.getId(), post.getContent());
        storage.put(post.getId(), newPost);
        return newPost;
    }
}