package com.wipro.nexus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
		String responseCode="";
		String responseMsg="";
		try {
	    	System.out.println("############Create repository###################"+url);
			Map<String,String> response=fetchRepo(url,userName,password,projName,repoType,repoKey,repoPolicy,repoProvider);   
			for(Map.Entry<String,String> entry:response.entrySet())
			{
				responseCode=entry.getKey();
				responseMsg=entry.getValue();
			}
			System.out.println("aaaaaaaaaaa:::::"+responseCode);
			
			if("200".equals(responseCode))
			{
				List<String> repoNames=getRepoList(responseMsg);
			 boolean existFlag=false;
			 System.out.println("existFlag:::"+existFlag);
			 String uniqueRepoName=projName;
			while (true)	
			{
				existFlag=
						checkRepoExists(repoNames,uniqueRepoName);
				System.out.println("existFlag::"+existFlag);
				if (existFlag) {
					uniqueRepoName =uniqueRepoName+ "-" +projName ;
				}else {
					
					break;
				}
               
			}
			URL obj3 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			String userpass = userName + ":" + password;			   
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
			conn.setRequestProperty ("Authorization", basicAuth); 
			String data = "{\"data\": {\"repoType\": \""+repoType+"\",\"id\": \""+repoKey+"\",\"name\": \""+uniqueRepoName+"\",\"browseable\": true,\"indexable\": true, \"repoPolicy\": \""+repoPolicy+"\",\"provider\": \""+repoProvider+"\",\"providerRole\": \"org.sonatype.nexus.proxy.repository.Repository\", \"downloadRemoteIndexes\": true, \"autoBlockActive\": true,\"fileTypeValidation\": true,\"exposed\": true,\"checksumPolicy\": \"WARN\" }}";
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data);
			out.close();
			responseCode = conn.getResponseCode()+"";
			System.out.println("aaaaaaaaaaa:::::"+responseCode);
			
			if(responseCode.equals("201"))
		    {
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    	String line = "";
			    	responseMsg="repository created";
			    	
					while ((line = reader.readLine())!= null) 
					{
						System.out.println("line........."+line);
					}
    				    	
		        }
			else {
				responseMsg=errorHandling(responseCode);
			}
			System.out.println("response msg.................... = "+responseMsg);
			
			}
		}
			 catch (Exception e) {
				System.out.println("..err......."+e);
				responseMsg="Technical error";
			   	
			}
			
		
		return responseMsg;
		}
	
	public static String errorHandling( String code) {
		String Msg;
		
		switch(code)
	    {	
	    	case "503":
	    		Msg="Service unavailable";
	    		
	    		break;
	    	case "401":
	    		Msg="The authentication credentials are incorrect";
	    		
	    		break;
	    	case "403":
	    		Msg="User does not have permission";
	    		
	    		break;
	    	case "402":
	    		Msg="Your license has expired";
	    		
	    		break;				    
	    	case "400":
	    		Msg="Method not Allowed";
	    		
	    		break;		    	
	    	default:
	    		Msg="Unable to create repository";
	        }
		return Msg;
	}
	
	public static boolean checkRepoExists(List<String> repoNames, String projName) {
		boolean dupStatus=false;
		 for(String repoName:repoNames)
		 {
				System.out.println("projName:::"+projName+"Reponame::"+repoName);

			 if(projName.equals(repoName))
			 {
				 dupStatus=true;
				 
			 }
		 }
		return dupStatus;
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
	
	//Fetching the repositories in XML format
	public static Map<String,String> fetchRepo(String url,String userName,String password,String projName,String repoType,String repoKey,String repoPolicy,String repoProvider)throws Exception {
		
    	System.out.println("############Fetch repositories###################"+url);
	     URL obj3;
	     String line = "";
	     String responseMessage=null;
			Map responseMap=new HashMap();

		try {
			obj3 = new URL(url);
		
		HttpURLConnection conn = (HttpURLConnection) obj3.openConnection();
		conn.setRequestProperty("Content-Type", "application/xml");
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		String userpass = userName + ":" + password;			   
		String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		conn.setRequestProperty ("Authorization", basicAuth); 
		
		 String responseCode = conn.getResponseCode() + "";
		if(responseCode.equals("200"))
		    {
			    
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder str=new StringBuilder();
			    	
			    	  while ((line = reader.readLine())!= null) 
					    {
					    	//appending the XML data in a single line
					    	str.append(line);
					    	}
					    responseMessage=str.toString();
		    }
		    		
		    	
		else {
			responseMessage=errorHandling(responseCode);
		}
		
		responseMap.put(responseCode, responseMessage);
		}
		catch (Exception e) 
		{
			System.out.println(e);
	    }
		return responseMap;
		
	}
	
	
	// Converting the XML data in readable format(list)
	public static List<String> getRepoList(String xmlString)
	{
		ArrayList<String> repoNameList = new ArrayList<String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc =null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("repositories-item");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String repoName = eElement.getElementsByTagName("name").item(0).getTextContent();
				 System.out.println("Repository Names : " + repoName);
				 if(repoName !=null)
				 {
					 repoNameList.add(repoName);
				 }
				
			}
		}
		 return repoNameList;
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
		/////////////FOR TESTIG////////////////
		url="http://18.224.155.110:8081/nexus";
		String userName="admin";
		String password="admin123";
		//String userName = (String) toolObj.get("userName");
        //String password = (String) toolObj.get("password");
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
        		System.out.println("repoName::::"+repoName);
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
		        		  String createRepoStatus=createProject(projUrl,userName,password,repoName,repoType,key,repoPolicy,provider);
		        		  if(createRepoStatus == "err") {
			        		  repoName = "rig-"+repoName;
			        	  }else {
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
