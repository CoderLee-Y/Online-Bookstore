package com.Lee.bookstore_backend.entity;

import java.util.List;
import javax.persistence.Column;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment {

  private String text;

  private int authorId;

  private List<Comment> reply;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getAuthorId() {
    return authorId;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
  }

  public List<Comment> getReply() {
    return reply;
  }

  public void setReply(List<Comment> reply) {
    this.reply = reply;
  }
}
