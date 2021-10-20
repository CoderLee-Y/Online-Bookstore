package com.Lee.bookstore_backend;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class BookstoreBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookstoreBackendApplication.class, args);
  }
  @Bean
  public TomcatServletWebServerFactory servletContainerFactory() {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
      @Override
      protected void postProcessContext(Context context) {
        //设置安全性约束
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");
        //设置约束条件
        SecurityCollection collection = new SecurityCollection();
        //拦截所有请求
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
      }
    };
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    connector.setScheme("http");
    //Connector监听的http的端口号
    connector.setPort(8080);
    connector.setSecure(true);
    //监听到http的端口号后转向到的https的端口号
    connector.setRedirectPort(8081);

    tomcat.addAdditionalTomcatConnectors(connector);
    return tomcat;
  }
}
