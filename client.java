
//           ----------------- Written by Parth Shah -------------------

/*
 * This is a client side socket program which takes a data sent from server about current PC status.
 * Through socket this program gets a string containing data. In statusCheck() method fisrt we split 
 * all string by special character '%'. Then we are checking each separated string for special 
 * variables in if-else loop and generate a JSON response. 
 */


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.*;
import java.util.*;
import java.lang.Object;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import org.json.simple.JSONObject;
public class client {


	public static FileWriter file = null;
	public JSONObject jsonObj = new JSONObject();


public void socketProgram() 
{
    int serverPort = 9998; // port number same as of server port number.
    String address = "132.194.70.107";	// IpAddress of a server 'Laxmi'
    try
    {
        InetAddress ipAddress = InetAddress.getByName(address);	
        System.out.println("Server with IP address "+address+" and port "+serverPort); 
        Socket socket = new Socket(ipAddress, serverPort);
        
        
        InputStream sin = socket.getInputStream();
        
        
        DataInputStream in = new DataInputStream(sin);
        
        
        
        
        
        while(true)
        {
	String line = null;
           
	line = in.readUTF();
	
	statusCheck(line);	//Method that check the specific variables
	
        }
        
    }
    catch(Exception x)
    {
        System.out.println(x.toString());
    }

}

@SuppressWarnings("unchecked")
public void statusCheck(String str) throws IOException
{
	
	String[] resultString = str.split("%");	// Splitting string by '%' sign.
	for (int i=0;i<resultString.length;i++)
	{
		
		StringTokenizer tokens = new StringTokenizer(resultString[i]," ");
		while(tokens.hasMoreTokens())
		{
			String word = tokens.nextToken();
			if(word.equals("pcrMSG_SESSIONSTART"))
			{
				jsonObj.put("TIME", currentTime());
				jsonObj.put("PC_STATUS",word);
				word = tokens.nextToken();
				word = tokens.nextToken();
				System.out.println("Session Started at PC "+word);
				jsonObj.put("PC_NAME", word);
				file.write(jsonObj.toJSONString());
				file.flush();
			}
			else if(word.equals("pcrMSG_CURRENTSTATUS"))
			{
				jsonObj.put("PC_STATUS",word);
				word = tokens.nextToken();
				word = tokens.nextToken();
				System.out.println("Current status is IN-USE at PC "+word);
				jsonObj.put("PC_NAME", word);
				file.write(jsonObj.toJSONString());
				file.flush();
			}
			else if(word.equals("pcrMSG_LOSTCONNECTION"))
			{
				jsonObj.put("PC_STATUS",word);
				word = tokens.nextToken();
				word = tokens.nextToken();
				System.out.println("Connection lost at PC "+word);
				jsonObj.put("PC_NAME", word);
				file.write(jsonObj.toJSONString());
				file.flush();
			}
			else if(word.equals("pcrREQ_SESSIONEND"))
			{
				jsonObj.put("PC_STATUS",word);
				word = tokens.nextToken();
				word = tokens.nextToken();
				System.out.println("Session Ended at PC "+word);
				jsonObj.put("PC_NAME", word);
				file.write(jsonObj.toJSONString());
				file.flush();
			}
			
		}
		
	}
}

public String currentTime()
{
	Calendar cal = Calendar.getInstance();
	cal.getTime();
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	return( sdf.format(cal.getTime()) );
}
public static void main(String args[])
{
try
{
	file = new FileWriter("/home/parth/Desktop/Server/test.json");
    	client obj = new client();
    	obj.socketProgram();
	//System.out.println(line);
}
catch(Exception e)
{
	System.out.println(e);
}
}
}

