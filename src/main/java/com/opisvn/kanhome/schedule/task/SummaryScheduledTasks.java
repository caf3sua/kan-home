package com.opisvn.kanhome.schedule.task;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.opisvn.kanhome.domain.Device;
import com.opisvn.kanhome.domain.DeviceStat;
import com.opisvn.kanhome.domain.SummaryData;
import com.opisvn.kanhome.repository.DeviceRepository;
import com.opisvn.kanhome.repository.DeviceStatRepository;
import com.opisvn.kanhome.repository.SummaryDataRepository;
import com.opisvn.kanhome.repository.UserRepository;
import com.opisvn.kanhome.web.rest.CityResource;


@Component
public class SummaryScheduledTasks {
	private final Logger log = LoggerFactory.getLogger(CityResource.class);
	
	private UserRepository userRepository;
	private DeviceRepository deviceRepository;
	private SummaryDataRepository summaryDataRepository;
	private DeviceStatRepository deviceStatRepository;
	
    public SummaryScheduledTasks(UserRepository userRepository, DeviceRepository deviceRepository,
			SummaryDataRepository summaryDataRepository, DeviceStatRepository deviceStatRepository) {
		super();
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
		this.summaryDataRepository = summaryDataRepository;
		this.deviceStatRepository = deviceStatRepository;
	}

	@Scheduled(fixedRate = 5*60*1000)
    public void summaryData() throws MqttException {
    	log.debug("Schedule summaryData");
    	
    	SummaryData summary = null;
    	
    	summary = summaryDataRepository.findLatestOne();
    	if (summary == null) {
    		summary = new SummaryData();
        	summary.setId(System.currentTimeMillis());
    	}
    	
    	// Count user status
    	long userNumber = userRepository.countByRole("ROLE_USER");
    	long adminNumber = userRepository.countByRole("ROLE_ADMIN");
    	long techNumber = userRepository.countByRole("ROLE_TECHNICAL");
    	long supportNumber = userRepository.countByRole("ROLE_SUPPORTOR");
    	 
    	// User
    	summary.setNumberAdminUser(adminNumber);
    	summary.setNumberUser(userNumber);
    	summary.setNumberTechnicalUser(techNumber);
    	summary.setNumberSupportorUser(supportNumber);
    	
    	// Device
    	List<DeviceStat> lstStat = deviceStatRepository.findAll();
    	List<Device> lstDevice = deviceRepository.findAll();
    	long numDevice = lstDevice.size();
    	
    	long numDeviceBlue = 0;
    	long numDeviceRed = 0;
    	long numDeviceYellow = 0;
    	long numDeviceGray = 0;

    	for (Device device : lstDevice) {
			for (DeviceStat stat : lstStat) {
				if (StringUtils.equals(stat.getId(), device.getId())) {
					if (StringUtils.equalsIgnoreCase(stat.getwQ(), "BLUE")) {
						numDeviceBlue++;
					} else if (StringUtils.equalsIgnoreCase(stat.getwQ(), "RED")) {
						numDeviceRed++;
					} else if (StringUtils.equalsIgnoreCase(stat.getwQ(), "YELLOW")) {
						numDeviceYellow++;
					}
					continue;
				}
			}
		}
    	
    	numDeviceGray = numDevice - (numDeviceBlue + numDeviceRed + numDeviceYellow);
    	
    	summary.setNumberBlueDevice(numDeviceBlue);
    	summary.setNumberGrayDevice(numDeviceGray);
    	summary.setNumberRedDevice(numDeviceRed);
    	summary.setNumberYellowDevice(numDeviceYellow);
    	
    	summary.setNumberDevice(numDevice);
    	
    	// Save
    	SummaryData result = summaryDataRepository.save(summary);
    	log.debug("End schedule summaryData, {}", result);
    }

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public DeviceRepository getDeviceRepository() {
		return deviceRepository;
	}

	public void setDeviceRepository(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public SummaryDataRepository getSummaryDataRepository() {
		return summaryDataRepository;
	}

	public void setSummaryDataRepository(SummaryDataRepository summaryDataRepository) {
		this.summaryDataRepository = summaryDataRepository;
	}
}
