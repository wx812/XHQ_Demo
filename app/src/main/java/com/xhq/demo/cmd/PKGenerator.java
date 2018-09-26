package com.xhq.demo.cmd;

/**
 * PK生成器(UUID 32位字符)
 * Created by Akmm at 2016-01-15 15:21
 */

@SuppressWarnings("UnusedDeclaration")
public class PKGenerator {
	private static PKGenerator instance = null;

	static {
		instance = new PKGenerator();
	}

	private TwitterIDBuilder idBuilder = null;

	private PKGenerator() {
		idBuilder = new TwitterIDBuilder(0, 0);
	}

	public static PKGenerator getInstance() {
		return instance;
	}

	/**
	 * 初始化PK生成器，不同服务器不同编号，确保生成的id全局唯一
	 * @param datacenter_id 服务中心编号
	 * @param worker_id 工作站编号
	 */
	public void init(int datacenter_id, int worker_id) {
		this.idBuilder = new TwitterIDBuilder(datacenter_id, worker_id);
	}

	/**
	 * 获得新Key
	 * @return 返回新的id，全数字字符串，可以当long
	 */
	private String newKey() {
		return idBuilder.newNextId();
	}

	/**
	 * 生成新的id
	 * @return 新id，可以当long使用
	 */
	public static String newId() {
		return getInstance().newKey();
	}
}


