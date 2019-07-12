package com.kotlinCoroutines.starter

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.kotlin.core.json.json

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class bindingResp(@JsonProperty("result") val result: Boolean,
                       @JsonProperty("path") val path: String,
                       @JsonProperty("feature_tags") val feature_tags: String)

class MainVerticle : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {
    println("Hi There!!")
    val resp = bindingResp(true, "path2Rome", "alliin")

    val mapper = jacksonObjectMapper()

    val serialize = mapper.writeValueAsString(resp)

    println("Serialized: $serialize")
    val state: bindingResp = mapper.readValue(serialize)


  }
}
