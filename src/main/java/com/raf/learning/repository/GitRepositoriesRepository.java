package com.raf.learning.repository;

import com.raf.learning.model.GitRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface GitRepositoriesRepository extends ListCrudRepository<GitRepository, String> {
}
