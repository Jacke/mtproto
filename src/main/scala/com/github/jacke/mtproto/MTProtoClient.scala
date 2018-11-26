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
import java.net.InetSocketAddress
import akka.actor.{Props, ActorSystem}
import scala.concurrent.Promise


object MTProtoClient extends App {

  class TcpClient(remote: InetSocketAddress, 
    requestData: String, 
    thePromise: Promise[String]) extends Actor {
      import Tcp._    
      import context.system

      println("Connecting")
      IO(Tcp) ! Connect(remote)

      def receive = {
          case CommandFailed(_: Connect) =>
              println ("Connect failed")
              context stop self

          case c @ Connected(remote, local) =>
              println ("Connect succeeded")
              val connection = sender()
              connection ! Register(self)
              println("Sending request early")
              connection ! Write(ByteString(requestData))

              context become {
                  case CommandFailed(w: Write) =>
                      println("Failed to write request.")
                  case Received(data) =>
                      println("Received response.")
                      //Fulfill the promise
                      thePromise.success(
                          data.decodeString("UTF-8"))
                  case "close" =>
                      println("Closing connection")
                      connection ! Close
                  case _: ConnectionClosed =>
                      println("Connection closed by server.")
                      context stop self
              }
          case _ => println("Something else is up.")
      }
  }



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
        import scala.concurrent.ExecutionContext.Implicits.global
        implicit val system = ActorSystem("tg-system")
        val host = "example.com"
        val promise = Promise[String]()
        val props = Props(classOf[TcpClient],
          new InetSocketAddress(host, 80),
          s"GET / HTTP/1.1\r\nHost: ${host}\r\nAccept: */*\r\n\r\n",
          promise)
        //Discover the actor
        val tcpActor = system.actorOf(props)

        promise.future map { data =>
          tcpActor ! "close"
          println(s"First: ${data}")
          system.terminate()
        }
    }
}