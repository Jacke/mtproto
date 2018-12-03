package com.github.jacke.mtproto

import scalaz.zio.{App, IO}
import scalaz.zio.console._
import scalaz.zio._
import scalaz.zio.interop._
import scalaz.zio.interop.future._

import java.io.IOException
import scala.concurrent.ExecutionContext.Implicits.global

object ZIOExample extends App {
  val KEY = "AAAA0r_WvnE:APA91bFVaDl_9qhHFXVh72v0_5g4zJVc7nw4D0RbcWEP_MYheMxTT_m_fQJMSQCwnjK6fMpT3O01dOgiiMekNNFT8CzcMCdMadLaumhAJOi37JgWfE10z7JRHBwr4WDHmBHf5cM5CHfv"
  val TO = "chUEblRLVl4:APA91bFF3XG4836K3fIe3INGInEMqwbh2JGCYj9TE2iLfkUCvOApVncNJajagAh_YcxwzCAO3aaUOzXUvGKaLauGvzoH3_W7s3blN7aJwN7B72qSOx-8OIznwfQu0esWOeBnk4UJn9El"
  def run(args: List[String]): IO[Nothing, ExitStatus] = {
    myAppLogic.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }

  def request(title: String, body: String, token: String, device: String) = {
    IO.fromFuture( () =>
    com.github.jacke.mtproto.http.FCMClient.push(title, body, token, device))(scala.concurrent.ExecutionContext.Implicits.global)
  }

  def myAppLogic: IO[Throwable, Boolean] =
    for {
      _ <- putStrLn("FCM Token: ")
      k <- getStrLn
      _ <- putStrLn("Device token: ")
      to <- getStrLn
      _ <- putStrLn("Title: ")
      title <- getStrLn
      _ <- putStrLn("Body: ")
      body <- getStrLn
      r <- request(title, body, k, to) 
    } yield (r)
}
