This program has an interesting security flaw.

In Bootstrap.groovy it tries to access the springSecurityService.principal
object.  This sets up some unknown chain of events that makes it so that when a user 
finally logs in, their principal object is "pinned" in some global state.

That means that subsequent requests, without any credentials (bearer token or JSESSIONID)
will be treated as coming from the previous user that logged in.

To see this in action, start the server, then run the following commands.

The first curl request will issue a login to the admin user.
Notice we don't do anything with the returned bearer token.

The 2nd request tries to access the "secured" endpoint that should return a 401,
but instead it returns the principal object of the ADMIN user.

This happens for multiple requests, before finally returning the expected 401.

------------------------------------------------

export PORT=8085

curl "http://localhost:$PORT/api/login" \
  -H 'Content-Type: application/json' \
  --data '{"username":"admin","password":"pass"}' \
  --compressed


curl "http://localhost:$PORT/app/whoami" \
  --compressed

------------------------------------------------


I stumbled across this behavior because my Bootstrap code was loading data through
a service that was trying to keep track of the user making the change by accessing the
springSecurityService.principal object.

I don't know where the object is getting pinned, and don't know if there are other ways
of getting a security object pinned.

