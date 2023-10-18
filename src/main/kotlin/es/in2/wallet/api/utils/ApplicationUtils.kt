package es.in2.wallet.api.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class ApplicationUtils {

    private val log: Logger = LoggerFactory.getLogger(ApplicationUtils::class.java)

    fun getRequest(url: String, headers: List<Pair<String, String>>): String {
        val client = HttpClient.newBuilder().build()
        val request = httpRequestBuilder(url=url, headers=headers)
            .GET()
            .build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get()
        logCRUD(request, response)
        return response.body()
    }

    fun postRequest(url: String, headers: List<Pair<String, String>>, body: String): String {
        val client = HttpClient.newBuilder().build()
        val request = httpRequestBuilder(url=url, headers=headers)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get()
        logCRUD(request, response, body)
        return response.body()
    }

    fun deleteRequest(url: String, headers: List<Pair<String, String>>) {
        val client = HttpClient.newBuilder().build()
        val request = httpRequestBuilder(url=url, headers=headers)
            .DELETE()
            .build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get()
        logCRUD(request, response)
    }

    private fun logCRUD(request: HttpRequest, response: HttpResponse<String>, body: String = "") {
        log.debug("********************************************************************************")
        log.debug(">>> URI: {}", request.uri())
        log.debug(">>> HEADERS: {}", request.headers())
        log.debug(">>> BODY: $body")
        log.debug("<<< STATUS CODE: ${response.statusCode()}")
        log.debug("<<< HEADERS: {}", response.headers().map())
        log.debug("<<< BODY: ${response.body()}")
        log.debug("********************************************************************************")
    }


    /**
     * Constructs an HTTP request builder with the given URL and headers.
     *
     * This function takes a URL and a list of header pairs and returns an instance of HttpRequest.Builder
     * with the URL and headers set. The headers are specified as a list of pairs, where each pair represents
     * a header name and its corresponding value.
     *
     * @param url The URL for the HTTP request.
     * @param headers A list of pairs representing the headers for the HTTP request. Each pair consists of a
     *                header name (String) and its value (String).
     * @return An instance of HttpRequest.Builder with the URL and headers set.
     */
    private fun httpRequestBuilder(url: String, headers: List<Pair<String, String>>): HttpRequest.Builder {
        val requestBuilder = HttpRequest.newBuilder().uri(URI.create(url))

        if (headers.isNotEmpty()) {
            val headerArray = headers.flatMap { listOf(it.first, it.second) }.toTypedArray()
            requestBuilder.headers(*headerArray)
        }

        return requestBuilder
    }

    /**
     * Builds a URL-encoded form data request body from a given map of parameters.
     *
     * @param formDataMap The map representing the form data parameters, where keys are parameter names, and values are their corresponding values.
     * @return A URL-encoded form data string built from the input map, suitable for use as the body of an HTTP request.
     */
    fun buildUrlEncodedFormDataRequestBody(formDataMap: Map<String, String?>): String {
        return formDataMap.map { (key, value) -> "${key.utf8()}=${value?.utf8()}" }.joinToString("&")
    }

    private fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

    /**
     * Converts the input to a String representing a JSON.
     * @param data data class instance with only JsonProperties attributes of either primitive types or other data
     * classes types that display this same characteristics recursively
     * @return a String representing a JSON
     */
    fun toJsonString(data: Any, naming: PropertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE): String {
        val objectMapper = ObjectMapper()
        objectMapper.setPropertyNamingStrategy(naming)
        return objectMapper.writeValueAsString(data)
    }

}
