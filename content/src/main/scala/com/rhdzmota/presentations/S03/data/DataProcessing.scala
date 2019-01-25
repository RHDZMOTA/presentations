package com.rhdzmota.presentations.S03.data

import com.rhdzmota.presentations.S03.config.Context
import com.rhdzmota.presentations.Settings
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.rand

object DataProcessing extends Context {
  import com.rhdzmota.presentations.Settings.S03
  import spark.implicits._

  val lags: Int = S03.Spark.Processing.lags
  val lagCols: List[String] = (1 to lags).toList.map(i => s"lag$i")
  val rawData: Dataset[String] = spark.read.textFile(S03.Data.source).repartition(
    3 * spark.sparkContext.defaultParallelism)

  // Create test and train data sets.
  val dataset: Dataset[Row] = rawData
    .flatMap(book => book
        .replace("\n", "")
        .split("""\."""))
    .map(sentences => sentences
      .split("""\s+""")
      .map(word => word
        .toLowerCase
        .replaceAll("[^a-z]", ""))
      .filter(word => word.length > 0))
    .filter(wordList => wordList.length > lags)
    .flatMap(_.sliding(lags + 1))
    .select((1 to lags).toList.map(i =>
      $"value".getItem(i - 1).as(s"lag${lags + 1 - i}")) :+ $"value".getItem(lags).as("target"): _*)

  val Array(train, test) = dataset.randomSplit(Array(0.7, 0.3))

  object Features {

    def wordIndexer(col: String): StringIndexerModel = new StringIndexer()
      .setInputCol(col)
      .setOutputCol(s"${col}_index")
      .fit(dataset)

    val encoder = new OneHotEncoderEstimator()
      .setInputCols((1 to lags).toArray.map(i => s"lag${i}_index"))
      .setOutputCols((1 to lags).toArray.map(i => s"lag${i}_vec_index"))

    val vectorAssembler: VectorAssembler = new VectorAssembler()
      .setInputCols((1 to lags).toArray.map(i => s"lag${i}_index"))
      .setOutputCol("features")
  }

  object Label {

    lazy val indexer: StringIndexerModel = Features.wordIndexer("target")

    // Convert indexed labels back to original labels.
    lazy val converter: IndexToString = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(indexer.labels)
  }

  lazy val stages: Array[PipelineStage] =
    (1 to lags).toArray.map(i => Features.wordIndexer(s"lag${i}")) :+
      Features.encoder :+
      Label.indexer :+
      Features.vectorAssembler
    //(1 to nlags).toArray.map(i => reconfigurableWordIndexer(s"lag${i}")) :+
    //  reconfigurableWordIndexer("target") :+
    //  Features.vectorAssembler

  lazy val pipeline: Pipeline = new Pipeline().setStages(stages)
}
