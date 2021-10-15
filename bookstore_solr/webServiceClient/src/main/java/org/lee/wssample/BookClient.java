
package org.lee.wssample;

import org.org.lee.introduction.GetSearchRequest;
import org.org.lee.introduction.GetSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;


public class BookClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(BookClient.class);

	public GetSearchResponse getBook(String Book) {

		GetSearchRequest request = new GetSearchRequest();
		request.setText("Java");
		log.info("Requesting location for " + Book);

		GetSearchResponse response = (GetSearchResponse) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8080/ws", request,
						new SoapActionCallback(
								"http://spring.io/guides/gs-producing-web-service/GetBookRequest"));

		return response;
	}

}
