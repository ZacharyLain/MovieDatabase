import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class movieJdbc extends HttpServlet {
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
            
            // Add a form to the page
            // The form will have three text inputs for the user to enter search criteria
            out.println("<center><form action='movieJdbc' method='post'>" +
                        "<div>" +
                            "<label for='search1'>Release Date:</label>" +
                            "<input type='text' id='search1' name='search1'>" +
                        "</div>" +
                        "<div>" +
                            "<button type='submit'>Submit</button>" +
                        "</div>" +
                        "</form></center>");

            String releaseDate = request.getParameter("search1");


            // Create a statement and execute a query
            stmt = con.createStatement();
            String query = "SELECT m_id, m_title, m_date, m_length, cat_name, rating_name " +
                           "FROM movie, category, rating " +
                           "WHERE movie.rating_id = rating.rating_id AND movie.cat_id = category.cat_id";

            if (releaseDate != null && !releaseDate.isEmpty()) {
                query += " AND m_date = '" + releaseDate + "'";
            }

            rs = stmt.executeQuery(query);
            
            // HTML table header
            out.println("<html><head><title>Movie Table Report</title></head><body>");
            out.println("<center><table border='1'><tr BGCOLOR='#cccccc'>" +
                        "<td>ID</td><td>Title</td><td>Release Date</td><td>Duration</td>" +
                        "<td>Category</td><td>Rating</td></tr>");

            
            // Process the result set
            while (rs.next()) {
                out.println("<tr>" + 
                            "<td>" + rs.getString("m_id") + "</td>" +
                            "<td>" + rs.getString("m_title") + "</td>" +
                            "<td>" + rs.getString("m_date") + "</td>" +
                            "<td>" + rs.getString("m_length") + "</td>" +
                            "<td>" + rs.getString("cat_name") + "</td>" +
                            "<td>" + rs.getString("rating_name") + "</td></tr>");
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
        
        // add a button to take back to the main page
		out.println("<br><p><a href=\"\\Movie\\index.html\"><img border=\"0\" src=\".\\html\\goback.jpg\" width=\"100\" height=\"66\"></a></p>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
}
