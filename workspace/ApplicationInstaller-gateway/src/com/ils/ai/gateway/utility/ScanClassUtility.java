package com.ils.ai.gateway.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inductiveautomation.ignition.common.sqltags.model.ScanClass;
import com.inductiveautomation.ignition.common.sqltags.model.types.ScanClassComparison;
import com.inductiveautomation.ignition.common.sqltags.model.types.ScanClassMode;
import com.inductiveautomation.ignition.common.sqltags.scanclasses.ScanClassDefinition;
import com.inductiveautomation.ignition.common.util.Flags;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.common.util.csv.CSVReader;

/** Given a CSV file as a String, analyze and create Scan Class.
 * @See 
 */
public class ScanClassUtility {
	private static final String CLSS = "ScanClassUtility";
	private static final String NAME = "name";
	private static final String MODE = "mode";
	private static final String LOW_RATE = "low_rate";
	private static final String HIGH_RATE = "high_rate";
	private static final String DRIVING_TAG = "driving_tag";
	private static final String COMP_MODE = "comparison_mode";
	private static final String COMP_VALUE = "comparison_value";
	private static final String STALE_TIMEOUT = "stale_timeout";
	private static final String FLAGS = "flags";

	private final Map<String,Integer> headers = new HashMap<>();
	private final LoggerEx log = LogUtil.getLogger(getClass().getPackage().getName());

	/**
	 * Create a list of scan classes from a byte array.
	 * @param provider
	 * @param bytes
	 * @return list of scan classes
	 */
	public List<ScanClass> listFromBytes(String targetProvider,byte[] bytes) {


		List<ScanClass> results = new ArrayList<>();

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

		String[] headerRow = (String[])rows.get(0);
		Map<String,Integer> headers = new HashMap<>();
		for (int i = 0; i < headerRow.length; i++) {
			headers.put(headerRow[i].toLowerCase(), Integer.valueOf(i));
		}


		for (int i = 1; i < rows.size(); i++) {

			try
			{
				results.add(scanClassFromRow((String[])rows.get(i)));
			} 
			catch (Exception ex) {
				log.warnf("%s.listFromBytes: Error on row %d (%s)",CLSS,i+1,ex.getMessage());
			}
		}
		return results;
	}

	private ScanClass scanClassFromRow(String[] row) {
		String name = extract(row, NAME);
		ScanClassMode mode = extractScanClassMode(row, MODE, ScanClassMode.Direct);
		Integer loRate = Integer.valueOf(extractInt(row, LOW_RATE, 1000));
		Integer hiRate = Integer.valueOf(extractInt(row, HIGH_RATE, 1000));
		String drivingPath = extract(row, DRIVING_TAG, "");
		ScanClassComparison compMode = extractScanClassComparison(row, COMP_MODE, ScanClassComparison.Equal);
		Integer staleTimeout = Integer.valueOf(extractInt(row, STALE_TIMEOUT, 10000));
		Integer flags = Integer.valueOf(extractInt(row, FLAGS, 0));
		double compVal = extractDouble(row, COMP_VALUE, 0.0D);

		ScanClassDefinition def = new ScanClassDefinition(name);
		def.setMode(mode);
		def.setLoRate(loRate);
		def.setHiRate(hiRate);
		def.setDrivingTagPath(drivingPath);
		def.setCompareMode(compMode);
		def.setCompareValue(Double.valueOf(compVal));
		def.setStaleTimeout(staleTimeout);
		def.setExecutionFlags(new Flags(new int[] { flags.intValue() }));
		return def;
	}

	private int extractInt(String[] row, String column, int defaultValue) {
		String s = extract(row, column, null);
		if (s == null) {
			return defaultValue;
		}
		return Integer.parseInt(s);
	}

	private double extractDouble(String[] row, String column, double defaultValue) {
		String s = extract(row, column, null);
		if (s == null) {
			return defaultValue;
		}
		return Double.parseDouble(s);
	}

	private ScanClassMode extractScanClassMode(String[] row, String column, ScanClassMode defaultVal) {
		String v = extract(row, column, defaultVal.toString());
		ScanClassMode rtnValue = defaultVal;
		try {
			rtnValue = ScanClassMode.valueOf(v);
		}
		catch (IllegalArgumentException e) {}
		return rtnValue;
	}
	
	private ScanClassComparison extractScanClassComparison(String[] row, String column, ScanClassComparison defaultVal) {
		String v = extract(row, column, defaultVal.toString());
		ScanClassComparison rtnValue = defaultVal;
		try {
			rtnValue = ScanClassComparison.valueOf(v);
		}
		catch (IllegalArgumentException e) {}
		return rtnValue;
	}

	private String extract(String[] row, String column) {
		return extract(row, column, "");
	}

	private String extract(String[] row, String column, String defaultValue) {
		Integer position = (Integer)headers.get(column.toLowerCase());
		if ((position == null) || (position.intValue() >= row.length)) {
			return defaultValue;
		}
		return row[position.intValue()];
	}

	private boolean isEmpty(String[] row, String column) {
		String cell = extract(row, column, null);
		return cell.isEmpty();
	}

}
