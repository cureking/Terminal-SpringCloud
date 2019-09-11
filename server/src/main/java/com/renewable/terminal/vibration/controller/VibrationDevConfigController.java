package com.renewable.terminal.vibration.controller;

import com.renewable.terminal.terminal.common.ServerResponse;
import com.renewable.terminal.vibration.entity.VibrationDevConfig;
import com.renewable.terminal.vibration.service.IVibrationDevConfigService;
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
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/vibration_dev_config/")
public class VibrationDevConfigController {

	@Autowired
	private IVibrationDevConfigService iVibrationDevConfigService;

	// 增删改查

	/**
	 * 根据ID，查询配置
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("get.do")
	@ResponseBody
	public ServerResponse getDevConfigById(@RequestParam Integer id) {

		// 1.数据校验
		if (id == null) {
			return ServerResponse.createByErrorMessage("id is null !");
		}

		// 2.查询数据
		VibrationDevConfig vibrationDevConfig = iVibrationDevConfigService.getById(id);
		if (vibrationDevConfig == null) {
			return ServerResponse.createByErrorMessage("vibrationDevConfig is null with the id:" + id);
		}

		// 3.返回成功响应
		return ServerResponse.createBySuccess(vibrationDevConfig);
	}

	/**
	 * 获取所有配置的列表
	 *
	 * @return
	 */
	@GetMapping("list.do")
	@ResponseBody
	public ServerResponse listDevConfig() {

		// 2.查询数据
		List<VibrationDevConfig> vibrationDevConfigList = iVibrationDevConfigService.list();
		if (CollectionUtils.isEmpty(vibrationDevConfigList)) {
			return ServerResponse.createByErrorMessage("vibrationDevConfigList is null");
		}

		// 3.返回成功响应
		return ServerResponse.createBySuccess(vibrationDevConfigList);
	}

	/**
	 * 保存配置
	 *
	 * @param vibrationDevConfig
	 * @return
	 */
	@PostMapping("save.do")
	@ResponseBody
	public ServerResponse saveDevConfig(@RequestBody VibrationDevConfig vibrationDevConfig) {

		// 1.数据校验
		if (vibrationDevConfig == null) {
			return ServerResponse.createByErrorMessage("vibrationDevConfig is null ! vibrationDevConfig:" + vibrationDevConfig);
		}

		// 2.数据Assemble
		vibrationDevConfig.setId(null);
//		vibrationDevConfig.setCreateTime(null);
//		vibrationDevConfig.setUpdateTime(null);

		// 3.更新数据
		boolean result = iVibrationDevConfigService.save(vibrationDevConfig);
		if (!result) {
			return ServerResponse.createByErrorMessage("vibrationDevConfigList save fail !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(vibrationDevConfig);
	}

	/**
	 * 更新配置（需要上传中控室）
	 *
	 * @param vibrationDevConfig
	 * @return
	 */
	@PostMapping("update.do")
	@ResponseBody
	public ServerResponse updateDevConfig(@RequestBody VibrationDevConfig vibrationDevConfig) {

		// 1.数据校验
		if (vibrationDevConfig == null || vibrationDevConfig.getId() == null) {
			return ServerResponse.createByErrorMessage("vibrationDevConfig is null or its id is null ! vibrationDevConfig:" + vibrationDevConfig);
		}

		// 2.更新数据
		boolean result = iVibrationDevConfigService.updateById(vibrationDevConfig);
		if (!result) {
			return ServerResponse.createByErrorMessage("vibrationDevConfigList save fail !");
		}

		// 2+. 数据发送中控室


		// 3.返回成功响应
		return ServerResponse.createBySuccess(vibrationDevConfig);
	}

	/**
	 * 更新配置（从中控室获得，不需要再上传中控室）
	 *
	 * @param vibrationDevConfig
	 * @return
	 */
	@PostMapping("update_from_center.do")
	@ResponseBody
	public ServerResponse updateDevConfigFromCenter(@RequestBody VibrationDevConfig vibrationDevConfig) {

		// 1.数据校验
		if (vibrationDevConfig == null || vibrationDevConfig.getId() == null) {
			return ServerResponse.createByErrorMessage("vibrationDevConfig is null or its id is null ! vibrationDevConfig:" + vibrationDevConfig);
		}

		// 2.更新数据
		boolean result = iVibrationDevConfigService.updateById(vibrationDevConfig);
		if (!result) {
			return ServerResponse.createByErrorMessage("vibrationDevConfigList save fail !");
		}

		// 3.返回成功响应
		return ServerResponse.createBySuccess(vibrationDevConfig);
	}

	/**
	 * 删除配置
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("delete.do")
	@ResponseBody
	public ServerResponse deleteDevConfig(@RequestParam Integer id) {

		// 1.数据校验
		if (id == null) {
			return ServerResponse.createByErrorMessage("id is null !");
		}

		VibrationDevConfig vibrationDevConfig = iVibrationDevConfigService.getById(id);

		// 2.删除数据
		boolean result = iVibrationDevConfigService.removeById(id);
		if (!result) {
			return ServerResponse.createByErrorMessage("vibrationDevConfigList delete fail !");
		}

		// 3.返回成功响应
		return ServerResponse.createBySuccess(vibrationDevConfig);
	}
}
