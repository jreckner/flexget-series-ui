package com.recknerd.flexget.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IndexController {

    @RequestMapping('/')
    def index() {
        return 'index'
    }

}
