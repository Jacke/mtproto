package com.github.jacke.mtproto
import scodec._
import scodec.bits._
import scodec.codecs._
import java.util._
import akka.util.ByteString

import scodec.bits._
import scodec.interop.akka._
import akka.util.ByteString
import javax.xml.bind.DatatypeConverter
import scala.math.BigInt

case class UnencryptedMessage(auth_key_id: Long, message_id: Long, message_data_length: Int, message_data: ByteVector) {
  def toHex(): String =
    s"""
      UnencryptedMessage: 
        auth_key_id: ${DatatypeConverter.printHexBinary(BigInt(auth_key_id).toByteArray)}
        message_id: ${DatatypeConverter.printHexBinary(BigInt(message_id).toByteArray)}
        message_data_length: ${DatatypeConverter.printHexBinary(BigInt(message_data_length).toByteArray)}
        message_data: ${DatatypeConverter.printHexBinary(message_data.toByteString.toArray)}

    """
}
object UnencryptedMessage {
  def baseCodec = int64L :: int64L :: int32L :: bytes
  implicit val codec: Codec[UnencryptedMessage] = baseCodec.as[UnencryptedMessage]
}


case class res_pq(nonce: Tuple2[Long, Long])
object res_pq {
  def baseCodec = (int64L pairedWith int64L)
  implicit val codec: Codec[res_pq] = baseCodec.as[res_pq]
}
case class resPQ(nonce: Tuple2[Long, Long], server_nonce: Tuple2[Long, Long], pq: String)
object resPQ {
  def baseCodec = (int64L pairedWith int64L) :: (int64L pairedWith int64L) :: ascii
  implicit val codec: Codec[resPQ] = baseCodec.as[resPQ]
}

object AkkaToScodec {
  val encryptedMessageHeaderPair = int64L ~ int64L ~ int32L ~ bytes
  //val encryptedMessageHeaderPair = int64L ~ int64L ~ int32L ~ bytes
  val req_pqPair = int32L ~ (int64 ~ int64 ~ int8L)

  def byteStringToBitVector(a: ByteString): BitVector = {

    //val x: ByteVector = a.toByteVector //hex"deadbeef"
    //val y: ByteString = x.toByteString
    //val z: ByteVector = y.toByteVector

    println( DatatypeConverter.printHexBinary(a.toArray) )
    val (((auth_key_id, message_id), message_length), data) = encryptedMessageHeaderPair.decode( BitVector( a.toArray )).toOption.get.value
    val c = Codec[UnencryptedMessage].decode(BitVector( a.toArray )).toOption.get.value

    println(s"""
        c: ${c.toHex}
        auth_key_id: $auth_key_id
        message_id: $message_id ${DatatypeConverter.printHexBinary(BigInt(message_id).toByteArray)}
        message_length: $message_length ${DatatypeConverter.printHexBinary(BigInt(message_length).toByteArray)}
        data: $data""")

    val (op, (nonce, terminator)) = req_pqPair.decode( BitVector(data.toArray) ).toOption.get.value

    println( s"""op: ${DatatypeConverter.printHexBinary(BigInt(op).toByteArray)} 
      nonce: ${DatatypeConverter.printHexBinary( BigInt(nonce._1).toByteArray ++ BigInt(nonce._2).toByteArray )}
      terminator: ${DatatypeConverter.printHexBinary(BigInt(terminator).toByteArray)}
      """ )

    // Check for operations: 
    DatatypeConverter.printHexBinary(BigInt(op).toByteArray) match {
      case "60469778" => makeReqPq(data, nonce)
      case "d712e4be" => terminate
    }
  }

  def terminate: BitVector = hex"0xdeadbeef".bits
  // -> req_pq#60469778 nonce:int128 = ResPQ
  // <- resPQ#05162463  nonce:int128 server_nonce:int128 pq:string server_public_key_fingerprints:Vector long = ResPQ 
  def makeReqPq(a: ByteVector, nonce: Tuple2[Long, Long]): BitVector = {
    Codec[resPQ].encode(resPQ(nonce, server_nonce = (10L, 20L), pq = "string") ).toOption.get
  }


}
