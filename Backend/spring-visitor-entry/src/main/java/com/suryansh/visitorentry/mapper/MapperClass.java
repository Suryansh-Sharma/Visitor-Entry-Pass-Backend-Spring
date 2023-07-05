package com.suryansh.visitorentry.mapper;

import com.suryansh.visitorentry.dto.VisitDto;
import com.suryansh.visitorentry.entity.VisitDocument;
import com.suryansh.visitorentry.entity.VisitingRecord;
import com.suryansh.visitorentry.entity.VisitorChildren;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * This class is used for performing a mapping operation.
 */
@Service
public class MapperClass {
    public VisitDto mapEntityToDto(VisitDocument visitDocument){
        VisitDto visitDto = new VisitDto();
        visitDto.setVisitorContact(visitDocument.getVisitorContact());
        visitDto.setVisitorName(visitDocument.getVisitorName());
        visitDto.setVisitorImage(visitDocument.getVisitorImage());
        visitDto.setReason(visitDocument.getVisitingRecords().get(visitDocument.getVisitingRecords().size()-1).getReason());
        visitDto.setLocation(visitDocument.getVisitingRecords().get(visitDocument.getVisitingRecords().size()-1).getLocation());
        visitDto.setLatestVisitDate(visitDocument.getLatestVisitDate());
        visitDto.setLatestVisitTime(visitDocument.getLatestVisitTime());

        // Mapping VisitorAddress
        VisitDto.VisitorAddressDto visitorAddressDto = new VisitDto.VisitorAddressDto();
        visitorAddressDto.setCity(visitDocument.getVisitorAddress().getCity());
        visitorAddressDto.setPinCode(visitDocument.getVisitorAddress().getPinCode());
        visitorAddressDto.setLine1(visitDocument.getVisitorAddress().getLine1());
        visitDto.setVisitorAddress(visitorAddressDto);

        visitDto.setHasChildrenInSchool(visitDocument.isHasChildrenInSchool());

        // Mapping VisitorChildren
        List<VisitDto.VisitorChildDto> visitorChildrenDto = new ArrayList<>();
        for (VisitorChildren visitorChild : visitDocument.getVisitorChildren()) {
            VisitDto.VisitorChildDto visitorDto = new VisitDto.VisitorChildDto();
            visitorDto.setChildName(visitorChild.getChildName());
            visitorDto.setChildClass(visitorChild.getChildClass());
            visitorChildrenDto.add(visitorDto);
        }
        visitDto.setVisitorChildren(visitorChildrenDto);

        // Mapping VisitingRecords
        List<VisitDto.VisitingRecordDto> visitingRecordsDto = new ArrayList<>();
        for (VisitingRecord visitingRecord : visitDocument.getVisitingRecords()) {
            VisitDto.VisitingRecordDto visitingRecordDto = new VisitDto.VisitingRecordDto();
            visitingRecordDto.setDate(visitingRecord.getDate());
            visitingRecordDto.setTime(visitingRecord.getTime());
            visitingRecordDto.setReason(visitingRecord.getReason());
            visitingRecordDto.setLocation(visitingRecord.getLocation());
            visitingRecordDto.setStatus(visitingRecord.getStatus());
            visitingRecordDto.setCalledOn(visitingRecord.getCalledOn());
            visitingRecordDto.setVisitCompletedOn(visitingRecord.getVisitCompletedOn());

            visitingRecordsDto.add(visitingRecordDto);
        }
        visitDto.setVisitingRecord(visitingRecordsDto);
        return visitDto;
    }
}
