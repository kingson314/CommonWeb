package framework.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @Description: 实体顶层类，主要用于关联表继承，其他的基础表继承BaseEntity
 * @date Feb 14, 2014
 * @author:fgq
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SuperEntity implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	//@GeneratedValue(generator = "guid")
	//@GenericGenerator(name = "guid", strategy = "guid")
	// 实体的主键Id
	@Column(nullable = false,length = 40)
	protected String id;
	// 创建日期
	@Column(columnDefinition="timestamp default CURRENT_TIMESTAMP")
	protected Date createDate;
	// 更新日期
	@Column(columnDefinition="timestamp")
	protected Date modifyDate;
	// 创建人
	@Column(length = 40)
	protected String createUserId;
	// 更新人
	@Column(length = 40)
	protected String modifyUserId;
	// 版本号
	@Column(length = 10)
	protected String version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
