package com.binaryfoundain.poc;

import static com.binaryfoundain.poc.util.ATCSUtils.isValid;
import static com.binaryfoundain.poc.util.ATCSUtils.sortOutAC;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.binaryfoundain.poc.constant.ATCSApplicationConstants;
import com.binaryfoundain.poc.enums.AircraftTypeEnum;
import com.binaryfoundain.poc.model.Aircraft;

@RestController
public class TrafficController {

    private List<Aircraft> passengerAC = new ArrayList<Aircraft>();
    private List<Aircraft> cargoAC = new ArrayList<Aircraft>();
    
    @RequestMapping("/atcs/boot")
    public String start() {
    	passengerAC.clear();
    	cargoAC.clear();
        return ATCSApplicationConstants.BOOT;
    }
    
    @RequestMapping(value = "/atcs/enqueue", method = RequestMethod.POST)
    public String add(@RequestBody Aircraft ac) {
    	if(isValid(ac)) {
    		if(AircraftTypeEnum.CARGO.toString().equalsIgnoreCase(ac.getType())){
    			cargoAC.add(ac);
    		} else {
    			passengerAC.add(ac);
    		}
    		return ATCSApplicationConstants.ADDED;
    	}
        return ATCSApplicationConstants.REJECTED;
    }
    
    @RequestMapping("/atcs/dequeue")
    public Aircraft getNextOut() throws Exception {
    	List<Aircraft> out = (!passengerAC.isEmpty()) ? passengerAC : cargoAC;
    	if(out.isEmpty()){
    		throw new Exception(ATCSApplicationConstants.UNAVAILABLE);
    	}
    	out = sortOutAC(out);
		Aircraft release = out.get(0);
		out.remove(0);
		return release;
    }
}
