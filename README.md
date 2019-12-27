# funds-transfer

#1-Test cases are sufficient enough to demonstrate the functionality
#2- To compile and run in local
  2.1 mvn clean install
  2.2 java -jar funds-transfer-with-deps-0.0.1-SNAPSHOT.jar
  2.3 execute the apis exposed
 #3-create the user
 
 curl -X POST \
  http://localhost:9998/user/create \
  -H 'Accept: application/json' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 37' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9998' \
  -H 'Postman-Token: 052653dc-1c14-451d-9a56-3fb3472d549c,c96f66bd-200d-4cc0-a2df-6c249b8c42c7' \
  -H 'User-Agent: PostmanRuntime/7.20.1' \
  -H 'cache-control: no-cache' \
  -d '
{
"id":1,
"name":"demo",
"balance":100
}'

#4- Get the user created

curl -X GET \
  http://localhost:9998/user/ \
  -H 'Accept: application/json' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:9998' \
  -H 'Postman-Token: d60b7c5d-240b-4ccd-926f-3e224340de27,b057bb3c-d46e-4a46-a841-7d4c02b9a602' \
  -H 'User-Agent: PostmanRuntime/7.20.1' \
  -H 'cache-control: no-cache'
  
  #5- Transfer the funnds(ensure user ids match with the user created)
  
  curl -X POST \
  http://localhost:9998/funds/transfer \
  -H 'Accept: application/json' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 46' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:9998' \
  -H 'Postman-Token: ea36d3a0-7a50-4842-a37b-3d4baf6f4aff,4b5bef26-f4cb-459a-b5d9-515b2efe194b' \
  -H 'User-Agent: PostmanRuntime/7.20.1' \
  -H 'cache-control: no-cache' \
  -d '{
"toUserId":1,
"fromUserId":2,
"amount":100
}'
 
