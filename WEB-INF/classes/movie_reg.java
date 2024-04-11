import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class movie_reg extends HttpServlet {
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
    String m_title = request.getParameter("m_title");
    String m_date = request.getParameter("m_date");
    String m_length = request.getParameter("m_length");
    String cat_id = request.getParameter("cat_id");
    String rating_id = request.getParameter("rating_id");

    try {
      if (m_title.length() == 0) {
        out.println("Please: Movie title is required<br>");
        return; // End the method
      }

      storeMovie(m_title ,m_date , m_length, cat_id, rating_id);
    }
    catch(Exception ex) {
      out.println("\n Error: " + ex.getMessage());
    }
    finally {
      

      if (didAdd) {
        out.println("Movie registered successfully");
      } else {
        out.println("Movie registration failed");
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
      pstmt = con.prepareStatement("insert into movie (m_title, m_date, m_length, cat_id, rating_id) VALUES (?, ?, ?, ?, ?)");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /** Store a student record to the database */
  private void storeMovie(String m_title, String m_date, String m_length, String cat_id,
                          String rating_id ) throws SQLException {
    pstmt.setString(1, m_title);
    pstmt.setString(2, m_date);
    pstmt.setString(3, m_length);
    pstmt.setString(4, cat_id);
    pstmt.setString(5, rating_id);
    
    pstmt.executeUpdate();
    didAdd = true;
  }
}