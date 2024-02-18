package com.raf.learning.service;

import com.raf.learning.model.GitRepository;
import com.raf.learning.repository.GitRepositoriesRepository;

public class GitRepositoriesService {
    public GitRepositoriesService(GitRepositoriesRepository repository) {
        this.repository = repository;
    }

    private GitRepositoriesRepository repository;

    public void addRepository(String id, String url) {
        repository.save(new GitRepository(id, url));
    }

    public String getRepository(String id) {
        var result = repository.findById(id);
        if(result.isEmpty()) {
            return null;
        }
        return result.get().getUrl();
    }

    public void deleteRepository(String id) {
        repository.deleteById(id);
    }
}
