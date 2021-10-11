package com.Lee.bookstore_backend.webService;

import com.Lee.bookstore_backend.dao.BookDao;
import com.Lee.bookstore_backend.dto.FullTextIntroduction;
import com.Lee.bookstore_backend.entity.Book;
import io.spring.guides.gs_producing_web_service.GetSearchRequest;
import io.spring.guides.gs_producing_web_service.GetSearchResponse;
import java.io.IOException;
import java.util.List;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SearchEndpoint {
	private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

	private BookDao bookDao;

	@Autowired
	public SearchEndpoint(BookDao bookDao) {
		this.bookDao = bookDao;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSearchRequest")
	@ResponsePayload
	public GetSearchResponse getIntroduction(@RequestPayload GetSearchRequest request)
			throws SolrServerException, IOException {
		GetSearchResponse response = new GetSearchResponse();

		List<String> ids = response.getIds();
		List<String> intros = response.getIntros();

		String text = request.getText();
		List<Book> list = bookDao.getFullTextSearch(text);
		for(Book book: list){
			long bookId = book.getBookId();
			ids.add(Long.toString(bookId));
			intros.add(book.getDescription());
		}

		return response;
	}
}
