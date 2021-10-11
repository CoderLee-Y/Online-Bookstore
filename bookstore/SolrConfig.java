package com.Lee.bookstore_backend.fullTextSearch;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(
    repositoryBaseClass = com.Lee.bookstore_backend.repository.SolrIntroductionRepository.class,
//    schemaCreationSupport = true,
    namedQueriesLocation = "classpath:solr-named-queries.properties")
@ComponentScan
public class SolrConfig {

  @Value("spring.data.solr.host")
  String solrURL;

  @Bean
  public SolrClient solrClient() {
    return new HttpSolrClient.Builder(solrURL).build();
  }

  @Bean
  public SolrOperations solrTemplate() {
    return new SolrTemplate(solrClient());
  }

}
