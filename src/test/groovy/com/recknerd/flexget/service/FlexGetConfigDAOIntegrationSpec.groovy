package com.recknerd.flexget.service

import com.recknerd.flexget.configuration.FlexGetConfiguration
import com.recknerd.flexget.domain.model.FlexGetSeriesElement
import groovy.util.logging.Slf4j
import org.springframework.beans.FatalBeanException
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Slf4j
class FlexGetConfigDAOIntegrationSpec extends Specification {

    static final String RESOURCES = './src/test/resources/'

    def 'load config'() {
        setup:
        def cut = new FlexGetConfigDAO()
        cut.flexGetConfiguration = new FlexGetConfiguration(config: RESOURCES + 'testConfig.yml')

        when:
        cut.init()
        def backupFile = new File(RESOURCES + 'testConfig.yml.backup')

        then:
        cut.flexGetConfig.templates.template.series.hdGroup.size() == 7
        backupFile.exists()
        backupFile.delete()
    }

    def 'write config'() {
        setup:
        def src = new File(RESOURCES + 'testConfig.yml')
        def dst = new File("${src.canonicalPath}.test")
        if(dst.exists()) { dst.delete() }
        Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING)

        def cut = new FlexGetConfigDAO()
        cut.flexGetConfiguration = new FlexGetConfiguration(config: RESOURCES + 'testConfig.yml.test')

        when:
        cut.load()
        cut.flexGetConfig.templates.template.series.hdGroup.size() == 7
        cut.flexGetConfig.templates.template.series.hdGroup.add(new FlexGetSeriesElement('Test Show'))
        cut.flexGetConfig.templates.template.series.hdGroup.size() == 8
        cut.write()
        cut.flexGetConfig = null
        cut.load()

        then:
        cut.flexGetConfig.templates.template.series.hdGroup.find { it.title == 'Test Show' }
        cut.flexGetConfig.templates.template.series.hdGroup.size() == 8
        dst.delete()
    }

    def 'throw exception for missing config'() {
        setup:
        def cut = new FlexGetConfigDAO()
        cut.flexGetConfiguration = new FlexGetConfiguration(config: './missingConfig.yml')

        when:
        cut.init()

        then:
        thrown FatalBeanException
    }

    def 'create backup config'() {
        setup:
        def cut = new FlexGetConfigDAO()
        cut.flexGetConfiguration = new FlexGetConfiguration(config: RESOURCES + 'testConfig.yml')

        when:
        cut.createBackup(false)
        def backupFile = new File(RESOURCES + 'testConfig.yml.backup')

        then:
        backupFile.exists()
        backupFile.delete()
    }

    def 'create backup config with overwrite'() {
        setup:
        def cut = new FlexGetConfigDAO()
        cut.flexGetConfiguration = new FlexGetConfiguration(config: RESOURCES + 'testConfig.yml')

        when:
        cut.createBackup(true)
        def backupFile = new File(RESOURCES + 'testConfig.yml.backup')
        backupFile.exists()
        // TODO: We should add some data to the current file then look for it in the backup! See `write config` test
        cut.createBackup(true)
        // Look for our added data. Easiest thing would be a new show. See `write config` test

        then:
        backupFile.exists()
        backupFile.delete()
    }
}
