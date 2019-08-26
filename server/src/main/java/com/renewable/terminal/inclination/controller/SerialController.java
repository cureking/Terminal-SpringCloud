package com.renewable.terminal.inclination.controller;

import com.renewable.terminal.inclination.service.ISerialCommandSendService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/serial/")
public class SerialController {

	@Autowired
	private ISerialCommandSendService iSerialCommandSendService;

	// 想了想，抛弃该处代码，硬件调控部分，就只做硬件调控
	@GetMapping("set_zero.do")
	@ResponseBody
	public ServerResponse setZero(@RequestParam(value = "address") Integer address,
								  @RequestParam(value = "type") Integer type) {

		return iSerialCommandSendService.setZero(address, type);
	}

	@GetMapping("set_address.do")
	@ResponseBody
	public ServerResponse setAddress(@RequestParam(value = "address") Integer address,
									 @RequestParam(value = "targetAddress") Integer targetAddress) {
		return iSerialCommandSendService.setAddress(address, targetAddress);
	}

	// 理由同上，如果需要，这里可以增加一个直接发送指令到硬件的接口，而不走数据库
	@GetMapping("read_all.do")
	@ResponseBody
	public ServerResponse readAll() {
		return iSerialCommandSendService.readAll();
	}


	// 下列都是在系统进行正常数据采集前，乃至相关配置初始化前的一个操作：进行底层传感器硬件设置

	@GetMapping("list_real_port.do")
	@ResponseBody
	public ServerResponse listRealPort() {
		return iSerialCommandSendService.listRealPort();
	}

	@GetMapping("set_address_without_db.do")
	@ResponseBody
	public ServerResponse setAddressWithoutDb(@RequestParam(value = "address", defaultValue = "0") Integer address,
											  @RequestParam(value = "portName") String portName,
											  @RequestParam(value = "baudrate", defaultValue = "9600") Integer baudrate,
											  @RequestParam(value = "inclinationType", defaultValue = "826T") String inclinationType,
											  @RequestParam(value = "targetAddress", defaultValue = "0") Integer targetAddress) {
		if (portName == null) {
			return ServerResponse.createByErrorMessage("portName is null !");
		}
		return iSerialCommandSendService.setAddressWithoutDb(address, portName, baudrate, inclinationType, targetAddress);
	}


}
