package passage_system;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SurnameFinder
 */
@WebServlet("/SurnameFinder")
public class SurnameFinder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database = new Database();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SurnameFinder() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		String info = (String) request.getParameter("info");

		if ( info.equals("getStatus") ) {
			response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println(database.getStatus());
	    }
		if ( info.equals("getDepar") ) {
			response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println(database.getDepar());
	    }
		if ( info.equals("getFIO") ) {
			response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println(database.getFIO());
	    }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
