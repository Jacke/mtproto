package com.github.jacke.mtproto.http

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp._
import scala.concurrent.Future
import scala.util.{ Failure, Success }
import upickle._
import upickle.default.{ReadWriter => RW, macroRW}
import scala.concurrent.ExecutionContext.Implicits.global

case class FCMClientResponse(multicast_id: Long = 0, success: Int = 0, failure: Int = 0, canonical_ids: Int = 0)
object FCMClientResponse {
  implicit val rw: RW[FCMClientResponse] = macroRW
}

object FCMClient {
  def push(payload: String, title: String, token: String, device: String): Future[Boolean] = {
    implicit val backend = AkkaHttpBackend()
 
    val request = sttp
      .header("Authorization", 
      s"key=$token")
      .header("Content-Type", "application/json")
      .body(s"""
    {
        "notification": {
            "title": "$title",
            "body": "$payload",
            "click_action": "http://localhost:3000/",
            "icon": "http://url-to-an-icon/icon.png"
        },
        "to": "$device"
    }
    """)
      .post(uri"https://fcm.googleapis.com/fcm/send")
     request.send().map { response => 
       upickle.default.read[FCMClientResponse](response.body.right.toOption.get).success match {
        case success if success == 1 => true
        case _ => false 
       } 
     }
  }
}