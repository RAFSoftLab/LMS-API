package com.raf.learning.repository;

import com.raf.learning.model.StudentSubmission;
import org.springframework.data.repository.ListCrudRepository;

public interface StudentsSubmissionRepository extends ListCrudRepository<StudentSubmission, String> {
}
