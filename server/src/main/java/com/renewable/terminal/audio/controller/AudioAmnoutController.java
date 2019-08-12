package com.renewable.terminal.audio.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.renewable.terminal.audio.entity.AudioAmnout;
import com.renewable.terminal.audio.service.IAudioAmnoutService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
@RestController
@RequestMapping("/audio_amnout/")
public class AudioAmnoutController {

	@Autowired
	private IAudioAmnoutService iAudioAmnoutService;

	@RequestMapping(value = "list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
							   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Page<AudioAmnout> audioAmnoutPage = new Page<>();
		audioAmnoutPage.setCurrent(pageNum);
		audioAmnoutPage.setSize(pageSize);

		IPage<AudioAmnout> audioAmnoutIPage = iAudioAmnoutService.page(audioAmnoutPage);
		if (audioAmnoutIPage == null || audioAmnoutIPage.getSize() == 0) {
			return ServerResponse.createByErrorMessage("audioAmnoutList is null or audioAmnoutList's size is zero !");
		}
		return ServerResponse.createBySuccess(audioAmnoutIPage);
	}

	@RequestMapping(value = "list_time.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse listByTime(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
									 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
									 @RequestParam(value = "startTime", defaultValue = "1970-1-1 0:0:0") String startTime,
									 @RequestParam(value = "endTime", defaultValue = "2100-03-14 11:33:54") String endTime) {
		Page<AudioAmnout> audioAmnoutPage = new Page<>();
		audioAmnoutPage.setCurrent(pageNum);
		audioAmnoutPage.setSize(pageSize);

		Wrapper<AudioAmnout> audioAmnoutWrapper = new QueryWrapper<AudioAmnout>().between("create_time",startTime,endTime);

		IPage<AudioAmnout> audioAmnoutIPage = iAudioAmnoutService.page(audioAmnoutPage,audioAmnoutWrapper );
		if (audioAmnoutIPage == null || audioAmnoutIPage.getSize() == 0) {
			return ServerResponse.createByErrorMessage("audioAmnoutList is null or audioAmnoutList's size is zero !");
		}
		return ServerResponse.createBySuccess(audioAmnoutIPage);
	}

	@RequestMapping(value = "get_by_id.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getById(@RequestParam(value = "id", defaultValue = "1") Integer id) {
		AudioAmnout audioAmnout = iAudioAmnoutService.getById(id);
		if (audioAmnout == null) {
			return ServerResponse.createByErrorMessage("audioAmnout is null with id:" + id);
		}
		return ServerResponse.createBySuccess(audioAmnout);
	}

	@RequestMapping(value = "count.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse count() {
		Integer count = iAudioAmnoutService.count();
		if (count == null) {
			return ServerResponse.createByErrorMessage("count is null !");
		}
		return ServerResponse.createBySuccess(count);
	}
}
