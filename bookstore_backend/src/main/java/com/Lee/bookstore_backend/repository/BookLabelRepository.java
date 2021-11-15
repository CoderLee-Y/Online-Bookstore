package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.Label;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

@Transactional
public interface BookLabelRepository extends Neo4jRepository<Label, String> {

  @Query(value = "match (label1:label{name:$labelName})--(r:relation)-->(label2:label) return label2")
  List<Label> findLabelsByName(String name);
}
