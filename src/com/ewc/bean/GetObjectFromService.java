package com.ewc.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ewc.base.App;
import com.ewc.db.PreferenceMap;

public class GetObjectFromService {

	static PreferenceMap preference = new PreferenceMap(App.ctx);

	public static Environment getLoginResult(String jsonstr) {
		Environment environment = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				final String Lng = json.getString("Lng");
				final String Lat = json.getString("Lat");
				final String Time = json.getString("Tim");
				final String PM25 = json.getString("PM25");
				final String PM10 = json.getString("PM10");
				final String SO = json.getString("SO");
				final String Date = json.getString("Date");
				final String Hum = json.getString("Hum");
				final String Tem = json.getString("Tem");

				environment = new Environment();
				environment.setHum(Hum);
				environment.setLat(Lat);
				environment.setLng(Lng);
				environment.setPm10(PM10);
				environment.setPm25(PM25);
				environment.setSo(SO);
				environment.setTem(Tem);
				environment.setTime(Time);
				environment.setDate(Date);
			}
		} catch (Exception e) {
		}
		return environment;
	}

	/**
	 * 获取简单的json结果
	 * 
	 * @param jsonstr
	 * @return
	 */
	public static Boolean getSimplyResult(String jsonstr) {
		Boolean flag = false;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (!json.getString("ret").equals("success")) {
				return flag;
			} else {
				flag = true;
			}
		} catch (Exception e) {
		}
		return flag;

	}

	/**
	 * 获取登录结果
	 * 
	 * @param jsonstr
	 * @param number
	 * @param password
	 * @return
	 */
	public static Boolean getLogin2Result(String jsonstr) {
		boolean result = false;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				final String id = json.getString("userId");
				final String name = json.getString("nickName");
				final String phone = json.getString("phone");

				User user = new User();
				user.setPhone(phone);
				user.setUserId(id);
				user.setUserName(name);
				preference.setUser(user);
				result = true;
			}
		} catch (Exception e) {
		}
		return result;
	}

	
	/**
	 * 获取时间范围内数据列表
	 */
	public static List<Environment> getHistroyList(String jsonStr){
		ArrayList<Environment> datas = null ;
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Environment>();
				JSONArray jsonarray = json.getJSONArray("Points");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Environment environment = new Environment();
					environment.setLat(obj.getString("Lat"));
					environment.setLng(obj.getString("Lng"));
					environment.setPm10(obj.getString("PM10"));
					environment.setPm25(obj.getString("PM25"));
					environment.setSo(obj.getString("SO"));
					environment.setHum(obj.getString("Hum"));
					environment.setTem(obj.getString("Tem"));
					environment.setTime(obj.getString("Tim"));
					datas.add(environment);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	public static List<PostModel> getHealthNewsList(String jsonstr) {
		List<PostModel> datas = new ArrayList<PostModel>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("HealthNewsList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					PostModel post = new PostModel();
					post.setCollection(obj.getString("collection"));
					post.setContent(obj.getString("description"));
					post.setId(obj.getString("newsID"));
					post.setImageurl(obj.getString("imgUrl"));
					post.setSource(obj.getString("source"));
					post.setTime(obj.getString("time"));
					post.setTitle(obj.getString("title"));
					post.setView_count(obj.getString("viewCount"));
					datas.add(post);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	public static Notify getNotificationDetail(String jsonstr) {
		Notify notify = new Notify();
		try {
			JSONObject json = new JSONObject(jsonstr);
			String type = json.getString("type");
			notify.setType(type);

			if (type.equals("PushNews")) {
				notify.setTitle(json.getString("title"));
				notify.setContent(json.getString("newsID"));
				notify.setIsCollection(json.getString("collection"));
				notify.setSource(json.getString("source"));
			}

		} catch (Exception e) {
		}
		return notify;
	}

	
}
