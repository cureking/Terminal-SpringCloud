package com.renewable.terminal.inclination.controller;


import com.renewable.terminal.inclination.entity.InclinationWarning;
import com.renewable.terminal.inclination.service.IInclinationWarningService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
@RestController
@RequestMapping("/inclination-warning")
public class InclinationWarningController {

	@Autowired
	private IInclinationWarningService iInclinationWarningService;

	@RequestMapping(value = "list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listByTimeWithCount() {
		List<InclinationWarning> inclinationWarningList = iInclinationWarningService.list();
		if (CollectionUtils.isEmpty(inclinationWarningList)){
			return ServerResponse.createBySuccessMessage("the inclinationWarningList is null !");
		}
		return ServerResponse.createBySuccess(inclinationWarningList);
	}

	@RequestMapping(value = "list_time_with_count.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listByTimeWithCount(@RequestParam(value = "count", defaultValue = "1024") Integer count,
											  @RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
											  @RequestParam(value = "endTime", defaultValue = "2100-03-14 11:33:54") String endTime) {
		return iInclinationWarningService.listByTimeWithCount(count, startTime, endTime);
	}
}
