package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vo.Device;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Builder.JSONBuilder;
import Builder.MessageBuilder;
import Builder.PatternBuilder;
import dao.DeviceDao;

@WebServlet( { "/devices" , "/devices/*" } )
public class DeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DeviceDao deviceDao = new DeviceDao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PatternBuilder patternBuilder = new PatternBuilder("/([0-9]+)", request.getPathInfo() );
		MessageBuilder messageBuilder = new MessageBuilder(response);
		String jsonString = new JSONBuilder().generateJson(request).build();
		String blockIdString = request.getParameter("blockId");
		
		if(patternBuilder.getGroupCount() == 0) {
			Type type = new TypeToken<List<Device>>() {}.getType();
			List<Device> devices = new Gson().fromJson(jsonString, type);
			
			Integer blockId = Integer.parseInt(blockIdString);
			
			try {
				Boolean result = deviceDao.addDeviceToBlock( devices , blockId);
				if(result == true) {
					messageBuilder.set(200, "Device added successfully");
					return;
				}
				messageBuilder.set(400, "Request Failed");
				return;
				
			} catch (SQLException e) {
				messageBuilder.set(400, "Request Failed");
				e.printStackTrace();
			}
			
			return;
		}
		
		messageBuilder.set(400, messageBuilder.DEFAULT_INVALID_URL);
	}

}
