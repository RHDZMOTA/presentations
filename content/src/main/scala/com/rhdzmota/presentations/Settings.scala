package com.rhdzmota.presentations

import com.typesafe.config.{Config, ConfigFactory}
import collection.JavaConversions._

object Settings {
  private val app: Config = ConfigFactory.load().getConfig("application")

  object S03 {
    private val s03: Config = app.getConfig("S03")
    object Spark {
      private val spark: Config = s03.getConfig("spark")
      val name: String = spark.getString("name")
      object Label {
        private val label: Config = spark.getConfig("label")
        val master: String = label.getString("master")
        val executorMem: String = label.getString("executorMem")
        val driverMem: String = label.getString("driverMem")
      }
      object Value {
        private val value: Config = spark.getConfig("value")
        val master: String = value.getString("master")
        val executorMem: String = value.getString("executorMem")
        val driverMem: String = value.getString("driverMem")
      }
      object Processing {
        private val processing: Config = spark.getConfig("processing")
        val lags: Int = processing.getInt("lags")
      }
      object Model {
        private val model: Config = spark.getConfig("model")
        val file: String = model.getString("file")
        val crossValidationFold: Int = model.getInt("crossValidationFold")
        val parallelism: Int = model.getInt("parallelism")
        object Classification {
          private val classification: Config = model.getConfig("classification")
          // Logistic Regression Configuration
          object LogisticRegression {
            private val logisticRegression: Config = classification.getConfig("logisticRegression")
            val maxIter: Array[Int] =
              logisticRegression.getIntList("maxIter").toList.map(_.toInt).toArray
            val regParam: Array[Double] =
              logisticRegression.getDoubleList("regParam").toList.map(_.toDouble).toArray
            val elasticNetParam: Array[Double] =
              logisticRegression.getDoubleList("elasticNetParam").toList.map(_.toDouble).toArray
          }
          // Random Forest Configuration
          object RandomForest {
            private val randomForest: Config = classification.getConfig("randomForest")
            val numTrees: Array[Int] = randomForest.getIntList("numTrees").toList.map(_.toInt).toArray
            val maxDepth: Array[Int] = randomForest.getIntList("maxDepth").toList.map(_.toInt).toArray
          }
        }
      }
    }
    object Data {
      private val data: Config = s03.getConfig("data")
      val source: String = data.getString("source")
    }
  }


}
