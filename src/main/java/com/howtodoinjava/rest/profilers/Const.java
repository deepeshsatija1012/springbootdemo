package com.howtodoinjava.rest.profilers;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Created by rakesh on 9/14/16.
 */
public class Const {
	public static final String AUTH_BASE_URL = System.getenv("LT_UTILS_AUTH_SERVICE");

	public static final String AUTH_URL = "/api/v1/people-service/users/auth";
	public static final String authorizationHeaderName = "Authorization";
	public static final String ACCESS_SECRET = "serviceOwnersSecret";
	public static final String xRequestId = "x-request-id";
	public static final String authHeaderValuePrefix = "Bearer ";
	public static final String idTokenType = "id";
	public static final String accessTokenType = "auth";
	public static final String CONTENT_TYPE = "content-type";
	public static final String SPRING_API_HEADER = "spring_api_name";
	public static final String APPLICAION_NAME = "K8S_SERVICE_NAME";
	public static final String REQUEST_START_TIME = "requestStartTime";
	
	//HTTP Header to be Logged
	public static final String HTTP_HEADER_LOG_STATUS = "status";
	public static final String HTTP_HEADER_LOG_UPSTREAM_STATUS = "upstream_status";
	public static final String HTTP_HEADER_LOG_SPRING_API = "spring_api";
	public static final String HTTP_HEADER_LOG_HTTP_USER_AGENT = "http_user_agent";
	public static final String HTTP_HEADER_LOG_METHOD = "method";
	public static final String HTTP_HEADER_LOG_PATH = "path";
	public static final String HTTP_HEADER_LOG_REQUEST_PROTO = "request_proto";
	public static final String HTTP_HEADER_LOG_REQUEST_TIME = "request_time";
	public static final String HTTP_HEADER_LOG_REQUEST_ID = "request_id";
	public static final String HTTP_HEADER_LOG_X_FORWARD_FOR = "x-forward-for";
	public static final String HTTP_HEADER_LOG_REMOTE_ADDR = "remote_addr";
	public static final String HTTP_HEADER_LOG_TIME = "time";
	

	// Error message section
	public static final String RESOURCE_NOT_FOUND = "resource for %s with id %s does not exists";
	public static final String CONSTRAINT_VIOLATION = "unable to create resource. constraint voilation exception for %s.";
	public static final String HIBERNATE_EXCEPTION = "Hibernate Exception occurred with cause %s";

	//Pool size of executors
	public static final int EMAIL_EXECUTOR_POOL_SIZE = (System.getenv("EMAIL_EXECUTOR_POOL_SIZE") == null) ? 10
			: Integer.valueOf(System.getenv("EMAIL_EXECUTOR_POOL_SIZE"));

	//LT Util Executor
	public static final int LT_EXECUTOR_POOL_SIZE = (System.getenv("LT_EXECUTOR_POOL_SIZE") == null) ? 10
			: Integer.valueOf(System.getenv("LT_EXECUTOR_POOL_SIZE"));
	
	public static final int SMS_EXECUTOR_POOL_SIZE = (System.getenv("SMS_EXECUTOR_POOL_SIZE") == null) ? 10
			: Integer.valueOf(System.getenv("SMS_EXECUTOR_POOL_SIZE"));
	
	public static final int TL_MESSAGE_ERROR_EXECUTOR_POOL_SIZE = (System.getenv("TL_MESSAGE_ERROR_EXECUTOR_POOL_SIZE") == null) ? 2
			: Integer.valueOf(System.getenv("TL_MESSAGE_ERROR_EXECUTOR_POOL_SIZE"));
	
	//Conveyor URL
	public static final String CONVEYOR_URL = System.getenv("CONVEYOR_URL");
	public static final String CONVEYOR_SMS_URL = CONVEYOR_URL + "/convey/sms";
	public static final String CONVEYOR_SMS_URL_V2 = CONVEYOR_URL + "/api/v2/sms/convey";
	public static final String CONVEYOR_EMAIL_URL = CONVEYOR_URL + "/api/v2/email/convey";
	public static final String CONVEYOR_SMS_TOPIC_NAME = "lt.conveyor-service.v2.convey.sms";
	public static final String CONVEYOR_EMAIL_TOPIC_NAME = "lt.conveyor-service.v2.convey.email";

	//Bugsnag 
	public static final String ENVIRONMENT_IDENTIFIER_KEY = "JAVA_SERVICE_ENV";
	public static final String BUGSNAG_KEY = System.getenv("BUGSNAG_KEY");
	public static final String SERVICE_ENV = System.getenv(ENVIRONMENT_IDENTIFIER_KEY);
	public static final Set<String> ENABLED_ENVIRONMENTS_FOR_SWAGGER = Sets.newHashSet("test", "staging");
	
	//Interceptor
	public static final long EXECUTION_TIME_THRESHOLD = 1000;

	//Coordinator URL
    public static final String COORDINATOR_URL = System.getenv("COORDINATOR_URL");
    public static final String COORDINATOR_EXPORT_PDF_URL = COORDINATOR_URL + "/api/v1/exportPDF";

    
    public static final String PROFIL_ENABLED_SYS_PROPERTY = "profiling.enabled";
    public static final boolean IS_PROFILING_ENABLED = (System.getenv(PROFIL_ENABLED_SYS_PROPERTY)==null) ? false
    		: Boolean.parseBoolean(System.getenv(PROFIL_ENABLED_SYS_PROPERTY));
}
