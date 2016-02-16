package com.recknerd.flexget.web

import static org.springframework.web.bind.annotation.RequestMethod.*

import com.recknerd.flexget.domain.model.FlexGetSeriesElementSettings
import com.recknerd.flexget.domain.web.TvShow
import com.recknerd.flexget.domain.web.TvShowDetails
import com.recknerd.flexget.ex.SettingsDomainModelValidationException
import com.recknerd.flexget.service.SeriesService
import com.recknerd.flexget.service.TvMazeService
import groovy.util.logging.Slf4j
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
@RequestMapping(value = '/api/series')
@Slf4j
class SeriesController {

    @Autowired
    SeriesService seriesService

    @Autowired
    TvMazeService tvMazeShowSearchService

    @RequestMapping(method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = 'Return All Series',
            response = String,
            responseContainer = 'List',
            consumes = 'application/json',
            produces = 'application/json',
            nickname = 'retrieveAllSeries'
    )
    @ApiResponses([
            @ApiResponse(code = 200, message = 'Success'),
            @ApiResponse(code = 500, message = 'Internal Server Error'),
            @ApiResponse(code = 503, message = 'Server Unavailable')
    ])
    def getAllSeriesGroups() {
        def template = seriesService.allSeriesGroups

        List<TvShow> hdtvshows = []
        template.series.hdGroup.each {
            hdtvshows.add(it.asTvShow())
        }
        List<TvShow> stdtvshows = []
        template.series.stdGroup.each {
            stdtvshows.add(it.asTvShow())
        }
        return [
                'hdGroup': hdtvshows,
                'stdGroup': stdtvshows,
                'available_settings': availableSettings
        ]
    }

    @RequestMapping(value = '/{series}', method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = 'Get Series',
            response = String,
            responseContainer = 'List',
            consumes = 'application/json',
            produces = 'application/json',
            nickname = 'getSeries'
    )
    @ApiResponses([
            @ApiResponse(code = 200, message = 'Success'),
            @ApiResponse(code = 404, message = 'No such Series'),
            @ApiResponse(code = 500, message = 'Internal Server Error'),
            @ApiResponse(code = 503, message = 'Service Unavailable')
    ])
    def getSeries(
            @PathVariable('series') String series ) {
        def group = seriesService.getSeriesGroup(series)

        List<TvShow> tvshows = []
        group.each {
            tvshows.add(it.asTvShow())
        }
        return tvshows
    }

    @RequestMapping(value = '/{series}/{show}', method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = 'Get Show',
            response = String,
            responseContainer = 'List',
            consumes = 'application/json',
            produces = 'application/json',
            nickname = 'getShow'
    )
    @ApiResponses([
            @ApiResponse(code = 200, message = 'Success'),
            @ApiResponse(code = 404, message = 'No such Series'),
            @ApiResponse(code = 404, message = 'No such Show'),
            @ApiResponse(code = 500, message = 'Internal Server Error'),
            @ApiResponse(code = 503, message = 'Service Unavailable')
    ])
    def getShowFromSeries(
            @PathVariable('series') String series,
            @PathVariable('show') String show ) {
        def flexGetSeriesElement = seriesService.getShowFromSeriesGroup(series, show)
        return flexGetSeriesElement.asTvShow()
    }

    @RequestMapping(value = '/{series}/{show}/details', method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = 'Get Show with Details',
            response = String,
            responseContainer = 'List',
            consumes = 'application/json',
            produces = 'application/json',
            nickname = 'getShowWithDetails'
    )
    @ApiResponses([
            @ApiResponse(code = 200, message = 'Success'),
            @ApiResponse(code = 404, message = 'No such Series'),
            @ApiResponse(code = 404, message = 'No such Show'),
            @ApiResponse(code = 500, message = 'Internal Server Error'),
            @ApiResponse(code = 503, message = 'Service Unavailable')
    ])
    def getShowFromSeriesWithDetails(
            @PathVariable('series') String series,
            @PathVariable('show') String show ) {
        def flexGetSeriesElement = seriesService.getShowFromSeriesGroup(series, show)
        def tvShow = flexGetSeriesElement.asTvShow()
        def details = tvMazeShowSearchService.search(tvShow.title)
        return new TvShowDetails(tvShow: tvShow, status: details.status, rating: details.rating.average)
    }

    @RequestMapping(value = '/{series}/{show}', method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = 'Create Show with optional settings',
            response = String,
            responseContainer = 'List',
            consumes = 'application/json',
            produces = 'application/json',
            nickname = 'createShow'
    )
    @ApiResponses([
            @ApiResponse(code = 201, message = 'Created'),
            @ApiResponse(code = 404, message = 'No such Series'),
            @ApiResponse(code = 404, message = 'No such Show'),
            @ApiResponse(code = 500, message = 'Internal Server Error'),
            @ApiResponse(code = 503, message = 'Service Unavailable')
    ])
    def addShowToSeries(
            @PathVariable('series') String series,
            @PathVariable('show') String show,
            @RequestBody(required = false) @Valid FlexGetSeriesElementSettings settings,
            BindingResult bindingResult ) {
        if (bindingResult.hasErrors()) {
            log.error 'Invalid add show with settings Request {}'
            throw new SettingsDomainModelValidationException('Invalid add show to series request', bindingResult)
        }
        def flexGetSeriesElement = seriesService.addShowToSeriesGroup(series, show, settings)
        return flexGetSeriesElement.asTvShow()
    }

    @RequestMapping(value = '/{series}/{show}', method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(
            value = 'Delete a Show',
            nickname = 'createShow'
    )
    @ApiResponses([
            @ApiResponse(code = 204, message = 'No Content'),
            @ApiResponse(code = 404, message = 'No such Series'),
            @ApiResponse(code = 404, message = 'No such Show'),
            @ApiResponse(code = 500, message = 'Internal Server Error'),
            @ApiResponse(code = 503, message = 'Service Unavailable')
    ])
    def removeShowFromSeries(
            @PathVariable('series') String series,
            @PathVariable('show') String show ) {
        log.error("Deleting $show")
        seriesService.removeShowFromSeriesGroup(series, show)
        return []
    }

    private getAvailableSettings() {
        def availableSettings = FlexGetSeriesElementSettings.metaClass.properties*.name
        availableSettings.remove('class')
        return availableSettings
    }
}
