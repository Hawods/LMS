package org.hawods.lms.data;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * 数据库管理对象
 * <p>
 * 实现数据库session的管理
 * <p>
 * 
 * @author hawods
 * @version 1.0
 * @since 1.0
 */
public class DBHelper {
	private static SqlSessionFactory sessionFactory = null;

	private static Logger logger = LogManager.getLogger();

	/**
	 * 第一次调用时创建一个sessionFactory，检查数据库，根据需要初始化数据库
	 * @return session
	 */
	public static SqlSession openSession() {
		SqlSession sqlSession = null;
		if (sessionFactory == null) {
			try {
				String resource = "org/hawods/lms/data/mybatis-config.xml";
				InputStream inputStream = Resources
						.getResourceAsStream(resource);
				sessionFactory = new SqlSessionFactoryBuilder()
						.build(inputStream);
				sqlSession = sessionFactory.openSession();
				// 开启数据库用户验证
				// Statement statement = sqlSession.getConnection()
				// .createStatement();
				// statement
				// .execute("call syscs_util.syscs_set_database_property('derby.authentication.provider','builtin')");
				// statement
				// .execute("call syscs_util.syscs_set_database_property('derby.connection.requireAuthentication','true')");
				// statement
				// .execute("call syscs_util.syscs_set_database_property('derby.user.lms','123456')");
				// statement
				// .execute("call syscs_util.syscs_set_database_property('derby.database.fullAccessUsers','lms')");
				// statement
				// .execute("call syscs_util.syscs_set_database_property('derby.database.defaultConnectionMode','noAccess')");
				LmsMapper mapper = sqlSession.getMapper(LmsMapper.class);
				if (mapper.checkTable() == 0) {
					mapper.createTable();
				}
				sqlSession.commit();
				logger.info("数据库启动成功");
			} catch (Exception e) {
				logger.error("数据库启动失败", e);
				sqlSession = null;
			}
		} else {
			sqlSession = sessionFactory.openSession();
		}
		return sqlSession;
	}

	/**
	 * 关闭数据库
	 */
	public static void closeDatabase() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se) {
			if ("XJ015".equals(se.getSQLState())) {
				logger.info("数据库正常关闭");
			} else {
				logger.error("数据库关闭异常", se);
			}
		}
	}
}
