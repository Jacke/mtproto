package com.github.jacke.mtproto
import scodec._
import scodec.bits._
import codecs._
import java.util._
import akka.util.ByteString

import scodec.bits._
import scodec.interop.akka._
import akka.util.ByteString
import javax.xml.bind.DatatypeConverter
import scala.math.BigInt



object AkkaToScodec {
  val encryptedMessageHeaderPair = int64L ~ int64L ~ int32L ~ bytes
  //val encryptedMessageHeaderPair = int64L ~ int64L ~ int32L ~ bytes
  val req_pqPair = int32L ~ (int64 ~ int64 ~ int8L)

  def byteStringToBitVector(a: ByteString): BitVector = {

    val x: ByteVector = a.toByteVector //hex"deadbeef"
    val y: ByteString = x.toByteString
    val z: ByteVector = y.toByteVector

    println( DatatypeConverter.printHexBinary(a.toArray) )

    val (((auth_key_id, message_id), message_length), data) = encryptedMessageHeaderPair.decode( BitVector( a.toArray )).toOption.get.value
    println(s"""
        auth_key_id: $auth_key_id
        message_id: $message_id ${DatatypeConverter.printHexBinary(BigInt(message_id).toByteArray)}
        message_length: $message_length ${DatatypeConverter.printHexBinary(BigInt(message_length).toByteArray)}
        data: $data""")

    val (op, (nonce, terminator)) = req_pqPair.decode( BitVector(data.toArray) ).toOption.get.value

    println( s"""op: ${DatatypeConverter.printHexBinary(BigInt(op).toByteArray)} 
      nonce: ${DatatypeConverter.printHexBinary( BigInt(nonce._1).toByteArray ++ BigInt(nonce._2).toByteArray )}
      terminator: ${DatatypeConverter.printHexBinary(BigInt(terminator).toByteArray)}
      """ )
    BitVector( a.toArray )
  }

}
