package com.rhdzmota.presentations.S03.ml.classification

import com.rhdzmota.presentations.S03.data.DataProcessing.Label
import com.rhdzmota.presentations.S03.ml.ML
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.tuning.ParamGridBuilder

final case class RandomForest(basePipeline: Pipeline, processing: Pipeline) extends ML.ClassificationModel[RandomForestClassifier] {
  import com.rhdzmota.presentations.Settings.S03.Spark.Model.Classification.RandomForest._

  val model: RandomForestClassifier = new RandomForestClassifier()
    .setLabelCol("target_index")
    .setFeaturesCol("features")
    .setMaxBins(50000)

  val paramGrid: Array[ParamMap] = new ParamGridBuilder()
    .baseOn(basePipeline.stages -> (processing.getStages :+ model :+ Label.converter))
    .addGrid(model.numTrees, numTrees)
    .addGrid(model.maxDepth, maxDepth)
    .build()
}
