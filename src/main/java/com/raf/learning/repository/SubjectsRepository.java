package com.raf.learning.repository;

import com.raf.learning.model.Subject;
import org.springframework.data.repository.ListCrudRepository;

public interface SubjectsRepository extends ListCrudRepository<Subject, Long> {
}
