 package com.lifetrenz.lths.appointment.model.value_object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String id;
    private String locationName;
    private String aliasName;
    private String locationCode;
    private String locationStatus;
    private String locationType;
    private String administrativeLocationTypeIdentifierCode;
//    private String landline;
//    private Boolean isExternal;
//    private String customerBusinessSite;
//    private Boolean isStockArea;
//    private TelecomDTO mobile;
    

}