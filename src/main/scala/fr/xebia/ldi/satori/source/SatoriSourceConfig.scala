package fr.xebia.ldi.satori.source

import java.util

import fr.xebia.ldi.satori.common.SatoriConstant._
import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}
import org.apache.kafka.connect.data.{Schema, SchemaBuilder}

import scala.util.{Failure, Success, Try}


/**
  * Created by loicmdivad
  */
object SatoriSourceConfig {

  val TOPIC = "topic"

  val ENDPOINT = "satori.endpoint"
  val APPKEY = "satori.appkey"
  val AUTH = "satori.auth"
  val ROLE = "satori.role"
  val ROLEKEY = "satori.rolekey"
  val CHANNEL = "satori.channel"

  val CONFIG_DEF: ConfigDef = new ConfigDef()
    .define(TOPIC, Type.STRING, Importance.HIGH, "destination topic")
    .define(ENDPOINT, Type.STRING, Importance.HIGH, "websocket endpoint delivered by satori")
    .define(APPKEY, Type.STRING, Importance.HIGH, "application key of the selected channel")
    .define(AUTH, Type.STRING, Importance.HIGH, "whether authentication is enable or not")
    .define(ROLE, Type.STRING, "", Importance.MEDIUM, "role used for a private channel")
    .define(ROLEKEY, Type.STRING, "", Importance.MEDIUM, "role key used for a private channel")
    .define(CHANNEL, Type.STRING, Importance.HIGH, "the source channel streamed from satori")

  val SRC_SCHEMA: Schema = SchemaBuilder.struct()
    .name("fr.xebia.ldi.source.satori.schema")
    .field("channel", Schema.STRING_SCHEMA)
    .field("position", Schema.INT64_SCHEMA)
    .field("message", Schema.STRING_SCHEMA)
    .build()

  case class SatoriSrcRecord(channel: String, offset: Long, message: String)

}

import fr.xebia.ldi.satori.source.SatoriSourceConfig._

/**
  * Created by loicmdivad
  *
  * @param props java util map from the start hook of task/connector
  */
class SatoriSourceConfig(props: util.Map[String, String]) extends AbstractConfig(CONFIG_DEF, props) {

  val topic: String = getString(TOPIC)
  val appkey: String = getString(APPKEY)
  val channel: String = getString(CHANNEL)
  val auth: Boolean = getString(AUTH).toLowerCase().toBoolean

  val role: String = if(auth) getString(ROLE) else ""
  val roleKey: String = if(auth) getString(ROLEKEY) else ""

  val endPoint: Option[String] = END_POINT_REGEX.findFirstIn(getString(ENDPOINT))

  def validate: Try[Unit] = (role, roleKey, endPoint) match {
    case (_, "", _) | ("", _, _) if auth =>
      Failure(new IllegalArgumentException(s"$ROLE & $ROLEKEY has to be initialised when auth is required."))
    case (_, _, None) =>
      Failure(new IllegalArgumentException(s"The endpoint (${getString(ENDPOINT)}) does not match: $END_POINT_REGEX"))
    case _ => Success()
  }


}
