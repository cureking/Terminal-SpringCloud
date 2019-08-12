package com.renewable.terminal.inclination.controller;


import com.renewable.terminal.inclination.service.IInclinationInitService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@RestController
@RequestMapping("/inclination_init/")
public class InclinationInitController {

	@Autowired
	private IInclinationInitService iInclinationInitService;

	/**
	 * 在指定时间区域内，请求指定数量的数据（自动计算步长）
	 *
	 * @param count
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "list_time_with_count.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listByTimeWithCount(@RequestParam(value = "count", defaultValue = "1024") Integer count,
											  @RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
											  @RequestParam(value = "endTime", defaultValue = "2100-03-14 11:33:54") String endTime) {
		return iInclinationInitService.listByTimeWithCount(count, startTime, endTime);
	}


	/**
	 * 内部测试&供给client（之后有时间，做一个分离，避免外部访问。不过到时候加上网关，这个服务应该外部都不可以访问了）
	 *
	 * @return
	 */
	@RequestMapping(value = "clean_data_persistence.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse cleanDataPersistence() {
		return iInclinationInitService.cleanDataPersistence();
	}
}
