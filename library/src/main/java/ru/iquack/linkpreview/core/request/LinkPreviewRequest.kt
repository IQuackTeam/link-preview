package ru.iquack.linkpreview.core.request

import java.net.Proxy

/**
 * Represents a request to fetch link preview data.
 *
 * @property url The URL to fetch the link preview for.
 * @property memoryCacheKey An optional key for caching the response in memory.
 * @property memoryCachePolicy The caching policy to use for memory caching.
 * @property method The HTTP method to use for the request (e.g., GET, POST).
 * @property headers A map of HTTP headers to include in the request.
 * @property cookies A map of cookies to include in the request.
 * @property userAgent The User-Agent string to use for the request.
 * @property timeout The timeout duration (in milliseconds) for the request.
 * @property referrer The referrer URL to include in the request.
 * @property followRedirects Whether to follow HTTP redirects.
 * @property ignoreHttpErrors Whether to ignore HTTP errors and still attempt to parse the response.
 * @property ignoreContentType Whether to ignore the content type of the response and still attempt to parse it.
 * @property data A map of data parameters to include in the request body (for POST requests).
 * @property requestBody An optional raw string to include as the request body (for POST requests).
 * @property maxBodySize The maximum size (in bytes) of the response body to read.
 * @property proxy An optional proxy server to route the request through.
 * @property postDataCharset The character set to use when encoding POST data.
 */
class LinkPreviewRequest private constructor(
    val url: String,
    val memoryCacheKey: String?,
    val memoryCachePolicy: CachePolicy?,
    val method: RequestMethod,
    val headers: Map<String, String>,
    val cookies: Map<String, String>,
    val userAgent: String?,
    val timeout: Int?,
    val referrer: String?,
    val followRedirects: Boolean?,
    val ignoreHttpErrors: Boolean?,
    val ignoreContentType: Boolean?,
    val data: Map<String, String>,
    val requestBody: String?,
    val maxBodySize: Int?,
    val proxy: Proxy?,
    val postDataCharset: String?,
) {
    /**
     * Builder for constructing [LinkPreviewRequest] instances.
     */
    class Builder {
        private var url: String? = null
        private var memoryCacheKey: String? = null
        private var memoryCachePolicy: CachePolicy? = null
        private var method: RequestMethod = RequestMethod.GET
        private val headers: MutableMap<String, String> = mutableMapOf()
        private val cookies: MutableMap<String, String> = mutableMapOf()
        private var userAgent: String? = null
        private var timeout: Int? = null
        private var referrer: String? = null
        private var followRedirects: Boolean? = null
        private var ignoreHttpErrors: Boolean? = null
        private var ignoreContentType: Boolean? = null
        private val data: MutableMap<String, String> = mutableMapOf()
        private var requestBody: String? = null
        private var maxBodySize: Int? = null
        private var proxy: Proxy? = null
        private var postDataCharset: String? = null

        /**
         * Sets the URL to fetch the link preview for.
         * @param url The target URL.
         * @return This builder instance.
         */
        fun url(url: String) = apply { this.url = url }

        /**
         * Sets the memory cache key for this request.
         * @param memoryCacheKey The cache key.
         * @return This builder instance.
         */
        fun memoryCacheKey(memoryCacheKey: String?) = apply { this.memoryCacheKey = memoryCacheKey }

        /**
         * Sets the memory cache policy for this request.
         * @param memoryCachePolicy The cache policy.
         * @return This builder instance.
         */
        fun memoryCachePolicy(memoryCachePolicy: CachePolicy) = apply { this.memoryCachePolicy = memoryCachePolicy }

        /**
         * Sets the HTTP method for this request.
         * @param method The HTTP method.
         * @return This builder instance.
         */
        fun method(method: RequestMethod) = apply { this.method = method }

        /**
         * Adds a single HTTP header to the request.
         * @param key Header name.
         * @param value Header value.
         * @return This builder instance.
         */
        fun header(key: String, value: String) = apply { this.headers[key] = value }

        /**
         * Adds multiple HTTP headers to the request.
         * @param headers Map of headers.
         * @return This builder instance.
         */
        fun headers(headers: Map<String, String>) = apply { this.headers.putAll(headers) }

        /**
         * Adds a single cookie to the request.
         * @param key Cookie name.
         * @param value Cookie value.
         * @return This builder instance.
         */
        fun cookie(key: String, value: String) = apply { this.cookies[key] = value }

        /**
         * Adds multiple cookies to the request.
         * @param cookies Map of cookies.
         * @return This builder instance.
         */
        fun cookies(cookies: Map<String, String>) = apply { this.cookies.putAll(cookies) }

        /**
         * Sets the User-Agent string for the request.
         * @param userAgent The User-Agent value.
         * @return This builder instance.
         */
        fun userAgent(userAgent: String) = apply { this.userAgent = userAgent }

        /**
         * Sets the timeout for the request in milliseconds.
         * @param timeout Timeout in ms.
         * @return This builder instance.
         */
        fun timeout(timeout: Int) = apply { this.timeout = timeout }

        /**
         * Sets the referrer URL for the request.
         * @param referrer The referrer URL.
         * @return This builder instance.
         */
        fun referrer(referrer: String) = apply { this.referrer = referrer }

        /**
         * Sets whether to follow HTTP redirects.
         * @param follow True to follow redirects.
         * @return This builder instance.
         */
        fun followRedirects(follow: Boolean) = apply { this.followRedirects = follow }

        /**
         * Sets whether to ignore HTTP errors.
         * @param ignore True to ignore HTTP errors.
         * @return This builder instance.
         */
        fun ignoreHttpErrors(ignore: Boolean) = apply { this.ignoreHttpErrors = ignore }

        /**
         * Sets whether to ignore the content type of the response.
         * @param ignore True to ignore content type.
         * @return This builder instance.
         */
        fun ignoreContentType(ignore: Boolean) = apply { this.ignoreContentType = ignore }

        /**
         * Adds a single data parameter to the request body (for POST requests).
         * @param key Parameter name.
         * @param value Parameter value.
         * @return This builder instance.
         */
        fun data(key: String, value: String) = apply { this.data[key] = value }

        /**
         * Adds multiple data parameters to the request body (for POST requests).
         * @param data Map of parameters.
         * @return This builder instance.
         */
        fun data(data: Map<String, String>) = apply { this.data.putAll(data) }

        /**
         * Sets the raw request body (for POST requests).
         * @param body The request body.
         * @return This builder instance.
         */
        fun requestBody(body: String) = apply { this.requestBody = body }

        /**
         * Sets the maximum response body size in bytes.
         * @param size The max body size.
         * @return This builder instance.
         */
        fun maxBodySize(size: Int) = apply { this.maxBodySize = size }

        /**
         * Sets the proxy server for the request.
         * @param proxy The proxy instance.
         * @return This builder instance.
         */
        fun proxy(proxy: Proxy) = apply { this.proxy = proxy }

        /**
         * Sets the charset for POST data encoding.
         * @param charset The charset name.
         * @return This builder instance.
         */
        fun postDataCharset(charset: String) = apply { this.postDataCharset = charset }

        /**
         * Builds the [LinkPreviewRequest] instance.
         * @return The constructed [LinkPreviewRequest].
         * @throws IllegalStateException if required fields are missing.
         */
        fun build(): LinkPreviewRequest {
            return LinkPreviewRequest(
                url = url ?: throw IllegalStateException("Data must be set"),
                memoryCacheKey = memoryCacheKey,
                memoryCachePolicy = memoryCachePolicy,
                method = method,
                headers = headers.toMap(),
                cookies = cookies.toMap(),
                userAgent = userAgent,
                timeout = timeout,
                referrer = referrer,
                followRedirects = followRedirects,
                ignoreHttpErrors = ignoreHttpErrors,
                ignoreContentType = ignoreContentType,
                data = data.toMap(),
                requestBody = requestBody,
                maxBodySize = maxBodySize,
                proxy = proxy,
                postDataCharset = postDataCharset
            )
        }
    }
}

/**
 * Enum representing supported HTTP request methods for [LinkPreviewRequest].
 * @property method The corresponding [org.jsoup.Connection.Method].
 */
enum class RequestMethod(
    internal val method: org.jsoup.Connection.Method
){
    /** HTTP GET method. */
    GET(org.jsoup.Connection.Method.GET),
    /** HTTP POST method. */
    POST(org.jsoup.Connection.Method.POST),
    /** HTTP PUT method. */
    PUT(org.jsoup.Connection.Method.PUT),
    /** HTTP DELETE method. */
    DELETE(org.jsoup.Connection.Method.DELETE),
    /** HTTP PATCH method. */
    PATCH(org.jsoup.Connection.Method.PATCH),
    /** HTTP HEAD method. */
    HEAD(org.jsoup.Connection.Method.HEAD),
    /** HTTP OPTIONS method. */
    OPTIONS(org.jsoup.Connection.Method.OPTIONS),
    /** HTTP TRACE method. */
    TRACE(org.jsoup.Connection.Method.TRACE);
}