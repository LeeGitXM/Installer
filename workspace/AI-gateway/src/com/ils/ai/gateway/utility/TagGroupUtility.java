package com.ils.ai.gateway.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ils.common.JsonToJava;
import com.inductiveautomation.ignition.common.config.PropertyValue;
import com.inductiveautomation.ignition.common.tags.config.CommonTagGroupProperties;
import com.inductiveautomation.ignition.common.tags.config.TagGroupConfiguration;
import com.inductiveautomation.ignition.common.tags.config.TagGroupMode;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;

/** Given a JSON file as a byte array, analyze and create Scan Class.
 * @See 
 */
public class TagGroupUtility {
	private static final String CLSS = "TagGroupUtility";
	// These values are keys in the JSON file
	private static final String NAME = "name";
	private static final String DRIVING_COMPARISON = "drivingComparison";
	private static final String DRIVING_COMPARISON_VALUE = "drivingComparisonValue";
	private static final String DRIVING_EXPRESSION = "drivingExpression";
	private static final String LEASED_RATE = "leasedRate";
	private static final String MODE = "mode";
	private static final String ONE_SHOT = "oneShot";   // Not observed
	private static final String RATE = "rate";
	
	/*
	 * The following constants were observed in exported tag groups, 
	 * but there is no obvious correspondence in CommonTagGroupProperties.
	 * Consequently these paramaters have not been considered in the tag group configuration.
	private static final String OPC_DATA_MODE = "opcDataMode";
	private static final String OPC_RATE = "opcRate";
	private static final String OPC_WRITE_TIMEOUT = "opcWriteTimeout";
	private static final String OPC_WRITES = "opcWrites";
	private static final String READ_AFTER_WRITE = "readAfterWrite";
	private static final String OPT_WRITE_TIMEOUT = "optWriteTimeout";
	private static final String OPT_WRITES = "optWrites";
	*/


	private final Map<String,Integer> headers = new HashMap<>();
	private final LoggerEx log = LogUtil.getLogger(getClass().getPackage().getName());

	/**
	 * Create a list of scan classes from a byte array.
	 * @param provider
	 * @param bytes
	 * @return list of scan classes
	 */
	public List<TagGroupConfiguration> listFromBytes(String targetProvider,byte[] bytes) {
		
		List<TagGroupConfiguration> results = new ArrayList<>();
		String contents = new String(bytes);
		List<Map<String,?>> configMaps = new JsonToJava().jsonToListOfMaps(contents);
		for(Map map:configMaps) {
			Object val;
			TagGroupConfiguration tg = new TagGroupConfiguration(map.get(NAME).toString(),true);
			val = map.get(DRIVING_COMPARISON);
			if( val!=null ) {
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.DrivingComparison,val));  // Datatype?
			}
			val = map.get(DRIVING_COMPARISON_VALUE);
			if( val!=null ) {
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.DrivingComparisonValue,val));
			}
			val = map.get(DRIVING_EXPRESSION);
			if( val!=null ) {
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.DrivingExpression,val));
			}
			val = map.get(LEASED_RATE);
			if( val!=null ) {
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.LeasedRate,val));
			}
			val = map.get(MODE);
			if( val!=null ) {
				TagGroupMode mode = TagGroupMode.valueOf(val.toString());
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.Mode,mode));
			}
			val = map.get(ONE_SHOT);
			if( val!=null ) {
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.OneShot,val));
			}
			val = map.get(RATE);
			if( val!=null ) {
				tg.getConfig().set(new PropertyValue(CommonTagGroupProperties.Rate,val));
			}

	
			results.add(tg);
		}
		//log.info(contents);  // Use to dump contents of JSON

		
		return results;
	}

}
