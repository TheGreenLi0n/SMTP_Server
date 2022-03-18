    import java.io.*;
    import java.net.*;
    import java.util.*;
     
    public class crudeClient {
     
       public static void main(String argv[])
          {
	   Scanner in = new Scanner(System.in);
	        
    	   try{
    		    Socket socketClient= new Socket("smtp2.bhsi.xyz",2525);
    		    System.out.println("Client: "+"Connection Established");
     
    		    BufferedReader reader = 
    		    		new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
     
    		    BufferedWriter writer= 
    	        		new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
    		    String s,serverMsg;
			s = in.nextLine();
    		    writer.write(s+"\r\n");
			writer.flush();
			s = in.nextLine();
    		    writer.write(s+"\r\n");
                	writer.flush();
    			while((serverMsg = reader.readLine()) != null){
    				System.out.println("Client: " + serverMsg);
    			}
     
    	   }catch(Exception e){e.printStackTrace();}
          }
    }
