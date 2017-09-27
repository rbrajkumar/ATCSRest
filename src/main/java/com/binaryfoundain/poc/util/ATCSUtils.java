package com.binaryfoundain.poc.util;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import com.binaryfoundain.poc.enums.AircraftSizeEnum;
import com.binaryfoundain.poc.enums.AircraftTypeEnum;
import com.binaryfoundain.poc.model.Aircraft;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ATCSUtils {
	
	public static boolean isValid(Aircraft ac){
		boolean valid = true;
		if(AircraftTypeEnum.fromValue(ac.getType()) == null || 
				AircraftSizeEnum.fromValue(ac.getSize()) == null) valid = false;
		return valid;
	}
	
	public static List<Aircraft> sortOutAC(List<Aircraft> list){
		Comparator<Aircraft> bySize = (s1, s2) -> s1.getSize().compareTo(s2.getSize()); // sort comparator on size
		Comparator<Aircraft> byDuration = (s1, s2) -> Long.compare(s1.getEntered(), s2.getEntered());  // sort comparator by time

		Collections.sort(list, bySize.thenComparing(byDuration));  // Sequence it, if you wanna keep same structure for both type here,
																   
		return list;
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
