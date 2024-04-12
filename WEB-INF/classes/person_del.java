import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class person_del extends HttpServlet 
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
    {        
			Statement state4 = null;
			ResultSet result = null;
			String query="";        
			Connection con=null; 
          
            String p_id = request.getParameter("p_id");
            String p_firstName = request.getParameter("p_firstName");
            String p_lastName = request.getParameter("p_lastName");
            String p_gender = request.getParameter("p_gender");

		try
		{			
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); 
            con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "c##project", "project");
	       	System.out.println("Congratulations! You are connected successfully.");      
     	}
        catch(SQLException e)
		{	
			System.out.println("Error: "+e);	
		}
		catch(Exception e) 
		{
			System.err.println("Exception while loading  driver");		
		}
	    try 
		{
        	state4 = con.createStatement();
		} 
		catch (SQLException e) 	
		{
			System.err.println("SQLException while creating statement");			
		}
		
		response.setContentType("text/html");
		PrintWriter out = null ;
		try
		{
			out =  response.getWriter();
		}
		catch (IOException e) 
		{
  			e.printStackTrace();
		}
		
		 query = "DELETE FROM person WHERE p_id = '" + p_id + "' OR (LOWER(p_firstName) = '" + p_firstName.toLowerCase() + "' AND LOWER(p_lastName) = '" + p_lastName.toLowerCase() + "')";
		// could remove by name here too if you do query = "DELETE FROM person WHERE LOWER(p_firstName) = '" + p_firstName.toLowerCase() + "' AND LOWER(p_lastName) = '" + p_lastName.toLowerCase() + "'";
		out.println("<html><head><title>Person has been deleted</title>");	 
		out.println("</head><body>");
		
		out.print( "<br /><b><center><font color=\"BLACK\"><H2>One Record has deleted</H2></font>");
        out.println( "</center><br />" );
       	try 
		{ 
			result=state4.executeQuery(query);
				
	  	}
		catch (SQLException e) 
		{
			System.err.println("SQLException while executing SQL Statement."); 
		}
		
		try 
		{ 
   			result.close(); 
			state4.close(); 	
			con.close();
    		System.out.println("Connection is closed successfully.");
 	   	}
		catch (SQLException e) 
		{
			e.printStackTrace();	
		}

		// add a button to take back to the main page
		out.println("<br><p><a href=\"\\Movie\\index.html\"><img border=\"0\" src=\".\\html\\goback.jpg\" width=\"100\" height=\"66\"></a></p>");
  		out.println("</body></html>");
    } 
}
