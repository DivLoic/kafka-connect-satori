package fr.xebia.ldi.satori.source

import org.scalatest.{BeforeAndAfterEach, FlatSpec, GivenWhenThen, Matchers}

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}

/**
  * Created by loicmdivad
  */
class SatoriSourceConfigSpec extends FlatSpec with Matchers with GivenWhenThen with BeforeAndAfterEach {

  val basicConf = Map(
    "topic" -> "MY-TOPIC",
    "satori.endpoint" -> "wss://open-data.api.satori.com",
    "satori.appkey" -> "3DfdDEbe3B08cd0CA3Af3e1dbB8D5B8B",
    "satori.auth" -> "False",
    "satori.role" -> "foo",
    "satori.rolekey" -> "bar",
    "satori.channel" -> "github-events"
  )

  "validate" should "validate a good configuration object" in {
    Given("a valid configuration file")
    val input: Map[String, String] = basicConf

    When("a task load the configuration")
    val config = new SatoriSourceConfig(input)

    Then("the validation pass")
    config.validate shouldBe a [Success[_]]
  }

  it should "reject an invalid endpoint syntax" in {
    Given("a set of multiple endpoints")
    val endpoint0 = "wss://j1E7OBon.api.satori.com"
    val endpoint1 = "wss://open-data.api.satori.com"
    val endpoint2 = "wss://foo,bar&baz.com"
    val endpoint3 = "mutmut@bladibla.fr"

    When("the endpoints are used in a configuration")
    val conf0 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint0))
    val conf1 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint1))
    val conf2 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint2))
    val conf3 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint3))

    Then("the validation failed")
    conf0.validate shouldBe a [Success[_]]
    conf1.validate shouldBe a [Success[_]]
    conf2.validate shouldBe a [Failure[_]]
    conf3.validate shouldBe a [Failure[_]]
  }

  "endPoint" should "respect a pattern" in {
    Given("a set of multiple endpoints")
    val endpoint0 = "wss://j1E7OBon.api.satori.com"
    val endpoint1 = "wss://open-data.api.satori.com"
    val endpoint2 = "wss://foo,bar&baz.com"
    val endpoint3 = "mutmut@bladibla.fr"

    When("the endpoints are used in a configuration")
    val conf0 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint0))
    val conf1 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint1))
    val conf2 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint2))
    val conf3 = new SatoriSourceConfig(basicConf + ("satori.endpoint" -> endpoint3))

    Then("the enpoint parsing should failed on bad address")
    conf0.endPoint shouldBe defined
    conf1.endPoint shouldBe defined
    conf2.endPoint should not be defined
    conf3.endPoint should not be defined
  }

  "auth" should "required the role and roleKey" in {
    Given("a set combinations of role & roleKey")
    val role0 = ("", "")
    val role1 = ("foo", "")
    val role2 = ("", "bar")
    val role3 = ("foo", "bar")

    When("the auth paramater is true")
    val authPam = "true"

    val conf0 = new SatoriSourceConfig(basicConf +
      ("satori.auth" -> authPam, "satori.role" -> role0._1, "satori.rolekey" -> role0._2))
    val conf1 = new SatoriSourceConfig(basicConf +
      ("satori.auth" -> authPam, "satori.role" -> role1._1, "satori.rolekey" -> role1._2))
    val conf2 = new SatoriSourceConfig(basicConf +
      ("satori.auth" -> authPam, "satori.role" -> role2._1, "satori.rolekey" -> role2._2))
    val conf3 = new SatoriSourceConfig(basicConf +
      ("satori.auth" -> authPam, "satori.role" -> role3._1, "satori.rolekey" -> role3._2))

    Then("then both role and roleKey are checked")
    conf0.validate shouldBe a [Failure[_]]
    conf1.validate shouldBe a [Failure[_]]
    conf2.validate shouldBe a [Failure[_]]
    conf3.validate shouldBe a [Success[_]]
  }

}
