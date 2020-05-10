package rates.struct

sealed trait Auth

case class Login(user: String, password: String) extends Auth

case class MSSQLCredentials(
                        server: String,
                        database: String,
                        credentials: Login
                      )