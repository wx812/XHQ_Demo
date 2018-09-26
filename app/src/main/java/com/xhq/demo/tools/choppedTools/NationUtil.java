package com.xhq.demo.tools.choppedTools;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者:zhaoge
 * 时间:2017/8/9.
 * <p>
 * 民族转码
 */

public class NationUtil {
    public static String nationCheck(String key) {
        Map<String, String> map = new HashMap<>();
        map.put("汉族", "19001");
        map.put("壮族", "19002");
        map.put("满族", "19003");
        map.put("回族", "19004");
        map.put("苗族", "19005");
        map.put("维吾尔族", "19006");
        map.put("土家族", "19007");
        map.put("彝族", "19008");
        map.put("蒙古族", "19009");
        map.put("藏族", "19010");
        map.put("布依族", "19011");
        map.put("侗族", "19012");
        map.put("瑶族", "19013");
        map.put("朝鲜族", "19014");
        map.put("白族", "19015");
        map.put("哈尼族", "19016");
        map.put("哈萨克族", "19017");
        map.put("黎族", "19018");
        map.put("傣族", "19019");
        map.put("畲族", "19020");
        map.put("傈僳族", "19021");
        map.put("仡佬族", "19022");
        map.put("东乡族", "19023");
        map.put("高山族", "19024");
        map.put("拉祜族", "19025");
        map.put("水族", "19026");
        map.put("佤族", "19027");
        map.put("纳西族", "19028");
        map.put("羌族", "19029");
        map.put("土族", "19030");
        map.put("仫佬族", "19031");
        map.put("锡伯族", "19032");
        map.put("柯尔克孜族", "19033");
        map.put("达斡尔族", "19034");
        map.put("景颇族", "19035");
        map.put("毛南族", "19036");
        map.put("撒拉族", "19037");
        map.put("布朗族", "19038");
        map.put("塔吉克族", "19039");
        map.put("阿昌族", "19040");
        map.put("普米族", "19041");
        map.put("鄂温克族", "19042");
        map.put("怒族", "19043");
        map.put("京族", "19044");
        map.put("基诺族", "19045");
        map.put("德昂族", "19046");
        map.put("保安族", "19047");
        map.put("俄罗斯族", "19048");
        map.put("裕固族", "19049");
        map.put("乌孜别克族", "19050");
        map.put("门巴族", "19051");
        map.put("鄂伦春族", "19052");
        map.put("独龙族", "19053");
        map.put("塔塔尔族", "19054");
        map.put("赫哲族", "19055");
        map.put("珞巴族", "19056");

        return map.get(key);
    }


    //	/**
//	 * 返回原因id
//	 * @param reasonName
//	 * @return
//	 */
//	public static int getReasonId(List<Reason> list, String reasonName){
//		int id =1;
//		for(Reason reason: list){
//			if(reason.getName().equals(reasonName)){
//				id = reason.getId();
//			}
//		}
//		return id;
//	}
//
//	public static List<Nation> initNationList(){
//		List<Nation> nationList = new ArrayList<>();
//		nationList.add(new Nation("汉族", 19001));
//		nationList.add(new Nation("壮族", 19002));
//		nationList.add(new Nation("满族", 19003));
//		nationList.add(new Nation("回族", 19004));
//		nationList.add(new Nation("苗族", 19005));
//		nationList.add(new Nation("维吾尔族", 19006));
//		nationList.add(new Nation("土家族", 19007));
//		nationList.add(new Nation("彝族", 19008));
//		nationList.add(new Nation("蒙古族", 19009));
//		nationList.add(new Nation("藏族", 19010));
//		nationList.add(new Nation("布依族", 19011));
//		nationList.add(new Nation("侗族", 19012));
//		nationList.add(new Nation("瑶族", 19013));
//		nationList.add(new Nation("朝鲜族", 19014));
//		nationList.add(new Nation("白族", 19015));
//		nationList.add(new Nation("哈尼族", 19016));
//		nationList.add(new Nation("哈萨克族", 19017));
//		nationList.add(new Nation("黎族", 19018));
//		nationList.add(new Nation("傣族", 19019));
//		nationList.add(new Nation("畲族", 19020));
//		nationList.add(new Nation("傈僳族", 19021));
//		nationList.add(new Nation("仡佬族", 19022));
//		nationList.add(new Nation("东乡族", 19023));
//		nationList.add(new Nation("高山族", 19024));
//		nationList.add(new Nation("拉祜族", 19025));
//		nationList.add(new Nation("水族", 19026));
//		nationList.add(new Nation("佤族", 19027));
//		nationList.add(new Nation("纳西族", 19028));
//		nationList.add(new Nation("羌族", 19029));
//		nationList.add(new Nation("土族", 19030));
//		nationList.add(new Nation("仫佬族", 19031));
//		nationList.add(new Nation("锡伯族", 19032));
//		nationList.add(new Nation("柯尔克孜族", 19033));
//		nationList.add(new Nation("达斡尔族", 19034));
//		nationList.add(new Nation("景颇族", 19035));
//		nationList.add(new Nation("毛南族", 19036));
//		nationList.add(new Nation("撒拉族", 19037));
//		nationList.add(new Nation("布朗族", 19038));
//		nationList.add(new Nation("塔吉克族", 19039));
//		nationList.add(new Nation("阿昌族", 19040));
//		nationList.add(new Nation("普米族", 19041));
//		nationList.add(new Nation("鄂温克族", 19042));
//		nationList.add(new Nation("怒族", 19043));
//		nationList.add(new Nation("京族", 19044));
//		nationList.add(new Nation("基诺族", 19045));
//		nationList.add(new Nation("德昂族", 19046));
//		nationList.add(new Nation("保安族", 19047));
//		nationList.add(new Nation("俄罗斯族", 19048));
//		nationList.add(new Nation("裕固族", 19049));
//		nationList.add(new Nation("乌孜别克族", 19050));
//		nationList.add(new Nation("门巴族", 19051));
//		nationList.add(new Nation("鄂伦春族", 19052));
//		nationList.add(new Nation("独龙族", 19053));
//		nationList.add(new Nation("塔塔尔族", 19054));
//		nationList.add(new Nation("赫哲族", 19055));
//		nationList.add(new Nation("珞巴族", 19056));
//
//		return nationList;
//	}

//	/**
//	 * 返回民族id
//	 * @param nationName
//	 * @return
//	 */
//	public static int getNationId(String nationName){
//		int nationId =0;
//		List<Nation> list = initNationList();
//
//		for(Nation nation: list){
//
//			if(!nationName.contains("族")){
//				nationName = nationName + "族";
//			}
//			if(nation.getName().equals(nationName)){
//				LogUtil.e("nation.getName() = "+nation.getName());
//				nationId = nation.getId();
//			}
//		}
//		return nationId;
//	}


//	/**
//	 * 返回民族
//	 * @param nationId
//	 * @return
//	 */
//	public static String getNation(int nationId){
//		String nationStr = "";
//		List<Nation> list = initNationList();
//
//		for(Nation nation: list){
//			if(nation.getId() == nationId){
//				LogUtil.e("nation.getName() = "+nation.getName());
//				nationStr = nation.getName();
//			}
//		}
//		return nationStr;
//	}

}
