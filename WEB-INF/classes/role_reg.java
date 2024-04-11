import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class role_reg extends HttpServlet {
  // Use a prepared statement to store a movie into the database
  private PreparedStatement pstmt;
  private boolean didAdd = false;

  /** Initialize global variables */
  public void init() throws ServletException {
    initializeJdbc();
  }

  /** Process the HTTP Post request */
  public void doPost(HttpServletRequest request, HttpServletResponse
      response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // Obtain parameters from the client
    String mr_roleName = request.getParameter("mr_roleName");
    
    try {
      if (mr_roleName.length() == 0) {
        out.println("Please: Role name is required<br>");
        return; // End the method
      }

      storeRole(mr_roleName);
    }
    catch(Exception ex) {
      out.println("\n Error: " + ex.getMessage());
    }
    finally {
      

      if (didAdd) {
        out.println("Role registered successfully");
      } else {
        out.println("Role registration failed");
      }
      // add a button to take back to the main page
      out.println("<br><p><a href=\"\\Movie\\index.html\"><img border=\"0\" src=\".\\html\\goback.jpg\" width=\"100\" height=\"66\"></a></p>");

      out.close(); // Close stream
    }
  }

  /** Initialize database connection */
  private void initializeJdbc() {
    try {
      // Load the Oracle JDBC driver
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      // Attempt to establish a connection to the database
      Connection con = DriverManager.getConnection(
          "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "c##project", "project"
      );

      // Create a Statement
      pstmt = con.prepareStatement("insert into movie_role (mr_roleName) VALUES (?)");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /** Store a student record to the database */
  private void storeRole(String mr_roleName) throws SQLException {
    pstmt.setString(1, mr_roleName);
    
    pstmt.executeUpdate();
    didAdd = true;
  }
}