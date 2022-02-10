# Product REST API
A simple REST API that reads and writes to an in-memory database (H2). This project runs on top of Spring Boot and is bundled with Gradle.

## Building and running the project
To use this project, your machine must have `gradle` installed. If on Mac, you can use Homebrew:

```
$ brew install gradle
```

To read more about installation options, see Gradle's [installation documentation](https://gradle.org/install/).

To build the application, make sure to run `gradle build`. Once the project is compiled, use the `gradle run` command to start the project.

```
$ gradle build && gradle run
```

By default, this server listens for HTTP requests on port `8080`. If you wish to change it, you can edit it in `resources/application.properties`.

## API
Currently, the API only supports two HTTP methods: `GET` and `POST`. Both are accessed via the endpoint: `http://localhost:8080/v1/products`.

* `POST http://localhost:8080/v1/products`
  * Returns a JSON object with the following fields:
    * `data` - Product inserted in JSON format
    * `status` - HTTP status of request
* `GET http://localhost:8080/v1/products`
  * Returns a JSON object with the following fields:
    * `data` - An array of products sorted from newest created to oldest, with an optional filter by `category`
      * Query parameters: `max`, `category`
    * `status` - HTTP status of request

## Testing
By default, tests are compiled and executed at the start of runtime. I have included 4 tests, of which being:
1. `writeThenRead` - tests that we can read our writes
2. `insertMany` - tests that if we insert `N` products, we will read `N` products via `GET` request
3. `retrieveAllByCategory` - tests that if we query by a specific value for the category field, then all the items retrieved will have the same category value
4. `retrieveProductPage` - tests pagination functionality