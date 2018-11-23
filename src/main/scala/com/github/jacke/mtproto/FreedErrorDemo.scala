
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits._
/*
object FreedErrorDemo {
    def main(args: Array[String]): Unit = {

      val boom = new RuntimeException("BOOM")
      type Target[A] = Either[Throwable, A]

      def shortCircuit[F[_]: ErrorM] =
        for {
          a <- FreeS.pure(1)
          b <- ErrorM[F].either[Int](Left(boom))
          c <- FreeS.pure(1)
        } yield a + b + c

      shortCircuit[ErrorM.Op].interpret[Target]
      def continueWithRightValue[F[_]: ErrorM] =
        for {
          a <- FreeS.pure(1)
          b <- ErrorM[F].either[Int](Right(1))
          c <- FreeS.pure(1)
        } yield a + b + c
      continueWithRightValue[ErrorM.Op].interpret[Target]        
      Unit
    }
}
*/