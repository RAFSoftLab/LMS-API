package com.raf.learning.repository;

import com.raf.learning.model.Test;
import org.springframework.data.repository.ListCrudRepository;

public interface TestsRepository extends ListCrudRepository<Test, Long> {
}
