package com.renewable.terminal.inclination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renewable.terminal.inclination.entity.InclinationOrigin;
import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.terminal.common.ServerResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
public interface IInclinationWarningService extends IService<InclinationWarning> {

	ServerResponse saveAndUpload(InclinationWarning inclinationWarning);

	ServerResponse listByTimeWithCount(Integer countRequest, String startTime, String endTime);
}
