package rates

import com.bloomberglp.blpapi.Event.EventType
import com.bloomberglp.blpapi.{Event, Session}

class SimpleCurrRatesRequest extends SessionTrait {

  private val serviceName = "//blp/refdata"

  private val securities = Array("USDJPY Curncy", "CADUSD Curncy", "USDAUD Curncy", "USDSGD Curncy", "USDTHB Curncy", "USDCHF Curncy", "USDCNY Curncy", "USDINR Curncy", "USDMYR Curncy", "XAUUSD Curncy", "XAGUSD Curncy")
  private val fields = Array("PX_BID")

  def run: Unit = {

    implicit val session: Session = createSession

    if (session.openService(serviceName)) {
      val request = session.getService(serviceName).createRequest("ReferenceDataRequest")

      securities.foreach(sec => request.append("securities", sec))
      fields.foreach(sec => request.append("fields", sec))

      session.sendRequest(request, null)

      extractData

    }
    else {
      System.err.println("Failed to open " + serviceName)
    }
  }

  def extractData(implicit session: Session): Unit = {
    var done = false
    while (!done) {
      val event = session.nextEvent
      if (event.eventType() == EventType.PARTIAL_RESPONSE) {
        processResponseEvent(event)
      } else if (event.eventType() == EventType.RESPONSE) {
        processResponseEvent(event)
        done = true
        session.stop()
      }
    }

  }

  def processResponseEvent(event: Event): Unit = {

    val securityDataArray = event.messageIterator.next.asElement.getElement("securityData")
    val smth = for (i <- 0 until securityDataArray.numValues)
      yield securityDataArray.getValueAsElement(i)
    val map = smth.map {
      v => (v.getElementAsString("security"), v.getElement("fieldData").getElementAsFloat64("PX_BID"))
    }
    map.foreach(println)

  }

}