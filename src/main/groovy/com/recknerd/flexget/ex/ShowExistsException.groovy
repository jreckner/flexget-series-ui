package com.recknerd.flexget.ex

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = 'Show already exists')
@InheritConstructors
@Slf4j
class ShowExistsException extends RuntimeException {
}
