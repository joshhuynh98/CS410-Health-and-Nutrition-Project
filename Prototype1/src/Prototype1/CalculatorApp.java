package Prototype1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@WebServlet("/calcApp")
public class CalculatorApp extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		int foodid;
		String username="";
		
		PlanManager planManager = new PlanManager();
		planManager.setup();
		
		// Check for user input and update the user_plan table
		if (request.getParameter("paramFood")!=null && request.getParameter("paramUser")!=null) {
			foodid = Integer.parseInt(request.getParameter("paramFood"));
			username = request.getParameter("paramUser");
			planManager.create(username, foodid);
		} else if (request.getParameter("paramUser")!=null) {
			username = request.getParameter("paramUser");
		}
		
		// Check if user wants to delete a food item from plan
		if (request.getParameter("deleteID")!=null) {
			foodid = Integer.parseInt(request.getParameter("deleteID"));
			planManager.delete(username, foodid);
		}
		
		// Read the details of the food in the plan
		String planTableRows = "";
		String planTokens = planManager.read(username);
		String totalTableRow = "";
		StringTokenizer stk = new StringTokenizer(planTokens);

		planManager.exit();
		
		FoodManager foodManager = new FoodManager();
		foodManager.setup();
		
		while (stk.hasMoreTokens()) {
			foodid = Integer.parseInt(stk.nextToken());
			int count = Integer.parseInt(stk.nextToken());
			planTableRows += foodManager.readPlan(foodid, count, username);
		}
		
		totalTableRow = foodManager.getTotal();
		foodManager.exit();
		
		//Send back HTML
		String docType =
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
				"Transitional//EN\">\n";
		
		out.println(docType +		
				"<HTML>" +
				"<HEAD><TITLE>Calculator Page</TITLE>" +
				"<script src=\"js/jquery.min.js\"></script>\n" + 
				"<script src=\"js/skel.min.js\"></script>\n" + 
				"<script src=\"js/skel-layers.min.js\"></script>\n" + 
				"<script src=\"js/init.js\"></script>\n" + 
				"<noscript>\n" + 
				"	<link rel=\"stylesheet\" href=\"css/skel.css\" />\n" + 
				"	<link rel=\"stylesheet\" href=\"css/style.css\" />\n" + 
				"	<link rel=\"stylesheet\" href=\"css/style-xlarge.css\" />\n" + 
				"</noscript>" +
				"</HEAD><BODY>");
		
		// Nav bar
		out.println(
		"			<header id=\"header\">\n" + 
		"				<h1><a href=\"index.html\">Health and Nutrition</a></h1>\n" + 
		"				<nav id=\"nav\">\n" + 
		"					<ul>\n" + 
		"						<li><a href='index.jsp'>Home</a></li>\n" + 
		"						<li><form action='healthApp' target='_SELF' method='POST'>"+ 
		"						<button type='submit' class='nav-button'>Search Page</button>" + 
		"						<input hidden type='text' name='userName' class='hide' value='" + username + "'>"+ 
		"		    	</form></li>" + 
		"					</ul>\n" + 
		"				</nav>\n" + 
		"			</header>");
				
				// Help navigation back to health app
				out.println(
				"<form action=\"healthApp\" target=\"_SELF\" method=\"POST\">" +
						"<button type=\"submit\">Search Page</button>" +
						"<input hidden type=\"text\" name=\"userName\" value=\"" + username + "\">" +
		    	"</form>");
				
				out.println(
				"<section><div class='table-wrapper'>" +
				"<table class='alt'>" +
				"<tr>" +
				"<th>ID</th>" +
				"<th>Group</th>" +
				"<th>Name</th>" +
				"<th>Protein</th>" +
				"<th>Fat</th>" +
				"<th>Carbohydrates</th>" +
				"<th>Calories</th>" +
				"<th>Count</th>" +
				"<th></th>" +
				"</tr>");
				
				out.println(planTableRows);
				out.println(totalTableRow);
				
				out.println("</table></div></section>");
				
				out.println(
				"</BODY></HTML>");
	}
}
