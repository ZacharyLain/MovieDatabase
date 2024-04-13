import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class moviePeopleJdbc  extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
    // Initialize these here so they can be closed in the finally block
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
      // Load the Oracle JDBC driver
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      // Attempt to establish a connection to the database
      con = DriverManager.getConnection(
          "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "c##project", "project"
      );
          // Create a statement and execute a query
          stmt = con.createStatement();

      // HTML header
      out.println("<html><head><title>Cast/Crew Report</title></head><body>");

      // Add a form to the page
      // The form will have three text inputs for the user to enter search criteria
	  out.println("<center><form action='moviePeopleJdbc' method='post'>" +
                "<div>" +
                    "<label for='search1'>Movie Name:</label>" +
                    "<input type='text' id='search1' name='search1'>" +
                "</div>" +
                "<div>" +
                    "<label for='search2'>Full Person Name:</label>" +
                    "<input type='text' id='search2' name='search2'>" +
                "</div>" +
                "<div>" +
                    "<label for='search3'>Role Name:</label>" +
                    "<input type='text' id='search3' name='search3'>" +
                "</div>" +
                "<div>" +
                    "<button type='submit'>Submit</button>" +
                "</div>" +
                "</form></center>");


      // Make table header
      out.println("<center><table border='1'><tr BGCOLOR='#cc cccc'>" +
                  "<td>Cast/Crew ID</td><td>Movie ID</td><td>Movie Name</td><td>Person ID<td>Full Name</td><td>Role ID</td><td>Role</td><td>Compensation</td></tr>");

      String movie = request.getParameter("search1");
      String person = request.getParameter("search2");
      String role = request.getParameter("search3");
      Boolean movieEmpty = true;
      Boolean personEmpty = true;
      Boolean roleEmpty = true;

      String query = 
          "SELECT mpr.mrp_id, m.m_id, m.m_title, p.p_id, p.p_firstName || \' \' || p.p_lastName as full_name, mr.mr_id, mr.mr_roleName, mpr.compensation " +
          "FROM movie_people_role mpr " +
          "JOIN person p ON mpr.p_id = p.p_id " +
          "JOIN movie m ON mpr.m_id = m.m_id " +
          "JOIN movie_role mr ON mpr.mr_id = mr.mr_id";

      // Add sorting to the query
      boolean whereAdded = false;

      if (movie != null && !movie.isEmpty()) {
          query += " WHERE LOWER(m.m_title) = '" + movie.toLowerCase() + "'";
          whereAdded = true;
      }

      if (person != null && !person.isEmpty()) {
        query += (whereAdded ? " AND" : " WHERE") + " LOWER(p.p_firstName || ' ' || p.p_lastName) = '" + person.toLowerCase() + "'";
        whereAdded = true;
      }

      if (role != null && !role.isEmpty()) {
          query += (whereAdded ? " AND" : " WHERE") + " LOWER(mr.mr_roleName) = '" + role.toLowerCase() + "'";
      }


      rs = stmt.executeQuery(query);
      
      // Process the result set
      while (rs.next()) {
        out.println("<tr>" + 
                    "<td>" + rs.getString("mrp_id") + "</td>" +
                    "<td>" + rs.getString("m_id") + "</td>" +
                    "<td>" + rs.getString("m_title") + "</td>" +
                    "<td>" + rs.getString("p_id") + "</td>" +
                    "<td>" + rs.getString("full_name") + "</td>" +
                    "<td>" + rs.getString("mr_id") + "</td>" +
                    "<td>" + rs.getString("mr_roleName") + "</td>" + 
                    "<td>" + rs.getString("compensation") + "</td>" + 
                    "</tr>");
      }

      // Close the table
      out.println("</table></center>");
      
    } catch (SQLException e) {
      out.println("<h1>Error connecting to database: " + e.getMessage() + "</h1>");
      e.printStackTrace();
    } finally {
      // Close all resources to avoid resource leaks
      try {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (con != null) con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    // add a button to take back to the main page
		out.println("<br><p><a href=\"\\Movie\\index.html\"><img border=\"0\" src=\".\\html\\goback.jpg\" width=\"100\" height=\"66\"></a></p>");
    out.println("</body></html>");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    doGet(request, response);
  }
}

