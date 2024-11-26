package Builder;


import java.io.*;

import jakarta.servlet.http.HttpServletRequest;

public class JSONBuilder {
	private StringBuilder jsonString = new StringBuilder();
	
	public JSONBuilder generateJson(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		String line;
        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        return this;
	}
	
	public String build() {
		return jsonString.toString();
	}
	
}
