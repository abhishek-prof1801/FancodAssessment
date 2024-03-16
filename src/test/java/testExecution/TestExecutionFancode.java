package testExecution;

import dataObjects.Address;
import dataObjects.POJOTodos;
import dataObjects.POJOUsers;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import urlpackage.BaseUrlClass;

import java.util.HashMap;
import java.util.List;

public class TestExecutionFancode {
    HashMap<Integer,Integer> mapUser=new HashMap<>();
    @BeforeMethod
    public void generate_fancodeUserId(){
        RestAssured.baseURI= BaseUrlClass.jsonPlaceholderUrl;
        RequestSpecification requestSpecification=RestAssured.given();
        Response response=requestSpecification.get("users");
        List<POJOUsers> usersList=response.jsonPath().getList("",POJOUsers.class);
        //start storing user if with required lat_long values
        for(int i=0;i<usersList.size();i++){
            String lat=usersList.get(i).address.geo.lat;
            String lng=usersList.get(i).address.geo.lng;
            double lat_convert=Double.valueOf(lat);
            double lng_convert=Double.valueOf(lng);
            if((lat_convert<5 && lat_convert>-40) && (lng_convert>5 && lng_convert<100)) {
                //generate all fancode users
                mapUser.put(usersList.get(i).id, 0);
            }
        }


    }
    @Test(description = "Generate User ID completing more than 50% of work ")
    public void find_userId(){
        RestAssured.baseURI= BaseUrlClass.jsonPlaceholderUrl;
        RequestSpecification requestSpecification=RestAssured.given();
        Response response=requestSpecification.get("todos");
        List<POJOTodos> todos=response.jsonPath().getList("",POJOTodos.class);
        for(int i=0;i<todos.size();i++){
            if(todos.get(i).completed==true && mapUser.containsKey(todos.get(i).userId))
                System.out.println("User with id "+todos.get(i).userId+ " completed his todo with title "+ todos.get(i).title);
        }
        //Put asssertions here if any
    }
    @AfterMethod
    public void close_test(){
        mapUser.clear();
    }
}
