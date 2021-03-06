package com.krishagni.participantcsv.core;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.util.CsvException;
import com.krishagni.participantcsv.datasource.DataSource;
import com.krishagni.participantcsv.datasource.Impl.CsvFileDataSource;

public class ParticipantCsvImporter {
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;
	
	private DataSource dataSource;
	
	private String filename = "/home/user/Music/participant.csv";
	
	public void importcsv() {
		dataSource = new CsvFileDataSource(filename);
		while (dataSource.hasNext()) {
		    Record record = dataSource.nextRecord();
		    cprSvc.createRegistration(new RequestEvent<CollectionProtocolRegistrationDetail>(getCPRDetail(record)));
		}
		    dataSource.close();
	}
	
	private CollectionProtocolRegistrationDetail getCPRDetail(Record record) {
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setParticipant(new ParticipantDetail());
		return populateCPRDetail(record, cprDetail);
	}

	private CollectionProtocolRegistrationDetail populateCPRDetail(Record record, CollectionProtocolRegistrationDetail cprDetail) {
		cprDetail.setCpId(Long.parseLong(record.getValue("cpId")));
		cprDetail.getParticipant().setFirstName(record.getValue("firstName"));
		cprDetail.getParticipant().setMiddleName(record.getValue("middleName"));
		cprDetail.getParticipant().setLastName(record.getValue("lastName"));
		cprDetail.setPpid(record.getValue("ppId"));
		cprDetail.setRegistrationDate(new Date(record.getValue("registrationDate")));
		return cprDetail;
	}
}
