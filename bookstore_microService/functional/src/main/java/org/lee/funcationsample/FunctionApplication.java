package org.lee.funcationsample;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class FunctionApplication {

  @Bean
  public Function<Flux<List<List<Double>>>, Flux<Double>> computeSum() {
    AtomicReference<Double> ans = new AtomicReference<>(0.0);
    return (flux) -> {
      List<Double> books = flux.elementAt(0).blockOptional().orElseThrow(IndexOutOfBoundsException::new).get(0);
      List<Double> price = flux.elementAt(0).blockOptional().orElseThrow(IndexOutOfBoundsException::new).get(1);
      int n = books.size();
      for (int i = 0; i < n; ++i) {
        int finalI = i;
        ans.updateAndGet(v -> v + books.get(finalI) * price.get(finalI));
      }
      Double d = (double) (Math.round(ans.get() * 100) / 100);
      return Flux.just(d);
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(FunctionApplication.class, args);
  }

}
