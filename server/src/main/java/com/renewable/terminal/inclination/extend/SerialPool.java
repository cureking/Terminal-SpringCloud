package com.renewable.terminal.inclination.extend;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description：
 * @Author: jarry
 */
public class SerialPool {


	// 0.标识（flag）线程池的工作状态
	private volatile boolean isWorking = true;

	// 1.任务仓库
	private BlockingQueue<Runnable> blockingQueue;
	// 2.线程仓库
	private List<Thread> workerList;

	// 3.Worker定义
	private static class Worker extends Thread {

		private SerialPool pool;

		private Worker(SerialPool pool) {
			this.pool = pool;
		}

		@Override
		public void run() {
			// 开始工作
			// TODO 需要修改
			while (this.pool.isWorking || this.pool.blockingQueue.size() > 0) {
				Runnable task = null;

				try {
					if (this.pool.isWorking) {
						task = this.pool.blockingQueue.take();
					} else {
						task = this.pool.blockingQueue.poll();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (task != null) {
					task.run();
					System.out.println("Thread " + Thread.currentThread().getName() + "has executed .");
				}
			}
		}
	}

	// 4.初始化工作线程数量与任务数量
	public SerialPool(int poolSize, int taskSize) {
		// 数据校验
		if (poolSize <= 0 || taskSize <= 0) {
			throw new IllegalArgumentException("非法参数");
		}
		this.blockingQueue = new LinkedBlockingDeque<>(taskSize);
		this.workerList = Collections.synchronizedList(new ArrayList<>());

		for (int i = 0; i < poolSize; i++) {
			Worker worker = new Worker(this);
			worker.start();
			workerList.add(worker);
		}
	}

	// 5.用户提交任务（非阻塞）
	public boolean submit(Runnable task) {
		if (this.isWorking) {
			return this.blockingQueue.offer(task);
		} else {
			return false;
		}
	}

	// 6.用户提交任务（阻塞）
	public void execute(Runnable task) throws InterruptedException {
		this.blockingQueue.put(task);
	}

	//7.需要一个关闭方法
	public void shutDown() {
		this.isWorking = false;
		for (Thread worker : workerList) {
			if (worker.getState().equals(Thread.State.WAITING) || worker.getState().equals(Thread.State.BLOCKED)) {
				worker.interrupt();
			}
		}
	}

	@Bean
	public SerialPool serialPool() {
		return new SerialPool(10, 100);
	}

	@Bean
	public SerialPool serialPool(int poolSize, int taskSize) {
		return new SerialPool(poolSize, taskSize);
	}

	public static void main(String[] args) {
		SerialPool pool = new SerialPool(3, 6);
		for (int i = 0; i < 6; i++) {
			pool.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println("一个线程被放入到我们的仓库中...");
					try {
						Thread.sleep(2000L);
					} catch (InterruptedException e) {
						System.out.println("======被唤醒=====");
					}
				}
			});
		}
		pool.shutDown();
	}
}
