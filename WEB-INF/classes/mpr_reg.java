import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class mpr_reg extends HttpServlet {
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
    String m_id = request.getParameter("m_id");
    String p_id = request.getParameter("p_id");
    String mr_id = request.getParameter("mr_id");
	String compensation = request.getParameter("compensation");

    try {
      if (m_id.length() == 0 || p_id.length() == 0 || mr_id.length() == 0 || compensation.length() == 0) {
        out.println("Please: all fields are required<br>");
        return; // End the method
      }

      storeMoviePerson(m_id , p_id, mr_id, compensation);
    }
    catch(Exception ex) {
      out.println("\n Error: " + ex.getMessage());
    }
    finally {
      

      if (didAdd) {
        out.println("Person has been registered to Movie with Role successfully");
      } else {
        out.println("Person has failed to register to Movie with Role");
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
      pstmt = con.prepareStatement("insert into movie_people_role (m_id, p_id, mr_id, compensation) VALUES (?, ?, ?, ?)");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /** Store a student record to the database */
  private void storeMoviePerson(String m_id, String p_id, String mr_id, String compensation) throws SQLException {
    pstmt.setString(1, m_id);
    pstmt.setString(2, p_id);
    pstmt.setString(3, mr_id);
	pstmt.setString(4, compensation);
    
    pstmt.executeUpdate();
    didAdd = true;
  }
}