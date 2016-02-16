package com.recknerd.flexget.ex

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = 'Invalid Request Model')
@InheritConstructors
@Slf4j
class SettingsDomainModelValidationException extends RuntimeException {
}
