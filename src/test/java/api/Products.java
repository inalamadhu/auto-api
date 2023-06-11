package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Products {

    RequestSpecBuilder specBuilder;
    RequestSpecification requestSpecification;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        RestAssured.baseURI = "http://localhost:3030";
    }

    @Test (enabled = false, groups="jsonfilter")
    public void getProducts(){
        RequestSpecBuilder builder = new RequestSpecBuilder();

               ExtractableResponse extRes =  given()

                    .header("content-type", "application/json")
                .when()
                    .get("/products")
                .then().extract();

               String str =extRes.path("data.find{it.manufacturer == 'Duracell' && it.model=='MN2400B4Z'}.categories.find{it.name=='Household Batteries'}.name");
               // extRes.path("data.find{it.manufacturer == 'Duracell' && it.model=='MN2400B4Z'}.categories.find{it.name=='Household Batteries'}")

                Assert.assertEquals(str, "Household Batteries");
//            List<Map<String,?>> list = extRes.path("data.findAll{it.type=='Test2_HardGood'}", );


        System.out.println(str);



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

    @Test(enabled=false, groups="getproduct")
    public void getProducts3(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("","");
        RequestSpecification spec = builder.build();

                ValidatableResponse res = given()
                      .header("content-type", "application/json")
                .when()
                .get("/products")
                .then();

                res.body("data.find{it.manufacturer == 'Duracell' && it.model=='MN2400B4Z'}", hasKey("shipping"));
                List<Integer> list = new ArrayList<Integer>();
            

    }

    @Test(groups="postproduct", enabled = false)
    public void postProducts() throws FileNotFoundException {

        Gson gson = new GsonBuilder().create();
        JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir")+"/src/resources/product.json"));
        ProductPOJO product = gson.fromJson(reader, ProductPOJO.class);

        System.out.println(product.toString());
        ValidatableResponse ext = given()
                .header("content-type", "application/json")
                .when()
                    .body(product)
                    .post("/products")
                .then();
        ext.body(".", hasValue("Test2_Duracell - AAA Batteries (4-Pack)"));



    }

    @Test(enabled = true)
    public void schemaValidator(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();

        ValidatableResponse res = given()
                .header("content-type", "application/json")
                .when()
                .get("/products")
                .then();
                res.assertThat().statusCode(200);
                ResponseSpecBuilder b = new ResponseSpecBuilder();
                ResponseSpecBuilder resb = new ResponseSpecBuilder();

                res.assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir")+"/src/resources/GetProductsSchema.json")));


    }



    public void fileUpload(){
        File file = new File("");
        given()
                .multiPart("file", file)
                .contentType("multipart/form-data")
        .when()
                .post("");


    }

    public void multifileUpload(){
        File file1 = new File("");
        File file2 = new File("");
        given()
                .multiPart("files", file1)
                .multiPart("files", file2)
                .contentType("multipart/form-data")
                .when()
                .post("");


    }



}
