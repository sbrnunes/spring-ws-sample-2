import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ut.eurodw.eu.edservices.UploadPerformanceDataResponse;

import com.bnpparibas.goe.service.edservice.UploadPerformanceDataService;

/**
 * jUnit test that allows to test UploadPerformanceData web service,
 * by using default configurations obtained from properties files.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath*:web-services-application-context.xml",	
	"classpath*:edservices/web-services-edw-config.xml"	
})
public class TestUploadPerformanceData {
	
	@Autowired
	private UploadPerformanceDataService service;
	
	@Autowired
	private SimpleDateFormat dateFormater;
	
	Date cutoffDate;
	Date uploadDate;
	String signedData;
	byte[] fileStream;
	
	@Before
	public void init() throws Exception {
		File zippedFile = new File("files/RMBSFRAA6718100120117_20111031_20121030.xml.gz");
		fileStream = FileUtils.readFileToByteArray(zippedFile);
		
		File signedDataFile = new File("files/RMBSFRAA6718100120117_20111031_20121030.xml.gz.asc");
		signedData = FileUtils.readFileToString(signedDataFile, "UTF-8");
		
		cutoffDate = dateFormater.parse("2011-10-31");
		uploadDate = new Date();
	}
	
	@Test
	public void testServiceWithPropertiesConfiguration() throws Exception {
		
		Object result = service.execute("RMBSFRAA6718100120117", cutoffDate, signedData, uploadDate, fileStream);
		
		Assert.assertTrue(result instanceof UploadPerformanceDataResponse);
	}
}
