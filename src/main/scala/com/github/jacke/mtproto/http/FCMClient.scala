package com.github.jacke.mtproto.http

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp._
import scala.concurrent.Future
import scala.util.{ Failure, Success }

object FCMClient {
  def push(payload: String): Future[Response[String]] = {
    implicit val backend = AkkaHttpBackend()
 
    val request = sttp
      .header("Authorization", 
      "key=AAAA0r_WvnE:APA91bFVaDl_9qhHFXVh72v0_5g4zJVc7nw4D0RbcWEP_MYheMxTT_m_fQJMSQCwnjK6fMpT3O01dOgiiMekNNFT8CzcMCdMadLaumhAJOi37JgWfE10z7JRHBwr4WDHmBHf5cM5CHfv")
      .header("Content-Type", "application/json")
      .body(payload)
      .post(uri"https://fcm.googleapis.com/fcm/send")
     val response = request.send()

    response
  }
}