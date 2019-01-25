package com.rhdzmota.presentations.S03.config

import com.rhdzmota.presentations.Settings
import org.apache.spark.sql.SparkSession

trait Context {
  import Settings.S03.Spark.{Label, Value}
  val spark: SparkSession = SparkSession.builder
    .appName(Settings.S03.Spark.name)
    .config(Label.master, Value.master)
    .config(Label.driverMem, Value.driverMem)
    .config(Label.executorMem, Value.executorMem)
    .config("spark.driver.maxResultSize", "3g")
    .getOrCreate()

  //spark.sparkContext.setLogLevel("WARN")
}
