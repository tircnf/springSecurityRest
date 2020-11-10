package security2

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*

@Secured('isAuthenticated()')
class AppController {
    @SuppressWarnings('unused')
    static responseFormats = ['json', 'xml']

    SpringSecurityService springSecurityService

    def whoami() {
        def principal = springSecurityService.getPrincipal() ?: [message: "No principal defined"]
        render principal as JSON
    }

}
