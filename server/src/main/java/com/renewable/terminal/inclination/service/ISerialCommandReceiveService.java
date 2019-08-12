package com.renewable.terminal.inclination.service;

import com.renewable.terminal.inclination.entity.InclinationOrigin;

/**
 * @Description：
 * @Author: jarry
 */
public interface ISerialCommandReceiveService {

	/**
	 * 对只包含最原始数据的InclinationOrigin进行进一步的计算与处理
	 *
	 * @param address           现在通过address来唯一标识，所以只通过address就可以保存数据
	 * @param inclinationOrigin 从硬件采集的数据，经过了初步处理，获得了只包含angleX，angleY，temperate的InclinationOrigin
	 */
	void receiveDataWithOrigin(int address, InclinationOrigin inclinationOrigin);
}
