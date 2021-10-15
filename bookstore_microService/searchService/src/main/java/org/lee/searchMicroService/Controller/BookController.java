package org.lee.searchMicroService.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.lee.searchMicroService.entity.Book;
import org.lee.searchMicroService.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping("/searchBookByAuthor")
    public List<Book> searchByAuthor(@RequestBody Map<String, String> author){
        String auth = author.get("author");
        System.out.println(auth);
        return bookRepository.findAllByAuthorLike(auth);
    }

    @GetMapping("/searchAuthorByName")
    public List<String> searchAuthorByName(@RequestParam("bookName") String a){
        List<Book> books =  bookRepository.findAllByName(a);
        List<String> authors = new ArrayList<>();

        for(Book book1: books){
            authors.add(book1.getAuthor());
        }
        return authors;
    }
}
