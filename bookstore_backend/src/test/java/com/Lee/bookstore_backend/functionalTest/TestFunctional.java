package com.Lee.bookstore_backend.functionalTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@AutoConfigureWebTestClient
public class TestFunctional  {

  @Autowired
  private WebTestClient client;

  @Test
  public void words() throws Exception {

    List<Double> a = new ArrayList<>();
    List<Double> b = new ArrayList<>();
    a.add(12.0);
    b.add(14.0);

    List<List<Double>> ret = new ArrayList<>();
    ret.add(a);
    ret.add(b);
    System.out.println(ret.toString());
    FluxExchangeResult<Double> re = client.post().uri("http://localhost:8081/computeSum").
        body(Mono.just(ret.toString()), List.class).exchange()
        .returnResult(Double.class);

    System.out.println(Objects.requireNonNull(re.getResponseBody().blockFirst()).toString());
  }

}
