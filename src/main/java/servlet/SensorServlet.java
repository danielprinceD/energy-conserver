package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vo.Entry;
import vo.LiveSensorData;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import Builder.JSONBuilder;
import Builder.MessageBuilder;
import dao.DeviceDao;

@WebServlet("/sensors")
public class SensorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DeviceDao deviceDao = new DeviceDao();
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		String isLive = request.getParameter("live");
		Integer blockId = null;
		try {
			 blockId = Integer.parseInt(request.getParameter("blockId"));			
		} catch (Exception e) {
		}
		
		if(isLive.equals("true")) {
			List<LiveSensorData> entries = deviceDao.getLiveSensorData(blockId);
			
			if(entries.size() > 0) {
				messageBuilder.set(200, new Gson().toJson(entries));
				return;
			}
			messageBuilder.set(400, "No Result Found");
			return;
		}
		
		messageBuilder.set(400, messageBuilder.DEFAULT_INVALID_URL);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		String jsonString = new JSONBuilder().generateJson(request).build();
		
		System.out.println(jsonString);
		Entry entry = new Gson().fromJson(jsonString, Entry.class);
		
		Boolean result = deviceDao.addSensorData(entry);
		
		if(result == true) {
			messageBuilder.set(200, "Sensor Data Entered Successfuly");
			return;
		}
		
		
		messageBuilder.set(400, messageBuilder.DEFAULT_INVALID_URL);
		
	}

}
