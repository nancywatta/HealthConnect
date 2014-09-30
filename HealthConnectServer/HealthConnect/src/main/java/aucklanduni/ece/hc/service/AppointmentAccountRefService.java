package aucklanduni.ece.hc.service;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;

@Transactional
public interface AppointmentAccountRefService extends
		BaseService<AppointmentAccountRef> {

}
