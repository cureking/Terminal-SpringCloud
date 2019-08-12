package com.renewable.terminal.vibration.controller;


import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.service.IVibrationAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jarry
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/vibration_area/")
public class VibrationAreaController {

	@Autowired
	private IVibrationAreaService iVibrationAreaService;

	/**
	 * 在指定时间区域内，请求指定数量的数据（自动计算步长）
	 * @param devId
	 * @param passagewayCode
	 * @param count
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@GetMapping(value = "list_time_with_count.do")
	@ResponseBody
	public ServerResponse listByTimeWithCount(@RequestParam(value = "devId", defaultValue = "0") Integer devId,
											  @RequestParam(value = "passagewayCode", defaultValue = "0") Integer passagewayCode,
											  @RequestParam(value = "count", defaultValue = "1024") Integer count,
											  @RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
											  @RequestParam(value = "endTime", defaultValue = "2100-03-14 11:33:54") String endTime) {
		return iVibrationAreaService.listByTimeWithCount(devId, passagewayCode, count, startTime, endTime);
	}

}
