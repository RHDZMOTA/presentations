package com.rhdzmota.presentations.S03

import com.rhdzmota.presentations.S03.config.Context
import com.rhdzmota.presentations.S03.data.DataProcessing
import com.rhdzmota.presentations.S03.ml.ML
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.{Dataset, Row}

object Application extends Context {
  import com.rhdzmota.presentations.Settings.S03.Spark.Model._
  import ML.Implicits._

  // Get best model using grid-search and cross-validation
  val model: PipelineModel = ML.Classification.GridSearch(DataProcessing.pipeline, crossValidationFold, parallelism)
    .fit(DataProcessing.train)

  // Generate predictions for train and test
  val predTrain: Dataset[Row] = model.transform(DataProcessing.train)
  val predTest: Dataset[Row]  = model.transform(DataProcessing.test)

  // Calculate accuracy
  val accuracyTrain: Double = ML.Classification.evaluator evaluate predTrain
  val accuracyTest: Double  = ML.Classification.evaluator evaluate predTest

  // Generate prediction for complete dataset
  val prediction: Dataset[Row] = model.transform(DataProcessing.dataset)
  val accuracy: Double = ML.Classification.evaluator evaluate prediction

  def main(args: Array[String]): Unit = {

    // Save the model
    val modelExmported: Boolean = model.exportPMML(
      DataProcessing.dataset.schema, accuracyTest, None
    )

    // Save predictions
    prediction.write.option("header", value = true).csv(s"out-${accuracy}")

    spark.close()
  }
}
