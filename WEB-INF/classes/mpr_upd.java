import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class mpr_upd extends HttpServlet 
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
    {        
			Statement state4 = null;
			ResultSet result = null;
			String query="";        
			Connection con=null; 
          
            String m_id = request.getParameter("m_id");
			String p_id = request.getParameter("p_id");
			String mr_id = request.getParameter("mr_id");
			String compensation = request.getParameter("compensation");
			

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
			System.err.println("Exception while loading driver");		
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

		// create the output stream
		try
		{
			out =  response.getWriter();
		}
		catch (IOException e) 
		{
  			e.printStackTrace();
		}
		
		// create the query to update the movie
		//query = "update movie_people_role set p_firstName = \'"+p_firstName+"\', p_lastName = \'"+p_lastName+"\', p_gender = \'"+p_gender+"\'";
		query = "update movie_people_role set compensation = \'" + compensation + "\'" +
			"WHERE m_id = \'" + m_id + "\'" +
			"AND p_id = \'" + p_id + "\'" +
			"AND mr_id = \'" + mr_id + "\'";

		out.println("<html><head><title>Crew/Cast Member's Compensation has been updated</title>");	 
		out.println("</head><body>");
		
		out.print( "<br /><b><center><font color=\"BLACK\"><H2>One Record has updated</H2></font>");
        out.println( "</center><br />" );

		// execute the query
       	try 
		{ 
			result=state4.executeQuery(query);
				
	  	}
		catch (SQLException e) 
		{
			System.err.println("SQLException while executing SQL Statement."); 
		}

		// close the connection
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
