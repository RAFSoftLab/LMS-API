package com.raf.learning.repository;

import com.raf.learning.model.Token;
import org.springframework.data.repository.ListCrudRepository;

public interface TokensRepository extends ListCrudRepository<Token, String> {
}
