package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token

import scala.annotation.tailrec
import scala.util.{ Random, Try }

object Test {

  def getPaddedFileName(n: Int): String =
    if n < 10 then f"$n%02d" else if n < 100 then f"$n%03d" else n.toString

  def main(args: Array[String]): Unit = {
    val ls1 = Random.shuffle(List(1, 2, 3, 4, 5))
    val ls2 = Random.shuffle(List(1, 3, 4, 6))

    println(s"intersect: ${ls1.intersect(ls2)}")
    println(s"diff: ${ls1.diff(ls2)}")
    println(s"diff: ${ls1.diff(List.empty)}")
    println()
    println(s"intersect: ${ls2.intersect(ls1)}")
    println(s"diff: ${ls2.diff(ls1)}")

    Seq(1.134, 1.33, 1.71, 1.76, 1.93).foreach { d =>
      println(s"d = ${roundToHalf(d)}")
    }

    val tokens = Seq(
      Token(
        chapterNumber = 1,
        verseNumber = 1,
        tokenNumber = 3,
        token = "test 1"
      ),
      Token(
        chapterNumber = 1,
        verseNumber = 2,
        tokenNumber = 1,
        token = "test 3"
      ),
      Token(
        chapterNumber = 1,
        verseNumber = 1,
        tokenNumber = 4,
        token = "test 2"
      ),
      Token(
        chapterNumber = 1,
        verseNumber = 2,
        tokenNumber = 2,
        token = "test 4"
      )
    )

    val v = tokens
      .sortBy(_.id)
      .map(_.token)
      .mkString(" ")
    println(v)

    val seq = Seq(1, 3, 4, 6, 7, 9, 11)
    val n1 = 7
    val n2 = 11
    val s =
      seq.filter { i =>
        n1 <= i && i <= n2
      }
    println(s)

    println()
    val ls = (0 to 10).toList
    printList(ls.sliding(3, 3))
  }

  private def roundToHalf(d: Double) = math.round(d * 2) / 2.0

  private def printList(ls: Iterator[List[Int]]): Unit =
    ls.foreach { l =>
      println(l.mkString("[", ", ", "]"))
    }

}
