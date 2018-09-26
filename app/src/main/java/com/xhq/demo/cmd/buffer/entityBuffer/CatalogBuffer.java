package com.xhq.demo.cmd.buffer.entityBuffer;


import com.xhq.demo.cmd.buffer.AbsEntityBuffer;
import com.xhq.demo.db.db_sql.entity.CatalogDao;
import com.xhq.demo.db.db_sql.entity.CatalogEntity;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmm on 2017/4/12.
 * 分组表还粗n
 */

public class CatalogBuffer extends AbsEntityBuffer<CatalogEntity>{
	private static CatalogBuffer INSTANCE = null;

	private CatalogBuffer() {
	}

	public static CatalogBuffer getInstance() {
		if (INSTANCE == null) {
			synchronized (CatalogBuffer.class) {
				if (INSTANCE == null) {
					CatalogBuffer inst = new CatalogBuffer();
//                    inst.init();
					INSTANCE = inst;
				}
			}
		}
		return INSTANCE;
	}

//    //分组是否被选择
//    private volatile boolean isSelected = false;
//
//    private void setSelectedFalse(){
//        isSelected = false;
//    }
//
//    private void setSelectedTrue(){
//        isSelected = true;
//    }

	public List<CatalogEntity> getCatalogs() {
		List<CatalogEntity> list = new ArrayList<>();
		for (CatalogEntity user : mapAll.values()) {
			if(user.getCatalogId().equals(SPUtils.get(SPKey.USER_CONFIG, SPKey.UID,""))){
				list.add(0,user);   //将默认分组排在最前
			}else {
				list.add(user);
			}
		}
		return list;
	}

	@Override
	protected CatalogDao getEntityDao() {
		return new CatalogDao();
	}
}
