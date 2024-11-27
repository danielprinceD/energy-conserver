package Builder;

import java.io.IOException;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

class Message {
	private Integer code;
	private String message;
	private String data;
	
	public void setCode(Integer code) {
		this.code = code;
		if(code == 200)
			message = "success";
		else message = "error";
	}
	
	
	public void setMessage(String message) {
		this.data = message;
	}
}

public class MessageBuilder {
	
	HttpServletResponse response;
	public final  String DEFAULT_INVALID_URL = "Enter valid url";
	
	public MessageBuilder(HttpServletResponse response) {
		this.response = response;
	}
	
	public MessageBuilder set(Integer code , String data) throws IOException {
		response.setStatus(code);
		Message message = new Message();
		message.setCode(code);
		message.setMessage(data);
		
		response.getWriter().write(new Gson().toJson(message));
		return this;
	}
	
	
	
}
