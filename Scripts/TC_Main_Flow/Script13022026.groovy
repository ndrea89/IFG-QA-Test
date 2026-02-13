import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

/* SCENARIO 1: RESTful API (Producer & Consumer)
   Using JSONPlaceholder as the mock server.
*/

// 1. CONSUMER: GET Request (Fetch User Data)
// Send request to get user with ID 1
def responseGet = WS.sendRequest(findTestObject('Object Repository/API/GET_User', [('id') : '1']))
// Verify status is 200 OK
WS.verifyResponseStatusCode(responseGet, 200)
println("GET Request Success: " + responseGet.getResponseText())

// 2. PRODUCER: POST Request (Create New Data)
// Send request to create a new post
def responsePost = WS.sendRequest(findTestObject('Object Repository/API/POST_User', [('name') : 'Katalon Candidate', ('job') : 'QA Automation']))
// Verify status is 201 Created
WS.verifyResponseStatusCode(responsePost, 201)
println("POST Request Success: " + responsePost.getResponseText())


/* SCENARIO 2: KAFKA CONSUMER
   Using Custom Keyword to subscribe and poll messages.
*/

String topicName = "transaction-topic"
println("Starting Kafka Consumer Test for topic: " + topicName)

// Call the Custom Keyword defined in Keywords/com/backend/KafkaHelper.groovy
CustomKeywords.'com.backend.KafkaHelper.consumeKafkaMessage'(topicName)
