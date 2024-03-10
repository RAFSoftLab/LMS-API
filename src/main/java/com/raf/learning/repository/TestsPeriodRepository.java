package com.raf.learning.repository;

import com.raf.learning.model.TestPeriod;
import org.springframework.data.repository.ListCrudRepository;

public interface TestsPeriodRepository extends ListCrudRepository<TestPeriod, Long> {
}
