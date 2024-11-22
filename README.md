# End-to-End Ecommerce API Automation Flow

This project is an **End-to-End Automation Flow** for an ecommerce platform built using **RestAssured**. The flow covers the complete process of logging in, creating products, purchasing orders, deleting orders, and deleting products. The automation includes **SpecBuilder**, **Serialization & Deserialization**, and **JsonPath** techniques to validate and interact with the API.

## Features

- **Login API**: Authenticate users to access the platform.
- **Create Product**: Admin can create new products on the platform.
- **Create Order**: Users can create/place orders on the created products.
- **View Order Details**: Users can view their order details.
- **Delete Product**: Admin can remove products from the catalog.

## Tools & Technologies

- **RestAssured**: For API testing and automation.
- **SpecBuilder**: To define reusable request specifications.
- **Serialization & Deserialization**: For converting Java objects to JSON and vice versa.
- **JsonPath**: To extract data from JSON responses.

## End-to-End Flow

1. **Login**: Test the authentication process for users.
2. **Create Product**: Admin creates a product and validates the product creation.
3. **Create Order**: User Creates/places an order for a product, and the order is validated.
4. **View Order Details**: User can view their placed order details, and validation is performed.
5. **Delete Product**: Admin deletes the product, and the product is removed from the catalog.

## Test result

![TestNG report](https://github.com/rohitpunekar242/ecommerce-api-flow-rest-assured/blob/master/TestNG_Report.png)


