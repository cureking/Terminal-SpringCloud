package com.renewable.terminal.inclination.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renewable.terminal.inclination.dao.InclinationConfigMapper;
import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@Service
public class InclinationConfigServiceImpl extends ServiceImpl<InclinationConfigMapper, InclinationConfig> implements IInclinationConfigService {

}
