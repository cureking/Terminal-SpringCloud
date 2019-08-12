package com.renewable.terminal.inclination.service.impl;

import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.service.IInclinationOriginService;
import com.renewable.terminal.inclination.service.ISerialCommandReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description：
 * @Author: jarry
 */
@Service
@Slf4j
public class ISerialCommandReceiveServiceImpl implements ISerialCommandReceiveService {

	@Autowired
	private IInclinationOriginService iInclinationOriginService;

	@Override
	public void receiveDataWithOrigin(int address, InclinationOrigin inclinationOrigin) {
		// 由于目前只有倾斜数据，而且差异性较低，所以直接调用倾斜数据处理服务即可
		iInclinationOriginService.receiveAndDealOriginData(address, inclinationOrigin);
	}
}
