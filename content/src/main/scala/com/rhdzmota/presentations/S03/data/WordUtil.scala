package com.rhdzmota.presentations.S03.data

import com.rhdzmota.presentations.S03.config.Context
import org.apache.spark.ml.feature.{StringIndexer, StringIndexerModel}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.Dataset

object WordUtil extends Context {
  import spark.implicits._

  def getUniqueWords(rawData: Dataset[String]): Dataset[String] = rawData
    .flatMap(_.split("""\s+"""))
    .map(_.toLowerCase.replaceAll("[^a-z]", ""))
    .filter(_.length > 0)
    .distinct()

  def getWordIndexer(words: Dataset[String]): StringIndexerModel = new StringIndexer()
    .setInputCol("value")
    .setOutputCol("word_index")
    .fit(words)

  def configWordIndexer(wordIndexer: StringIndexerModel)(col: String): StringIndexerModel =
    wordIndexer.setInputCol(col).setOutputCol(s"${col}_index").copy(ParamMap())
}
