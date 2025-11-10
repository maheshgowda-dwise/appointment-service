/**
 * 
 */
package com.lifetrenz.lths.appointment;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Ajith.K
 *
 */

@SpringBootApplication
@EnableFeignClients
public class AppointmentApplication {
	final static Logger log = LoggerFactory.getLogger(AppointmentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppointmentApplication.class, args);
		log.info("Hi Welcome to Appointment Service" + new Date());
	}
}
