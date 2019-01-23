package com.rhdzmota.presentations.S03

import com.rhdzmota.presentations.S03.config.Context
import com.rhdzmota.presentations.S03.data.DataProcessing

object Application extends Context {

  def main(args: Array[String]): Unit = {
    DataProcessing.WordOps.words.show()
    spark.close()
  }
}
