package rates

import java.sql.{Connection, DriverManager}

import rates.struct.MSSQLCredentials

trait MSSQLConnection {

  def openMSSQLConnection(cred: MSSQLCredentials): Connection = {

    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    } catch {case e => e.printStackTrace()}

    val jdbcUrl = s"jdbc:sqlserver://${cred.server}:1433;" +
      s"database=${cred.database};" +
      s"user=${cred.user};" +
      s"password=${cred.password}"

    val driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver"


    Class.forName(driverClass).getDeclaredConstructor().newInstance()

    try {
      DriverManager.getConnection(jdbcUrl)
    } catch {
      case e: Exception => {e.printStackTrace(); throw e}
    }

  }

  def insertData(data: (String, Double), conn: Connection): Unit = {
    try {
      val sql = "INSERT INTO dbo.BloombergData VALUES (?, ?, CURRENT_TIMESTAMP)"
      val pstmt = conn.prepareStatement(sql)
      pstmt.setString(1, data._1)
      pstmt.setDouble(2, data._2)
      pstmt.executeUpdate()
      println(s"Values ${data._1}, ${data._2} inserted into dbo.BloombergData")
      conn.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
