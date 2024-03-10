package com.raf.learning.repository;

import com.raf.learning.model.Assignment;
import org.springframework.data.repository.ListCrudRepository;

public interface AssignmentsRepository extends ListCrudRepository<Assignment, Long> {
}
