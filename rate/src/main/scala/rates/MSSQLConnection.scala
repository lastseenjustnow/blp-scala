package rates

import java.sql.{Connection, DriverManager}

import rates.struct.{ExchangeRateDataRow, MSSQLCredentials}

trait MSSQLConnection {

  def openMSSQLConnection(cred: MSSQLCredentials): Connection = {

    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    } catch {case e: Throwable => e.printStackTrace()}

    val jdbcUrl = s"jdbc:sqlserver://${cred.server}:1433;" +
      s"database=${cred.database};" +
      s"user=${cred.credentials.user};" +
      s"password=${cred.credentials.password}"

    val driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver"

    Class.forName(driverClass).getDeclaredConstructor().newInstance()

    try {
      DriverManager.getConnection(jdbcUrl)
    } catch {
      case e: Exception => e.printStackTrace(); throw e
    }

  }

  def insertData(data: ExchangeRateDataRow, conn: Connection): Unit = {
    try {
      val sql =
        s"""
           |INSERT INTO dbo.ExchangeRateData(
           |[Date]
           |,[CurrencyCode]
           |,[CrossCurrencyCode]
           |,[CrossCurrencyRate]) VALUES (
           |CURRENT_TIMESTAMP,
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
           |inserted into dbo.ExchangeRateData""".stripMargin)
      conn.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
