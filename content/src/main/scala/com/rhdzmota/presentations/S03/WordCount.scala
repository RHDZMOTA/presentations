package com.rhdzmota.presentations.S03

import com.rhdzmota.presentations.Settings.S03
import com.rhdzmota.presentations.S03.config.Context
import org.apache.spark.sql._

object WordCount extends Context {
  import spark.implicits._

  final case class WordCount(word: String, count: Long)

  val data: Dataset[String] = spark.read.textFile(S03.Data.source)

  val wordcount: Dataset[WordCount] = data
    .flatMap(_.split("""\s+"""))
    .map(_.toLowerCase.replaceAll("[^A-Za-z0-9]", ""))
    .filter(_.length > 1)
    .groupByKey(identity).count()
    .map({case (w, c) => WordCount(w, c)})
    .sort($"count".desc)

  def main(args: Array[String]): Unit = {
    println("S03 WordCount Application")
    wordcount.show()
    println(s"- Number of unique words: ${wordcount.count()}")
    spark.close()
  }
}