package rates

import java.sql.{Connection, DriverManager}

import rates.struct.MSSQLCredentials

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
}
