package com.raf.learning.repository;

import com.raf.learning.model.TaskSubmissionInfo;
import org.springframework.data.repository.ListCrudRepository;

public interface TaskSubmissionsInfoRepository extends ListCrudRepository<TaskSubmissionInfo, String> {
}
