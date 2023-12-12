import com.google.cloud.MonitoredResource
import com.google.cloud.logging.LogEntry
import com.google.cloud.logging.LoggingOptions
import com.google.cloud.logging.Payload.{JsonPayload, StringPayload}
import com.google.cloud.logging.Severity
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import java.util.Collections
import scala.collection.JavaConverters._

object GCPLoggingTestApp extends App {

  val logName = "application-logging-scala"

  //Construct a JSON payload for test
  val jsonPayload = Map[String, Any](
    "stringValue1" -> "stringValue1",
    "stringValue2" -> "stringValue2",
    "integerValue" -> 100,
    "doubleValue"-> 18.0
  )

  val payload = JsonPayload.of(jsonPayload.asJava)

  val logging = LoggingOptions.getDefaultInstance.getService
  try {
    val entry = LogEntry.newBuilder(payload)
      .setSeverity(Severity.WARNING)
      .setLogName(logName)
      .setResource(MonitoredResource.newBuilder("global").build)
      .build

    logging.write(Collections.singleton(entry))

    // Optional - flush any pending log entries just before Logging is closed
    logging.flush()
  } finally if (logging != null) logging.close()

  System.out.printf("Logged: %s%n", jsonPayload)

}