package com.binaryfoundain.poc.enums;

public enum AircraftTypeEnum {
	PASSENGER("passenger"), CARGO("cargo");

	private String value;

	AircraftTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public static String fromValue(String v) {
    	for(AircraftTypeEnum c : AircraftTypeEnum.values()){
    		if (c.value.equalsIgnoreCase(v)) {
                return c.value;
            }
        }
        return null; //throw new IllegalArgumentException(v);
    }
}
