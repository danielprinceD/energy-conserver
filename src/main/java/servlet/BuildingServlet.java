package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vo.BlockDetails;
import vo.Building;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.NonWritableChannelException;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Builder.JSONBuilder;
import Builder.MessageBuilder;
import Builder.PatternBuilder;
import dao.BuildingDao;
import dao.UserDao;

@WebServlet( { "/buildings" , "/buildings/*" })
public class BuildingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	BuildingDao buildingDao =  new BuildingDao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PatternBuilder patternBuilder = new PatternBuilder( "/([0-9]+)" , request.getPathInfo());
		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		Integer userId = null;
		try {
			userId = Integer.parseInt(request.getParameter("userId"));
			
		}catch (Exception e) {
		}
		
		if(userId != null) {
			List<Building> result = buildingDao.getBuildingList(userId);
			if(result.size() > 0)
			{
				messageBuilder.set(200, new Gson().toJson(result));
				return;
			}
			
			messageBuilder.set(400, "No Data is recorded");
			return;
		}
		
		if(patternBuilder.getGroupCount() == 1) {
			
			Integer buildingId = Integer.parseInt(patternBuilder.group(1));
			List<BlockDetails> result = buildingDao.getBuildingDetailsByBuildingId(buildingId);
			
			if(result == null) {
				messageBuilder.set(500, "Cannot Fetch Details");
				return;
			}
			
			if(result.size() > 0)
			{
				messageBuilder.set(200, new Gson().toJson(result));
				return;
			}
			
		}
		
		messageBuilder.set(400, "No Data Found");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PatternBuilder patternBuilder = new PatternBuilder( "/([0-9]+)" , request.getPathInfo());
		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		System.out.println(patternBuilder.getGroupCount());
		
		if(patternBuilder.getGroupCount() == 1) {
			
			
			Integer userId = Integer.parseInt( request.getParameter("userId") );
			
			String jsonString = new JSONBuilder().generateJson(request).build();
			
			Type listType = new TypeToken<List<Building>>(){}.getType();
			
			List<Building> buildings = new Gson().fromJson( jsonString, listType);
			
			buildingDao.createBuilding(buildings, userId);
			
			messageBuilder.set(200, "Buildings have been created");
			
			return;
		}
		
		messageBuilder.set(400, "Request Failed");
		
	}

}
