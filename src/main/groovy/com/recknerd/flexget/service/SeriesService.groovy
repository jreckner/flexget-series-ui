package com.recknerd.flexget.service

import com.recknerd.flexget.domain.model.FlexGetSeriesElement
import com.recknerd.flexget.domain.model.FlexGetSeriesElementSettings
import com.recknerd.flexget.domain.model.FlexGetTemplateGroup
import com.recknerd.flexget.ex.SeriesNotFoundException
import com.recknerd.flexget.ex.ShowExistsException
import com.recknerd.flexget.ex.ShowNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
class SeriesService {

    @Autowired
    FlexGetConfigDAO yamlManager

    FlexGetTemplateGroup getAllSeriesGroups() {
        return yamlManager.flexGetConfig.templates.template
    }

    List<FlexGetSeriesElement> getSeriesGroup(String seriesName) {
        return getSeriesByName(seriesName)
    }

    FlexGetSeriesElement getShowFromSeriesGroup(String seriesName, String showTitle) {
        List<FlexGetSeriesElement> seriesFromDAO = getSeriesByName(seriesName)
        return getShowByName(seriesFromDAO, showTitle)
    }

    FlexGetSeriesElement addShowToSeriesGroup(
            String seriesName, String showTitle, FlexGetSeriesElementSettings settings) {
        List<FlexGetSeriesElement> seriesFromDAO = getSeriesByName(seriesName)
        def elementForAddition = seriesFromDAO.find {
            it.title == showTitle
        }

        if (!elementForAddition) {
            def newShow = new FlexGetSeriesElement(showTitle)
            if (settings) {
                newShow.settings = settings
            }
            seriesFromDAO.add(newShow)
            yamlManager.write()
            return newShow
        }
        throw new ShowExistsException()
    }

    void removeShowFromSeriesGroup(String seriesName, String showTitle) {
        List<FlexGetSeriesElement> seriesFromDAO = getSeriesByName(seriesName)
        FlexGetSeriesElement elementForRemoval = getShowByName(seriesFromDAO, showTitle)
        seriesFromDAO.remove(elementForRemoval)
        yamlManager.write()
    }

    private List<FlexGetSeriesElement> getSeriesByName(String seriesName) {
        try {
             return yamlManager.flexGetConfig.templates.template.series[seriesName]
        }
        catch (MissingPropertyException) {
            throw new SeriesNotFoundException()
        }
    }

    private FlexGetSeriesElement getShowByName(List<FlexGetSeriesElement> seriesFromDAO, String showTitle) {
        FlexGetSeriesElement show = seriesFromDAO.find {
            it.title == showTitle
        }
        if (show) { return show }
        throw new ShowNotFoundException()
    }
}
