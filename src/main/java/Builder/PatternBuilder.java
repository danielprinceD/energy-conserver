package Builder;

import java.util.regex.*;

public class PatternBuilder {
	private Pattern pattern;
	private Matcher matcher;
	
	public PatternBuilder(String regex , String pathInfo ) {
		pattern = Pattern.compile(regex);
		if(pathInfo == null)pathInfo = "";
		matcher = pattern.matcher(pathInfo);
	}
	
	public Integer getGroupCount() {
		if(!matcher.find())
			return 0;
		return matcher.groupCount();
	}
	
	public Boolean has() {
		return matcher.find();
	}
	
	public String group(Integer group) {
		return matcher.group(group);
	}
	
}
