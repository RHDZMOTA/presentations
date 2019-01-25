package com.rhdzmota.presentations.S03.ml.classification

import com.rhdzmota.presentations.S03.data.DataProcessing.Label
import com.rhdzmota.presentations.S03.ml.ML
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.tuning.ParamGridBuilder

final case class LogisticReg(basePipeline: Pipeline, processing: Pipeline) extends ML.ClassificationModel[LogisticRegression] {
  import com.rhdzmota.presentations.Settings.S03.Spark.Model.Classification.LogisticRegression._

  val model: LogisticRegression = new LogisticRegression()
    .setLabelCol("target_index")
    .setFeaturesCol("features")

  val paramGrid: Array[ParamMap] = new ParamGridBuilder()
    .baseOn(basePipeline.stages -> (processing.getStages :+ model :+ Label.converter))
    .addGrid(model.maxIter, maxIter)
    .addGrid(model.regParam, regParam)
    .addGrid(model.elasticNetParam, elasticNetParam)
    .build()

}
