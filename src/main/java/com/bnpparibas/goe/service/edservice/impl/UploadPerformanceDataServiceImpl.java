package com.bnpparibas.goe.service.edservice.impl;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.addressing.client.ActionCallback;

import ut.eurodw.eu.edservices.UploadPerformanceDataRequest;
import ut.eurodw.eu.edservices.UploadPerformanceDataResponse;

import com.bnpparibas.goe.service.edservice.UploadPerformanceDataService;

/**
 * 
 * 
 *
 */
@Component
public class UploadPerformanceDataServiceImpl implements UploadPerformanceDataService {

	private WebServiceTemplate webServiceTemplate;
	private String serviceEndpoint;
	private String wsAddressingAction;
	
	@Autowired
	private SimpleDateFormat dateFormater;

	@Autowired
	public UploadPerformanceDataServiceImpl(String serviceEndpoint, String wsAddressingAction) {
		this.serviceEndpoint = serviceEndpoint;
		this.wsAddressingAction = wsAddressingAction;
	}
	
	@Autowired
	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bnpparibas.goe.service.edservice.UploadPerformanceDataService#execute(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, byte[])
	 */
	public Object execute(String edCodeText, Date cutOffDateText, String signatureDataText, 
			Date uploadDateText, byte[] fileStream) throws Exception {
		
		UploadPerformanceDataMessageCallback callback = new UploadPerformanceDataMessageCallback(new URI(wsAddressingAction));
		callback.setEdCodeText(edCodeText);
		callback.setCutOffDateText(dateFormater.format(cutOffDateText));
		callback.setSignatureDataText(signatureDataText);
		callback.setUploadDateText(dateFormater.format(uploadDateText));
		
		UploadPerformanceDataRequest requestPayload = new UploadPerformanceDataRequest();
		requestPayload.setFilestream(fileStream);
		
		return (UploadPerformanceDataResponse)webServiceTemplate.marshalSendAndReceive(serviceEndpoint, requestPayload, 
				callback);
	}
	
	/**
	 * Utility class responsible for adding custom SOAP headers to the request.
	 */
	private class UploadPerformanceDataMessageCallback extends ActionCallback {
		private QName edCode;
		private String edCodeText;
		private QName cutOffDate;
		private String cutOffDateText;
		private QName signatureData;
		private String signatureDataText;
		private QName uploadDate;
		private String uploadDateText;
		
		public UploadPerformanceDataMessageCallback(URI action) {
			super(action);
			this.edCode = new QName("http://www.eurodw.eu/EDServices/1.0","EDCode");
			this.cutOffDate = new QName("http://www.eurodw.eu/EDServices/1.0","PoolCutOffDate");
			this.signatureData = new QName("http://www.eurodw.eu/EDServices/1.0","SignatureData");
			this.uploadDate = new QName("http://www.eurodw.eu/EDServices/1.0","UploadDate");
		}
		
		public QName getEdCode() {
			return edCode;
		}
		public String getEdCodeText() {
			return edCodeText;
		}
		public void setEdCodeText(String edCodeText) {
			this.edCodeText = edCodeText;
		}
		public QName getCutOffDate() {
			return cutOffDate;
		}
		public String getCutOffDateText() {
			return cutOffDateText;
		}
		public void setCutOffDateText(String cutOffDateText) {
			this.cutOffDateText = cutOffDateText;
		}
		public QName getSignatureData() {
			return signatureData;
		}
		public String getSignatureDataText() {
			return signatureDataText;
		}
		public void setSignatureDataText(String signatureDataText) {
			this.signatureDataText = signatureDataText;
		}
		public QName getUploadDate() {
			return uploadDate;
		}
		public String getUploadDateText() {
			return uploadDateText;
		}
		public void setUploadDateText(String uploadDateText) {
			this.uploadDateText = uploadDateText;
		}
		
		@Override
		public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
			SoapMessage soapMsg = (SoapMessage)message;
			
			SoapHeader header = soapMsg.getSoapHeader();
			
			SoapHeaderElement edCodeHeader = header.addHeaderElement(getEdCode());
			edCodeHeader.setText(getEdCodeText());
			
			SoapHeaderElement cutOffDate = header.addHeaderElement(getCutOffDate());
			cutOffDate.setText(getCutOffDateText());
		
			SoapHeaderElement signatureData = header.addHeaderElement(getSignatureData());
			signatureData.setText(getSignatureDataText());
			
			SoapHeaderElement uploadDate = header.addHeaderElement(getUploadDate());
			uploadDate.setText(getUploadDateText());
			
			//Call the superclass method to add WS-Addressing headers 
			super.doWithMessage(message);
		}
		
	}
}
