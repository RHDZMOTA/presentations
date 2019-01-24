package com.rhdzmota.presentations.S03.ml.classification

import com.rhdzmota.presentations.S03.data.DataProcessing.Label
import com.rhdzmota.presentations.S03.ml.ML
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.tuning.ParamGridBuilder

final case class DecisionTree(basePipeline: Pipeline, processing: Pipeline) extends ML.ClassificationModel[DecisionTreeClassifier] {

  val model: DecisionTreeClassifier = new DecisionTreeClassifier()
    .setLabelCol("target_index")
    .setFeaturesCol("features")
    .setMaxBins(25000)

  val paramGrid: Array[ParamMap] = new ParamGridBuilder()
    .baseOn(basePipeline.stages -> (processing.getStages :+ model :+ Label.converter))
    .build()

}

