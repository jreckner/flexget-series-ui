package com.recknerd.flexget.ex

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = 'No such Show')
@InheritConstructors
@Slf4j
class ShowNotFoundException extends RuntimeException {
}
