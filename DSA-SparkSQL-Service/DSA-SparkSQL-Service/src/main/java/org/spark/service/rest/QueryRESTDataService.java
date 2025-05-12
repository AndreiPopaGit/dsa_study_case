package org.spark.service.rest;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

/*
SELECT java_method('org.spark.service.rest.QueryRESTDataService', 'pingService')
 */
public class QueryRESTDataService {
    private static Logger logger = Logger.getLogger(QueryRESTDataService.class.getName());

    public static String pingService(){
        return "Pinged Spark SQL Thrift Server!";
    }

    public static String getRESTDataDocument(String httpURL){
        RestTemplate restTemplate = new RestTemplate();
        //
        logger.info("DEBUG: getRESTDataDocument ...");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        //
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                httpURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        String viewList = responseEntity.getBody();
        return viewList;
    }
}
/*
https://www.baeldung.com/spark-framework-rest-api

*/