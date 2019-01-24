package com.rhdzmota.presentations.S03.ml

import org.apache.spark.ml.evaluation.{MulticlassClassificationEvaluator, RegressionEvaluator}
import javax.xml.transform.stream.StreamResult
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.tuning.CrossValidator
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Dataset, Row}
import org.dmg.pmml.PMML
import org.jpmml.model.JAXBUtil
import org.jpmml.sparkml.PMMLBuilder
import java.io.BufferedWriter
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}

import com.rhdzmota.presentations.S03.ml.classification.{DecisionTree, LogisticReg, RandomForest}
import com.rhdzmota.presentations.Settings

sealed trait ML[T] {
  def basePipeline: Pipeline
  def processing: Pipeline
  def model: T
  def paramGrid: Array[ParamMap]
}

object ML {

  trait ClassificationModel[T] extends ML[T]
  trait RegressionModel[T] extends ML[T]

  object Implicits {
    implicit class PMMLModel(pipelineModel: PipelineModel) {
      val precision = 0.01
      val zeroThreshold = 1e-5
      def toPMML(schema: StructType, verificationData: Option[Dataset[Row]] = None): PMML = {
        val pmmlBuilder = new PMMLBuilder(schema, pipelineModel)
        if (verificationData.nonEmpty) pmmlBuilder.verify(verificationData.get, precision, zeroThreshold).build()
        else pmmlBuilder.build()
        pmmlBuilder.build()
      }
      def exportPMML(schema: StructType, name: String, verificationData: Option[Dataset[Row]] = None): Boolean = {
        val modelPath: Path = Paths.get("resources", "output", "model")
        if (!Files.exists(modelPath)) Files.createDirectory(modelPath)
        val pmmlFile: Path = modelPath.resolve(
          Settings.S03.Spark.Model.file.replaceAll("name", name))
        val modelWriter: BufferedWriter = Files.newBufferedWriter(pmmlFile, StandardCharsets.UTF_8,
          StandardOpenOption.WRITE,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING)
        val pmml: PMML = pipelineModel.toPMML(schema, verificationData)
        pmml.addExtensions()
        val writeOp = scala.util.Try(JAXBUtil.marshalPMML(pmml, new StreamResult(modelWriter))).toOption
        modelWriter.close()
        writeOp.nonEmpty
      }
    }
  }

  object Classification {
    val evaluator: MulticlassClassificationEvaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("target_index")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    final case class GridSearch(
                               processing: Pipeline,
                               crossValidationFolds: Int = 1,
                               parallelism: Int = 1
                               ) {

      // Define an empty pipeline
      val basePipeline: Pipeline = new Pipeline()

      // List the candidate ML Classification models
      val randomForest: RandomForest = RandomForest(basePipeline, processing)
      val logisticReg: LogisticReg = LogisticReg(basePipeline, processing)
      val decisionTree: DecisionTree = DecisionTree(basePipeline, processing)

      // Define parameter grid
      val paramGrid: Array[ParamMap] = logisticReg.paramGrid //randomForest.paramGrid

      // Cross Validation
      val crossValidator: CrossValidator = new CrossValidator()
        .setEstimator(basePipeline)
        .setEvaluator(evaluator)
        .setEstimatorParamMaps(paramGrid)
        .setNumFolds(crossValidationFolds)
        .setParallelism(parallelism)

      def fit[A](train: Dataset[A]): PipelineModel =
        crossValidator.fit(train).bestModel.asInstanceOf[PipelineModel]
    }


  }

}
