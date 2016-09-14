package com.ils.ai.gateway.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.common.util.csv.CSVReader;

/** Given a CSV file as a byte array, analyze and create a list of lists.
 * Individual elements are trimmed and stripped of quotes, but are not
 * identified. The first line is ignored as commentary.
 */
public class CSVUtility {
	private static final String CLSS = "CSVUtility";
	private final LoggerEx log = LogUtil.getLogger(getClass().getPackage().getName());

	/**
	 * Create a list of lists of strings from a byte array.
	 * @param bytes
	 * @return list of rows
	 */
	public List<List<String>> listFromBytes(byte[] bytes) {
		
		List<List<String>> results = new ArrayList<>();

		List<String[]> rows = null;
		ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(inStream));
			rows = reader.readAll();
		}
		catch(IOException ioe) {
			log.warnf("%s.listFromBytes: Exception reading the CSV file (%s)",CLSS,ioe.getMessage());
		}
		finally {
			if (reader != null) {
				try{reader.close();} catch(IOException ignore){}
			}
		}

		// First row is comment 
		for (int i = 1; i < rows.size(); i++) {
			try
			{
				results.add(listFromRow((String[])rows.get(i)));
			} 
			catch (Exception ex) {
				log.warnf("%s.listFromBytes: Error on row %d (%s)",CLSS,i+1,ex.getMessage());
			}
		}
		return results;
	}

	/**
	 * @param row
	 * @return a list of strings corresponding to the supplied array.
	 */
	private List<String> listFromRow(String[] row) {
		List<String> list = new ArrayList<>();
		int index = 0;
		while( index<row.length) {
			String value = extract(row,index);
			list.add(value);
			index = index+1;
		}
		return list;
	}

	
	private String extract(String[] row, int col) {
		String val = row[col];
		if( val==null ) val = "";
		val = val.trim();
		val = stripQuotes(val);
		return val;
	}
	// Strip quotes off of the input string
	private String stripQuotes(String txt) {
		String result = txt;
		result = result.trim();
		if( result.endsWith("\"") )   result = result.substring(0,result.length()-1);
		if( result.startsWith("\"") ) result = result.substring(1);
		
		return result;
	}
}
