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
      object Model {
        private val model: Config = spark.getConfig("model")
        val file: String = model.getString("model")
        val crossValidationFold: Int = model.getInt("crossValidationFold")
        val parallelism: Int = model.getInt("parallelism")
        object Classification {
          private val classification: Config = model.getConfig("classification")
          // Logistic Regression Configuration
          object LogisticRegression {
            private val logisticRegression: Config = classification.getConfig("logisticRegression")
            val maxIter: Array[Double] =
              logisticRegression.getDoubleList("maxIter").toList.map(_.toDouble).toArray
            val regParam: Array[Double] =
              logisticRegression.getDoubleList("regParam").toList.map(_.toDouble).toArray
            val elasticNetParam: Array[Double] =
              logisticRegression.getDoubleList("elasticNetParam").toList.map(_.toDouble).toArray
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
