package org.hawods.lms.data;

import java.util.List;

import org.hawods.lms.model.LmsVo;

public interface LmsMapper {
	public int checkTable();

	public void createTable();

	public List<LmsVo> select(LmsVo vo);

	public int checkId(String id);

	public void insert(LmsVo vo);

	public void update(LmsVo vo);

	public void delete(String id);
}
