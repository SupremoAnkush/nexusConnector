package com.wipro.nexus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class nexus {
	static String pwd = System.getProperty("user.dir");
	static File file = new File(pwd);
	static File parentPath = file.getParentFile();
	static String jsonFile = "data.json";
	public static String checkUrl(String url,String userName,String password) {
		 try {
		 	URL obj3 = new URL(url);
		 	System.out.println(url);
		    HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
		    conn.setRequestMethod("GET");
		    String userpass = userName + ":" + password;			   
		    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		    conn.setRequestProperty ("Authorization", basicAuth); 
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = "";
		    while ((line = reader.readLine())!= null) 
		    {
		    	System.out.println("*createurl**"+line);
		    	return line;
		    }
		    
	    } catch (Exception e) {
	    	System.out.println("check......."+e);
	    	return "success";
		}
		return "error";
		}
	public static String createProject(String url,String userName,String password,String projName,String repoType,String repoKey,String repoPolicy,String repoProvider) {
		try {
			URL obj3 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			String userpass = userName + ":" + password;			   
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
			conn.setRequestProperty ("Authorization", basicAuth); 
			String data = "{\"data\": {\"repoType\": \""+repoType+"\",\"id\": \""+repoKey+"\",\"name\": \""+projName+"\",\"browseable\": true,\"indexable\": true, \"repoPolicy\": \""+repoPolicy+"\",\"provider\": \""+repoProvider+"\",\"providerRole\": \"org.sonatype.nexus.proxy.repository.Repository\", \"downloadRemoteIndexes\": true, \"autoBlockActive\": true,\"fileTypeValidation\": true,\"exposed\": true,\"checksumPolicy\": \"WARN\" }}";
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data);
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) 
			{
				System.out.println("line........."+line);
			  	return line;
			}
			
			} catch (Exception e) {
				System.out.println("..err......."+e);
			   	return "err";
			}
		fetchRepo(url,userName,password,projName,repoType,repoKey,repoPolicy,repoProvider);
		return "error";
		}
	
	public static String fetchRepo(String url,String userName,String password,String projName,String repoType,String repoKey,String repoPolicy,String repoProvider) {
		
			 String projUrl = url+"/service/local/repositories";
		     URL obj3 = new URL(projUrl);
			HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			String userpass = userName + ":" + password;			   
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
			conn.setRequestProperty ("Authorization", basicAuth); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = "";
		    while ((line = reader.readLine())!= null) 
		    {
		    	System.out.println("output"+line);
		    }
		}
			
	
	private static String getServerInfo(String rigletName, String serverIp, String toolName) {
		try {
			String url=serverIp+"/riglets/getToolserverDetails";
			 URL obj3 = new URL(url);			 
			 HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
			 conn.setRequestProperty("Content-Type", "application/json");
			 conn.setDoOutput(true);
			 conn.setRequestMethod("POST");
//			 String userpass = userName + ":" + password;			   
//			 String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
//			 conn.setRequestProperty ("Authorization", basicAuth); 
			 String data = "{\"rigletName\":\""+rigletName+"\",\"toolName\":\""+toolName+"\"}";
			 OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			 out.write(data);
			 out.close();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 String line = "";
			 while ((line = reader.readLine())!= null) 
			 {
			  	return line;
			 }
		} catch (Exception e) {
		   	return "err";
		}
		return "error";
	}
	private static String postData1(String ip,String rigletName,List<String> list) {
		try {
			String url=ip+"/riglets/storeProjectData";
			 URL obj3 = new URL(url);			 
			 HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
			 conn.setRequestProperty("Content-Type", "application/json");
			 conn.setDoOutput(true);
			 conn.setRequestMethod("POST");
//			 String userpass = userName + ":" + password;			   
//			 String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
//			 conn.setRequestProperty ("Authorization", basicAuth); 
			 String data = "{\"blueprintData\":"+list+",\"rigletName\":\""+rigletName+"\"}";
			 OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			 out.write(data);
			 out.close();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 String line = "";
			 while ((line = reader.readLine())!= null) 
			 {
			  	return line;
			 }
		} catch (Exception e) {
		   	return "err";
		}
		return "error";
	}
	private static String postData(String ip,String rigletName,String list) {
		try {
			String url="http://3.15.143.196:5050/api/riglets/saveToolProjectInfo";
			 URL obj3 = new URL(url);			 
			 HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
			 conn.setRequestProperty("Content-Type", "application/json");
			 conn.setDoOutput(true);
			 conn.setRequestMethod("POST");
//			 String userpass = userName + ":" + password;			   
//			 String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
//			 conn.setRequestProperty ("Authorization", basicAuth); 
			 String data = list;
			 OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			 out.write(data);
			 out.close();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 String line = "";
			 while ((line = reader.readLine())!= null) 
			 {
			  	return line;
			 }
		} catch (Exception e) {
		   	return "err";
		}
		return "error";
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
//		String rigletName = args[0];
//		String serverIp = args[1];
		String rigletName = "Tycoon_8872";
		String serverIp = "http://23.101.140.72:3010";
		JSONParser parser=new JSONParser();
		Object obj;
		obj = parser.parse(new FileReader(file+"/"+rigletName+"/json/"+jsonFile));
		JSONObject jsonObject = (JSONObject) obj;
		JSONObject brmObj = (JSONObject) jsonObject.get("brm");
		JSONObject reposObj = (JSONObject) brmObj.get("repositories");
		JSONArray repoArr = (JSONArray) reposObj.get("repository");
	 	String toolName = "nexus";
		Object res = parser.parse(getServerInfo(rigletName, serverIp,toolName));
		JSONObject toolObj = (JSONObject) res;
		String url = (String) toolObj.get("url");
		String userName = (String) toolObj.get("userName");
        String password = (String) toolObj.get("password");
        String repoName = "";
        String key = "";
        
//      Create Repository
        for(Object repo: repoArr) {
        	JSONObject repoObj = (JSONObject) repo;
        	Boolean createStatus = (Boolean) repoObj.get("create");
        	if(createStatus) {
        		repoName= (String) repoObj.get("repository_name");
        		String repoType = (String) repoObj.get("repository_type");
        		String repoPolicy = (String) repoObj.get("repository_policy");
        		String provider = (String) repoObj.get("repository_provider");
        		String data ="";
        		int length=3;
        		while (data == "") {  
        		  String repoNameKey=repoName.replaceAll(" ","");  
        		  if(repoNameKey.length() >= length) {
  		        	  key=repoNameKey.substring(0, Math.min(repoNameKey.length(), length)).toUpperCase();
  			       }else {
  	        		  String appendStr = "";
  	        		  int projLength = repoNameKey.length();
  	        		  int currentLength=length;
  	        		  int len = currentLength%projLength;
  	        		  int repeat = currentLength/projLength;
          			System.out.println("data........."+repeat);
  	        		  for (int i=0;i<repeat;i++) { 
  	        			  appendStr = appendStr + repoNameKey;
  	        		  }
  	        		  appendStr=appendStr+repoName.substring(0, Math.min(repoNameKey.length(), len));
  	        		  key=appendStr.toUpperCase();
  	        	  }
  		          String checkUrl=url+"/service/local/repositories/"+key;
		          String checkKey= checkUrl(checkUrl,userName,password);
		          if (checkKey != "success") {
			        	 length=length + 1;
			      }else {
			    	  System.out.println("url "+url);
			    	  String projUrl = url+"/service/local/repositories";
		        	  boolean createProj = true;
		        	  while (createProj == true) {
		        		  String createRepoStatus=createProject(projUrl,userName,password,repoName,repoType,classifier,extension,version);
		        		  if(createRepoStatus == "err") {
			        		  repoName = "rig-"+repoName;
			        		  
			        		  
//			        		 if(repoName.substring(4)=repoName)
//			        		 {
//			        			 repoName = "rig-"+repoName+"-"+repoName;
//			        		 }
			        			 
			        	  }
		        		  else {
			        		  System.out.println("err "+createRepoStatus);
			        		  createProj = false; 
			        		  data = "success";
			        	  } 
		        	  }
			      }
        		}      
        	}
        	List<String> list = new ArrayList<>();
        	list.add("{\"toolName\":\"nexus\",\"repoKey\":\""+key+"\",\"repoName\":\""+repoName+"\",\"category\":\"brm\"}");
	   		postData1(serverIp, rigletName, list);
	   		System.out.println("list "+list);
	   		String data = "{\"toolName\":\"nexus\",\"rigletName\":\""+rigletName+"\",\"data\":{\"projectKey\":\""+key+"\",\"projectName\":\""+repoName+"\",\"projectUrl\":\""+url+"/#view-repositories\"}}";
	   		System.out.println("data "+data);
	   		postData(serverIp, rigletName, data);
        }
	}

}
