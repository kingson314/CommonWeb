package framework.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @Description: 基础实体类
 * @date Jan 27, 2014
 * @author:fgq
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity extends SuperEntity {
	private static final long serialVersionUID = 1L;

	// 实体记录的名称
	@Column(length = 100)
	protected String name;
	// 记录状态 （0表示正常，-1 表示不可用）
	@Column(columnDefinition="INT default 0")
	protected int state;
	// 排序顺序
	@Column(columnDefinition="INT default 0")
	protected int ord;
	// 备注说明
	@Column(length = 4000)
	protected String memo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
