package rates

import com.bloomberglp.blpapi.Event.EventType
import com.bloomberglp.blpapi.{Event, Session}
import rates.struct.{ExchangeRateDataRow, MSSQLCredentials}

class SimpleCurrRatesRequest
  extends SessionTrait
    with MSSQLConnection
    with TSQL
{

  private val serviceName = "//blp/refdata"

  private val securities = Array(
    "USDJPY Curncy",
    "CADUSD Curncy",
    "USDAUD Curncy",
    "USDSGD Curncy",
    "USDTHB Curncy",
    "USDCHF Curncy",
    "USDCNY Curncy",
    "USDINR Curncy",
    "USDMYR Curncy",
    "XAUUSD Curncy",
    "XAGUSD Curncy",
    "USDCAD Curncy",
    "USDCNH Curncy",
    "USDEUR Curncy",
    "USDGBP Curncy",
    "USDXAU Curncy",
    "USDXAG Curncy"
  )

  private val fields = Array("PX_BID")

  def run(cred: MSSQLCredentials): Unit = {

    implicit val session: Session = createSession

    if (session.openService(serviceName)) {
      val request = session.getService(serviceName).createRequest("ReferenceDataRequest")

      securities.foreach(sec => request.append("securities", sec))
      fields.foreach(sec => request.append("fields", sec))

      session.sendRequest(request, null)

      extractData.foreach(data => insertData(cred.table, data, openMSSQLConnection(cred)))

    }
    else {
      System.err.println("Failed to open " + serviceName)
    }
  }

  def extractData(implicit session: Session): Array[ExchangeRateDataRow] = {

    val constantValues = Array(
      ExchangeRateDataRow("USD", "AED", 3.672),
      ExchangeRateDataRow("USD", "USD", 1)
    )

    var done = false
    var arr: Array[ExchangeRateDataRow] = Array()
    while (!done) {
      val event = session.nextEvent
      if (event.eventType() == EventType.PARTIAL_RESPONSE) { // due to request size restrictions. See Bloomberg Core Developer Guide. https://www.bloomberg.com/professional/download/blpapi-core-developer-guide/
        arr = arr ++ processResponseEvent(event)
      } else if (event.eventType() == EventType.RESPONSE) {
        arr = arr ++ processResponseEvent(event) ++ constantValues
        done = true
        session.stop()
        arr
      }
    }
    arr
  }

  def processResponseEvent(event: Event): Array[ExchangeRateDataRow] = {
    val securityDataArray = event.messageIterator.next.asElement.getElement("securityData")
    val smth = for (i <- 0 until securityDataArray.numValues)
      yield securityDataArray.getValueAsElement(i)
    smth.map {
      v => ExchangeRateDataRow(
        v.getElementAsString("security").substring(0,3),
        v.getElementAsString("security").substring(3,6),
        v.getElement("fieldData").getElementAsFloat64("PX_BID")
      )
    }.toArray // TODO: fix that nightmare
  }

}
