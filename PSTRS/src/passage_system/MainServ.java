package passage_system;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class MainServ
 */
@WebServlet("/MainServ")
public class MainServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database = new Database();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServ() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	    String username = (String) request.getParameter("username");
	    String status = (String) request.getParameter("status");
	    String dateStart = (String) request.getParameter("calStart");
	    String dateFin = (String) request.getParameter("calFin");
	    String depart = (String) request.getParameter("depart");
	    
	    
	    String info = (String) request.getParameter("info");
	    //информация о сообщении
	    
	    String surnMes = (String) request.getParameter("surnMes");
	    String soundMes = (String) request.getParameter("typeMes");
	    String typeCom = (String) request.getParameter("typeCom");
	   // key = username;

	    System.out.println(status);
	    System.out.println(depart);
	    //System.out.println(typeCom);
	  //  System.out.println(info);
	   // if (username.equals("admin") && key.equals("admin")) {
	    	//System.out.println(key);
	    if (info.endsWith("find_info") ) {
	    String answ =  database.dateFinder(dateStart, dateFin, username, status, depart);
	    response.setContentType("text/html; charset=UTF-8");
	    response.getWriter().println( answ );
	    }
	    if ( info.equals("addMes") ) {
	    	response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println(database.addMesForWorker(surnMes, soundMes, typeCom));
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
