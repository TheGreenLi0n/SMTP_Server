import  java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;

    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";

        /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

        /* Create an SMTPConnection object. Create the socket and the associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException {
        connection = new Socket(envelope.DestAddr,2525)/* Fill in */;
        fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));/* Fill in */;
        toServer = new DataOutputStream(connection.getOutputStream());

        /* Fill in */


        /* Read a line from server and check that the reply code is 220. If not, throw an IOException. */
        String reply = fromServer.readLine();
        if (parseReply(reply)!=220){
            System.out.println("NOORT sennddd");
            throw new IOException("Reply code was not 220");
        }else
            System.out.println("Sendddd");
        /* Fill in */

	    /* SMTP handshake. We need the name of the local machine. Send the appropriate SMTP handshake command. */
        String localhost = "127.0.0.1";
        sendCommand("HELO " + localhost + "\r\n",250 /* Fill in */ );
        isConnected = true;
    }

        /* Send the message. Write the correct SMTP-commands in the correct order. No checking for errors, just throw them to the caller. */
    public void send(Envelope envelope) throws IOException {
	    /* Send all the necessary commands to send a message. Call sendCommand() to do the dirty work. Do _not_ catch the exception thrown from sendCommand(). */
        sendCommand("MAIL FROM: "+"<" + envelope.Sender+">" + "\r\n", 250);
        sendCommand("RCPT TO: " +"<"+ envelope.Recipient + ">" + "\r\n", 250);
        sendCommand("DATA\r", 354);
        sendCommand(envelope.Message + "\r\n"+".",250);
    }

        /* Close the connection. First, terminate on SMTP level, then close the socket. */
    public void close() {
        isConnected = false;
        try {
            sendCommand( "QUIT" + "\r\n",221);
            connection.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }

        /* Send an SMTP command to the server. Check that the reply code is what is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {
        /* Fill in */
        /* Write command to server and read reply from server. */
        toServer.writeBytes(command + "\r\n");
	    /* Check that the server's reply code is the same as the parameter rc. If not, throw an IOException. */
        if (parseReply(fromServer.readLine()) != rc){
            System.out.println("NOT SEND Command");
            throw new IOException("Reply code was not the same as RC");
        }else
            System.out.println("Command sent!");
    }

    /* Parse the reply line from the server. Returns the reply code. */
    private int parseReply(String reply) {
        StringTokenizer token = new StringTokenizer(reply);
        String rc = token.nextToken();
        return Integer.parseInt(rc);
    }

    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
        if(isConnected) {
            close();
        }
        super.finalize();
    }
}