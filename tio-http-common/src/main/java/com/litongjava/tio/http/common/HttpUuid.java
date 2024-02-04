package com.litongjava.tio.http.common;

import java.util.concurrent.atomic.AtomicLong;

import com.litongjava.tio.core.intf.TioUuid;

/**
 * @author tanyaowu
 * 2017年6月5日 上午10:44:26
 */
public class HttpUuid implements TioUuid {
	//	private static Logger log = LoggerFactory.getLogger(HttpUuid.class);

	private static java.util.concurrent.atomic.AtomicLong seq = new AtomicLong();

	/**
	 *
	 * @author tanyaowu
	 */
	public HttpUuid() {
	}

	/**
	 * @return
	 * @author tanyaowu
	 */
	@Override
	public String uuid() {
		return seq.incrementAndGet() + "";
	}
}
