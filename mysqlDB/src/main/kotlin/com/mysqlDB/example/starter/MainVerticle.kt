package com.mysqlDB.example.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Future.future
import io.vertx.ext.asyncsql.MySQLClient
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.core.json.JsonObject
import java.sql.*
import java.util.*

class MainVerticle : AbstractVerticle() {
  private var conn: Connection?= null

  override fun start(startFuture: Future<Void>) {

    println("[RAJA] test program for mysql")

    val connectionProps = Properties()
    connectionProps["user"] = "registrar"
    connectionProps["password"] = "registrar"

    try {
      Class.forName("com.jdbc.Driver").newInstance()
      var url = "jdbc:mysql://localhost:3306/registrations"

      conn = DriverManager.getConnection(url, connectionProps)
    } catch (ex: SQLException) {
      ex.printStackTrace();
    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    getBindings()
  }

  private fun getBindings(): Unit {
    var s: Statement? = null
    var resultset: ResultSet? = null

    try {
      s = conn!!.createStatement()
      resultset = s!!.executeQuery("show databases;")
      while (resultset!!.next()) {
        println("bindigs: ${resultset.getString("bindings")}")
      }
    } catch (ex: SQLException) {
      ex.printStackTrace()
    }
    finally {

    }
  }
}
