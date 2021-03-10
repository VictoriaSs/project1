import groovy.json.JsonToken;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Newsapi {

    public static final String API_KEY = "fc776e3c582a4ef5b05d592aba91b45d";
    public static final String BASE_URI = "https://newsapi.org/";
    public static final String endPoint = "v2/sources";
    public static RequestSpecification spec; //переменная для подготовки первоначального состояния запросов

    @BeforeAll
    static void setUp(){
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .setAccept(ContentType.JSON)
                .addQueryParam("apiKey", API_KEY)
                .build();
    }

    @DisplayName("Тест проверяет что в результате GET-запроса возвращается код 200")
    @Test
    void checkStatusCode(){
          given().spec(spec).queryParam("language","en")
                .when().get(endPoint)
                .then().log().all()
                .statusCode(200); //проверяем код ответа от сервера

    }

    @DisplayName("Тест проверяет корректность contentType")
    @Test
    void checkHeader() {
        given().spec(spec)
                .when().get(endPoint)
                .then().log().all().contentType("application/json; charset=utf-8");
    }

    @DisplayName("Тест проверяет что язык - английский, а источник содержит контент")
    @Test
    void checkFields() {
        given().spec(spec).queryParam("language", "en")
                .when().get(endPoint)
                .then().log().all()
                .body("status", Matchers.equalTo("ok"))
                .and().body("sources", Matchers.notNullValue());
    }
}
