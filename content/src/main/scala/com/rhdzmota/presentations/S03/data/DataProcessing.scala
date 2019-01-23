package com.rhdzmota.presentations.S03.data

import com.rhdzmota.presentations.S03.config.Context
import com.rhdzmota.presentations.Settings
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.feature.{StringIndexer, StringIndexerModel, VectorAssembler}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions.rand

object DataProcessing extends Context {

  import com.rhdzmota.presentations.Settings.S03
  import spark.implicits._

  final case class Datapoint(target: String, lags: List[String])

  val nlags: Int = 3
  val rawData: Dataset[String] = spark.read.textFile("gutenberg-small").cache()
  val Array(train, test) = rawData
    .flatMap(_.replace("\n", "").split("""\."""))
    .map(sentences => sentences.split("""\s+""")
      .map(_.toLowerCase.replaceAll("[^a-z]", ""))
      .filter(_.length > 0))
    .filter(_.length > nlags)
    .flatMap(_.sliding(nlags + 1))
    .select((1 to nlags).toList.map(i => $"value".getItem(i - 1).as(s"nlag${nlags + 1 - i}")) :+ $"value".getItem(nlags).as("target"): _*)
    //.toDF(((1 to nlags).toList.map(i => s"nlag${nlags + 1 - i}") :+ "target"):_*)
    //.map(sentence => sentence.reverse.toList match {
    //case head :: tail => Datapoint(head, tail)}))
    //.orderBy(rand())
    .randomSplit(Array(0.7, 0.3))

  object WordOps {

    final case class Word(word: String)

    val words: Dataset[String] = rawData
      .flatMap(_.split("""\s+"""))
      .map(_.toLowerCase.replaceAll("[^a-z]", ""))
      .filter(_.length > 0)
      //.distinct()
      //.map(word => Word(word))
      .cache()


    val wordIndexer: StringIndexerModel = new StringIndexer()
      .setInputCol("value")
      .setOutputCol("word_index")
      .fit(words)
  }/**
    def getWordIndexer(col: String = "word"): StringIndexerModel =
      wordIndexer.setInputCol(col).setOutputCol(s"${col}_index")
  }

  object Features {
    val cols = Array("lag3", "lag2", "lag1")
    val vectorAssembler: VectorAssembler = new VectorAssembler()
      .setInputCols(cols.map(col => s"${col}_index"))
      .setOutputCol("features")
  }

  val stages: Array[PipelineStage] = Array(
    WordOps.getWordIndexer("lag3"),
    WordOps.getWordIndexer("lag2"),
    WordOps.getWordIndexer("lag1"),
    WordOps.getWordIndexer("target"),
    Features.vectorAssembler
  )

  val pipeline: Pipeline = new Pipeline().setStages(stages) **/
}
