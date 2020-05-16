package rates

import java.sql.Connection

import rates.struct.ExchangeRateDataRow

trait TSQL {

  implicit def convertIntToBoolean(b: Int): Boolean = if (b == 1) true else false

  def isTableEmpty(tbname: String, conn: Connection): Boolean = {

    val sql =
      s"""
         |SELECT
         | CASE
         |   WHEN EXISTS(Select 1 from dbo.$tbname) THEN 1 ELSE 0
         | END AS IsEmpty
         |""".stripMargin

    val stmt = conn.createStatement()
    stmt.executeQuery(sql).getInt(1)

  }

  def insertData(tbname: String, data: ExchangeRateDataRow, conn: Connection): Unit = {
    try {
      val sql =
        s"""
           |INSERT INTO dbo.${tbname}(
           |[Date]
           |,[CurrencyCode]
           |,[CrossCurrencyCode]
           |,[CrossCurrencyRate]) VALUES (
           |FORMAT(DATEADD(DAY, -1, CURRENT_TIMESTAMP),'yyyy-MM-dd 00:00:00'),
           |?,
           |?,
           |?
           |)""".stripMargin
      val pstmt = conn.prepareStatement(sql)
      pstmt.setString(1, data.currencyCode)
      pstmt.setString(2, data.crossCurrencyCode)
      pstmt.setDouble(3, data.crossCurrencyRate)
      pstmt.executeUpdate()
      println(
        s"""
           |Values
           |${data.currencyCode},
           |${data.crossCurrencyCode},
           |${data.crossCurrencyRate}
           |inserted into dbo.$tbname""".stripMargin)
      conn.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
