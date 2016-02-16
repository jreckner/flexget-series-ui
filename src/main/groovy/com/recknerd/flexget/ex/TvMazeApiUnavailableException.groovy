/*
 * Copyright 2015 Lexmark International, Inc.  All rights reserved.
 */

package com.recknerd.flexget.ex

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = 'TvMaze API Service Unavailable')
@InheritConstructors
@Slf4j
class TvMazeApiUnavailableException extends RuntimeException {

}
