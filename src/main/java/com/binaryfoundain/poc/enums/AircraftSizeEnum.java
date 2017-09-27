package com.binaryfoundain.poc.enums;

public enum AircraftSizeEnum {
	SMALL("small"), LARGE("large");

	private String value;

	AircraftSizeEnum(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public static String fromValue(String v) {
    	for(AircraftSizeEnum c : AircraftSizeEnum.values()){
    		if (c.value.equalsIgnoreCase(v)) {
                return c.value;
            }
        }
        return null; //throw new IllegalArgumentException(v);
    }
}
