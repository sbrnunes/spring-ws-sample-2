package com.bnpparibas.goe.service.edservice;

import java.util.Date;

import org.springframework.ws.client.core.WebServiceTemplate;


public interface UploadPerformanceDataService {

	/**
	 * Sets the Spring web service template. 
	 * 
	 * @param webServiceTemplate
	 */
	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate);
	
	/**
	 * 
	 * @param edCodeText
	 * @param cutOffDateText
	 * @param signatureDataText
	 * @param uploadDateText
	 * @param fileStream
	 * @return
	 * @throws Exception
	 */
	public Object execute(String edCodeText, Date cutOffDate, String signatureDataText, 
			Date uploadDate, byte[] fileStream) throws Exception;
	
}
