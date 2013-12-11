import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Subject
{

	private Socket connectionToServer = null;
	private PrintWriter outStream = null;
	private BufferedReader inStream = null;
	
	public Client(Observer observer) 
	{
		setObserver(observer);
	}

	// set up a connection to a specified server and port
	public void connect(String serverAddress, int serverPort) throws IOException 
	{
		log("Connecting to the most amazing server ever being made...");

		// Set up a socket and connect it to our server
		connectionToServer = new Socket(serverAddress, serverPort);

		// The getOutputStream and getInputStream methods return byte streams
		outStream = new PrintWriter(new OutputStreamWriter(
				connectionToServer.getOutputStream()));
		inStream = new BufferedReader(new InputStreamReader(
				connectionToServer.getInputStream()));

		log("Connected!");
	}

	// close the streams and the socket
	public void close() 
	{
		log("Closing connection.");
		try {
			inStream.close();
			outStream.close();
			connectionToServer.close();
		} 
		catch (IOException e) {
			logError("Failed to properly close the connection. "
					+ e.getMessage());
		}

		log("Connection closed.");
	}

	// write a message to the stream going to the server
	public void send(String message) 
	{
		log("Sending...");
		outStream.println(message);
		outStream.flush();
		log("Message sent.");
	}

	// read a message from the stream coming from the server
	
	public void receive() {
		
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				while(true)
				{
					log("Receiving...");
					String receivedMessage = "";
					
					try {
						receivedMessage = inStream.readLine();
						log("Message received: "+receivedMessage);
						notifyObservers();
					} 
					catch (IOException e) {
						logError("Receive failed! " + e.getMessage());
					}
				}
			}
		}).start();
	}
	private void log(String message) {
		System.out.println("Client: " + message);
	}
	private void logError(String message) {
		System.err.println("Client: " + message);
	}
	
}