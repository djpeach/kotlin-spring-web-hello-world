package org.example.kotlinspring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {

  @GetMapping("/greeting")
  fun getGreeting(@RequestParam(name = "name", defaultValue = "World") name: String) = Greeting(1, "Hello $name!")
}