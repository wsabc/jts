Integrate GraphQL with Spring Boot

> This is a simple demo for integrate GraphQL and Spring Boot. For more details of GraphQL, visit to [GraphQL.cn](https://graphql.cn/)

GraphQL Introduction

GraphQL is a query language to retrieve data from a server. It is an alternative to REST, SOAP or gRPC in some way.

The most import part is the "schema" - it describes what queries are possible and what fields you can get back. 

GraphQL Java Introduction

[GraphQL Java](https://www.graphql-java.com/) is the Java (server) implementation for GraphQL. GraphQL Java Engine itself is only concerned with executing queries. It doesn’t deal with any HTTP or JSON related topics.

Integration

Component: Spring Boot(Web), GraphQL Spring Boot Starter

Maven:

```
<dependency>    <groupId>com.graphql-java</groupId>    <artifactId>graphql-java</artifactId>    <version>11.0</version></dependency><dependency>    <groupId>com.graphql-java</groupId>    <artifactId>graphql-java-spring-boot-starter-webmvc</artifactId>    <version>1.0</version></dependency>
```

GraphQL Schema Definition

GraphQLProvider Component

GraphQL Data Fetcher

Test

GraphQL vs. Restful API Comparison

1. With a REST API, you would typically gather the data by accessing multiple endpoints. With GraphQL, you’d simply send a single query to the GraphQL server that includes the concrete data requirements.
2. No more Over- and Underfetching for GraphQL, but in most case, REST API client downloads more information than is actually required in the app, and on the other hand, it faces is underfetching and the n+1-requests problem.
3. With every change that is made to the UI, there is a high risk that now there is more (or less) data required than before. Consequently, the backend needs to be adjusted as well to account for the new data needs. This kills productivity and notably slows down the ability to incorporate user feedback into a product.
4. Both are contract-first style, a protocol/schema definition is a must before development.