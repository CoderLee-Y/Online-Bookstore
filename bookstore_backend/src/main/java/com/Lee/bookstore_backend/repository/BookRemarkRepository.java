package com.Lee.bookstore_backend.repository;

import com.Lee.bookstore_backend.entity.BookRemark;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRemarkRepository extends MongoRepository<BookRemark, Integer> {

  BookRemark findDistinctByBookId(Long BookId);


}
