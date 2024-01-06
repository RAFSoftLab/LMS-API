package com.raf.learning.repository;

import com.raf.learning.model.Student;
import org.springframework.data.repository.ListCrudRepository;

public interface StudentRepository extends ListCrudRepository<Student, Integer> {
}
