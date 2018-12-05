package com.github.jacke.mtproto.http

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp._
import scala.concurrent.Future
import scala.util.{ Failure, Success }
import upickle._
import upickle.default.{ReadWriter => RW, macroRW}
import scala.concurrent.ExecutionContext.Implicits.global

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
       scala.util.Try(upickle.default.read[FCMClientResponse](response.body.right.toOption.get)) match {
         case Success(v) if v.success == 1 => true
         case _ => false           
       }
     }
  }
}