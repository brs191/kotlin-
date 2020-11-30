package hclient

import okhttp3.*
import java.io.IOException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.net.HttpCookie
import java.net.URLEncoder
import java.security.cert.X509Certificate
import java.time.Duration
import javax.net.ssl.*

private val client = OkHttpClient()

private val trustManager = arrayOf<TrustManager>(object : X509TrustManager {
    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
})
private val sslContext: SSLContext = SSLContext.getInstance("SSL").apply {
    this.init(null, trustManager, java.security.SecureRandom())
}
private val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

private val secureclient = OkHttpClient().newBuilder()
.writeTimeout(Duration.ofMillis(50000L))
.hostnameVerifier(HostnameVerifier {_, _ -> true})
.apply {
    sslSocketFactory(sslSocketFactory, trustManager[0] as X509TrustManager)
}.build().apply {
    this.dispatcher.maxRequests = 2 // Todo
    this.dispatcher.maxRequestsPerHost = 3 // Todo
}

fun syncTestNonPS() {
    var jsonString: String = File("/home/bollam/github/brs191/kotlin/hclient/src/main/kotlin/nonps_secondary.json").readText(Charsets.UTF_8)
    val testNonPS = jacksonObjectMapper().readValue<MutableMap<String, String>>(jsonString)

    println("testNonPS is ${testNonPS["msisdn"]}")
    // build url based on Test Case
    var url = HttpUrl.Builder()
            .scheme("http")
            .host("127.0.0.1")
            .port(7072)
    for ((k, v) in testNonPS) {
        url.addQueryParameter(k, v)
    }

    var request = Request.Builder()
            .url(url.build())
            .addHeader("User-Agent", "TTA-RCS/1.0 SM-N950N/SH3 Device_Type/RCS_Android_Phone SKT")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Host", "acs-global.skt-oregon.cloud-rcsmaap.com")
            .addHeader("Accept-Language", "ko-KR")
            .addHeader("Cache-Control", "max-age=0")
            .build()

    var response = client.newCall(request).execute()
    if (response.code != 200) {
      println("Something went wrong with get ${response.code}")
        return
    }

    if (response.body!!.contentLength() != 0L) {
        println("ACS Config success")
        return
    }

    val cookie = response.headers("Set-Cookie")
    var cookies = HttpCookie.parse(cookie.toString())
    var cName = cookies[0].name
    if (cName[0] == '[') cName = cName.replace("[", "")

    jsonString = File("/home/bollam/github/brs191/kotlin/hclient/src/main/kotlin/nonps_final.json").readText(Charsets.UTF_8)
    var nonps_final = jacksonObjectMapper().readValue<MutableMap<String, String>>(jsonString)

    url = HttpUrl.Builder()
            .scheme("http")
            .host("127.0.0.1")
            .port(7072)
    for ((k, v) in nonps_final) {
        url.addQueryParameter(k, v)
    }
    request = Request.Builder()
        .url(url.build())
        .addHeader("User-Agent", "TTA-RCS/1.0 SM-N950N/SH3 Device_Type/RCS_Android_Phone SKT")
        .addHeader("Connection", "Keep-Alive")
        .addHeader("Host", "acs-global.skt-oregon.cloud-rcsmaap.com")
        .addHeader("Accept-Language", "ko-KR")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("Cookie", "$cName=${cookies[0].value}")
        .build()

    println("final request is \n${request.toString()}")
    response = client.newCall(request).execute()

    Thread.sleep(4_000)
    if(response.code != 200) {
        println("Something went wrong with final ${response.code}")
        return
    }

    if (response.body!!.contentLength() != 0L) {
        println("ACS Config success final")
        val body = response.body?.string()
        println("response body \n ${body.toString()}")
    }
}

fun testNonPS() {
    val jsonString: String = File("/home/bollam/github/brs191/kotlin/hclient/src/main/kotlin/nonps_secondary.json").readText(Charsets.UTF_8)
    var testNonPS = jacksonObjectMapper().readValue<MutableMap<String, String>>(jsonString)

    var msisdn: String? = testNonPS["msisdn"]
    var intMs: Long? = msisdn?.toLong()
    val newmsisdn: String = intMs.toString()
    println("msisdn $msisdn")
    println("msisdn $intMs")

    if (intMs != null) {
        intMs = intMs + 1
    }
    println("msisdn string ${intMs.toString()}")
    val newval = (testNonPS["msisdn"]?.toLong()!! + 1).toString()
    println("msisdn newvalg $newval")

    var url = HttpUrl.Builder()
            .scheme("http")
            .host("127.0.0.1")
            .port(7072)
    for ((k, v) in testNonPS) {
        url.addQueryParameter(k, v)
    }

    val request = Request.Builder()
            .url(url.build())
            .addHeader("User-Agent", "TTA-RCS/1.0 SM-N950N/SH3 Device_Type/RCS_Android_Phone SKT")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Host", "acs-global.skt-oregon.cloud-rcsmaap.com")
            .addHeader("Accept-Language", "ko-KR")
            .addHeader("Cache-Control", "max-age=0")
            .build()

    println("testNonPS req: ${request.toString()}")
    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }
        override fun onResponse(call: Call, response: Response) {
            val code = response.code
            if (code != 200) {
                println("something went wrong in the GET request $code")
            } else {
                if (response.body!!.contentLength() != 0L) {
                    println("acs configuration first completed")
                    println("${response.body!!}")
                } else {
                    println("Received 200 OK. Send NonPS Final Request")
                    finalNonPS(response.headers("Set-Cookie").toString())
                }
            }
        }
    })
}

fun finalNonPS(cookie: String) {
    val jsonString: String = File("/home/bollam/github/brs191/kotlin/hclient/src/main/kotlin/nonps_final.json").readText(Charsets.UTF_8)
    val nonps_final = jacksonObjectMapper().readValue<MutableMap<String, String>>(jsonString)

    var url = HttpUrl.Builder()
            .scheme("http")
            .host("127.0.0.1")
            .port(7072)
    for ((k, v) in nonps_final) {
        url.addQueryParameter(k, v)
    }

    var cookies = HttpCookie.parse(cookie)
    var cName = cookies[0].name
    if (cName[0] == '[') cName = cName.replace("[", "")

    val request = Request.Builder()
            .url(url.build())
            .addHeader("User-Agent", "TTA-RCS/1.0 SM-N950N/SH3 Device_Type/RCS_Android_Phone SKT")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Host", "acs-global.skt-oregon.cloud-rcsmaap.com")
            .addHeader("Accept-Language", "ko-KR")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("Cookie", "$cName=${cookies[0].value}")
            .build()

    println("testNonPS final req: ${request}")
    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {

            response.use {
                if (!response.isSuccessful) {
                    println("something went wrong in the final GET request ${response.code}")
                    return
                } else {
                    if (response.body!!.contentLength() != 0L) {
                        println("acs configuration completed final")
                        println("${response.body?.string().toString()}")
                    } else {
                        println("acs configuration null content length.")
                    }
                }
            }
        }
    })
}

fun main() {
    testNonPS()
    //syncTestNonPS()
}