package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vo.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import Builder.JSONBuilder;
import Builder.MessageBuilder;
import Builder.PatternBuilder;
import dao.UserDao;


@WebServlet( {  "/users" }  )
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserDao userDao = new UserDao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		String jsonString = new JSONBuilder().generateJson(request).build();
		String ownerParam = request.getParameter("owner");
		
		if(ownerParam.equals("true")) {
			List<User> result = userDao.getAllUsers();
			messageBuilder.set(200, new Gson().toJson(result));
			return;
		}
		
		User user = new Gson().fromJson(jsonString, User.class);
		
		try {
			User result = userDao.loginUser(user);
			
			if(result == null)
			{
				messageBuilder.set(400, "No User Found");
				return;
			}
			
			messageBuilder.set(200, new Gson().toJson(result) );
			return;
			
		} catch (SQLException e) {
			messageBuilder.set(400, "Invalid Data" );
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		String jsonString = new JSONBuilder().generateJson(request).build();
		
		User user = new Gson().fromJson(jsonString, User.class);
		
		try {
			Boolean result = userDao.registerUser(user);
			
			if(result == false)
			{
				messageBuilder.set(400, "Registration Failed");
				return;
			}
			
			messageBuilder.set(200, "Registration Successfull" );
			
		} catch (SQLException e) {
			messageBuilder.set(400, "Invalid Data" );
			e.printStackTrace();
			
		}
		
		
	}

}
