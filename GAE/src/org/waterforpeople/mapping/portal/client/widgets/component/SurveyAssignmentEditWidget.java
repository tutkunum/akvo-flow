package org.waterforpeople.mapping.portal.client.widgets.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.waterforpeople.mapping.app.gwt.client.device.DeviceDto;
import org.waterforpeople.mapping.app.gwt.client.device.DeviceService;
import org.waterforpeople.mapping.app.gwt.client.device.DeviceServiceAsync;
import org.waterforpeople.mapping.app.gwt.client.survey.SurveyAssignmentDto;
import org.waterforpeople.mapping.app.gwt.client.survey.SurveyAssignmentService;
import org.waterforpeople.mapping.app.gwt.client.survey.SurveyAssignmentServiceAsync;
import org.waterforpeople.mapping.app.gwt.client.survey.SurveyDto;
import org.waterforpeople.mapping.portal.client.widgets.component.SurveySelectionWidget.Orientation;
import org.waterforpeople.mapping.portal.client.widgets.component.SurveySelectionWidget.TerminalType;

import com.gallatinsystems.framework.gwt.util.client.MessageDialog;
import com.gallatinsystems.framework.gwt.util.client.ViewUtil;
import com.gallatinsystems.framework.gwt.wizard.client.CompletionListener;
import com.gallatinsystems.framework.gwt.wizard.client.ContextAware;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * component for editing an assignment of surveys to devices
 * 
 * @author Christopher Fagiani
 * 
 */
public class SurveyAssignmentEditWidget extends Composite implements
		ContextAware, ClickHandler {
	private static final String LABEL_STYLE = "input-label-padded";
	private static final int DEFAULT_ITEM_COUNT = 5;
	private Panel contentPanel;
	private SurveySelectionWidget surveySelectWidget;
	private Map<String, Object> contextBundle;
	private SurveyAssignmentServiceAsync surveyAssignmentService;
	private DeviceServiceAsync deviceService;
	private ListBox devicePickerListbox;
	private ListBox selectedDevicesListbox;
	private ListBox selectedSurveyListbox;
	private TextBox eventName;
	private DateBox effectiveStartDate;
	private DateBox effectiveEndDate;
	private Button addSelectedButton;
	private Button removeSelectedButton;
	private Button clearButton;

	private List<DeviceDto> allDevices;

	public SurveyAssignmentEditWidget() {
		surveyAssignmentService = GWT.create(SurveyAssignmentService.class);
		deviceService = GWT.create(DeviceService.class);
		allDevices = new ArrayList<DeviceDto>();
		contentPanel = new VerticalPanel();
		contentPanel.add(constructSelectorPanel());
		contentPanel.add(constructDetailsPanel());
		getDevices();
		initWidget(contentPanel);
	}

	private Composite constructSelectorPanel() {
		Panel mainPanel = new VerticalPanel();
		Panel selectorPanel = new HorizontalPanel();
		CaptionPanel selectorPanelCap = new CaptionPanel("Selection Criteria");
		selectorPanelCap.add(mainPanel);
		surveySelectWidget = new SurveySelectionWidget(Orientation.VERTICAL,
				TerminalType.SURVEY);
		selectorPanel.add(surveySelectWidget);
		VerticalPanel devPanel = new VerticalPanel();
		devPanel.add(ViewUtil.initLabel("Devices", LABEL_STYLE));
		devicePickerListbox = new ListBox(true);
		devicePickerListbox.setVisibleItemCount(DEFAULT_ITEM_COUNT);
		devPanel.add(devicePickerListbox);
		selectorPanel.add(devPanel);
		mainPanel.add(selectorPanel);
		addSelectedButton = new Button("Add Selected");
		addSelectedButton.addClickHandler(this);
		mainPanel.add(addSelectedButton);
		return selectorPanelCap;
	}

	private Composite constructDetailsPanel() {
		CaptionPanel detailPanelCap = new CaptionPanel("Assignment Details");
		selectedDevicesListbox = new ListBox(true);
		selectedDevicesListbox.setVisibleItemCount(DEFAULT_ITEM_COUNT);
		selectedSurveyListbox = new ListBox(true);
		selectedSurveyListbox.setVisibleItemCount(DEFAULT_ITEM_COUNT);
		HorizontalPanel labelPanel = new HorizontalPanel();
		labelPanel.add(ViewUtil.initLabel("Trip Name: ", LABEL_STYLE));
		eventName = new TextBox();
		labelPanel.add(eventName);
		labelPanel.add(ViewUtil.initLabel("Start: ", LABEL_STYLE));
		effectiveStartDate = new DateBox();
		effectiveStartDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getShortDateFormat()));
		labelPanel.add(effectiveStartDate);
		labelPanel.add(ViewUtil.initLabel("End: ", LABEL_STYLE));
		effectiveEndDate = new DateBox();
		effectiveEndDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getShortDateFormat()));
		labelPanel.add(effectiveEndDate);
		VerticalPanel main = new VerticalPanel();
		main.add(labelPanel);
		HorizontalPanel selectedItemPanel = new HorizontalPanel();
		selectedItemPanel.add(ViewUtil.initLabel("Selected Surveys",
				LABEL_STYLE));
		selectedItemPanel.add(selectedSurveyListbox);
		selectedItemPanel.add(ViewUtil.initLabel("Selected Devices",
				LABEL_STYLE));
		selectedItemPanel.add(selectedDevicesListbox);
		main.add(selectedItemPanel);
		Panel buttonPanel = new HorizontalPanel();
		removeSelectedButton = new Button("Remove Selected");
		removeSelectedButton.addClickHandler(this);
		buttonPanel.add(removeSelectedButton);
		clearButton = new Button("Undo Changes");
		clearButton.addClickHandler(this);
		buttonPanel.add(clearButton);
		main.add(buttonPanel);
		detailPanelCap.add(main);
		return detailPanelCap;
	}

	private void getDevices() {
		deviceService
				.listDeviceByGroup(new AsyncCallback<HashMap<String, ArrayList<DeviceDto>>>() {
					public void onFailure(Throwable caught) {
						MessageDialog errDia = new MessageDialog("Error",
								"Could not get devices. Please try again");
						errDia.showCentered();
					}

					public void onSuccess(
							HashMap<String, ArrayList<DeviceDto>> result) {
						if (result != null) {
							for (Entry<String, ArrayList<DeviceDto>> entry : result
									.entrySet()) {
								if (entry.getValue() != null) {
									for (DeviceDto dto : entry.getValue()) {
										allDevices.add(dto);
									}
								}
							}
							populateDeviceControl();
						}
					}
				});
	}

	private void populateDeviceControl() {
		devicePickerListbox.clear();
		for (DeviceDto dto : allDevices) {
			devicePickerListbox.addItem(dto.getPhoneNumber()
					+ (dto.getDeviceIdentifier() != null ? " ("
							+ dto.getDeviceIdentifier() + ")" : ""), dto
					.getKeyId().toString());
		}
	}

	@Override
	public void flushContext() {
		if (contextBundle != null) {
			contextBundle.remove(BundleConstants.SURVEY_ASSIGNMENT);
		}
	}

	@Override
	public Map<String, Object> getContextBundle(boolean doPopulation) {
		if (contextBundle == null) {
			contextBundle = new HashMap<String, Object>();
		}
		return contextBundle;
	}

	@Override
	public void persistContext(final CompletionListener listener) {
		SurveyAssignmentDto dto = (SurveyAssignmentDto) contextBundle
				.get(BundleConstants.SURVEY_ASSIGNMENT);

		if (dto == null) {
			dto = new SurveyAssignmentDto();
		}
		dto.setName(eventName.getText());
		dto.setStartDate(effectiveStartDate.getValue());
		dto.setEndDate(effectiveEndDate.getValue());
		List<Long> ids = getSelectedIds(selectedSurveyListbox);
		ArrayList<SurveyDto> surveys = new ArrayList<SurveyDto>();
		for (Long id : ids) {
			SurveyDto s = new SurveyDto();
			s.setKeyId(id);
			surveys.add(s);
		}
		dto.setSurveys(surveys);

		ids = getSelectedIds(selectedDevicesListbox);
		ArrayList<DeviceDto> devices = new ArrayList<DeviceDto>();
		for (Long id : ids) {
			DeviceDto d = new DeviceDto();
			d.setKeyId(id);
			devices.add(d);
		}
		dto.setDevices(devices);

		surveyAssignmentService.saveSurveyAssignment(dto,
				new AsyncCallback<SurveyAssignmentDto>() {
					@Override
					public void onSuccess(SurveyAssignmentDto result) {
						contextBundle.put(BundleConstants.SURVEY_ASSIGNMENT,
								result);
						listener.operationComplete(true, contextBundle);
					}

					@Override
					public void onFailure(Throwable caught) {
						listener.operationComplete(false, contextBundle);
					}
				});
	}

	public List<Long> getSelectedIds(ListBox box) {
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < box.getItemCount(); i++) {
			if (box.isItemSelected(i)) {
				ids.add(new Long(box.getValue(i)));
			}
		}
		return ids;
	}

	@Override
	public void setContextBundle(Map<String, Object> bundle) {
		if (bundle == null) {
			contextBundle = new HashMap<String, Object>();
		} else {
			contextBundle = bundle;
		}
		populateControl();
	}

	private void populateControl() {
		SurveyAssignmentDto dto = (SurveyAssignmentDto) contextBundle
				.get(BundleConstants.SURVEY_ASSIGNMENT);
		if (dto != null) {
			eventName.setText(dto.getName());
			effectiveEndDate.setValue(dto.getEndDate());
			effectiveStartDate.setValue(dto.getStartDate());
			if (dto.getSurveys() != null) {
				for (SurveyDto s : dto.getSurveys()) {
					selectedSurveyListbox.addItem(s.getName(), s.getKeyId()
							.toString());
				}
			}
			if (dto.getDevices() != null) {
				for (DeviceDto d : dto.getDevices()) {
					selectedDevicesListbox.addItem(d.getPhoneNumber()
							+ (d.getDeviceIdentifier() != null ? " ("
									+ d.getDeviceIdentifier() + ")" : ""), d
							.getKeyId().toString());
				}
			}
		}
	}

	private void resetUI() {
		surveySelectWidget.reset();
		eventName.setText("");
		effectiveEndDate.setValue(null);
		effectiveStartDate.setValue(null);
		selectedDevicesListbox.clear();
		selectedSurveyListbox.clear();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == addSelectedButton) {
			handleSurveySelection();
			handleDeviceSelection();
		} else if (event.getSource() == removeSelectedButton) {
			handleRemove(selectedSurveyListbox);
			handleRemove(selectedDevicesListbox);
		} else if (event.getSource() == clearButton) {
			resetUI();
			populateControl();
		}
	}

	/**
	 * removes all selected items from a list box
	 * 
	 * @param box
	 */
	private void handleRemove(ListBox box) {
		List<Integer> victimIndicies = new ArrayList<Integer>();
		for (int i = 0; i < box.getItemCount(); i++) {
			if (box.isItemSelected(i)) {
				victimIndicies.add(i);
			}
		}
		if (victimIndicies.size() > 0) {
			Collections.sort(victimIndicies);
			for (int i = victimIndicies.size() - 1; i >= 0; i--) {
				box.removeItem(victimIndicies.get(i));
			}
		}
	}

	private void handleSurveySelection() {
		String group = surveySelectWidget.getSelectedSurveyGroupName();
		List<String> name = surveySelectWidget.getSelectedSurveyNames();
		List<Long> ids = surveySelectWidget.getSelectedSurveyIds();
		for (int i = 0; i < name.size(); i++) {
			boolean alreadyThere = false;
			for (int j = 0; j < selectedSurveyListbox.getItemCount(); j++) {
				if (selectedSurveyListbox.getValue(j).equals(
						ids.get(i).toString())) {
					alreadyThere = true;
					break;
				}
			}
			if (!alreadyThere) {
				selectedSurveyListbox.addItem(group + ": " + name.get(i), ids
						.get(i).toString());
			}
		}
	}

	private void handleDeviceSelection() {
		for (int i = 0; i < devicePickerListbox.getItemCount(); i++) {
			if (devicePickerListbox.isItemSelected(i)) {
				boolean alreadyThere = false;
				for (int j = 0; j < selectedDevicesListbox.getItemCount(); j++) {
					if (selectedDevicesListbox.getValue(j).equals(
							devicePickerListbox.getValue(i))) {
						alreadyThere = true;
						break;
					}
				}
				if (!alreadyThere) {
					selectedDevicesListbox.addItem(devicePickerListbox
							.getItemText(i), devicePickerListbox.getValue(i));
				}
			}
		}

	}
}
