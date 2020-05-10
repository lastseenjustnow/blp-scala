package rates

import com.bloomberglp.blpapi.{Session, SessionOptions}

trait SessionTrait {

  val serverHost = "127.0.0.1"
  val serverPort = 8194

  def createSession: Session = {
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
    session
  }
}
