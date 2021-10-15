package com.Lee.bookstore_backend.microService.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "searchMicroService", configuration = FeignAutoConfiguration.class)
public interface FeignClientForSearch {

  @GetMapping("/searchAuthorByName")
  List<String> getAuthorByName(@RequestParam("bookName") String bookName);
}
