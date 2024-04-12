import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class person_reg extends HttpServlet {
  // Use a prepared statement to store a person into the database
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
    String p_firstName = request.getParameter("p_firstName");
    String p_lastName = request.getParameter("p_lastName");
    String p_gender = request.getParameter("p_gender");

    try {
      if (p_firstName.length() == 0 || p_lastName.length() == 0) {
        out.println("Please: First and last name required<br>");
        return; // End the method
      }

      storePerson(p_firstName , p_lastName, p_gender);
    }
    catch(Exception ex) {
      out.println("\n Error: " + ex.getMessage());
    }
    finally {
      

      if (didAdd) {
        out.println("Person registered successfully");
      } else {
        out.println("Person registration failed");
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
      pstmt = con.prepareStatement("insert into person (p_firstName, p_lastName, p_gender) VALUES (?, ?, ?)");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /** Store a student record to the database */
  private void storePerson(String p_firstName, String p_lastName, String p_gender ) throws SQLException {
    pstmt.setString(1, p_firstName);
    pstmt.setString(2, p_lastName);
    pstmt.setString(3, p_gender);
    
    pstmt.executeUpdate();
    didAdd = true;
  }
}