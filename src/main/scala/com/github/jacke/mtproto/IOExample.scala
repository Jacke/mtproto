package com.github.jacke.mtproto

import cats.effect.IO
import monix.eval.Task

object IOExample extends EffectDemo with TaskApp with AsyncDemo with SyncDemo with LiftIODemo {
  override def run: Task[Unit] = {
    //delay[Task]
    //suspend[Task
//    val task: Task[String] = Task("Hello World!")
//    val ioa: IO[Unit] = runAsync[Task, String](task)
//    ioa.to[Task]
   val program = for {
        _    <- IO { println("Welcome to Scala!  What's your name?") }
        name <- IO { scala.io.StdIn.readLine }
        _    <- IO { println(s"Well hello, $name!") }
    } yield ()

    program.unsafeRunSync() 
 
    shiftingProgram[Task]
    //for {
    //    x <- async[Task]
    //    _ <- Task.delay(println(x))
    //}// yield ()
  

  }
}
