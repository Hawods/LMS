package org.hawods.lms.service;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hawods.lms.data.DBHelper;
import org.hawods.lms.data.LmsMapper;
import org.hawods.lms.model.LmsVo;

public class LmsService {
	private Logger logger = LogManager.getLogger();

	public boolean initDatabase() {
		SqlSession session = DBHelper.openSession();
		if (session != null) {
			return true;
		} else {
			return false;
		}
	}

	public void closeDatabase() {
		DBHelper.closeDatabase();
	}

	public List<LmsVo> select(LmsVo vo) {
		List<LmsVo> list = null;
		SqlSession session = DBHelper.openSession();
		try {
			list = session.getMapper(LmsMapper.class).select(vo);
			logger.info("查询记录成功，记录数为" + list.size());
		} catch (Exception e) {
			logger.error("查询记录失败", e);
		} finally {
			session.close();
		}
		return list;
	}

	public boolean insert(LmsVo vo) {
		SqlSession session = DBHelper.openSession();
		try {
			LmsMapper mapper = session.getMapper(LmsMapper.class);
			if (mapper.checkId(vo.getId()) > 0) {
				return false;
			}
			mapper.insert(vo);
			session.commit();
			logger.info("新增记录成功，id为" + vo.getId());
		} catch (Exception e) {
			logger.error("新增记录失败", e);
		} finally {
			session.close();
		}
		return true;
	}

	public void update(LmsVo vo) {
		SqlSession session = DBHelper.openSession();
		try {
			session.getMapper(LmsMapper.class).update(vo);
			session.commit();
			logger.info("更新记录成功，id为" + vo.getId());
		} catch (Exception e) {
			logger.error("更新记录失败", e);
		} finally {
			session.close();
		}
	}

	public void delete(String[] idArr) {
		SqlSession session = DBHelper.openSession();
		try {
			LmsMapper mapper = session.getMapper(LmsMapper.class);
			for (String id : idArr) {
				mapper.delete(id);
			}
			session.commit();
			logger.info("删除记录成功，id为" + Arrays.toString(idArr));
		} catch (Exception e) {
			logger.error("删除记录失败", e);
		} finally {
			session.close();
		}
	}
}
