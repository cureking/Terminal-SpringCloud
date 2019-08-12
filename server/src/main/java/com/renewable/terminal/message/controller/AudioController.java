package com.renewable.terminal.message.controller;

import com.renewable.terminal.audio.entity.AudioAmnout;
import com.renewable.terminal.audio.entity.AudioDba;
import com.renewable.terminal.inclination.entity.InclinationInit;
import com.renewable.terminal.inclination.entity.InclinationTotal;
import com.renewable.terminal.message.service.IAudioProducerService;
import com.renewable.terminal.message.service.IInclinationProducerService;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
@RestController
@RequestMapping("/message/audio/")
public class AudioController {

	@Autowired
	private IAudioProducerService iAudioProducerService;

	@PostMapping("upload_amnout_list.do")
	@ResponseBody
	public ServerResponse uploadAmnoutList(@RequestBody List<AudioAmnout> audioAmnoutList){
		iAudioProducerService.sendAudioAmnoutList(audioAmnoutList);
		return ServerResponse.createBySuccess("audioAmnoutList has sended to mq. audioAmnoutList:",audioAmnoutList);
	}

	@PostMapping("upload_amnout_list_str.do")
	@ResponseBody
	public ServerResponse uploadAmnoutListStr(String audioAmnoutListStr){
		iAudioProducerService.sendAudioAmnoutList(audioAmnoutListStr);
		return ServerResponse.createBySuccess("audioAmnoutList has sended to mq. audioAmnoutListStr:",audioAmnoutListStr);
	}

	@PostMapping("upload_dba_list.do")
	@ResponseBody
	public ServerResponse uploadDbaList(@RequestBody List<AudioDba> audioDbaList){
		iAudioProducerService.sendAudioDbaList(audioDbaList);
		return ServerResponse.createBySuccess("audioDbaList has sended to mq. audioDbaList:",audioDbaList);
	}

	@PostMapping("upload_dba_list_str.do")
	@ResponseBody
	public ServerResponse uploadDbaListStr(String audioDbaListStr){
		iAudioProducerService.sendAudioDbaList(audioDbaListStr);
		return ServerResponse.createBySuccess("audioDbaList has sended to mq. audioDbaListStr:",audioDbaListStr);
	}
}
