package com.suryansh.visitorentry.service;

import com.suryansh.visitorentry.dto.PagingVisitDto;
import com.suryansh.visitorentry.dto.TelegramMessageDto;
import com.suryansh.visitorentry.dto.VisitDto;
import com.suryansh.visitorentry.entity.VisitDocument;
import com.suryansh.visitorentry.entity.VisitingRecord;
import com.suryansh.visitorentry.entity.VisitorAddress;
import com.suryansh.visitorentry.entity.VisitorChildren;
import com.suryansh.visitorentry.exception.SpringVisitorException;
import com.suryansh.visitorentry.mapper.MapperClass;
import com.suryansh.visitorentry.repository.VisitRepository;
import com.suryansh.visitorentry.service.interfaces.TelegramService;
import com.suryansh.visitorentry.service.interfaces.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

/**
 * This class is used for performing visit related operation.
 *
 * @author suryansh
 */
@Service
public class VisitorServiceImpl implements VisitorService {
    private static final Logger logger = LoggerFactory.getLogger(VisitorServiceImpl.class);
    private final VisitRepository visitRepository;
    private final MapperClass mapperClass;
    private final MongoTemplate mongoTemplate;
    private final TelegramService telegramService;

    public VisitorServiceImpl(VisitRepository visitRepository, MapperClass mapperClass, MongoTemplate mongoTemplate, TelegramService telegramService) {
        this.visitRepository = visitRepository;
        this.mapperClass = mapperClass;
        this.mongoTemplate = mongoTemplate;
        this.telegramService = telegramService;
    }

    /**
     * This method adds a new visit.
     * It handles both users is present or not present situation.
     * @param visitModel The visit data to be added.
     * @return A completableFuture representing the asynchronous result of adding a {@link VisitDto}.
     */
    @Override
    @Transactional
    @Async
    public CompletableFuture<VisitDto> addNewVisitInDb(VisitDto visitModel) {
        var visitDocument = visitRepository.findById(visitModel.getVisitorContact());
        VisitDocument newVisitDocument = visitDocument.map(
                        document -> addNewVisitPersonIsPresent(document, visitModel))
                .orElseGet(() -> addNewVisitPersonNotPresent(visitModel)
                );
        try {
            visitRepository.save(newVisitDocument);
            logger.info("New Visit is added for user {} to {} :addNewVisitPersonNotPresent ",
                    visitModel.getVisitorName(), visitModel.getLocation());
            var telegramMessageDto = new TelegramMessageDto(
                    visitModel.getVisitorContact(), visitModel.getVisitorName(),
                    visitModel.getReason(), visitModel.getVisitorContact(), visitModel.getLocation(),
                    visitModel.getVisitorAddress().getCity(), visitModel.getVisitorAddress().getLine1()
            );
            telegramService.sendNewVisitMessage(telegramMessageDto);
            return CompletableFuture.completedFuture(visitModel);
        } catch (Exception e) {
            logger.error("Unable to add new visit :addNewVisitPersonNotPresent " + e);
            throw new SpringVisitorException("Unable to add new visit ", ErrorType.BAD_REQUEST
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * It returns Visitor details by its contact no.
     * @param visitorContact The visitor contact number.
     * @return The visitor details dto {@link VisitDto}.
     */
    @Override
    public VisitDto getVisitorDetailByContact(String visitorContact) {
        VisitDocument visitDocument = visitRepository.findById(visitorContact)
                .orElseThrow(() -> new SpringVisitorException("Unable to find visitor ", ErrorType.NOT_FOUND
                        , HttpStatus.NOT_FOUND));
        return mapperClass.mapEntityToDto(visitDocument);
    }

    /**
     * It retrieves all visitors on a specific date.
     * @param date The date in format "yyyy-MM-dd".
     * @param pageSize pass page-size for pagination.
     * @param pageNumber pass page-number.
     * @return A PagingVisitDto {@link PagingVisitDto} object containing the page size, page number, list of visits, total pages, and total data.
     */
    @Override
    public PagingVisitDto getVisitorOnSpecificDate(String date, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VisitDocument> visitDocumentPage = visitRepository.findVisitBySpecificDate(date, pageable);
        List<VisitDto> visitDocuments = visitDocumentPage.stream()
                .map(mapperClass::mapEntityToDto)
                .toList();
        return new PagingVisitDto(
                pageNumber,
                visitDocumentPage.getTotalPages(),
                visitDocuments,
                pageSize,
                visitDocumentPage.getTotalElements()
        );
    }
    /**
     * This method is used to get today's all visits.
     * @param pageSize Pass page size for pagination.
     * @param pageNumber Pass page-no for pagination.
     * @return A PagingVisitDto {@link PagingVisitDto} object containing the page size, page number, list of visits, total pages, and total data.
     */
    @Override
    public PagingVisitDto getVisitorOfToday(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Instant currentInstant = Instant.now();
        LocalDate localDate = currentInstant.atZone(ZoneId.systemDefault()).toLocalDate();

        Page<VisitDocument> visitDocumentPage = visitRepository.findVisitBySpecificDate(localDate.toString(), pageable);
        List<VisitDto> visitDocuments = visitDocumentPage.stream()
                .map(mapperClass::mapEntityToDto)
                .toList();

        return new PagingVisitDto(
                pageNumber,
                visitDocumentPage.getTotalPages(),
                visitDocuments,
                pageSize,
                visitDocumentPage.getTotalElements()
        );
    }

    /**
     * This method searches records based on the provided search data, date, field, page size, and page number.
     *
     * @param searchData The search data to match.
     * @param date The specific date to search (optional).
     * @param field The field to search within.
     * @param pageSize The page size for pagination.
     * @param pageNumber The page number for pagination.
     * @return A {@link PagingVisitDto} object containing the page size, page number, list of visits, total pages, and total data.
     * @throws SpringVisitorException If the field provided is not supported.
     */
    @Override
    public PagingVisitDto searchRecords(String searchData, String date, String field, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        switch (field) {
            case "allRecords" -> logger.info("");
            case "contact" -> criteria.add(Criteria.where("_id").regex(searchData));
            case "visitorName" ->
                    criteria.add(Criteria.where("visitorName").regex(Pattern.compile(searchData, Pattern.CASE_INSENSITIVE)));
            case "Reception", "Rajesh Sir", "Sanjeev Sir", "Principal Sir", "P.T.M", "Teacher", "Admission" -> {
                criteria.add(Criteria.where("visitingRecords.location").regex(field));
                criteria.add(Criteria.where("visitorName").regex(Pattern.compile(searchData, Pattern.CASE_INSENSITIVE)));
            }
            case "ChildrenName" ->
                    criteria.add(Criteria.where("visitorChildren.childName").regex(Pattern.compile(searchData, Pattern.CASE_INSENSITIVE)));
            default ->
                    throw new SpringVisitorException("Unable to find this field " + field, ErrorType.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        if (date != null && date.length() > 0) {
            criteria.add(Criteria.where("latestVisitDate").regex(date));
        }

        query.addCriteria(new Criteria()
                .andOperator(criteria.toArray(new Criteria[0]))
        );
        Page<VisitDocument> visitDocumentPage = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, VisitDocument.class
                ), pageable, () -> mongoTemplate.count(query.skip(0).limit(0), VisitDocument.class)
        );
        List<VisitDto> visitDocuments = visitDocumentPage.stream()
                .map(mapperClass::mapEntityToDto)
                .toList();
        return new PagingVisitDto(
                pageNumber,
                visitDocumentPage.getTotalPages(),
                visitDocuments,
                pageSize,
                visitDocumentPage.getTotalElements()
        );
    }

    /**
     *This method adds a new visit when the person is not present.
     *
     * @param model The {@link VisitDto} object containing the visit details.
     * @return The created {@link VisitDocument} object.
     */
    private VisitDocument addNewVisitPersonNotPresent(VisitDto model) {
        Instant currentInstant = Instant.now();
        LocalDate localDate = currentInstant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = currentInstant.atZone(ZoneId.systemDefault()).toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = localTime.format(formatter);

        VisitorAddress address = new VisitorAddress(model.getVisitorAddress().getCity(), model.getVisitorAddress().getPinCode(),
                model.getVisitorAddress().getLine1());
        VisitingRecord newVisitingRecord = createNewVisitingRecord(currentInstant, model.getReason(), model.getLocation());
        List<VisitingRecord> visitingRecords = new ArrayList<>();
        visitingRecords.add(newVisitingRecord);
        List<VisitorChildren> visitorChildren = new ArrayList<>();
        if (model.isHasChildrenInSchool()) {
            model.getVisitorChildren().forEach(visitorChild ->
                    visitorChildren.add(new VisitorChildren(visitorChild.getChildName(), visitorChild.getChildClass()))
            );
        }
        return VisitDocument.builder()
                .visitorContact(model.getVisitorContact())
                .visitorName(model.getVisitorName())
                .visitorImage(model.getVisitorImage())
                .visitorAddress(address)
                .hasChildrenInSchool(model.isHasChildrenInSchool())
                .latestVisitDate(localDate.toString())
                .latestVisitTime(formattedTime)
                // For future update
                .visitCompleted(true)
                .visitorChildren(visitorChildren)
                .visitingRecords(visitingRecords)
                .build();
    }

    /**
     * This method adds a new visit when the person is present.
     *
     * @param visitDocument The existing {@link VisitDocument} object.
     * @param model The {@link VisitDto} object containing the visit details.
     * @return The updated {@link VisitDocument} object.
     */
    private VisitDocument addNewVisitPersonIsPresent(VisitDocument visitDocument, VisitDto model) {
        Instant currentInstant = Instant.now();

        LocalDate localDate = currentInstant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = currentInstant.atZone(ZoneId.systemDefault()).toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = localTime.format(formatter);

        VisitingRecord newVisitingRecord = createNewVisitingRecord(currentInstant, model.getReason(), model.getLocation());
        List<VisitingRecord> visitingRecords = visitDocument.getVisitingRecords();
        visitingRecords.add(newVisitingRecord);
        visitDocument.setLatestVisitDate(localDate.toString());
        visitDocument.setLatestVisitTime(formattedTime);
        visitDocument.setVisitingRecords(visitingRecords);
        return visitDocument;
    }

    /**
     * This method creates a new visiting record based on the provided instant, reason, and location.
     *
     * @param instant The instant representing the visit date and time.
     * @param reason The reason for the visit.
     * @param location The location of the visit.
     * @return The created {@link VisitingRecord} object.
     */
    private VisitingRecord createNewVisitingRecord(Instant instant, String reason, String location) {
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = instant.atZone(ZoneId.systemDefault()).toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = localTime.format(formatter);

        return VisitingRecord.builder()
                .date(localDate.toString())
                .time(formattedTime)
                .reason(reason)
                .location(location)
                .status("New Visit is added .*This is old version")
                .build();
    }
}
