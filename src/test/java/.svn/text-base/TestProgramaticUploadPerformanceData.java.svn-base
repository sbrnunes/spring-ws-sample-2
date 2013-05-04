import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import ut.eurodw.eu.edservices.UploadPerformanceDataResponse;

import com.bnpparibas.goe.service.edservice.UploadPerformanceDataService;
import com.bnpparibas.goe.service.edservice.proxy.ProxyHostConfiguration;
import com.bnpparibas.goe.service.edservice.proxy.ProxyHttpState;

/**
 * jUnit test that allows to test UploadPerformanceData web service,
 * using programatically added configurations.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath*:web-services-application-context.xml",	
	"classpath*:edservices/web-services-edw-config.xml"	
})
public class TestProgramaticUploadPerformanceData {
	
	@Autowired
	private BeanFactory beanFactory;
	
	@Autowired
	private SimpleDateFormat dateFormater;
	
	Date cutoffDate;
	Date uploadDate;
	String signedData;
	byte[] fileStream;

	private String validProxyUser;
	private String validProxypass;
	private String validUsername;
	private String validPassword;
	
	@Before
	public void init() throws Exception {
		validProxyUser = "<proxy user>";
		validProxypass = "<proxy pass>";
		validUsername = "julien.dupre@bnpparibas.com";
		validPassword = "GOE4Ever";
		
		File zippedFile = new File("files/RMBSFRAA6718100120117_20111031_20121030.xml.gz");
		fileStream = FileUtils.readFileToByteArray(zippedFile);
		
		File signedDataFile = new File("files/RMBSFRAA6718100120117_20111031_20121030.xml.gz.asc");
		signedData = FileUtils.readFileToString(signedDataFile, "UTF-8");
		
		cutoffDate = dateFormater.parse("2011-10-31");
		uploadDate = new Date();
	}
	
	@Test
	public void testProgramaticProxyConfiguration() throws Exception {
		/*
		 * Get new bean instances from application context
		 */
		ProxyHostConfiguration hostConfiguration = (ProxyHostConfiguration)beanFactory.getBean("proxyHostConfiguration");
		ProxyHttpState proxyHttpState = (ProxyHttpState)beanFactory.getBean("proxyHttpState");
		HttpClient httpClient = (HttpClient)beanFactory.getBean("edwHttpClient");
		CommonsHttpMessageSender httpMsgSender = (CommonsHttpMessageSender)beanFactory.getBean("edwMessageSender");
		WebServiceTemplate webServiceTemplate = (WebServiceTemplate)beanFactory.getBean("edwServicesTemplate");
		UploadPerformanceDataService service = (UploadPerformanceDataService)beanFactory.getBean("uploadPerformanceDataService");
		
		/*
		 * Set up proxy configuration.
		 * By default, beans will load configuration from properties files.
		 */
		hostConfiguration.setProxy("localhost", 8888);
		
		//Enter a valid username and password
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(validProxyUser, validProxypass);
		
		proxyHttpState.setProxyCredentials(credentials);
		
		httpClient.setHostConfiguration(hostConfiguration);
		httpClient.setState(proxyHttpState);
		
		//Set updated HttpClient into CommonsHttpMessageSender
		
		httpMsgSender.setHttpClient(httpClient);
		
		//Set updated CommonsHttpMessageSender into WebServiceTemplate 
		webServiceTemplate.setMessageSender(httpMsgSender);
		
		service.setWebServiceTemplate(webServiceTemplate);
		
		try {
			Object result = service.execute("RMBSFRAA6718100120117", cutoffDate, signedData, uploadDate, fileStream);
			
			Assert.assertTrue(result instanceof UploadPerformanceDataResponse);
			
		} catch (Exception sfe) {
			//Missing: how to handle fault exception
			throw sfe;
		}
	}
	
	@Test(expected=java.lang.Exception.class)
	public void testProgramaticProxyConfigurationFailure() throws Exception {
		/*
		 * Get new bean instances from application context.
		 * 
		 * ATTENTION: in your application, use dependency injection.
		 */
		ProxyHostConfiguration hostConfiguration = (ProxyHostConfiguration)beanFactory.getBean("proxyHostConfiguration");
		ProxyHttpState proxyHttpState = (ProxyHttpState)beanFactory.getBean("proxyHttpState");
		HttpClient httpClient = (HttpClient)beanFactory.getBean("edwHttpClient");
		CommonsHttpMessageSender httpMsgSender = (CommonsHttpMessageSender)beanFactory.getBean("edwMessageSender");
		WebServiceTemplate webServiceTemplate = (WebServiceTemplate)beanFactory.getBean("edwServicesTemplate");
		UploadPerformanceDataService service = (UploadPerformanceDataService)beanFactory.getBean("uploadPerformanceDataService");
		
		/*
		 * Set up proxy configuration.
		 * By default, beans will load configuration from properties files.
		 */
		hostConfiguration.setProxy("localhost", 8888);
		
		//Enter a valid username and password
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("wrongUser", "wrongPassword");
		
		proxyHttpState.setProxyCredentials(credentials);
		
		httpClient.setHostConfiguration(hostConfiguration);
		httpClient.setState(proxyHttpState);
		
		//Set updated HttpClient into CommonsHttpMessageSender
		httpMsgSender.setHttpClient(httpClient);
		
		//Set updated CommonsHttpMessageSender into WebServiceTemplate 
		webServiceTemplate.setMessageSender(httpMsgSender);
		
		//Set web service template in service
		service.setWebServiceTemplate(webServiceTemplate);
		
		try {
			service.execute("RMBSFRAA6718100120117", cutoffDate, signedData, uploadDate, fileStream);
			
		} catch (Exception sfe) {
			//Missing: how to handle fault exception
			throw sfe;
		}
	}
	
	
	@Test
	public void testUsernameToken() throws Exception {
		/*
		 * Get new bean instances from application context.
		 * 
		 * ATTENTION: in your application, use dependency injection.
		 */
		Wss4jSecurityInterceptor interceptor = (Wss4jSecurityInterceptor)beanFactory.getBean("edwUsernameTokenInterceptor");
		WebServiceTemplate webServiceTemplate = (WebServiceTemplate)beanFactory.getBean("edwServicesTemplate");
		UploadPerformanceDataService service = (UploadPerformanceDataService)beanFactory.getBean("uploadPerformanceDataService");
		
		/*
		 * Set-up Wss4jSecurityInterceptor
		 */
		interceptor.setSecurementUsername(validUsername);
		interceptor.setSecurementPassword(validPassword);
		
		//Set updated interceptor in web service template
		webServiceTemplate.setInterceptors(new ClientInterceptor[]{interceptor});
		
		//Set web service template in service
		service.setWebServiceTemplate(webServiceTemplate);
		
		try {
			Object result = service.execute("RMBSFRAA6718100120117", cutoffDate, signedData, uploadDate, fileStream);
			
			Assert.assertTrue(result instanceof UploadPerformanceDataResponse);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test(expected=SoapFaultClientException.class)
	public void testUsernameTokenFailure() throws Exception {
		/*
		 * Get new bean instances from application context.
		 * 
		 * ATTENTION: in your application, use dependency injection.
		 */
		Wss4jSecurityInterceptor interceptor = (Wss4jSecurityInterceptor)beanFactory.getBean("edwUsernameTokenInterceptor");
		WebServiceTemplate webServiceTemplate = (WebServiceTemplate)beanFactory.getBean("edwServicesTemplate");
		UploadPerformanceDataService service = (UploadPerformanceDataService)beanFactory.getBean("uploadPerformanceDataService");
		
		/*
		 * Set-up Wss4jSecurityInterceptor
		 */
		interceptor.setSecurementUsername("wrongUser");
		interceptor.setSecurementPassword("wrongPassword");
		
		//Set updated interceptor in web service template
		webServiceTemplate.setInterceptors(new ClientInterceptor[]{interceptor});
		
		//Set web service template in service
		service.setWebServiceTemplate(webServiceTemplate);
		
		service.execute("RMBSFRAA6718100120117", cutoffDate, signedData, uploadDate, fileStream);
	}
	
	@Test
	public void testServiceWithPropertiesConfiguration() throws Exception {
		/*
		 * Get new bean instances from application context.
		 * 
		 * ATTENTION: in your application, use dependency injection.
		 */
		UploadPerformanceDataService service = (UploadPerformanceDataService)beanFactory.getBean("uploadPerformanceDataService");
		
		try {
			Object result = service.execute("RMBSFRAA6718100120117", cutoffDate, signedData, uploadDate, fileStream);
			
			if(result instanceof UploadPerformanceDataResponse) {
				Assert.assertNotNull(((UploadPerformanceDataResponse)result).getRequestId());			
			}
			
		} catch (SoapFaultClientException sfe) {
			//Missing: how to handle fault exception
			throw sfe;
		}
		
	}
}
