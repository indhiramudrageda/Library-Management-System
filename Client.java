/* This program is provide an interface at the client end.
   To compile: javac Client.java
   To execute: java ClientSide localhost portno 
*/

import java.net.*;
import java.io.*;
import java.util.*;

class ClientSide
{
   public static void main(String [] args)
   {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      try
      {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out =new DataOutputStream(outToServer);              
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =new DataInputStream(inFromServer);               
         System.out.println(in.readUTF());
		 System.out.println(in.readUTF());
		 int input;
		 do{
		   System.out.println(in.readUTF());
		   System.out.println(in.readUTF());
		   System.out.println(in.readUTF());
		   Scanner sc=new Scanner(System.in);
		   int i=sc.nextInt();
		   out.writeUTF(Integer.toString(i));
		   switch(i)
		   {
		     case 1:
				   System.out.println(in.readUTF());
			       System.out.println(in.readUTF());
				   String id=sc.next();
				   out.writeUTF(id);
				   System.out.println(in.readUTF());
				   String name=sc.next();
				   out.writeUTF(name);
				   System.out.println(in.readUTF());
				   String phn=sc.next();
				   out.writeUTF(phn);
				   System.out.println(in.readUTF());
				   String address=sc.next();
				   out.writeUTF(address);
				   System.out.println(in.readUTF());
				   break;
		    case 2:
			       System.out.println(in.readUTF());
			       String id2=sc.next();
				   out.writeUTF(id2);
			       int count2=Integer.parseInt(in.readUTF());
				   if(count2==0)
				     System.out.println(in.readUTF());
				   else
				   {
				     System.out.println(in.readUTF());
				     int choice=sc.nextInt();
				     out.writeUTF(Integer.toString(choice));
				     if(choice==1){
				       System.out.println(in.readUTF());
				       String newval=sc.next();
					   out.writeUTF(newval);
					 }
				     else if(choice==2){
				       System.out.println(in.readUTF());
					   String newval=sc.next();
					   out.writeUTF(newval);
					 }
				     else
				       System.out.println(in.readUTF());
				    }
					break;
		    case 3:
			        System.out.println(in.readUTF());
			        id=sc.next();
				    out.writeUTF(id);
			        System.out.println(in.readUTF()); 
			        break;
		    case 4:
			        System.out.println(in.readUTF());
				    System.out.println(in.readUTF());
				    out.writeUTF(sc.next());
				    System.out.println(in.readUTF());
				    out.writeUTF(sc.next());
				    System.out.println(in.readUTF());
				    out.writeUTF(sc.next());
				    System.out.println(in.readUTF());
				    out.writeUTF(sc.next());
				    System.out.println(in.readUTF());
				    break;
			case 5:
			        System.out.println(in.readUTF());
				    out.writeUTF(sc.next()); 
				    int flag=Integer.parseInt(in.readUTF());
				    if(flag==1)
				    {
				       System.out.println(in.readUTF());
				       System.out.println(in.readUTF());
				       out.writeUTF(sc.next());
				    }
				    else
				       System.out.println(in.readUTF());
				    break;
			case 6:
			        System.out.println(in.readUTF());
				    String cl=sc.next();
				    out.writeUTF(cl);
			        System.out.println(in.readUTF()); 
			        break;
			case 7:
			        int count=Integer.parseInt(in.readUTF());
				    while(count>0){
				    System.out.println(in.readUTF());
				    count--;
				    }
				    break;
		    case 8:
			        int count5=Integer.parseInt(in.readUTF());
				    while(count5>0){
				    System.out.println(in.readUTF());
				    count5--;
				    }
				    break;
		    case 9:
			        System.out.println(in.readUTF());
				    out.writeUTF(sc.next());
				    int count3=Integer.parseInt(in.readUTF());
				    if(count3==0)
				      System.out.println(in.readUTF());
				    else
				    {
				      System.out.println(in.readUTF());
				      out.writeUTF(sc.next());
				      System.out.println(in.readUTF());
				    }
				    break;
		    case 10:
			        System.out.println(in.readUTF());
			        out.writeUTF(sc.next());
				    int count6=Integer.parseInt(in.readUTF());
				    if(count6==0)
				      System.out.println(in.readUTF());
				    else
				      System.out.println(in.readUTF());
				    break;
		    case 11:
			        System.out.println(in.readUTF());
					out.writeUTF(sc.next());
					System.out.println(in.readUTF());
					out.writeUTF(sc.next());
					int count1=Integer.parseInt(in.readUTF());
					System.out.println(count1);
					if(count1==0)
					 System.out.println(in.readUTF());
					while(count1>0){
					 System.out.println(in.readUTF());
					 count1--;
					}
					System.out.println(in.readUTF());
					break;
		    default:
			        System.out.println(in.readUTF());
					break;
		    }
		    System.out.println(in.readUTF());
		    input=sc.nextInt();
		    out.writeUTF(Integer.toString(input));
		    }while(input==1); 
            client.close();
        }
		catch(IOException e)
        {
          e.printStackTrace();
        }
    }
}