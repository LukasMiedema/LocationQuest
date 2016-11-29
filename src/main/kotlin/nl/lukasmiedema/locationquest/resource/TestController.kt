package nl.lukasmiedema.locationquest.resource

import nl.lukasmiedema.locationquest.LocationQuestApplication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Lukas Miedema
 */
@RestController
@Transactional
open class TestController {

    @RequestMapping("/something")
    open fun something():String {
        return LocationQuestApplication.REST_PREFIX;
    }
}