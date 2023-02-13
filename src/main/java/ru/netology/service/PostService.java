package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.List;

public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        if(repository.contain(id)) return repository.getById(id).orElseThrow(NotFoundException::new);
        else throw new NotFoundException();
    }

    public Post save(Post post) {
        if(post.getId()==0) return repository.save(post);
        return repository.changePost(post);
    }

    public void removeById(long id) {
        if (repository.contain(id)) repository.removeById(id);
            else throw new NotFoundException();
    }
}
