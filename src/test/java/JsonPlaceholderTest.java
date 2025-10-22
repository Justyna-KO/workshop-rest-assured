
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import models.PostRequest;
import models.PostResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import services.JsonPlaceholderService;


import java.util.List;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.path.json.JsonPath.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonPlaceholderTest {

    private static JsonPlaceholderService jsonPlaceholderService;

    @Before
    public static void beforeALL() {
        jsonPlaceholderService = new JsonPlaceholderService();
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter());
    }

    @Test
    public void shouldReturnAllPosts() {
        Response response = jsonPlaceholderService.getPosts();
        Assertions.assertEquals(200, response.statusCode());

    }




    @Test
    public void shouldReturAllPosts() {
        RestAssured
                .given()
                .log().all()
                .header("Accept", "application/json")
                .when()
                .get("/posts")
                .then()
                .header("Content-Type", "application/json; charset=utf-8")
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].id", Matchers.is(1));

    }

    @Test
    public void shouldFetchPostById() {
        RestAssured
                .given()
                .log().all()
                .header("Accept", "application/json")
                .when()
                .get("/posts/1")
                .then()
                .header("Content-Type", "application/json; charset=utf-8")
                .statusCode(200)
                .body("id", is(1))
                .body("userId", is(1))
                .body("title", notNullValue())
                .body("body", notNullValue())
        ;
    }

    @Test
    public void shouldCreateNewPost() {

        String body = "                {\n" +
                "                    \"title\": \"Bar\"\n" +
                "                     \"body\": \"xx\"\n" +
                "                     \"userId\": 1\n" +
                "                 }  ";
        RestAssured
                .given()
                .log().all()
                .header("ContentType", "application/json")
//                .header("Accept", "application/json")
                .body(body)
                .when()
                .post("/posts")
                .then()
                .header("Content-Type", "application/json; charset=utf-8")
                .statusCode(201)
                .body("id", is(101))
        ;
    }
    @Test
    public void shouldUpdatePost() {

        String body = "{\n" +
                "  \"title\": \"Bar\",\n" +
                "  \"body\": \"xx u\",\n" +
                "  \"userId\": 1\n" +
                "}";
        RestAssured
                .given()
                .log().all()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .patch("/posts/1")
                .then()
                .contentType(JSON)
                .statusCode(200)
                .body("id", is(1))
                .body("userId", is(1))
                .body("title", is("Bar"))
                .body("body", is("xx u"))
        ;
    }
    @Test
    public void shouldPatchPost() {

        String body = "                {\n" +
                "                    \"title\": \"Bar pacz\"\n" +
                "                 }";


            RestAssured
                    .given()
            .log().all()
            .contentType(JSON)
            .accept(JSON)
            .body(body)
            .when()
            .patch("/posts/1")
            .then()
            .contentType(JSON)
            .statusCode(200)
            .body("id", is(1))
            .body("userId", is(1))
            .body("title", is("Bar pacz"))
    ;
    }

    @Test
    public void shouldDeletePost() {


        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200);

        ;
    }

    @Test
    public void shouldFilterPostsByUserId() {
        RestAssured
                .given()
                .log().all()
                .header("Accept", "application/json")
                .when()
                .get("/posts?userId=1")
                .then()
                .header("Content-Type", "application/json; charset=utf-8")
                .statusCode(200)
                .body("userId", everyItem(equalTo(1)));

    }


    @Test
    public void shouldFetchAllPosts() {
        Response response = jsonPlaceholderService.getPosts();
        List<PostResponse> posts = response.jsonPath().getList("", PostResponse.class);

        assertEquals(200, response.getStatusCode());
        assertFalse(posts.isEmpty());
    }

    @Test
    public void shouldFetchAllPosts() {
        Response response = jsonPlaceholderService.getPosts();
        List<PostResponse> posts = response.jsonPath().getList("",  PostResponse.class);

        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertFalse(posts.isEmpty());
        Assertions.assertEquals(1, posts.get(0).getId());
    }

    @Test
    public void shouldReturnAllPosts() {
        Response response = jsonPlaceholderService.getPosts();
        Assertions.assertEquals(200, response.statusCode());
        List<PostResponse> postResponseList = response.jsonPath().getList("", PostResponse.class);
        Assert.assertFalse(postResponseList.isEmpty());
        Assert.assertEquals(1, postResponseList.get(0).getId());
    }

    @Test
    public void shouldFetchPostById() {
        Response response = jsonPlaceholderService.getPostById(1);
        Assertions.assertEquals(200, response.statusCode());
        PostResponse postResponse = response.as(PostResponse.class);
        Assert.assertNotNull(postResponse);
        Assert.assertEquals(1, postResponse.getId());

    }

    @Test
    public void shouldFetchPostById() {
        Response response = jsonPlaceholderService.getPostById(1);
        Assertions.assertEquals(200, response.statusCode());
        PostResponse postResponse = response.as(PostResponse.class);
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(1, postResponse.getId());
    }

    @Test
    public void shouldFetchPostById2() {
        Response response1 = jsonPlaceholderService.getPosts();
        List<PostResponse> postResponseList = response1.jsonPath().getList("", PostResponse.class);
        Response response = jsonPlaceholderService.getPostById(postResponseList.getFirst().getId());
        Assertions.assertEquals(200, response.statusCode());
        PostResponse postResponse = response.as(PostResponse.class);
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(1, postResponse.getId());
    }
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void shouldFetchPostById3(int id) {
        Response response = jsonPlaceholderService.getPostById(id);
        PostResponse postResponse = response.as(PostResponse.class);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertNotNull(postResponse);
        Assert.assertEquals(1, postResponse.getId());
    }
    public Response createPost(PostRequest postRequest) {
        return given()
                .accept(JSON)
                .body(postRequest.toString())
                .when()
                .post("/posts")
                .then()
                .extract()
                .response();

        Assert.assertEquals(postRequest.getBody(), postResponse.getBody());
        Assert.assertEquals(postRequest.getTitle(), postResponse.getTitle());

    }
}