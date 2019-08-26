package com.renewable.terminal.inclination.controller;


import com.renewable.terminal.inclination.entity.InclinationConfig;
import com.renewable.terminal.inclination.service.IInclinationConfigService;
import com.renewable.terminal.inclination.util.MatlabUtil;
import com.renewable.terminal.message.client.InclinationMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jarry
 * @since 2019-07-22
 */
@RestController
@RequestMapping("/inclination_config/")
public class InclinationConfigController {

	@Autowired
	private IInclinationConfigService iInclinationConfigService;

	// TODO 这里没有对incliantionMessageClient做处理，而是选择在控制层直接使用，日后进行处理
	@Autowired
	private InclinationMessageClient inclinationMessageClient;

	@PostMapping("save.do")
	@ResponseBody
	public ServerResponse saveConfig(@RequestBody InclinationConfig inclinationConfig) {
		// 数据校验
		if (inclinationConfig == null || inclinationConfig.getAddress() == null){
			return ServerResponse.createByErrorMessage("inclinationConfig is null or its address is null !");
		}
		// 计算inclinationConfig的X和Y
//		ServerResponse<InclinationConfig> response = iInclinationConfigService.calXAndY(inclinationConfig);
		ServerResponse<InclinationConfig> response = iInclinationConfigService.calInitWithMatlab(inclinationConfig);
		if (response.isFail()){
			return response;
		}
		InclinationConfig inclinationConfigCalXAndY = response.getData();
		if (iInclinationConfigService.getById(inclinationConfigCalXAndY.getAddress()) != null){
			return ServerResponse.createByErrorMessage("duplicate the primary key:inclinationConfig.address !");
		}

		boolean result = iInclinationConfigService.save(inclinationConfigCalXAndY);
		if (!result) {
			return ServerResponse.createByErrorMessage("fail !");
		}
		return ServerResponse.createBySuccessMessage("success");
	}

	@GetMapping("get_by_id.do")
	@ResponseBody
	public ServerResponse getConfig(@RequestParam("id") Integer id) {
		if (id == null) {
			return ServerResponse.createByErrorMessage("id is null !");
		}
		InclinationConfig inclinationConfig = iInclinationConfigService.getById(id);
		if (inclinationConfig == null) {
			return ServerResponse.createByErrorMessage("inclinationConfig is null with id:" + id);
		}
		return ServerResponse.createBySuccess(inclinationConfig);
	}

	@GetMapping("list.do")
	@ResponseBody
	public ServerResponse listConfig() {
		List<InclinationConfig> inclinationConfigList = iInclinationConfigService.list();
		if (CollectionUtils.isEmpty(inclinationConfigList)) {
			return ServerResponse.createByErrorMessage("get inclinationConfigList fail !");
		}
		return ServerResponse.createBySuccess(inclinationConfigList);
	}

	@PostMapping("update_by_id.do")
	@ResponseBody
	public ServerResponse updateConfig(@RequestBody InclinationConfig inclinationConfig) {
		// 计算inclinationConfig的X和Y
//		ServerResponse<InclinationConfig> response = iInclinationConfigService.calXAndY(inclinationConfig);
		ServerResponse<InclinationConfig> response = iInclinationConfigService.calInitWithMatlab(inclinationConfig);
		if (response.isFail()){
			return response;
		}
		InclinationConfig inclinationConfigCalXAndY = response.getData();
		boolean result = iInclinationConfigService.updateById(inclinationConfigCalXAndY);
		if (!result) {
			return ServerResponse.createByErrorMessage("fail !");
		}

		// 4.上传中控室
		inclinationMessageClient.uploadConfig(inclinationConfig);
		return ServerResponse.createBySuccessMessage("success");
	}

	@PostMapping("update_from_center.do")
	@ResponseBody
	public ServerResponse updateConfigFromCenter(@RequestBody InclinationConfig inclinationConfig) {
		// 即使是中控室下发的数据，也需要计算X与Y（防止中控室没有进行相关操作）
		// 计算inclinationConfig的X和Y
//		ServerResponse<InclinationConfig> response = iInclinationConfigService.calXAndY(inclinationConfig);
		ServerResponse<InclinationConfig> response = iInclinationConfigService.calInitWithMatlab(inclinationConfig);
		if (response.isFail()){
			return response;
		}
		InclinationConfig inclinationConfigCalXAndY = response.getData();
		boolean result = iInclinationConfigService.updateById(inclinationConfigCalXAndY);

		if (!result) {
			return ServerResponse.createByErrorMessage("fail !");
		}
		return ServerResponse.createBySuccessMessage("success");
	}

	@GetMapping("delete_by_id.do")
	@ResponseBody
	public ServerResponse deleteConfig(@RequestParam("id") Integer id) {
		if (id == null) {
			return ServerResponse.createByErrorMessage("id is null !");
		}
		boolean result = iInclinationConfigService.removeById(id);
		if (!result) {
			return ServerResponse.createByErrorMessage("fail !");
		}
		return ServerResponse.createBySuccessMessage("success");
	}
}
