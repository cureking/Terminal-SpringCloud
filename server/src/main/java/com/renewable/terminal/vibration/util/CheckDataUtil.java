package com.renewable.terminal.vibration.util;

import com.renewable.terminal.terminal.common.ServerResponse;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @Description：
 * @Author: jarry
 */
public class CheckDataUtil {

	public static <T> ServerResponse<T> checkData(@NotNull T data, String... args) {

		if (data == null) {
			return ServerResponse.createByErrorMessage("the data is null !");
		}

		for (String arg : args) {
			Field param = null;
			try {
				param = data.getClass().getDeclaredField(arg);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				return ServerResponse.createByErrorMessage(e.toString());
			}
			if (param == null) {
				return ServerResponse.createByErrorMessage(param + " in " + data.toString() + " is null !");
			}
		}
		return ServerResponse.createBySuccess(data);
	}

	public static <T> ServerResponse<List<T>> checkData(@NotNull List<T> dataList, String... args) {

		if (CollectionUtils.isEmpty(dataList)) {
			return ServerResponse.createByErrorMessage("the dataList is empty !");
		}

		for (T data : dataList) {
			for (String arg : args) {
				Field param = null;
				try {
					param = data.getClass().getDeclaredField(arg);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					return ServerResponse.createByErrorMessage(e.toString());
				}
				if (param == null) {
					return ServerResponse.createByErrorMessage(param + " in " + data.toString() + " is null !");
				}
			}
		}

		return ServerResponse.createBySuccess(dataList);
	}

}
