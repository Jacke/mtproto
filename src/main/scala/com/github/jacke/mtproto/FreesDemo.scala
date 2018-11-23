package com.github.jacke.mtproto
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


object SampleCode extends App {

  class SimplisticHandler extends Actor {
    import Tcp._
    def receive = {
      case Received(data) ⇒ {
        println(s"data: ${data} ${DatatypeConverter.printHexBinary(data.toArray)} ${data.utf8String}")
        sender() ! Write(akka.util.ByteString(java.time.LocalDateTime.now.toString()) ++ akka.util.ByteString(" ") ++ data)
      }
      case PeerClosed     ⇒ context stop self
    }
  }
  object Server {
    def props: Props =
      Props(new Server)
  }

  class Server extends Actor {

    import Tcp._
    implicit val system = ActorSystem("iot-system")

    IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 3030))

    def receive = {
      case b @ Bound(localAddress) ⇒
        context.parent ! b

      case CommandFailed(_: Bind) ⇒ context stop self

      case c @ Connected(remote, local) ⇒
        val handler = context.actorOf(Props[SimplisticHandler])
        val connection = sender()
        println("connected")
        connection ! Register(handler)
    }

  }

    override def main(args: Array[String]): Unit = {
        import akka.io.{ IO, Tcp }
        implicit val system = ActorSystem("tg-system")
        val server = system.actorOf(Server.props, "server")
        println(s"First: $server")
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

        // server ! "printit"
        //val infiniteStream = Stream.emit(1).repeat.covary[IO].map(_ + 3)
        //val output = infiniteStream.compile.toVector.unsafeRunSync()
        //println(s"Result >> $output") // prints Result >> Vector(4, 4, 4, 4, ... )

    }
}