package TestsApi;

import ClientApi.UserData;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;

@Slf4j
public class UsersTests {

    private static final String BASE_URI = "https://gorest.co.in/public/v2";

    @Test
    void checkGetAllUsers(){
                log.info("START : getAllUsers");
        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get("/comments")
                .then()
                .statusCode(HttpStatus.SC_OK);
        log.info("END: getAllUsers");
    }
    @Test
    void checkGetAllUsersEndpoint(){
        log.info("START : getAllUsers");
        Response response = given()
                .baseUri(BASE_URI)
                .when()
                .get("/comments");
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");
        response.then().body(Matchers.not(Matchers.empty()));
        response.then().header("Content-type","application/json; charset=utf-8");

        log.info("END: getAllUsers");
    }
    @Test
    void checkGetAllUsersEndpointJsonPath(){
        log.info("START : getAllUsers");

        Response response = given()
                .baseUri(BASE_URI)
                .when()
                .get("/comments");
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().statusLine("HTTP/1.1 200 OK");
        response.then().body(Matchers.not(Matchers.empty()));
        response.then().header("Content-type","application/json; charset=utf-8");

        List<Object> firstPageClient = response.jsonPath().getList("$");
        Assertions.assertThat(firstPageClient.size()).isEqualTo(20);
        System.out.println();

        String firstClientName = response.jsonPath().getString("name[0]");
        Assertions.assertThat(firstClientName).isEqualTo("Balaaditya Kakkar");
        List<Object> name = response.jsonPath().get("name");
        for (Object nameList : name){
            System.out.println(nameList);
        }

        log.info("END: getAllUsers");
    }
    @Test
    public void testGetForName(){

        List<String> name =
                given()
                        .baseUri(BASE_URI)
                        .when()
                        .param("name", "Chakravarti Chopra")
                        .get("/comments")
                        .then()
                        .extract().path("name");

        for (String s : name) {
            Assertions.assertThat(s).isEqualTo("Chakravarti Chopra");
        }
    }


    @Test
    void checkCreateNewUserEndpoint() {
        String emailGenerated = RandomString.make(5) + "@test.com";

        UserData testClient = new UserData();
        testClient.setPost_id(1232L);
        testClient.setName("Mrs. Amarnath Patel");
        testClient.setEmail(emailGenerated);
        testClient.setBody("Non vel ut. Reprehenderit eum cumque.");


        Response postResponse = given().baseUri(BASE_URI)
                .when()
                .header("Authorization", "Bearer 6a2e66915f5232398603c71eda843f6076c46a853840ec5046ae6b7190db7f36")
                .header("Content-type", ContentType.JSON)
                .body(testClient)
                .post("/comments");

        postResponse.prettyPrint();
        postResponse.then().statusCode(201);


        postResponse.then().body("post_id",Matchers.equalTo(1232));
        postResponse.then().body("name",Matchers.equalTo("Mrs. Amarnath Patel"));
        postResponse.then().body("email",Matchers.equalTo(emailGenerated));
        postResponse.then().body("body",Matchers.equalTo("Non vel ut. Reprehenderit eum cumque."));

        postResponse.then().body("id", Matchers.not(Matchers.empty()));

        Assertions.assertThat(1232L).isEqualTo(testClient.getPost_id());
        Assertions.assertThat("Mrs. Amarnath Patel").isEqualTo(testClient.getName());
        Assertions.assertThat(emailGenerated).isEqualTo(emailGenerated);
        Assertions.assertThat("Non vel ut. Reprehenderit eum cumque.").isEqualTo(testClient.getBody());

        Long actualPost_id = postResponse.jsonPath().getLong("post_id");
        Assertions.assertThat(actualPost_id).isEqualTo(testClient.getPost_id());
        String actualName = postResponse.jsonPath().getString("name");
        Assertions.assertThat(actualName).isEqualTo(testClient.getName());
        String actualEmail = postResponse.jsonPath().getString("email");
        Assertions.assertThat(actualEmail).isEqualTo(testClient.getEmail());
        String actualBody = postResponse.jsonPath().getString("body");
        Assertions.assertThat(actualBody).isEqualTo(testClient.getBody());

    }
    @Test
    void checkUpdateUserEndpoint() {
        String emailGenerated = RandomString.make(5) + "@test.com";
        Long id = 1761L;

        UserData testClient = new UserData();
        testClient.setId(id);
        testClient.setPost_id(1232L);
        testClient.setName("Mrs. Emeli Krich");
        testClient.setEmail(emailGenerated);
        testClient.setBody("Non vel ut.");


        Response postResponse = given().baseUri(BASE_URI)
                .when()
                .header("Authorization", "Bearer 6a2e66915f5232398603c71eda843f6076c46a853840ec5046ae6b7190db7f36")
                .header("Content-type", ContentType.JSON)
                .body(testClient)
                .put(String.format("/comments/%s", id));

        postResponse.prettyPrint();
        postResponse.then().statusCode(200);


        postResponse.then().body("post_id",Matchers.equalTo(1232));
        postResponse.then().body("name",Matchers.equalTo("Mrs. Emeli Krich"));
        postResponse.then().body("email",Matchers.equalTo(emailGenerated));
        postResponse.then().body("body",Matchers.equalTo("Non vel ut."));

        postResponse.then().body("id", Matchers.not(Matchers.empty()));

        Assertions.assertThat(1761L).isEqualTo(testClient.getId());
        Assertions.assertThat(1232L).isEqualTo(testClient.getPost_id());
        Assertions.assertThat("Mrs. Emeli Krich").isEqualTo(testClient.getName());
        Assertions.assertThat(emailGenerated).isEqualTo(emailGenerated);
        Assertions.assertThat("Non vel ut.").isEqualTo(testClient.getBody());
    }

    @Test
    void checkDeleteUserEndpoint() {
        Long id = 1761L;

        Response postResponse = given().baseUri(BASE_URI)
                .when()
                .header("Authorization", "Bearer 6a2e66915f5232398603c71eda843f6076c46a853840ec5046ae6b7190db7f36")
                .header("Content-type", ContentType.JSON)
                .delete(String.format("/comments/%s", id));

        postResponse.prettyPrint();
        postResponse.then().statusCode(204);
    }

}

