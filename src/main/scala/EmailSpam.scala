import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD

object EmailSpam extends App {
  val conf = new SparkConf().setAppName("email-spam").setMaster("local[*]")
  val sc = new SparkContext(conf)
  val spam = sc.textFile("./enron1/spam/0052.2003-12-20.GP.spam.txt", 4)
  val normal = sc.textFile("./enron1/ham/0022.1999-12-16.farmer.ham.txt", 4)
  val tf = new HashingTF(numFeatures = 10000)
  val spamFeatures = spam.map(email => tf.transform(email.split(" ")))
  val normalFeatures = normal.map(email => tf.transform(email.split(" ")))
  val positiveExamples = spamFeatures.map(features => LabeledPoint(1, features))
  val negativeExamples = normalFeatures.map(features => LabeledPoint(0, features))
  val trainingData = positiveExamples.union(negativeExamples)
  trainingData.cache()
  val model = new LogisticRegressionWithSGD().run(trainingData)
  //Test on a positive example (spam) and a negative one (normal).
  val posTest = tf.transform(
    "insurance plan which change your life ...".split(" "))
  val negTest = tf.transform(
    "hi sorry yaar i forget tell you i cant come today".split(" "))
  println("Prediction for positive test example: " + model.predict(posTest))
  println("Prediction for negative test example: " + model.predict(negTest))

}