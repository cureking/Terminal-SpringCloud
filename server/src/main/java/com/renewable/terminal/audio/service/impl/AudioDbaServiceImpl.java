package com.renewable.terminal.audio.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.renewable.terminal.audio.dao.AudioDbaMapper;
import com.renewable.terminal.audio.entity.AudioDba;
import com.renewable.terminal.audio.service.IAudioDbaService;
import com.renewable.terminal.message.client.AudioMessageClient;
import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jarry
 * @since 2019-07-25
 */
@Service
public class AudioDbaServiceImpl extends ServiceImpl<AudioDbaMapper, AudioDba> implements IAudioDbaService {

	@Autowired
	private AudioMessageClient audioMessageClient;

	@Override
	public ServerResponse listByTimeWithCount(Integer countRequest, String startTime, String endTime) {

		// 1.数据校验
		if (countRequest == null){
			return ServerResponse.createByErrorMessage("the countRequest is null !");
		}

		// 2.获得总数，并计算ID步长
		Integer countTotal = this.count();
		if (countTotal == null){
			return ServerResponse.createByErrorMessage("countTotal is null !");
		}

		// 2.2 如果请求的数据量不足count
		if (countRequest >= countTotal){
			Wrapper<AudioDba> audioDbaWrapper = new QueryWrapper<AudioDba>().between("create_time",startTime,endTime);
			return ServerResponse.createBySuccess(this.list(audioDbaWrapper));
		}

		int countpace = countTotal/countRequest;

		// 2.x 获取在指定日期内的第一个数据
		Wrapper<AudioDba> audioDbaWrapper = new QueryWrapper<AudioDba>().between("create_time",startTime,endTime).last("LIMIT 1");
		AudioDba audioDbaStart = this.getOne(audioDbaWrapper);
		if (audioDbaStart == null){
			return ServerResponse.createByErrorMessage("audioDbaStart is null With the startTime: "+startTime+"and endTime:"+ endTime+" !");
		}

		// 2.y 获得指定日期内第一个数据的ID
		long idStart = audioDbaStart.getId();
		List<Long> idList = Lists.newArrayList();

		// 2.z 组装所求的ID集合
		for (long i = 0; i < countRequest; i++){
			idList.add(idStart + countpace * i);
		}

		// 3.请求指定ID集合的数据
		List<AudioDba> audioDbaList = (List<AudioDba>)this.listByIds(idList);
		if (audioDbaList == null || CollectionUtils.isEmpty(audioDbaList)){
			return ServerResponse.createByErrorMessage("audioDbaList is null or audioDbaList's size is zero !");
		}

		// 4.返回成功响应
		return ServerResponse.createBySuccess(audioDbaList);
	}

	@Override
	public ServerResponse saveAndUploadBatch(List<AudioDba> audioDbaList) {

		if (CollectionUtils.isEmpty(audioDbaList)){
			return ServerResponse.createByErrorMessage("audioDbaList is null !");
		}

		boolean result = this.saveBatch(audioDbaList);
		if (!result){
			return ServerResponse.createByErrorMessage("audioDbaList save fail !");
		}

		// 4.上传中控室
		audioMessageClient.uploadDbaList(audioDbaList);

		return ServerResponse.createBySuccessMessage("audioDbaList has sended to mq.");
	}
}
