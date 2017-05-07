package com.github.handioq.diber.controller;

import com.github.handioq.diber.model.dto.ErrorResponseDto;
import com.github.handioq.diber.model.dto.RequestDto;
import com.github.handioq.diber.model.entity.Request;
import com.github.handioq.diber.service.RequestService;
import com.github.handioq.diber.utils.Constants;
import com.github.handioq.diber.utils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_URL + Constants.URL_REQUESTS)
public class RequestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    RequestService requestService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getRequestById(@PathVariable("id") long id) {
        Request request = requestService.getById(id);

        if (request == null) {
            return new ResponseEntity<>("Request not found", HttpStatus.NOT_FOUND);
        }

        RequestDto requestDto = Converter.toRequestDto(request);

        return new ResponseEntity<>(requestDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changeStatus(@PathVariable("id") long id, @RequestBody RequestDto requestDto) {
        Request request = requestService.getById(id);

        if (request == null) {
            ErrorResponseDto error = new ErrorResponseDto("Not found", "Request not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if (requestDto.getStatus() == null || requestDto.getStatus().isEmpty()) {
            ErrorResponseDto error = new ErrorResponseDto("Empty", "Status is empty");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        request.setStatus(requestDto.getStatus());
        requestService.saveOrUpdate(request);

        return new ResponseEntity<>(Converter.toRequestDto(request), HttpStatus.OK);
    }

}