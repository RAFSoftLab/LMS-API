package com.raf.learning.repository;

import com.raf.learning.model.StudentSubmission;
import org.springframework.data.repository.ListCrudRepository;

public interface StudentSubmissionsRepository extends ListCrudRepository<StudentSubmission, String> {
}
