package com.github.jacke.mtproto.http

import upickle._
import upickle.default.{ReadWriter => RW, macroRW}

case class FCMClientResponse(multicast_id: Long = 0, success: Int = 0, failure: Int = 0, canonical_ids: Int = 0)
object FCMClientResponse {
  implicit val rw: RW[FCMClientResponse] = macroRW
}