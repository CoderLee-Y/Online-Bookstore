package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.Label;
import java.util.List;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface BookLabelRepository extends ReactiveNeo4jRepository<Label, String> {

  List<Label> findByName(String name);
}
