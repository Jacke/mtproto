package com.github.jacke.mtproto.http

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp._
import scala.concurrent.Future
import scala.util.{ Failure, Success }

/* Example payload:

{
    "notification": {
        "title": "Firebase",
        "body": "Firebase is awesome",
        "click_action": "http://localhost:3000/",
        "icon": "http://url-to-an-icon/icon.png"
    },
    "to": "chUEblRLVl4:APA91bFF3XG4836K3fIe3INGInEMqwbh2JGCYj9TE2iLfkUCvOApVncNJajagAh_YcxwzCAO3aaUOzXUvGKaLauGvzoH3_W7s3blN7aJwN7B72qSOx-8OIznwfQu0esWOeBnk4UJn9El"
}
*/
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