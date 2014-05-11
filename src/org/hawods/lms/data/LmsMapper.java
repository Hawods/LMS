package org.hawods.lms.data;

import java.util.List;

import org.hawods.lms.model.LmsVo;

/**
 * 
 * sql映射
 * <p>
 * 用ibatis实现sql映射
 * <p>
 * 
 * @author hawods
 * @version 1.0
 * @since 1.0
 */
public interface LmsMapper {
	public int checkTable();

	public void createTable();

	public List<LmsVo> select(LmsVo vo);

	public int checkId(String id);

	public void insert(LmsVo vo);

	public void update(LmsVo vo);

	public void delete(String id);
}
