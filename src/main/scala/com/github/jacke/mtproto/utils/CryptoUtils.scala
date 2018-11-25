package com.github.jacke.mtproto

import cats.effect._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import cats.effect._
import fs2._
import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress
import javax.xml.bind.DatatypeConverter
import javax.crypto._
import java.security._
import java.util._

object CryptoUtils {

  def main(): Unit = { 
    val password = "changeit".toCharArray()
    val alias = "123"
    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    val encoder = java.util.Base64.getEncoder();
    val kp:KeyPair = kpg.generateKeyPair();
    val pub: Key = kp.getPublic();
    val pvt: PrivateKey = kp.getPrivate();

    val encryptCipher = Cipher.getInstance("RSA/ECB/NoPadding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, pvt);
    val plainText  = "abcdefghijklmnopqrstuvwxyz".getBytes("UTF-8");
    val cipherText: Array[Byte] = encryptCipher.doFinal(plainText);

    println(s"pub: ${encoder.encodeToString(pub.getEncoded())} pvt: ${encoder.encodeToString(pvt.getEncoded())}")
    println(s"encrypted: ${new String(Base64.getEncoder().encode(cipherText))}");

    val decryptCipher = Cipher.getInstance("RSA/ECB/NoPadding");
    decryptCipher.init(Cipher.DECRYPT_MODE, pub);
    val plainText2: Array[Byte] = decryptCipher.doFinal(cipherText)

    println(s"decrypted: ${new String(plainText)} ${new String(plainText2) }")
  }

}