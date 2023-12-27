# Getting Started with Spring REST in Kotlin

Variation of the [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/) from Spring Guides, focusing on Kotlin-specific behaviours and idiosyncrasies.

# What we will build

A HTTP service that will accept GET requests at http://localhost/8080/greeting.

It will respond with a JSON representation of a `data class` used to store greetings.

```json
{"id": 1, "content": "Hello World"}
```

The endpoint will also accept an optional `?name=` query parameter to substitute in place of "World".

# Initialize the Project

Use [start.spring.io](https://start.spring.io), or use your editor's integration. We will choose Kotlin, Gradle kts, and add the `Spring Web` dependency.

# Resource Class

This class will represent instances of our greeting, with an `id` as the unique identifier, and `content` as the body of the greeting.

`Spring Web` includes and uses Jackson JSON to marshal instances of our data class to JSON when returned from our endpoint. More on this later.

```kotlin
package org.example.kotlinspring

data class Greeting(val id: Long, val content: String)
```

> Note that although we use a `data class` here for HTTP return type representations, you should be careful if attempting to use a `data class` in Kotlin (or `record` in Java) for data store representations, as there are [several caveats](https://jpa-buddy.com/blog/best-practices-and-common-pitfalls/) to be aware of. 

# HTTP Controller

To create an endpoint, we need a controller for Spring Web to use. In order for Spring to locate this controller, we will annotate it with `@RestController`.

> `@RestController` does not strictly need to be a REST-compliant HTTP controller. This annotation simply combines the `@Controller` annotation, which is a semantics-specialized bean annotation for Spring to use when for [dependency injection](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html), and `@ResponseBody`, which tells Spring Web to automatically convert the return value of the contained methods to the JSON body of the HTTP response.

We will also need to annotate the actual endpoint handler method with `@GetMapping`, which tells Spring Web to use this method as the handler for `GET` requests to the given endpoint.

```kotlin
package org.example.kotlinspring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {

  @GetMapping("/greeting")
  fun getGreeting() = Greeting(1, "Hello World!")
}
```

Additionally, to allow for the _optional_ query parameter discussed above, we will add the `@RequestParam` annotation to the method parameter of the handler.

```kotlin
  @GetMapping("/greeting")
  fun getGreeting(@RequestParam(name = "name", defaultValue = "World") name: String) = Greeting(1, "Hello $name!")
```

> Notice here we do not actually need to give the Kotlin method parameter a default value, as that will be injected at runtime by Spring Web based on the value of the `defaultValue` parameter to the `@RequestParam` annotation.

Due to the `@RestController` annotation, the `Greeting` object returned from `getGreeting()` will automatically be converted to JSON and bound to the body of the HTTP response. The JSON mapping is done by Spring's [`MappingJackson2HttpMessageConverter`](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.html), since Jackson JSON is found on the classpath. 

# Run the Application

Now, you can run the created `Application` class with the gutter icon in IntelliJ, or use `./gradlew bootRun` manually. 

Once the application is running, you can hit http://localhost:8080/greeting and be presented with 

```json
{
"id": 1,
"content": "Hello World!"
}
```

or use the `?name=` query param by hitting http://localhost:8080/greeting?name=Jim%20Bob and be presented with

```json
{
"id": 1,
"content": "Hello Jim Bob!"
}
```

# Summary

Congrats, you have created a basic HTTP endpoint using Spring and Kotlin! For any extra insight, or usage with Java, see the original [guide by Spring](https://spring.io/guides/gs/rest-service/)