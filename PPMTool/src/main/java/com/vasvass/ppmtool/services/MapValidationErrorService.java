package com.vasvass.ppmtool.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.*;

import java.util.*;

/**
 * <p><i>Created on: 03/05/2019</i></p>
 *
 * @author vasvass
 */

@Service
public class MapValidationErrorService {

   public ResponseEntity<?> MapValidationService(BindingResult result) {

     if (result.hasErrors()) {

       Map<String, String> errorMap = new HashMap<>();

       for(FieldError error: result.getFieldErrors()){
         errorMap.put(error.getField(), error.getDefaultMessage());
       }

       return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
     }

     return null;
   }
}
