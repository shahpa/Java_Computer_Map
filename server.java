/*

		------------Written by Parth Shah------------



		This is a small program for socket programming.
		This file runs at server side. I am accessing a Log-file
		from server. This file is updated at every second with current 
		status of all PCs in Library. So this file sends data from last
		minute to client. This program sends a data to client in appanding
		it in just one variable.




*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.lang.*;
import java.util.*;
import java.net.InetAddress;


public class server
{
	Socket socket = new Socket();
	public void connection()	// Set up the connection for socket connection
	{
		try
		{
			int serverPort = 9998;
			ServerSocket ss = new ServerSocket(serverPort);
			socket = ss.accept();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	
	
	
	public void socketProgram(String str)		// Sends a String to Socket
	{	System.out.println("At socket function..........."+str);
		
		
		
		try
		{
			
			
		
			OutputStream sout = socket.getOutputStream();

			DataOutputStream out = new DataOutputStream(sout);
		
			
				//System.out.println(str);
				out.writeUTF(str);
		
				out.flush();
			
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}


	}

	public String fileRead()		// Reads a file from perticular locaion
	{
		
		String flag;
		String mergeString = null;
		BufferedReader br = null;
		try
		{
			
			String sCurrentLine=null,tmp=null;
			br = new BufferedReader(new FileReader("C:/Program Files/EnvisionWare/PC Reservation/Management Console/pcres.log"));
			
				while((sCurrentLine = br.readLine())!= null)	//Reading a file till end
				{
					tmp = sCurrentLine;
					StringTokenizer tokens = new StringTokenizer(tmp," ");
					while(tokens.hasMoreTokens())
					{
						String word = tokens.nextToken();
						if(word.equals(currentDate()))
						{
							
							word = tokens.nextToken();
							if(word.contains(currentTime()))
							{	
								
								mergeString = mergeString + "%" +tmp;	//Merging all string with % sign
								
							}
							
						}
					}
				
				}
			br.close();	
			
			
		}
		catch(Exception x)
		{
			System.out.println(x.toString());
		}
		return mergeString;
	}

	public String currentTime()		// Gives a current time(hr:min)
	{
		Calendar cal = Calendar.getInstance();
		cal.getTime();					//setting time 1 minute behind the current time
		int min = cal.get(Calendar.MINUTE);
		if(min!= 0)
			min--;
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		
		String time = hr+":"+min;
		
		
		return time;
	}

	public String currentDate()		// Gives current Date(yyyy/MM/dd)
	{
		//Calendar cal = Calendar.getInstance();
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String cDate = dateFormat.format(date);
		//System.out.println(cDate);
		return cDate;
	}

	public static void main(String args[])
	{
		
		server obj = new server();
		obj.connection();	//Establish a secure socket Connection between server and client
		try
		{
			
			while(true)
			{
			
				String str =	obj.fileRead();		//Reads a Log file from EnvisionWare Software

				//System.out.println(str);
			
	
				if(str!=null)
				{
					obj.socketProgram(str);		//Sends data to Socket
					
				}
				Thread.sleep(30000);		//Program sleepsfor 10 seconds
			}
			
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}	
		
	}
	
}