/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.ontoqa.controller;

import com.acmutv.ontoqa.model.SimpleResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A simple controller to test that app is up and running.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/simple")
public class SimpleController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleController.class);

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity getGreeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
    LOGGER.info("Received: name={}", name);
    try {
      final String message = makeGreeting(name);
      return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse(System.currentTimeMillis(), message));
    } catch (IllegalArgumentException exc) {
      LOGGER.error(exc.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exc.getMessage());
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity postGreeting(@RequestBody JsonNode body) {
    LOGGER.info("Received: body={}", body);
    final String name = body.get("name").asText("world");
    try {
      final String message = makeGreeting(name);
      return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse(System.currentTimeMillis(), message));
    } catch (IllegalArgumentException exc) {
      LOGGER.error(exc.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exc.getMessage());
    }
  }

  public static String makeGreeting(String name) {
    if ("error".equalsIgnoreCase(name)) {
      throw new IllegalArgumentException("Illegal name");
    }
    return String.format("Hello %s", name);
  }
}
