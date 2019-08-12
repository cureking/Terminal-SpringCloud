package com.renewable.terminal.inclination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.terminal.common.ServerResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
public interface IInclinationTotalService extends IService<InclinationTotal> {

	ServerResponse listByTimeWithCount(Integer countRequest, String startTime, String endTime);


	// 建立一个对象（直接放本地缓存也可以啊），不断与新插入的数据进行比对，从而确保数据是所需要的数据。然后通过任务调度，一分钟提交一次数据库，并清零
	ServerResponse originDataSubmit(InclinationOrigin inclinationOrigin);

	ServerResponse cleanDataPersistence();

	ServerResponse getLastByCount(Integer address, Integer count);
}
