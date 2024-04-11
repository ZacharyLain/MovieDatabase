import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class personJdbc  extends HttpServlet {
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
            String query = "SELECT p_id, p_firstName, p_lastName, p_gender FROM person";
            rs = stmt.executeQuery(query);
            
            // HTML table header
            out.println("<html><head><title>Person Report</title></head><body>");
            out.println("<center><table border='1'><tr BGCOLOR='#cccccc'>" +
                        "<td>ID</td><td>First Name</td><td>Last Name</td><td>Gender</td></tr>");

            
            // Process the result set
            while (rs.next()) {
                out.println("<tr>" + 
                            "<td>" + rs.getString("p_id") + "</td>" +
                            "<td>" + rs.getString("p_firstName") + "</td>" +
                            "<td>" + rs.getString("p_lastName") + "</td>" +
                            "<td>" + rs.getString("p_gender") + "</td></tr>");
            }
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
        out.println("</body></html>");
    }
}

