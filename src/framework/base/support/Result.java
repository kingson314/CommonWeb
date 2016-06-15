package framework.base.support;

import java.util.HashMap;
import java.util.Map;

import common.util.json.UtilJackSon;

/**
 * 
 * @author fgq
 * 
 */
public class Result {
	private boolean success;
	private String msg;
	private Object data;
	private int rowcount;


	public Result(boolean success, String msg, Object data) {
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public Result(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public Result(boolean success) {
		this.success = success;
	}

	public Result(Object data, int rowcount) {
		this.success = true;
		this.data = data;
		this.rowcount = rowcount;
	}
	public Result(Object data) {
		this.success = true;
		this.data = data;
	}
	
	public String toJson() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", this.success);
		map.put("msg", this.msg);
		map.put("rowcount", this.rowcount);
		map.put("data", this.data == null ? "" : this.data);
		return UtilJackSon.toJson(map);
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
	}
}
