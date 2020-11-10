package security2

class BootStrap {
    def springSecurityService

    def init = { servletContext ->

        environments {
            development {
                User.withTransaction {

                    User admin = new User(username: "admin", password: "pass").save(flush: true, failOnError: true)
                    Role rAdmin = new Role(authority: "admin").save(flush: true, failOnError: true)
                    UserRole.create(admin, rAdmin)

                    // causes weirdness
                    def principal = springSecurityService.principal
                    log.info("Current user = ${principal}")

                }
            }
        }
    }
    def destroy = {
    }
}
