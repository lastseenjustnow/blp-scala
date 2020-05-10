package rates

import com.bloomberglp.blpapi.{Session, SessionOptions}

trait SessionTrait {

  val serverHost = "192.168.1.142" // establish connection to a port which is listening Bloomberg
  val serverPort = 8200

  def createSession: Session = {
    val sessionOptions = new SessionOptions()
    sessionOptions.setServerHost(serverHost)
    sessionOptions.setServerPort(serverPort)
    System.out.println("Connecting to " + serverHost + ":" + serverPort)
    val session = new Session(sessionOptions)
    if (!session.start()) {
      System.err.println("Failed to start session.")
    } else {
      System.out.println("Connection with Bloomberg successfully established!")
    }
    session
  }
}
