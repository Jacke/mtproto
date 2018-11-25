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

// int64 ~ int64 ~ int32 ~ bytes


object MTProtoServer extends App {
  class SimplisticHandler extends Actor {
    import Tcp._
    def receive = {
      case Received(data) ⇒ {
        println(s"""
          data: 
          ${data} 
          hex representation:
          ${DatatypeConverter.printHexBinary(data.toArray)} 
          utf-8 string representation:
          ${data.utf8String}""")

        println(s"byteStringToBitVector: ${ AkkaToScodec.byteStringToBitVector(data) }" )

        sender() ! Write(akka.util.ByteString(java.time.LocalDateTime.now.toString()) ++ akka.util.ByteString(" "))
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

        // server ! "printit"
        //val infiniteStream = Stream.emit(1).repeat.covary[IO].map(_ + 3)
        //val output = infiniteStream.compile.toVector.unsafeRunSync()
        //println(s"Result >> $output") // prints Result >> Vector(4, 4, 4, 4, ... )

    }
}