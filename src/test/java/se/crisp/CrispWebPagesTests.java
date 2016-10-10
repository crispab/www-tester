package se.crisp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class CrispWebPagesTests {

  public static final int DEFAULT_PORT = 80;
  public static final String DEFAULT_BASE_HOST = "http://www.crisp.se";

  @BeforeClass
  public static void setup() {
    String port = System.getProperty("server.port");
    if (port == null) {
      RestAssured.port = Integer.valueOf(DEFAULT_PORT);
    } else {
      RestAssured.port = Integer.valueOf(port);
    }

    String baseHost = System.getProperty("server.host");
    if (baseHost == null) {
      baseHost = DEFAULT_BASE_HOST;
    }
    RestAssured.baseURI = baseHost;
  }

  @Test
  public void startPage() {
    final String blogPostPath = "html.body.div.div.div.div.div.div.find { it.@class == 'blog-post' }";
    final String youTubePath = "**.findAll { it.@class == 'yt-item' }[0]";
    given()
      .when()
        .get("/")
      .then()
        .statusCode(200)
        .body("html.head.title", containsString("Get Agile With Crisp"))
        .body(blogPostPath, not(empty()))
        .body(youTubePath, not(empty()));
  }

  @Test
  public void allPagesLoadOK() {
    final List<String> pages = Arrays.asList("/", "/kurser", "/konsulter", "/konsulter/coacher", "/konsulter/utvecklare",
        "/konsulter/ux", "/det-vi-gor", "om-crisp", "/kontakt", "/bocker-och-produkter",
        "/konsulter/max-wenzin", "/kurser/tidigare", "/kurser/personas/utvecklare", "/?s=wenzin test");
    for (String page : pages) {
      when().get(page).then().statusCode(200);
    }
  }

}
