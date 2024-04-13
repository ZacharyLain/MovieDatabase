import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class MoviePeopleJdbc  extends HttpServlet {
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
      out.println("<html><head><title>Person Report</title></head><body>");

      String currentSort = request.getParameter("sort");

      // Make a sorting drop down menu
      out.println("<center><form action='personJdbc' method='post'>" +
                  "<select name='sort' onchange='this.form.submit()'>" +
                  "<option value='p_id'"  + ("p_id".equals(currentSort) ? " selected" : "") + ">ID</option>" +
                  "<option value='p_firstName'"  + ("p_firstName".equals(currentSort) ? " selected" : "") + ">First Name</option>" +
                  "<option value='p_lastName'"  + ("p_lastName".equals(currentSort) ? " selected" : "") + ">Last Name</option>" +
                  "<option value='male'" + ("male".equals(currentSort) ? " selected" : "") + ">Male</option>" +
                  "<option value='female'" + ("female".equals(currentSort) ? " selected" : "") + ">Female</option>" +
                  "<option value='compensation'"  + ("compensation".equals(currentSort) ? " selected" : "") + ">Compensation</option>" +
                  "</select>" +
                  "</form></center>");

      // Make table header
      out.println("<center><table border='1'><tr BGCOLOR='#cc cccc'>" +
                  "<td>ID</td><td>Movie Name</td><td>First Name</td><td>Last Name</td><td>Role</td><td>Compensation</td></tr>");

      String sort = request.getParameter("sort");
      String query = "SELECT mpr., p.p_firstName, p.p_lastName, p.p_gender, SUM(mpr.compensation) AS compensation FROM person \p " +
                      "LEFT JOIN movie_people_role mpr ON p.p_id = mpr.p_id ";

      // Add sorting to the query
      if ("male".equals(sort) || "female".equals(sort)) {
        query += "WHERE LOWER(p.p_gender) = '" + sort + "' ";
      }
                    
      query += "GROUP BY p.p_id, p.p_firstName, p.p_lastName, p.p_gender ";
      
      if (sort != null && !"male".equals(sort) && !"female".equals(sort)) {           
        switch (sort) {
          case "p_id":
              query += "ORDER BY p.p_id";
              break;
          case "p_firstName":
              query += "ORDER BY p.p_firstName";
              break;
          case "p_lastName":
              query += "ORDER BY p.p_lastName";
              break;
          case "compensation":
              query += "ORDER BY compensation DESC";
              break;
          default:
              // Default case, possibly throw an error or log a warning
              out.println("<h1>Invalid sort parameter: " + sort + "</h1>");
              break;
        }
      }

      if (sort == null) {
        // Set default sort if none is provided
        sort = "p_id";
      }

      rs = stmt.executeQuery(query);
      
      // Process the result set
      while (rs.next()) {
        out.println("<tr>" + 
                    "<td>" + rs.getString("p_id") + "</td>" +
                    "<td>" + rs.getString("p_firstName") + "</td>" +
                    "<td>" + rs.getString("p_lastName") + "</td>" +
                    "<td>" + rs.getString("p_gender") + "</td>" + 
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

