package com.Lee.bookstore_backend.dto;

import javax.persistence.Id;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

public class FullTextIntroduction  {

  @Field(value = "id")
  private Long bookId;

  @Field(value = "description")
  private String description;

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "FullTextIntroduction{" +
        "bookId=" + bookId +
        ", description='" + description + '\'' +
        '}';
  }
}
