package org.waterforpeople.mapping.app.gwt.client.surveyinstance;

import java.util.Date;

import com.gallatinsystems.framework.gwt.dto.client.BaseDto;

public class QuestionAnswerStoreDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7382482717003334827L;

	private Long arbitratyNumber;
	private String questionID;
	private String type;
	private String value;
	private Date collectionDate;
	private Long surveyId;
	private Long surveyInstanceId;
	private String oldValue;
	private String questionText;

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Long getSurveyInstanceId() {
		return surveyInstanceId;
	}

	public void setSurveyInstanceId(Long surveyInstanceId) {
		this.surveyInstanceId = surveyInstanceId;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Long getArbitratyNumber() {
		return arbitratyNumber;
	}

	public void setArbitratyNumber(Long arbitratyNumber) {
		this.arbitratyNumber = arbitratyNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getQuestionID() {
		return questionID;
	}

	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionText() {
		return questionText;
	}

}
