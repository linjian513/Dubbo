package com.linjian.WindowCounterTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class FixedWindowCounter {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	/**
	 * 固定窗口为1秒的毫秒数
	 **/
	private static final int MILLISECONDS_OF_FIX_WINDOW = 1000;

	private static final int DOUBLE_MILLISECONDS_OF_FIX_WINDOW = MILLISECONDS_OF_FIX_WINDOW * 2;

	private static ConcurrentHashMap<String, Counter> previousValueMap = new ConcurrentHashMap<String, Counter>();

	static class Counter {
		private long previousVisitAt;
		private AtomicLong previousTotal;
		private LongAdder currentTotal;

		Counter(Long previousVisitAt) {
			this.previousVisitAt = previousVisitAt;
			previousTotal = new AtomicLong();
			currentTotal = new LongAdder();
		}

		public long getPreviousVisitAt() {
			return previousVisitAt;
		}

		public void setPreviousVisitAt(long previousVisitAt) {
			this.previousVisitAt = previousVisitAt;
		}

		public AtomicLong getPreviousTotal() {
			return previousTotal;
		}

		/**
		 * 设置值进去
		 * @param previousTotal
		 */
		public void setPreviousTotal(long previousTotal) {
			this.previousTotal.set(previousTotal);
		}

		public LongAdder getCurrentTotal() {
			return currentTotal;
		}

		public void setCurrentTotal(LongAdder currentTotal) {
			this.currentTotal = currentTotal;
		}		
	}

	public static void count(String key) {
		long now = System.currentTimeMillis();
		Counter counter = previousValueMap.get(key);
		if (counter == null) {
			counter = new Counter(now / 1000 * 1000);
			previousValueMap.putIfAbsent(key, counter);
		}
		modifyCounter(counter);
		counter.getCurrentTotal().increment();
	}

	private static synchronized void modifyCounter(Counter counter) {
		long now = System.currentTimeMillis();
		if (now - counter.getPreviousVisitAt() > DOUBLE_MILLISECONDS_OF_FIX_WINDOW) {
			counter.setPreviousVisitAt(now / 1000 * 1000);
			counter.getPreviousTotal().set(0);
			counter.getCurrentTotal().reset();
		} else if (now - counter.getPreviousVisitAt() > MILLISECONDS_OF_FIX_WINDOW) { //介于一个窗口和两个窗口之间
			counter.setPreviousVisitAt(now / 1000 * 1000);
			counter.setPreviousTotal(counter.getCurrentTotal().longValue());
			counter.getCurrentTotal().reset();
		}
	}

	public static double previousValue(String key) {
		Counter counter = previousValueMap.get(key);
		if (counter == null) {
			return 0;
		}	
		modifyCounter(counter);			
		return counter.getPreviousTotal().doubleValue();
	}

	public static void print(String key) {
		Counter c = previousValueMap.get(key);
		if (c != null) {
			double p = previousValue(key);
			double currentTotal = c.getCurrentTotal().doubleValue();
			System.out.println("previousVisitAt=" + sdf.format(new Date(c.getPreviousVisitAt())) + ", previousValue=" + p + ", currentTotal="
					+ currentTotal);
		} else {
			System.out.println("null");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			String key = "123.584";
			FixedWindowCounter.count(key);
			System.out.println("--------------------- now=" + sdf.format(new Date()) + ", count i=" + i);
			if (i == 30) {
				Thread.sleep(3000);
			}
			FixedWindowCounter.print(key);
			Thread.sleep(100);
		}
	}

}
