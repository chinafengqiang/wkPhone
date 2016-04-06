package com.xy.db;

/**
 *本地数据库
 */
public interface DB {
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String dbPath = android.os.Environment.getExternalStorageDirectory().getPath() + "/xywkDB";
	public static final String DATABASE_NAME = dbPath + "/" + "xywk.db";
	
	
	/**
	 *本地表结构
	 */
	public interface TABLES {

		/**
		 * 保存章节结构
		 */
		public interface COURSE {
			public static final String TABLENAME = "course";

			public interface FIELDS {
				public static final String ID = "id";
				public static final String COURSE_ID = "course_id";//
				public static final String COURSE_NAME = "course_name";
				public static final String COURSE_ICON = "course_icon";
				public static final String D_TIME = "d_time";
				public static final String EXT1 = "ext1";
				public static final String EXT2 = "ext2";
			}

			public interface SQL {
				public static final String CREATE = "create table if not exists course(id integer PRIMARY KEY ,course_id int,course_name varchar(100),d_time varchar(50),ext1 varchar(100),ext2 varchar(100),course_icon varchar(255))";
				public static final String DROP = "drop table if exists course";
				public static final String INSERT = "insert into course(course_id,course_name,d_time,ext1,ext2,course_icon) values(%s,'%s','%s','%s','%s','%s') ";// 插入
				public static final String SELECT = "select * from course where %s";// 查询
				public static final String DELETE = "delete FROM course where %s";
				public static final String SELECT_ALL = "select * from course";
			}
		}

		public interface CHAPTER {
			public static final String TABLENAME = "chapter";

			public interface FIELDS {
				public static final String ID = "id";
				public static final String CHAPTER_ID = "chapter_id";
				public static final String CHAPTER_NAME = "chapter_name";
				public static final String PID = "pid";
				public static final String LEVEL = "level";
				public static final String ISLEAF = "isLeaf";
				public static final String URL = "chapter_url";
				public static final String VIDEO_URL = "video_url";
				public static final String COURSE_ID = "course_id";
				public static final String EXT1 = "ext1";
				public static final String EXT2 = "ext2";
			}

			public interface SQL {
				public static final String CREATE = "create table if not exists chapter(id integer PRIMARY KEY ,chapter_id int,chapter_name varchar(100),pid int,level int,isLeaf int,chapter_url varchar(255),course_id int,ext1 varchar(100),ext2 varchar(100),video_url varchar(255))";
				public static final String DROP = "drop table if exists chapter";
				public static final String INSERT = "insert into chapter(chapter_id,chapter_name,pid,level,isLeaf,chapter_url,course_id,ext1,ext2,video_url) values(%s,'%s',%s,%s,%s,'%s',%s,'%s','%s','%s') ";// 插入
				public static final String SELECT = "select * from chapter where %s";// 查询
				public static final String DELETE = "delete FROM chapter where %s";
				public static final String SELECT_ORDER_UNIT = "select * from chapter u where u.isLeaf = 1 and  u.pid in (select upt.chapter_id  from chapter upt where  upt.isLeaf = 0 and upt.course_id = %s) ORDER BY u.pid,u.id";
			}
		}

	}


}