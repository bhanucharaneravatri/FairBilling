package codingTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FairBilling {
	private static Integer timeBetween(String str1, String str2) {
		String[] a1=str1.split(":");
		String[] a2=str2.split(":");
		return 3600*(Integer.parseInt(a2[0])-Integer.parseInt(a1[0]))
				+60*(Integer.parseInt(a2[1])-Integer.parseInt(a1[1]))
				+(Integer.parseInt(a2[2])-Integer.parseInt(a1[2]));
		
	}
	private static boolean isValid(String data) {
		String[] arr =data.split(" "); 
		if(arr.length!=3)
			return false;
		String[] timeStamp =arr[0].split(":");
		if(timeStamp.length!=3)
			return false;
		try {
		Integer hours = Integer.parseInt(timeStamp[0]);
		if(hours>23 || hours<0)
			return false;
		}
		catch(NumberFormatException ex)
		{
			return false;
		}
		try {
			Integer minutes = Integer.parseInt(timeStamp[1]);
			if(minutes>59 || minutes<0)
				return false;
			}
			catch(NumberFormatException ex)
			{
				return false;
			}
		try {
			Integer seconds = Integer.parseInt(timeStamp[2]);
			if(seconds>59 || seconds<0)
				return false;
			}
			catch(NumberFormatException ex)
			{
				return false;
			}
			
		String userName = arr[1];
		for(int i = 0;i<userName.length();i++) {
			char a = userName.charAt(i);
			if((a>='a'&&a<='z')||(a>='A'&&a<='Z')||(a>='0'&&a<='9'))
				continue;
			else
				return false;
		}
		if(!(arr[2].equals("Start") || arr[2].equals("End")))
			return false;
		return true;
	}
	public static void main(String[] args) {
	    try {
	      File myObj = new File( args[0]);
	      Scanner myReader = new Scanner(myObj);
	      ArrayList<Log> logs = new ArrayList<>();
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        if(isValid(data))
	        logs.add(new Log(data));  
	      }
	      myReader.close();
	      String startTime = logs.get(0).data.split(" ")[0];
	      String endTime = logs.get(logs.size()-1).data.split(" ")[0];
	      HashMap<String,Integer> sessionDuration = new HashMap<>();
	      HashMap<String,Integer> sessionCount = new HashMap<>();
	      for(int i=0;i<logs.size();i++)
	      {
	    	  if(logs.get(i).flag==false)
	    	  {
	    	  String uname = logs.get(i).data.split(" ")[1];
	    	  	  if(logs.get(i).data.split(" ")[2].equals("End"))
	    	  		{
	    			  sessionDuration.put(uname,sessionDuration.getOrDefault(uname, 0)+ timeBetween(startTime,logs.get(i).data.split(" ")[0]));
	    			  sessionCount.put(uname, sessionCount.getOrDefault(uname,0)+1);
	    			  logs.get(i).flag=true;
	    	  		}
	    	  	  else if(logs.get(i).data.split(" ")[2].equals("Start"))
	    	  	  {
	    	  		  boolean found = false;
	    	  		  for(int j =i+1;j<logs.size();j++)
	    	  		  {
	    	  			  if(logs.get(j).flag==false 
	    	  					  && logs.get(j).data.split(" ")[1].equals(uname)
	    	  					  && logs.get(j).data.split(" ")[2].equals("End"))
	    	  			  {
	    	  				sessionDuration.put(uname, sessionDuration.getOrDefault(uname, 0)+ timeBetween(logs.get(i).data.split(" ")[0],logs.get(j).data.split(" ")[0]));
	    	  				sessionCount.put(uname, sessionCount.getOrDefault(uname,0)+1);
	    	  				logs.get(i).flag=true;
	    	  				logs.get(j).flag=true;
	    	  				found = true;
	    	  				break;
	    	  			  }
	    	  		  }
	    	  		  if(found==false) {
	    	  			  logs.get(i).flag=true;
	    	  			sessionDuration.put(uname,sessionDuration.getOrDefault(uname, 0)+ timeBetween(logs.get(i).data.split(" ")[0],endTime));
    	  				sessionCount.put(uname, sessionCount.getOrDefault(uname,0)+1);  
	    	  		  }
	    	  	  }
	    	  }
	      }
	      for (Map.Entry<String, Integer> set :sessionDuration.entrySet()) {
	            System.out.println(set.getKey() +" "
	            		+sessionCount.get(set.getKey())+ " "
	                               + set.getValue());
	        }
	    } catch (FileNotFoundException e) {
	      System.out.println("File not Found");
	    }
	  }
}
class Log {
	public Log(String data) {
		this.data = data;
	}
	boolean flag = false;
	String data;
}
