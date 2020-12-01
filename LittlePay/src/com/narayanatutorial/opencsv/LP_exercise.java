package com.narayanatutorial.opencsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class LP_exercise {
	
	public interface Comparable<T> extends java.lang.Comparable<T>{
		   public int compareTo(T other);
		}
	
	public static class tap implements java.lang.Comparable<tap>{
		 private int id;
		 private Date dateTimeUTC;
		 private String tapType;
		 private String stopId;
		 private String companyId;
		 private String busId;
		 private String PAN;

		 public tap(int id, Date dateTimeUTC, String tapType, String stopId, String companyId, String busId, String PAN) {
		    this.id = id;
		    this.dateTimeUTC = dateTimeUTC;
		    this.tapType = tapType;
		    this.stopId = stopId;
		    this.companyId = companyId;
		    this.busId = busId;
		    this.PAN = PAN;
		 }
		 
		 public String toString(){
		        return this.id + "" + this.dateTimeUTC + "" + this.tapType + "" + this.stopId + "" + this.companyId + "" +this.busId + "" + this.PAN + "";
		 }
		 
		 public int getid() {
			 return id;
		 }
		 
		 public Date getDateTime() {
			 return dateTimeUTC;
		 }
		 
		 public String getTapType() {
			 return tapType;
		 }
		 
		 public String getStopId() {
			 return stopId;
		 }
		 
		 public String getCompanyId() {
			 return companyId;
		 }
		 
		 public String getBusId() {
			 return busId;
		 }
		 
		 public String getPAN() {
			 return PAN;
		 }

		@Override
		public int compareTo(tap arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	public static String costComplete(String departure, String arrival) {
		if (departure.equals(arrival)) {
			return "0";
		} else if (departure.equals("Stop1") & arrival.equals("Stop2") | departure.equals("Stop2") & arrival.equals("Stop1")) {
			return "3.25";
		} else if (departure.equals("Stop1") & arrival.equals("Stop3") | departure.equals("Stop3") & arrival.equals("Stop1")) {
			return "5.50";
		} else {
			return "7.30";
		}
	}
	
	public static String costIncomplete(String departure) {
		System.out.println(departure);
		if (departure.equals(" Stop1") || departure.equals(" Stop3")) {
			return "7.30";
		} else {
			return "5.50";
		}
	}
	
	public static String timeDifference (Date start, Date finish) {
		
		long diff = finish.getTime()- start.getTime();
		return Long.toString(TimeUnit.SECONDS.convert(diff,TimeUnit.MILLISECONDS));
	}

	public static void output(List<tap> tapList) {
		try {
		FileWriter fw = new FileWriter("C:\\Users\\Michael\\Desktop\\LittlePay Excerise\\tapsOutput.csv",false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
			int i = 0;
			String [] headers = {"Started", "Finished", "DurationSecs", "FromStopId", "ToStopId", "ChargeAmount", "CompanyId", "BusID", "PAN", "Status"};
			pw.println(String.join(", ", headers));
			pw.flush();
			while (i < tapList.size()) {
				if (!(tapList.get(i).PAN).equals(tapList.get(i+1).PAN)){
					// Incomplete trip
					String [] outputArray = {tapList.get(i).dateTimeUTC.toString(),
						"Unknown",
						"Unknown",
						tapList.get(i).stopId,
						"Unknown",
						costIncomplete(tapList.get(i).stopId), 
						tapList.get(i).companyId, 
						tapList.get(i).busId, 
						tapList.get(i).PAN, 
						"Incomplete"};
					
					pw.println(String.join(",", outputArray));
					pw.flush();
				
				} else if (tapList.get(i).stopId.equals(tapList.get(i+1).stopId)) {
					// Cancelled trip
					String [] outputArray = {tapList.get(i).dateTimeUTC.toString(),
						tapList.get(i+1).dateTimeUTC.toString(),
						"0",
						tapList.get(i).stopId,
						tapList.get(i+1).stopId,
						costComplete(tapList.get(i).stopId, tapList.get(i+1).stopId), 
						tapList.get(i).companyId, 
						tapList.get(i).busId, 
						tapList.get(i).PAN, 
						"Cancelled"};
					pw.println(String.join(",", outputArray));
					pw.flush();
					i++;
				} else {
					// Completed trip
					String [] outputArray = {tapList.get(i).dateTimeUTC.toString(),
						tapList.get(i+1).dateTimeUTC.toString(),
						timeDifference(tapList.get(i).dateTimeUTC,tapList.get(i+1).dateTimeUTC),
						tapList.get(i).stopId,
						tapList.get(i+1).stopId,
						costComplete(tapList.get(i).stopId, tapList.get(i+1).stopId), 
						tapList.get(i).companyId, 
						tapList.get(i).busId, 
						tapList.get(i).PAN, 
						"Completed"};
					pw.println(String.join(",", outputArray));
					pw.flush();
					i++;
				}
				i++;
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public static void main(String[] args) throws NumberFormatException, ParseException {

		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<tap> tapList= new ArrayList<tap>();
		try (Scanner reader = new Scanner(new File("C:\\Users\\Michael\\Desktop\\LittlePay Excerise\\taps.csv"))) {
	        reader.nextLine();
	        while (reader.hasNextLine()){
	                String[] tempArray = reader.nextLine().split(",");
	                tap taps = new tap(Integer.parseInt(tempArray[0]), 
	                		df1.parse(tempArray[1]), 
                            tempArray[2],
                            tempArray[3],
                            tempArray[4],
                            tempArray[5],
                            tempArray[6]);
	                tapList.add(taps);
	        }

		} catch (IOException e) {
	       e.printStackTrace();
		}
		
		Collections.sort(tapList, Comparator.comparing(tap::getPAN)
	            .thenComparing(tap::getDateTime));

		output(tapList);
	}
}


