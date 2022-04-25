# spring-demo-app
This is test example of spring application.
Application has authentication using jwt token.
To work with pplication(app):
1. It is need to push post  request with json {"user":"user", "password":"password"} to to http://localhost:8091/authenticate and get response with token value. 
2. Add to Authorization header "Bearer_{token_value}" and push post  request with json {"user":"user", "message":"{your message}"} to http://localhost:8091/message and get response with saved message.
3. Add to Authorization header "Bearer_{token_value}" and push post  request with json {"user":"user", "message":"history 10"} to http://localhost:8091/message and get response with 10 last saved messages.
