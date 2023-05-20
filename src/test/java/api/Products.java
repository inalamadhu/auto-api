package api;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class Products {


    @BeforeMethod
    public void beforeMethod(){
        RestAssured.baseURI = "http://localhost:3030";
    }

    @Test (enabled = true, groups="jsonfilter")
    public void getProducts(){
               ExtractableResponse extRes =  given()
                    .header("content-type", "application/json")
                .when()
                    .get("/products")
                .then().extract();

               Map<String, ?> list = extRes.path("data.find{it.manufacturer == 'Duracell' && it.model=='MN2400B4Z'}");
               // extRes.path("data.find{it.manufacturer == 'Duracell' && it.model=='MN2400B4Z'}.categories.find{it.name=='Household Batteries'}")
        System.out.println(list);



    }

    @Test(enabled=false)
    public void getProducts2(){
        ExtractableResponse extRes =  given()
                .header("content-type", "application/json")
                .when()
                .get("/products")
                .then().extract();

        List<Map<String, ?>> list = JsonPath.read(extRes.asString(), "$.data[?(@.manufacturer == 'Duracell')]");
           // JsonPath.read(extRes.asString(), "$.data[?(@.manufacturer == 'Duracell' && @.model=='MN2400B4Z')].categories[?(@.name=='Alkaline Batteries')]")
        System.out.println(list);

    }

    @Test(enabled=false, groups="jsonfilter")
    public void getProducts3(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("","");
        RequestSpecification spec = builder.build();

                given()
                      .header("content-type", "application/json")
                .when()
                .get("/products")
                .then().body("data.find{it.manufacturer == 'Duracell' && it.model=='MN2400B4Z'}", hasKey("shipping1"));



    }



}
