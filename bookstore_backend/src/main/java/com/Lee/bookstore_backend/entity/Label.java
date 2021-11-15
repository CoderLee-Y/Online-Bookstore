package com.Lee.bookstore_backend.entity;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Label {

  @Id
  private String name;

  @Relationship(type = "relation")
  public Set<Label> alsoLike;

  public void likeLink(Label new_label){
    if(alsoLike == null){
      alsoLike = new HashSet<>();
    }
    alsoLike.add(new_label);
  }

  public Label(String name) {
    this.name = name;
  }

  public Label() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Label{" +
        "name='" + name + '\'' +
        ", alsoLike=" + alsoLike +
        '}';
  }
}
