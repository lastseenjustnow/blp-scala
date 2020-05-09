import com.bloomberglp.blpapi.{Session, SessionOptions}

import util.control.Breaks._

class SimpleCurrRatesRequest {

  val serverHost = "127.0.0.1"
  val serverPort = 8194
  val serviceName = "//blp/refdata"

  @throws[Exception]
  def main: Unit = {
    val example = new SimpleCurrRatesRequest
    example.run
  }

  def run: Unit = {
    val sessionOptions = new SessionOptions()
    sessionOptions.setServerHost(serverHost)
    sessionOptions.setServerPort(serverPort)
    System.out.println("Connecting to " + serverHost + ":" + serverPort)
    val session = new Session(sessionOptions)
    if (!session.start()) {
      System.err.println("Failed to start session.")
    } else {
      System.out.println("Connected successfully.")
    }
    if (session.openService(serviceName)) {
      val request = session.getService(serviceName).createRequest("ReferenceDataRequest")

      request.append("securities", "USDJPY Curncy")
      request.append("securities", "CADUSD Curncy")
      request.append("securities", "USDAUD Curncy")
      request.append("securities", "USDSGD Curncy")
      request.append("securities", "USDTHB Curncy")
      request.append("securities", "USDCHF Curncy")
      request.append("securities", "USDINR Curncy")
      request.append("securities", "USDMYR Curncy")
      request.append("securities", "XAUUSD Curncy")
      request.append("securities", "XAGUSD Curncy")

      request.append("fields", "PX_BID")

      while (true) {
        session.sendRequest(request, null);

        val event = session.nextEvent
        val msgIter = event.messageIterator

        breakable {
          while (true) {
            if (!msgIter.hasNext) {
              break
            }
            val msg = msgIter.next
            val topic = msg.correlationID
            System.out.println(topic + ": " + msg.asElement)
          }
        }
      }
    }
    else {
      System.err.println("Failed to open " + serviceName)
      session.stop()
    }
    Thread.sleep(10 * 1000)
  }
}