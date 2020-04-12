/*This program is to process data in library management system at server side taking inputs from client.
  To Complie:javac Server.java.
  To run:java ServereSide portno
 */

import java.net.*;
import java.io.*;
import java.sql.* ;  
import java.math.* ;
import java.util.*;

class ServerSide extends Thread
{
  ServerSocket serversocket=null; 
  ServerSide(int port){
  try{
   serversocket=new ServerSocket(port);
  }
  catch(IOException io){
    System.out.println(io.getMessage());
  }
  }
  public void run()
  {
    Connection conn=null;
	Statement stmt=null;  
    PreparedStatement psmt=null;
    try{
      Class.forName("oracle.jdbc.OracleDriver");
      String URL = "jdbc:oracle:thin:@10.103.10.221:1521:xe";
      String USER = "imudrage";
      String PASS = "imudrage";
      conn = DriverManager.getConnection(URL, USER, PASS);
	  try{
        int i;
        System.out.println("Waiting for client on port " +serversocket.getLocalPort() + "...");       
        Socket server = serversocket.accept();
        DataOutputStream out =new DataOutputStream(server.getOutputStream());
        out.writeUTF("Thank you for connecting to "+ server.getLocalSocketAddress()); 
        out.writeUTF("Welcome To Library Management System");
	    int input;
	    do{
	       out.writeUTF("-------------Menu------------");
           out.writeUTF("1.Add an employee\n2.Update employee details\n3.Delete employee details\n4.Add a book\n5.Update book details\n6.Delete Book\n7.View Employee details\n8.View Book details\n9.Borrow book\n10.Return Book\n11.Query Books");
           out.writeUTF("Enter your choice:");
           DataInputStream in=new DataInputStream(server.getInputStream());
           i=Integer.parseInt(in.readUTF());                                           //Input choice read from client
	       Scanner sc=new Scanner(System.in);
           switch(i)
           {
             case 1:
		       Employee emp1=new Employee();
		       emp1.addEmployee(out,in);                                               //Calls addEmployee() of Employee class.
		       break;
		     case 2:
		        Employee emp2=new Employee();
		        emp2.updateEmployee(out,in);                                          //Calls updateEmployee() of Employee class.
		        break;
		     case 3:
		         Employee emp3=new Employee();
		         emp3.deleteEmployee(out,in);                                         //Calls deleteEmployee() of Employee class.
		         break;
		     case 4:
		         Books book1=new Books();
		         book1.addBook(out,in);                                              //Calls addBook() of Books class.
		         break;
		     case 5:
		         Books book7=new Books();
		         book7.updateBook(out,in);                                           //Calls updateBook() of Books class.
                 break;
             case 6:
		         Books book6=new Books();
		         book6.deleteBook(out,in);                                          //Calls deleteBook() of Books class.
		         break;    
	         case 7:
		         Employee emp4=new Employee();
		         emp4.showEmployee(out,in);                                         //Calls showEmployee() of Employee class.
		         break;
		     case 8:
		         Books book2=new Books();
		         book2.showBook(out,in);                                           //Calls showBook() of Books class.
		         break;
		     case 9:
		         Books book3=new Books();
		         book3.borrowBook(out,in);                                          //Calls borrowBook() of Books class.
		         break;
		     case 10:
		         Books book4=new Books();
		         book4.returnBook(out,in);                                         //Calls returnBook() of Books class.
		         break;
		     case 11:
		         Books book5=new Books();
		         book5.queryBook(out,in);                                          //Calls queryBook() of Books class.
		         break;
		     default:
		         out.writeUTF("Enter valid choice..");                            //When an invalid input is entered by client.
		         break;
            }
	        out.writeUTF("Press 1 to continue...");
	        input=Integer.parseInt(in.readUTF());
	    }while(input==1);
        server.close();
      }
      catch(SQLException se){
	    System.out.println(se.getMessage());
	  }
	  catch(ClassNotFoundException ce){
	    System.out.println(ce.getMessage());
	  }
	  catch(IOException io){
	    System.out.println(io.getMessage());
	  }
      catch(Exception e){
        System.out.println(e.getMessage());
      }
    }
    catch(SQLException se){
	   System.out.println(se.getMessage());
	}
    catch(Exception e){
	  System.out.println("Exception..!!"+e.getMessage());
	}
	finally{
	  try{
	  if(stmt!=null)
	   stmt.close();
	  if(psmt!=null)
	   psmt.close();
	  if(conn!=null)
	   conn.close();
	  }
	  
	  catch(SQLException se){
	    System.out.println(""+se.getMessage());
	  }
	}
 }
 public static void main(String args[]){
   ServerSide s[]=new ServerSide[10];
   int i=0;
   for(String a:args)
   {
    s[i]=new ServerSide(Integer.parseInt(a));
	s[i].start();
    i++;	
   }
  }
}

class Employee
{
    Connection conn=null;
	Statement stmt=null;  
    PreparedStatement psmt=null;
	ResultSet rs;
    int id,seq,isbn,st=0;
	String name,address,callno,title,author,status;
	long phn;
    String URL = "jdbc:oracle:thin:@10.103.10.221:1521:xe";
    String USER = "imudrage";
    String PASS = "imudrage";
	Employee()throws SQLException,ClassNotFoundException
	{
	  Class.forName("oracle.jdbc.OracleDriver");
	  conn = DriverManager.getConnection(URL, USER, PASS);
	}
	public synchronized void addEmployee(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	  out.writeUTF("Enter employee details:");
	  try{
	    out.writeUTF("Employee ID:");
	    int id=Integer.parseInt(in.readUTF());
	    out.writeUTF("Employee name:");
		String name=in.readUTF();
		out.writeUTF("PhoneNumber:");
		long phn=Long.parseLong(in.readUTF());
	    out.writeUTF("Address:");
		String address=in.readUTF();
		psmt=conn.prepareStatement("insert into employee values(?,?,?,?)");               //Inserting into employee table
		psmt.setInt(1,id);
		psmt.setString(2,name);
		psmt.setLong(3,phn);
		psmt.setString(4,address);
		psmt.executeUpdate();
		out.writeUTF("added successfully");
		}
		catch(SQLException seee)
		{
		  out.writeUTF("ID already assigned to another employee..try again..!!");
	      return;
	    }   
	}
    public synchronized void updateEmployee(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    out.writeUTF("Enter Employee ID whose details are to be updated");
		id=Integer.parseInt(in.readUTF());
		psmt=conn.prepareStatement("select count(*) as count from employee where id=?");
		psmt.setInt(1,id);
		rs=psmt.executeQuery();
		rs.next();
		int count=rs.getInt("count");
		out.writeUTF(Integer.toString(count));
		if(count==0)                                                                //If select statement returns no result
		  out.writeUTF("Invalid Employee ID");
		else
		{
		  out.writeUTF("1.Modify phone number\n2.Modify address\nEnter choice:");
		  int choice=Integer.parseInt(in.readUTF()); 
		  if(choice==1){
		   out.writeUTF("enter new phone number:");
		   int newval=Integer.parseInt(in.readUTF());
		   psmt=conn.prepareStatement("update employee set phone_no=? where id=?");      //Updating employee table with new phone number
		   psmt.setLong(1,newval);
		   psmt.setInt(2,id);
		   psmt.executeUpdate();
		  }
		  else if(choice==2){
		   out.writeUTF("enter new address:");
		   String newval=in.readUTF();
		   psmt=conn.prepareStatement("update employee set address=? where id=?");       //Updating employee table with new address
		   psmt.setString(1,newval);
		   psmt.setInt(2,id);
		   psmt.executeUpdate();
		  }
		  else
		   out.writeUTF("Enter valid option");
		}
	}
	
	public synchronized void deleteEmployee(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	   out.writeUTF("Enter employee ID to be deleted:");
	   id=Integer.parseInt(in.readUTF());
	   psmt=conn.prepareStatement("select * from employee where id=?");
	   psmt.setInt(1,id);
	   rs=psmt.executeQuery();
	   if(rs.next())                                                                        //Checking if it is a valid ID
	   {
	      psmt=conn.prepareStatement("select count(*) as count from checkout where id=? and status=0");
    	  psmt.setInt(1,id);
		  ResultSet rs1=psmt.executeQuery();
		  rs1.next();
		  int count=rs1.getInt("count");
		  if(count==0)
		  {
		    psmt=conn.prepareStatement("delete from checkout where id=?");            //Deleting transaction pertaining to that employee in checkout table
			psmt.setInt(1,id);
		    psmt.executeQuery();
		    psmt=conn.prepareStatement("delete from employee where id=?");              //Deleting employee from employee table after checking he has no books due.
			psmt.setInt(1,id);
		    psmt.executeQuery();
			out.writeUTF("Employee deleted");
		  } 
		  else
	      out.writeUTF("Employee cannot be deleted");
	    }
        else
           out.writeUTF("Invalid Employee ID");	  
	}
	
	public void showEmployee(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    stmt=conn.createStatement();
	    String sql="select e.id as id,e.name as name,e.phone_no as phone_no,e.address as address,c.call_num as call_num,c.title as title,c.issuedate as issuedate,c.duedate as duedate,c.returndate as returndate,c.status as status from employee e,checkout c where e.id=c.id(+) order by e.id";
        rs=stmt.executeQuery(sql);
	    int count=0;
		while(rs.next())
		  count++;
		out.writeUTF(Integer.toString(count));
        rs=stmt.executeQuery(sql);
        while(rs.next())
		{
		   id=rs.getInt("id");
		   name=rs.getString("name");
		   phn=rs.getLong("phone_no");
		   address=rs.getString("address");
		   callno=rs.getString("call_num");
		   title=rs.getString("title");
		   java.util.Date issuedate=rs.getDate("issuedate");
		   java.util.Date duedate=rs.getDate("duedate");
		   java.util.Date returndate=rs.getDate("returndate");
		   out.writeUTF(id+" "+name+" "+phn+" "+address+" "+callno+" "+title+" "+issuedate+" "+duedate+" "+returndate);     //Displaying employee details along with books he borrowed if any
		}
	}
} 

class Books
{
    Connection conn=null;
	Statement stmt=null;  
    PreparedStatement psmt=null;
	ResultSet rs;
    int id,seq,isbn,st=0;
	String name,address,callno,title,author,status;
	long phn;
    String URL = "jdbc:oracle:thin:@10.103.10.221:1521:xe";
    String USER = "imudrage";
    String PASS = "imudrage";
	Books()throws SQLException,Exception
	{
	  Class.forName("oracle.jdbc.OracleDriver");
	  conn = DriverManager.getConnection(URL, USER, PASS);
	}
	
	public synchronized void addBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{ 
	    try
		{
	      out.writeUTF("Enter book details:");
          out.writeUTF("Enter sequence number:");
		  seq=Integer.parseInt(in.readUTF());
		  out.writeUTF("Enter ISBN:");
		  isbn=Integer.parseInt(in.readUTF());
          out.writeUTF("Enter book title:");	
          title=in.readUTF();
          out.writeUTF("Enter book author:");
          author=in.readUTF();	
		  String xx=title.substring(0,2).concat(author.substring(0,2));
		  System.out.println(xx);
		  callno=xx.concat(seq+"");                                                  //Generating call number in the given format taking sequence no as input
          psmt=conn.prepareStatement("insert into books values(?,?,?,?,?)");		 //Inserting book details in books table
		  psmt.setString(1,callno);
		  psmt.setInt(2,isbn);
		  psmt.setString(3,title);
		  psmt.setString(4,author);
		  psmt.setString(5,"available");
		  psmt.executeUpdate();
		  out.writeUTF("Book Added");
		}
		catch(SQLException se)
		{
		    out.writeUTF("Sequence no. already for exists for the title-author..!");
		}	
	}
	
	public synchronized void deleteBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	   out.writeUTF("Enter Book call number to be deleted");
	   callno=in.readUTF();
	   psmt=conn.prepareStatement("select * from books where call_num=?");
	   psmt.setString(1,callno);
	   rs=psmt.executeQuery();
	   if(rs.next())
	   {
	      psmt=conn.prepareStatement("select count(*) as count from checkout where call_num=? and status=0");
		  psmt.setString(1,callno);
		  rs=psmt.executeQuery();
		  rs.next();
		  int count=rs.getInt("count");
		  if(count==0) 
		  {
		    psmt=conn.prepareStatement("delete from checkout where call_num=?");            //Deleting transaction pertaining to that particular book from checkout table
			psmt.setString(1,callno);
		    psmt.executeQuery();
		    psmt=conn.prepareStatement("delete from books where call_num=?");             //Book deleted only if it is not in unreturned state if an employee has borrowed.
			psmt.setString(1,callno);
		    psmt.executeQuery();
		    out.writeUTF("Book deleted");
		  }
		  else
		     out.writeUTF("Book cant be deleted");
		  
	    }
	    else
	       out.writeUTF("Invalid Book Call Number");
	}
	
	public void showBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    stmt=conn.createStatement();
		String sql="select count(*) as count from books";
	    rs=stmt.executeQuery(sql);
		rs.next();
		int count=rs.getInt("count");
		out.writeUTF(Integer.toString(count));
        sql="select * from books";
		stmt=conn.createStatement();
		ResultSet rs2=stmt.executeQuery(sql);
      	while(rs2.next()){
            callno=rs2.getString("call_num");
            isbn=rs2.getInt("isbn");
            title=rs2.getString("title");
            author=rs2.getString("author");
			String status=rs2.getString("status");
			if(status.compareTo("unavailable")==0){
			    psmt=conn.prepareStatement("select * from checkout where call_num=? order by call_num");
                psmt.setString(1,callno);	
                ResultSet rs3=psmt.executeQuery();	
				rs3.next();
				out.writeUTF("Emp_ID"+rs3.getInt("id")+" "+callno+" "+isbn+" "+title+" "+author+" "+status+" "+rs3.getDate("issuedate")+" "+rs3.getDate("duedate"));           //Displaying book details if it were borrowed by an employee
			}
			else	 
			 out.writeUTF(callno+" "+isbn+" "+title+" "+author+" "+status);                  //Displaying book details if it is in available state
        }			
	}
	public void updateBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    int flag=1;
	    out.writeUTF("Enter book call number:");
	    callno=in.readUTF();
	    psmt=conn.prepareStatement("select * from books where call_num=?");
	    psmt.setString(1,callno);
	    rs=psmt.executeQuery();
	    if(rs.next())
	    {
	        out.writeUTF(Integer.toString(flag));
	        out.writeUTF("1.Modify ISBN:");
	        out.writeUTF("Enter new ISBN:");
	        int newval=Integer.parseInt(in.readUTF());
			psmt=conn.prepareStatement("update books set isbn=? where call_num=?");              //Updating isbn of a book as per given call number.
			psmt.setInt(1,newval);
			psmt.setString(2,callno);
			psmt.executeQuery();
	    }
	    else
	    {
	        flag=0;
	        out.writeUTF(Integer.toString(flag));
	        out.writeUTF("Invalid Book Call Number..!");
	    }
	}
		
	public synchronized void borrowBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    out.writeUTF("Enter book call number you want to borrow:");
        String callno3=in.readUTF();
    	psmt=conn.prepareStatement("select count(*) as count from books where call_num=?");
		psmt.setString(1,callno3);
		rs=psmt.executeQuery();
	    rs.next();
	    int count=rs.getInt("count");
   	    out.writeUTF(Integer.toString(count));
		if(count==0)
		   out.writeUTF("Invalid Book call number");
	    else
		{
			psmt=conn.prepareStatement("select status from books where call_num=?");              //Check if book is available
		    psmt.setString(1,callno3);
			rs=psmt.executeQuery();
			rs.next();
			status=rs.getString("status");
			if(status.compareTo("available")!=0)
			   out.writeUTF("Book not available");
			else
            {			 
			  try{
			    out.writeUTF("Enter employee id");
			    id=Integer.parseInt(in.readUTF());
			    psmt=conn.prepareStatement("select * from books where call_num=?");
			    psmt.setString(1,callno3);
			    rs=psmt.executeQuery();
			    rs.next();
			    author=rs.getString("author");
			    title=rs.getString("title");
			    int st=0;
			    java.util.Date utildate=new java.util.Date();
			    java.sql.Date sqldate=new java.sql.Date(utildate.getTime());             //Updating issuedate to current system date
			    Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 7);                                             //Updating duedate to current system date plus 7days
			    java.util.Date myDate = cal.getTime();
			    java.sql.Date sqldate2=new java.sql.Date(myDate.getTime());
			    java.util.Date myDate2;
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,00);
                cal.set(Calendar.DATE,00);
                cal.set(Calendar.YEAR,0000);
		        myDate2=cal.getTime();
		        java.sql.Date sqldate3=new java.sql.Date(myDate2.getTime());
                psmt=conn.prepareStatement("insert into checkout values(?,?,?,?,?,?,?,?)");				
                psmt.setInt(1,id);
                psmt.setString(2,callno3);
                psmt.setString(3,title);
			    psmt.setString(4,author);
			    psmt.setDate(5,sqldate);
			    psmt.setDate(6,sqldate2);
			    psmt.setDate(7,sqldate3);
			    psmt.setInt(8,st);
			    psmt.executeUpdate();
			    psmt=conn.prepareStatement("update books set status='unavailable' where call_num=?");         //Updating book status to unavailable once it is sanctioned.
			    psmt.setString(1,callno3);
			    psmt.executeQuery();
			    out.writeUTF("Book sanctioned..!");
			    }
			    catch(SQLException se)
			    {
			        out.writeUTF("Employee ID incorrect..!!");
			    }
		    }
		}
	}
	
	public synchronized void returnBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    out.writeUTF("Enter book call number you want to return:");
        callno=in.readUTF();
        psmt=conn.prepareStatement("select count(*) as count from books where call_num=?");
		psmt.setString(1,callno);
	    rs=psmt.executeQuery();
   	    rs.next();
	    int count=rs.getInt("count");
        out.writeUTF(Integer.toString(count));
		if(count==0)
		  out.writeUTF("Invalid Book call number");
		else
		{
          
		  java.util.Date utildate=new java.util.Date();
	      java.sql.Date sqldate=new java.sql.Date(utildate.getTime());
          psmt=conn.prepareStatement("update checkout set returndate=? where call_num=?");         //Updating returndate to current system date.
          psmt.setDate(1,sqldate);
          psmt.setString(2,callno);	
	      stmt=conn.createStatement();
		  psmt=conn.prepareStatement("update checkout set status=1 where call_num=?");             //Updating status in checkout as 1 to indicate it is returned
		  psmt.setString(1,callno);
		  psmt.executeUpdate();
		  psmt=conn.prepareStatement("update books set status='available' where call_num=?");            //Updating status in books back to available to indicate it is returned
          psmt.setString(1,callno);
          psmt.executeUpdate();
	      out.writeUTF("Book Returned..!!");
	    }
	}
	
	public void queryBook(DataOutputStream out,DataInputStream in)throws SQLException,IOException,Exception
	{
	    out.writeUTF("Enter the bookname:");
		title=in.readUTF();
		out.writeUTF("Enter the author:");
	    author=in.readUTF();
		psmt=conn.prepareStatement("select * from books where title=? and author=?");
		psmt.setString(1,title);
		psmt.setString(2,author);
		ResultSet rs6=psmt.executeQuery();
		psmt=conn.prepareStatement("select count(*) as count from books where title=? and author=?");     //Querying books with given title and author
		psmt.setString(1,title);
		psmt.setString(2,author);
		ResultSet rs7=psmt.executeQuery();
		rs7.next();
		int count1=rs7.getInt("count");
		out.writeUTF(Integer.toString(count1));
		if(count1==0)
			out.writeUTF("No such book exists..");
		while(rs6.next()){
			callno=rs6.getString("call_num");
			isbn=rs6.getInt("isbn");
			out.writeUTF(callno+" "+" "+isbn+" "+title+" "+author);  
		}
		psmt=conn.prepareStatement("select count(*) as count from books where title=? and author=? and status='available'");
		psmt.setString(1,title);
		psmt.setString(2,author);
		rs7=psmt.executeQuery();
		rs7.next();
		count1=rs7.getInt("count");
		out.writeUTF("Number of copies available for issue:"+count1);
    }
}

class DBCreation
{
  public static void main(String args[])
  {
    Connection conn=null;
	Statement stmt=null;
    try{
        Class.forName("oracle.jdbc.OracleDriver");
        String URL = "jdbc:oracle:thin:@10.103.10.221:1521:xe";
        String USER = "imudrage";
        String PASS = "imudrage";
        conn = DriverManager.getConnection(URL, USER, PASS);
        stmt=conn.createStatement();
	    String sql="create table employee(id integer PRIMARY KEY,name varchar2(25) NOT NULL,phone_no integer check(phone_no like '%%%%%%%%%%'),address varchar2(30))";                                 //Creating employee table for storing employee details
        stmt.executeUpdate(sql);
        System.out.println("table created");
	    String sql2="create table books(call_num varchar2(15) primary key,isbn integer unique,title varchar2(25) not null,author varchar2(25) not null,status varchar2(15) default 'available')";                         //Creating books table for storing book details
	    stmt.executeUpdate(sql2);
	    System.out.println("table created");
	    String sql3="create table checkout(id integer,call_num varchar2(15),title varchar2(25),author varchar2(25),issuedate date,duedate date,returndate date,status integer,foreign key(id) references employee,foreign key(call_num) references books)";                      //Creating checkout table for tracking book loan and book return details
	    stmt.executeQuery(sql3);
	    System.out.println("table created");
	}
	catch(SQLException se){
	  System.out.println(se.getMessage());
	}
	catch(Exception ee){
	  System.out.println(ee.getMessage());
	}
	finally{
	  try{
	  stmt.close();
	  conn.close();
	  }
	  catch(SQLException se){
	    System.out.println(se.getMessage());
	  }
	}
  }
}