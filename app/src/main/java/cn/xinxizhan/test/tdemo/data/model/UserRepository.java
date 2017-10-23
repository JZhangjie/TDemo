package cn.xinxizhan.test.tdemo.data.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/21.
 */

public class UserRepository {
    private List<User> mUsers;

    public String string() {
        JSONArray jsonArray = new JSONArray();
        if(mUsers!=null) {
            for(User user:mUsers){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username",user.getUsername());
                    jsonObject.put("password",user.getPassword());
                    jsonObject.put("aliasname",user.getAliasname());
                    jsonObject.put("xzqhdm",user.getXzqhdm());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray.toString();
    }

    public  void initFromString(String s) {
        mUsers = new ArrayList<>();
        if(s==null || s=="")
            return;
        try {
            JSONArray jsonArray = new JSONArray(s);
            for(int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                if(object!=null){
                    User user = new User(object.getString("username"),object.getString("password"));
                    user.setAliasname(object.getString("aliasname"));
                    user.setXzqhdm(object.getString("xzqhdm"));
                    this.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void add(User user){
        if(mUsers==null)
            mUsers=new ArrayList<>();
        for(User tempuser:mUsers){
            if(tempuser.getUsername().equals(user.getUsername())){
                return;
            }
        }
        mUsers.add(user);
    }

    public User getByUsername(String username){
        if(mUsers==null || username == null)
            return null;
        for(User user:mUsers){
            if(username.equals(user.getUsername())){
                return user;
            }
        }
        return null;
    }
}
