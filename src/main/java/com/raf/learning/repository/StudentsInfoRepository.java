package com.raf.learning.repository;

import com.raf.learning.model.StudentInfo;
import org.springframework.data.repository.ListCrudRepository;

public interface StudentsInfoRepository extends ListCrudRepository<StudentInfo, String> {
}
