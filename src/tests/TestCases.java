package tests;


import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo_classes.Create_Order_Details;
import pojo_classes.Login_Request;
import pojo_classes.Login_Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCases {
	String token;
	String userId;
	String productId;
	String ordersStr;

	@Test(priority = 1)
	public void Login()
	{
		//creating base request
		RequestSpecification LoginBasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();

		//driving login data from pojo class
		Login_Request loginRequest = new Login_Request();
		loginRequest.setUserEmail("alphatester3242@gmail.com");
		loginRequest.setUserPassword("Alphatester3242");

		//adding body to the base request
		RequestSpecification requestBody = given().log().all().spec(LoginBasereq).body(loginRequest);

		//adding resource and extracting response by using pojo class
		Login_Response loginResponse = requestBody.when().post("/api/ecom/auth/login").then().log().all().extract()
				.response().as(Login_Response.class);

		//extracting token and userId from the login response
		token = loginResponse.getToken();
		userId = loginResponse.getUserId();
	}

	@Test(priority = 2)
	public void Create_Product()
	{	
		//creating base request
		RequestSpecification CreateProductBasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token).build();

		//adding form body to the base request body
		RequestSpecification reqAddProduct = given().log().all().spec(CreateProductBasereq).param("productName", "Watch-ABCD")
				.param("productAddedBy", userId).param("productCategory", "Electronics").param("productSubCategory", "Watches")
				.param("productPrice", "100").param("productDescription", "Brand New Watch").param("productFor", "Men")
				.multiPart("productImage",new File("C:/Users/Admin/Downloads/download.jpg"));

		//adding resource and extracting response by using JsonPath class
		String respAddProduct = reqAddProduct.when().post("api/ecom/product/add-product").then().log().all().extract().response().asString();

		//extracting productId from response by using JsonPath.
		JsonPath js = new JsonPath(respAddProduct);
		productId = js.get("productId");
	}

	@Test(priority = 3)
	public void Create_Order()
	{
		//creating base request
		RequestSpecification CreateOrderBasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token)
				.setContentType(ContentType.JSON).build();

		//getting data for body from pojo classes...
		Create_Order_Details orderDetails = new Create_Order_Details();
		orderDetails.setCountry("India");
		orderDetails.setProductOrderedId(productId);

		List<Create_Order_Details> orderDetailList = new ArrayList<Create_Order_Details>();
		orderDetailList.add(orderDetails);

		pojo_classes.Create_Order order = new pojo_classes.Create_Order();
		order.setOrders(orderDetailList);

		//adding body to the request...
		RequestSpecification reqCreateOrder = given().log().all().spec(CreateOrderBasereq).body(order);

		//creating response..
		String respCreateOrder = reqCreateOrder.when().post("api/ecom/order/create-order").then().log().all()
				.extract().response().asString();

		//extract order id from the response
		JsonPath js1 = new JsonPath(respCreateOrder);

		List<String> orderId = js1.get("orders");
		ordersStr = String.join(", ", orderId);
	}

	@Test(priority = 4)
	public void View_Order_Details()
	{
		//creating base request
		RequestSpecification ViewOrderBasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token)
				.addQueryParam("id", ordersStr).build();

		//adding spec builder to the request..
		RequestSpecification reqViewOrder = given().log().all().spec(ViewOrderBasereq);

		//creating response..
		String respViewOrder = reqViewOrder.when().get("api/ecom/order/get-orders-details").then().log().all().extract().response().asString();

		JsonPath js2 = new JsonPath(respViewOrder);
		String message = js2.get("message");

		//Validate actual vs expected message value from response
		Assert.assertEquals(message, "Orders fetched for customer Successfully");
	}

	@Test(priority = 5)
	public void Delete_order()
	{
		//creating base request
		RequestSpecification DeleteOrderBasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token)
				.build();

		//adding spec builder to the request..
		RequestSpecification reqDeleteOrder = given().log().all().spec(DeleteOrderBasereq).pathParam("productId",productId);
		
		//creating response..
		String respDeleteOrder = reqDeleteOrder.when().delete("api/ecom/product/delete-product/{productId}").then().log().all().extract().response().asString();
		
		//extracting response and comparing actual vs expected message..
		JsonPath js3 = new JsonPath(respDeleteOrder);
		Assert.assertEquals(js3.get("message"),"Product Deleted Successfully");
	}


}
