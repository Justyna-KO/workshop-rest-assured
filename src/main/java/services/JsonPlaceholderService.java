package services;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class JsonPlaceholderService {
    public Response getPost () {
        return  given()
                .accept(JSON)
                .when()
                .get("/posts")
                .then()
                .extract()
                .response();

            }

    public Response getPostById(int id) {
        return given()
                .pathParams("id", id)
                .header("Accept", "application/json")
                .when()
                .get("/posts/{id}")
                .then()
                .extract()
                .response();
    }

    public Response createPost() {
        return given()
                .accept(JSON)
                .contentType(JSON)
                .when()
                .post("/posts")
                .then()
                .extract()
                .response();
    }

}
