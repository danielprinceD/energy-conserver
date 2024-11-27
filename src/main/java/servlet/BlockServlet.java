package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vo.Block;
import vo.Device;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Builder.JSONBuilder;
import Builder.MessageBuilder;
import Builder.PatternBuilder;
import dao.BlockDao;

@WebServlet( { "/blocks" , "/blocks/*" } )
public class BlockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	BlockDao blockDao = new BlockDao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PatternBuilder patternBuilder = new PatternBuilder("/([0-9]+)", request.getPathInfo());
		MessageBuilder messageBuilder = new MessageBuilder(response);
		
		if(patternBuilder.getGroupCount() == 1) {
			
			Integer blockId = Integer.parseInt( patternBuilder.group(1) );
			List<Device> blockDevices = blockDao.getBlockDevices(blockId);
			
			if(blockDevices.size() > 0) {
				messageBuilder.set(200, new Gson().toJson(blockDevices));
				return;
			}
			
			messageBuilder.set(400, "No data found");
			return;
		}
		
		messageBuilder.set(400, messageBuilder.DEFAULT_INVALID_URL );
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MessageBuilder messageBuilder = new MessageBuilder(response);
		String buildingIdString = request.getParameter("buildingId");
		String jsonString = new JSONBuilder().generateJson(request).build();
		
		if(buildingIdString != null) {
			Integer buildId = Integer.parseInt(buildingIdString);
			Type type = new TypeToken<List<Block>>() {}.getType();
			
			List<Block> devices = new Gson().fromJson(jsonString, type);
			
			Boolean result = blockDao.createBlockForBuildingId(buildId, devices);
			
			if(result == true) {
				messageBuilder.set(200, "Blocks Inserted Successfully");
				return;
			}
			
			messageBuilder.set(400, "Request Failed");
			return;
			
		}
		
		messageBuilder.set( 400, messageBuilder.DEFAULT_INVALID_URL);
		
		
	}

}
