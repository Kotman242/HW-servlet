package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
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
        return storage.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        return storage.entrySet().stream()
                .filter(entry -> entry.getKey() == id)
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public Post save(Post post) {
        while (storage.containsKey(counter.incrementAndGet())) ;
        Post post1 = new Post(counter.get(), post.getContent());
        storage.put(counter.get(), post1);
        return post1;
    }

    public void removeById(long id) {
        storage.remove(id);
    }

    public boolean contain(long id) {
        return storage.containsKey(id);
    }

    public Post changePost(Post post){
        Post newPost = new Post(post.getId(), post.getContent());
        storage.put(newPost.getId(), newPost);
        return newPost;
    }
}