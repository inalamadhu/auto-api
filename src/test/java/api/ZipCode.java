package api;

import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;

import java.util.Scanner;

import static io.restassured.RestAssured.*;

public class ZipCode {
//90210
    public static void main(String[] args) {
        Scanner scr = new Scanner(System.in);
        String zipCode = scr.nextLine();
        baseURI = "http://api.zippopotam.us";
       ExtractableResponse ext = given()
            .pathParams("country", "us")
            .pathParams("zipcode", zipCode)
        .when()
            .get("{country}/{zipcode}")
                    .then()
                .extract();
       String placeName = ext.path("places[0].'place name'");
       //JsonPath.read(ext.response().body().asString(), "places[0].['place name']")

        System.out.println("City name for the zipcode "+ zipCode+" is "+placeName);



    }


}
